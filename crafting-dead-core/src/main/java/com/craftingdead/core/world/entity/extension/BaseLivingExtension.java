/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between
 * you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be
 * bound by the terms and conditions of this Agreement as may be revised from time
 * to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download,
 * copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.core.world.entity.extension;

import com.craftingdead.core.world.item.ClothingItem;
import com.craftingdead.core.world.item.equipment.Equipment.Slot;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.ServerConfig;
import com.craftingdead.core.event.LivingExtensionEvent;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.CancelActionMessage;
import com.craftingdead.core.network.message.play.CrouchMessage;
import com.craftingdead.core.network.message.play.PerformActionMessage;
import com.craftingdead.core.sounds.ModSoundEvents;
import com.craftingdead.core.world.action.Action;
import com.craftingdead.core.world.action.ActionObserver;
import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.entity.EntityUtil;
import com.craftingdead.core.world.item.MeleeWeaponItem;
import com.craftingdead.core.world.item.equipment.Clothing;
import com.craftingdead.core.world.item.equipment.Equipment;
import com.craftingdead.core.world.item.equipment.Hat;
import com.craftingdead.core.world.item.gun.Gun;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;

class BaseLivingExtension<E extends LivingEntity, H extends LivingHandler>
    implements LivingExtension<E, H> {

  /**
   * The vanilla entity.
   */
  private final E entity;

  protected final Map<LivingHandlerType<? extends H>, H> handlers = new Object2ObjectArrayMap<>();

  protected final Map<LivingHandlerType<? extends H>, H> dirtyHandlers =
      new Object2ObjectArrayMap<>();

  private final IntSet dirtySlots = new IntOpenHashSet();

  private final EntitySnapshot[] snapshots = new EntitySnapshot[20];

  protected final ItemStackHandler itemHandler =
      new ItemStackHandler(Equipment.Slot.values().length) {
        @Override
        protected void onLoad() {
          if (this.getSlots() != Equipment.Slot.values().length) {
            this.setSize(Equipment.Slot.values().length);
          }
          Arrays.stream(Equipment.Slot.values())
              .map(Equipment.Slot::getIndex)
              .map(this::getStackInSlot)
              .forEach(BaseLivingExtension.this::applyEquipmentModifiers);
        }

        @Override
        public void onContentsChanged(int slot) {
          if (!BaseLivingExtension.this.level().isClientSide()) {
            BaseLivingExtension.this.dirtySlots.add(slot);
          }
        }
      };

  /**
   * The last held {@link ItemStack} - used to check if the entity has switched item.
   */
  protected ItemStack lastHeldStack = null;

  protected float[] equipmentDropChances = new float[Equipment.Slot.values().length];

  @Nullable
  private Action action;

  @Nullable
  private ActionObserver actionObserver;

  private boolean movementBlocked;

  private boolean crouching;

  private Vec3 lastPos;

  private boolean moving;

  private Visibility cachedVisibility = Visibility.VISIBLE;

  BaseLivingExtension(E entity) {
    this.entity = entity;
    Arrays.fill(this.equipmentDropChances, 2.0F);
  }

  @Override
  public void load() {
    MinecraftForge.EVENT_BUS.post(new LivingExtensionEvent.Load(this));
  }

  @Override
  public <T extends H> void registerHandler(LivingHandlerType<T> type, T handler) {
    if (this.handlers.put(type, handler) != null) {
      throw new IllegalArgumentException("Duplicate handler: " + type);
    }
  }

  @Override
  public <T extends H> void removeHandler(LivingHandlerType<T> type) {
    this.handlers.remove(type);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends LivingHandler> Optional<T> getHandler(LivingHandlerType<T> type) {
    return Optional.ofNullable((T) this.handlers.get(type));
  }

  @Override
  public <T extends LivingHandler> T getHandlerOrThrow(LivingHandlerType<T> type) {
    @SuppressWarnings("unchecked")
    T handler = (T) this.handlers.get(type);
    if (handler == null) {
      throw new IllegalStateException("Missing handler: " + type);
    }
    return handler;
  }

  @Override
  public Optional<Action> getAction() {
    return Optional.ofNullable(this.action);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends Action> boolean performAction(T action, boolean force, boolean sendUpdate) {
    if (MinecraftForge.EVENT_BUS.post(new LivingExtensionEvent.PerformAction<>(this, action))) {
      return false;
    }

    if (this.isObservingAction()
        || action.target().map(LivingExtension::isObservingAction).orElse(false)) {
      return false;
    }

    if (!action.start(false)) {
      return false;
    }

    this.cancelAction(true);
    this.action = action;
    this.setActionObserver(action.createPerformerObserver());

    action.target().ifPresent(target -> target.setActionObserver(action.createTargetObserver()));

    if (sendUpdate) {
      var target = this.level().isClientSide()
          ? PacketDistributor.SERVER.noArg()
          : PacketDistributor.TRACKING_ENTITY_AND_SELF.with(this::entity);
      var buf = new FriendlyByteBuf(Unpooled.buffer());
      ((ActionType<T>) action.type()).encode(action, buf);
      NetworkChannel.PLAY.getSimpleChannel().send(target,
          new PerformActionMessage(action.type(), this.entity().getId(), buf));
    }
    return true;
  }

  @Override
  public void cancelAction(boolean sendUpdate) {
    if (this.action == null) {
      return;
    }
    this.stopAction(Action.StopReason.CANCELLED);
    if (sendUpdate) {
      var target = this.level().isClientSide()
          ? PacketDistributor.SERVER.noArg()
          : PacketDistributor.TRACKING_ENTITY_AND_SELF.with(this::entity);
      NetworkChannel.PLAY.getSimpleChannel().send(target,
          new CancelActionMessage(this.entity().getId()));
    }
  }

  @Override
  public void setActionObserver(ActionObserver actionObserver) {
    this.actionObserver = actionObserver;
  }

  @Override
  public Optional<ActionObserver> getActionObserver() {
    return Optional.ofNullable(this.actionObserver);
  }

  private void stopAction(Action.StopReason reason) {
    if (this.action != null) {
      this.action.stop(reason);
      this.action.target().ifPresent(target -> target.setActionObserver(null));
      this.setActionObserver(null);
      this.action = null;
    }
  }

  @Override
  public void setMovementBlocked(boolean movementBlocked) {
    this.movementBlocked = movementBlocked;
  }

  @Override
  public boolean isMovementBlocked() {
    return this.movementBlocked;
  }

  @Override
  public boolean isMoving() {
    return this.moving;
  }

  @Override
  public void tick() {
    var heldStack = this.mainHandItem();
    if (heldStack != this.lastHeldStack) {
      if (this.lastHeldStack != null) {
        this.lastHeldStack.getCapability(Gun.CAPABILITY)
            .ifPresent(gun -> gun.reset(this));
      }
      if ((this.lastHeldStack == null || !heldStack.is(this.lastHeldStack.getItem()))
          && heldStack.getCapability(Gun.CAPABILITY).isPresent()) {
        this.entity.playSound(ModSoundEvents.GUN_EQUIP.get(), 0.25F, 1.0F);
      }
      this.lastHeldStack = heldStack;
    }

    // Reset this every tick
    this.movementBlocked = false;

    if (this.action != null && this.action.tick()) {
      // Action may have cancelled itself
      if (this.action != null) {
        this.stopAction(Action.StopReason.COMPLETED);
      }
    }

    heldStack.getCapability(Gun.CAPABILITY).ifPresent(gun -> gun.tick(this));

    this.getEquipmentInSlot(Equipment.Slot.CLOTHING, Clothing.class)
        .ifPresent(this::tickClothing);
    this.getEquipmentInSlot(Equipment.Slot.HAT, Hat.class)
        .ifPresent(this::tickHat);

    if (!this.level().isClientSide()) {
      // This is called at the start of the entity tick so it's equivalent of last tick's position.
      this.snapshots[this.entity.getServer().getTickCount() % 20] = this.makeSnapshot(1.0F);
    }

    this.moving = !this.entity.position().equals(this.lastPos);
    this.lastPos = this.entity.position();

    this.handlers.forEach(this::tickHandler);
  }

  protected void tickHandler(LivingHandlerType<? extends H> type, H handler) {
    handler.tick();

    // Precedence = (1) INVISIBLE (2) PARTIALLY_VISIBLE (3) VISIBLE
    this.cachedVisibility = Visibility.VISIBLE;
    switch (handler.getVisibility()) {
      case INVISIBLE:
        this.cachedVisibility = Visibility.INVISIBLE;
      case PARTIALLY_VISIBLE:
        if (this.cachedVisibility == Visibility.VISIBLE) {
          this.cachedVisibility = Visibility.PARTIALLY_VISIBLE;
        }
        break;
      default:
        break;
    }

    if (handler.isMovementBlocked()) {
      this.movementBlocked = true;
    }

    if (handler.requiresSync()) {
      this.dirtyHandlers.put(type, handler);
    }
  }

  private void tickHat(Hat hat) {
    if (hat.waterBreathing() && this.entity.isEyeInFluid(FluidTags.WATER)) {
      this.entity.addEffect(
          new MobEffectInstance(MobEffects.WATER_BREATHING, 2, 0, false, false, false));
    }

    if (hat.nightVision()) {
      this.entity.addEffect(
          new MobEffectInstance(MobEffects.NIGHT_VISION, 2, 0, false, false, false));
    }
  }

  private void tickClothing(Clothing clothing) {
    if (clothing.fireImmunity()) {
      if (this.entity.getRemainingFireTicks() > 0) {
        this.entity.clearFire();
      }

      this.entity.addEffect(
          new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2, 0, false, false, false));
    }

    if (clothing.enhancesSwimming() && this.entity.isEyeInFluid(FluidTags.WATER)) {
      this.entity.addEffect(
          new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 2, 0, false, false, false));
    }
  }

  @Override
  public float handleDamaged(DamageSource source, float amount) {
    var damage = this.handlers.values().stream().reduce(amount,
        (result, extension) -> extension.handleDamaged(source, result), (u, t) -> t);
    // Handle scenarios where the player is the source of the damage (attacking another entity)
    if (source.getEntity() instanceof Player player) {
      if (ServerConfig.instance.backstabEnabled.get()) {
        var usedMeleeWeapon = player.getItemInHand(player.getUsedItemHand())
            .getItem() instanceof MeleeWeaponItem;
        if (usedMeleeWeapon && !EntityUtil.canSee(this.entity(), player, 90F)) {
          damage *= ServerConfig.instance.backstabBonusDamage.get().floatValue();
        }
      }

      if (ServerConfig.instance.criticalHitEnable.get()) {
        if (ServerConfig.instance.criticalHitChance.get() > player.getRandom().nextFloat()) {
          damage *= ServerConfig.instance.criticalHitBonusDamage.get().floatValue();
        }
      }
    }

    // Handle scenarios where a player is receiving damage
    if (this.entity() instanceof Player player) {
      var playerExtension = PlayerExtension.getOrThrow(player);
      if (!playerExtension.getItemInSlot(Slot.CLOTHING).isEmpty()) {
        // Makes sure that the damage income is from an entity
        if (source.getEntity() != null) {
          damage = Objects.requireNonNull(ClothingItem.getClothingItem(player))
              .calculateDamage(damage);
        }
      }
    }

    return damage;
  }

  @Override
  public boolean handleHurt(DamageSource source, float amount) {
    return this.handlers.values().stream().anyMatch(e -> e.handleHurt(source, amount));
  }

  @Override
  public boolean handleKill(Entity target) {
    return this.handlers.values().stream().anyMatch(e -> e.handleKill(target));
  }

  @Override
  public boolean handleDeath(DamageSource cause) {
    if (this.handlers.values().stream().anyMatch(e -> e.handleDeath(cause))) {
      return true;
    }
    this.cancelAction(true);
    return false;
  }

  @Override
  public boolean handleDeathLoot(DamageSource cause, Collection<ItemEntity> drops,
      int lootingLevel) {
    if (this.handlers.values().stream()
        .anyMatch(e -> e.handleDeathLoot(cause, drops, lootingLevel))) {
      return true;
    }

    if (!this.keepInventory()) {
      for (int i = 0; i < this.itemHandler.getSlots(); i++) {
        var itemStack = this.itemHandler.extractItem(i, Integer.MAX_VALUE, false);
        var dropChance = this.equipmentDropChances[i];
        if (!itemStack.isEmpty()
            && Math.max(this.random().nextFloat() - (lootingLevel * 0.01F), 0.0F) < dropChance) {
          var itemEntity = new ItemEntity(this.level(), this.entity().getX(),
              this.entity().getY(), this.entity().getZ(), itemStack);
          itemEntity.setDefaultPickUpDelay();
          drops.add(itemEntity);
        }
      }
    }
    return false;
  }

  @Override
  public boolean handleBlockPlace(BlockSnapshot replacedBlock, BlockState placedBlock,
      BlockState placedAgainst) {
    return this.handlers.values().stream()
        .anyMatch(e -> e.handleBlockPlace(replacedBlock, placedBlock, placedAgainst));
  }

  @Override
  public boolean handleMultiBlockPlace(List<BlockSnapshot> replacedBlocks, BlockState placedBlock,
      BlockState placedAgainst) {
    return this.handlers.values().stream()
        .anyMatch(e -> e.handleMultiBlockPlace(replacedBlocks, placedBlock, placedAgainst));
  }

  protected boolean keepInventory() {
    return false;
  }

  @Override
  public Visibility getVisibility() {
    return this.cachedVisibility;
  }

  @Override
  public EntitySnapshot getSnapshot(int tick) {
    final int currentTick = this.entity.getServer().getTickCount();
    if (tick == currentTick) {
      return this.makeSnapshot(1.0F);
    } else if (tick < currentTick - 20) {
      return this.snapshots[0];
    } else if (tick > currentTick) {
      throw new IllegalStateException("Tick bigger than current tick");
    }

    final int snapshotIndex = tick % 20;
    var snapshot = this.snapshots[snapshotIndex];
    if (snapshot == null) {
      throw new IndexOutOfBoundsException();
    }
    return snapshot;
  }

  @Override
  public boolean isCrouching() {
    return this.crouching;
  }

  @Override
  public void setCrouching(boolean crouching, boolean sendUpdate) {
    if (!this.entity.isOnGround()) {
      return;
    }
    this.crouching = crouching;
    if (sendUpdate) {
      var target = this.level().isClientSide()
          ? PacketDistributor.SERVER.noArg()
          : PacketDistributor.TRACKING_ENTITY_AND_SELF.with(this::entity);
      NetworkChannel.PLAY.getSimpleChannel().send(target,
          new CrouchMessage(this.entity().getId(), crouching));
    }
  }

  @Override
  public E entity() {
    return this.entity;
  }

  @Override
  public float getEquipmentDropChance(Equipment.Slot slot) {
    return this.equipmentDropChances[slot.getIndex()];
  }

  @Override
  public void setEquipmentDropChance(Equipment.Slot slot, float chance) {
    this.equipmentDropChances[slot.getIndex()] = chance;
  }

  @Override
  public ItemStack getItemInSlot(Equipment.Slot slot) {
    return this.itemHandler.getStackInSlot(slot.getIndex());
  }

  @Override
  public ItemStack setItemInSlot(Equipment.Slot slot, ItemStack itemStack) {
    var attributes = this.entity.getAttributes();

    var oldStack = this.getItemInSlot(slot);
    oldStack.getCapability(Equipment.CAPABILITY)
        .map(Equipment::attributeModifiers)
        .ifPresent(attributes::removeAttributeModifiers);

    this.applyEquipmentModifiers(itemStack);

    this.itemHandler.setStackInSlot(slot.getIndex(), itemStack);
    return oldStack;
  }

  private void applyEquipmentModifiers(ItemStack itemStack) {
    var attributes = this.entity.getAttributes();
    itemStack.getCapability(Equipment.CAPABILITY)
        .map(Equipment::attributeModifiers)
        .ifPresent(attributes::addTransientAttributeModifiers);
  }

  @Override
  public CompoundTag serializeNBT() {
    var tag = new CompoundTag();
    tag.put("inventory", this.itemHandler.serializeNBT());
    for (var entry : this.handlers.entrySet()) {
      var extensionTag = entry.getValue().serializeNBT();
      if (!extensionTag.isEmpty()) {
        tag.put(entry.getKey().toString(), extensionTag);
      }
    }

    var dropChances = new ListTag();
    for (float dropChance : this.equipmentDropChances) {
      dropChances.add(FloatTag.valueOf(dropChance));
    }
    tag.put("dropChances", dropChances);
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    this.itemHandler.deserializeNBT(tag.getCompound("inventory"));
    for (var entry : this.handlers.entrySet()) {
      var extensionTag = tag.getCompound(entry.getKey().toString());
      if (!extensionTag.isEmpty()) {
        entry.getValue().deserializeNBT(extensionTag);
      }
    }

    if (tag.contains("dropChances", Tag.TAG_LIST)) {
      var dropChances = tag.getList("dropChances", Tag.TAG_FLOAT);

      for (int i = 0; i < dropChances.size(); i++) {
        this.equipmentDropChances[i] = dropChances.getFloat(i);
      }
    }
  }

  @Override
  public int hashCode() {
    return this.entity.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj)
        || (obj instanceof LivingExtension<?, ?> extension
            && extension.entity().equals(this.entity));
  }

  @Override
  public void encode(FriendlyByteBuf out, boolean writeAll) {
    // Item Handler
    if (writeAll) {
      for (int i = 0; i < this.itemHandler.getSlots(); i++) {
        out.writeShort(i);
        out.writeItem(this.itemHandler.getStackInSlot(i));
      }
    } else {
      this.dirtySlots.forEach(slot -> {
        out.writeShort(slot);
        out.writeItem(this.itemHandler.getStackInSlot(slot));
      });
      this.dirtySlots.clear();
    }
    out.writeShort(255);

    // Handlers
    var handlersToSend = writeAll ? this.handlers.entrySet() : this.dirtyHandlers.entrySet();
    out.writeVarInt(handlersToSend.size());
    for (var entry : handlersToSend) {
      out.writeResourceLocation(entry.getKey().id());
      var handlerData = new FriendlyByteBuf(Unpooled.buffer());
      entry.getValue().encode(handlerData, writeAll);
      out.writeVarInt(handlerData.readableBytes());
      out.writeBytes(handlerData);
    }
    this.dirtyHandlers.clear();
  }

  @Override
  public void decode(FriendlyByteBuf in) {
    // Item Handler
    int slot;
    while ((slot = in.readShort()) != 255) {
      this.itemHandler.setStackInSlot(slot, in.readItem());
    }

    // Handlers
    int handlersSize = in.readVarInt();
    for (int x = 0; x < handlersSize; x++) {
      var id = in.readResourceLocation();
      int dataSize = in.readVarInt();
      var handler = this.handlers.get(new LivingHandlerType<>(id));
      if (handler == null) {
        in.readerIndex(in.readerIndex() + dataSize);
        continue;
      }
      handler.decode(in);
    }
  }

  @Override
  public boolean requiresSync() {
    return !this.dirtySlots.isEmpty() || !this.dirtyHandlers.isEmpty();
  }
}
