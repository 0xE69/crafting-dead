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

package com.craftingdead.core.world.item;

import com.craftingdead.core.world.action.item.ItemActionType;
import com.craftingdead.core.world.entity.extension.LivingExtension;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.capability.CapabilityUtil;
import com.craftingdead.core.world.item.equipment.Equipment;
import com.craftingdead.core.world.item.equipment.SimpleClothing;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ClothingItem extends EquipmentItem {

  public static final UUID ARMOR_MODIFIER_ID =
      UUID.fromString("4117e432-16f5-4eea-a4fe-127b54d39af1");

  private final Multimap<Attribute, AttributeModifier> attributeModifiers;
  private final boolean fireImmunity;
  private final boolean enhancesSwimming;
  private final Supplier<? extends ItemActionType<?>> itemActionType;

  public ClothingItem(Properties properties, Supplier<? extends ItemActionType<?>> itemActionType) {
    super(properties);
    this.attributeModifiers = properties.attributeModifiers.build();
    this.fireImmunity = properties.fireImmunity;
    this.enhancesSwimming = properties.enhancesSwimming;
    this.itemActionType = itemActionType;
  }

  public ItemActionType<?> getActionType() {
    return this.itemActionType.get();
  }

  @Override
  public void appendHoverText(ItemStack stack, Level world, List<Component> lines,
      TooltipFlag tooltipFlag) {
    super.appendHoverText(stack, world, lines, tooltipFlag);
    if (this.fireImmunity) {
      lines.add(new TranslatableComponent("clothing.immune_to_fire")
          .withStyle(ChatFormatting.GRAY));
    }
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundTag nbt) {
    return CapabilityUtil.provider(
        () -> new SimpleClothing(this.attributeModifiers, this.fireImmunity, this.enhancesSwimming,
            new ResourceLocation(Objects.requireNonNull(this.getRegistryName()).getNamespace(),
                "textures/clothing/"
                    + this.getRegistryName().getPath() + "_" + "default" + ".png")),
        Equipment.CAPABILITY);
  }

  @Override
  public @NotNull InteractionResult useOn(UseOnContext context) {
    if (!context.getLevel().isClientSide()) {
      var performer = PlayerExtension.getOrThrow(context.getPlayer());
      if (this.getActionType().createBlockAction(performer, context)
          .map(action -> performer.performAction(action, true))
          .orElse(false)) {
        return InteractionResult.CONSUME;
      }
    }
    return InteractionResult.PASS;
  }

  @Override
  public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack itemStack,
      Player player, @NotNull LivingEntity targetEntity, @NotNull InteractionHand hand) {
    if (!player.getLevel().isClientSide()) {
      var performer = PlayerExtension.getOrThrow(player);
      var target = LivingExtension.getOrThrow(targetEntity);
      if (this.getActionType().createEntityAction(performer, target, hand)
          .map(action -> performer.performAction(action, true))
          .orElse(false)) {
        return InteractionResult.CONSUME;
      }
    }
    return InteractionResult.PASS;
  }

  @Override
  public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player,
      @NotNull InteractionHand hand) {
    if (!player.getLevel().isClientSide()) {
      var performer = PlayerExtension.getOrThrow(player);
      var hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
      if (hitResult.getType() == HitResult.Type.BLOCK
          && this.getActionType()
          .createBlockAction(performer, new UseOnContext(player, hand, hitResult))
          .map(action -> performer.performAction(action, true))
          .orElse(false)) {
        return InteractionResultHolder.consume(player.getItemInHand(hand));
      }

      if (this.getActionType().createAction(performer, hand)
          .map(action -> performer.performAction(action, true))
          .orElse(false)) {
        return InteractionResultHolder.consume(player.getItemInHand(hand));
      }
    }

    return InteractionResultHolder.pass(player.getItemInHand(hand));
  }

  @Override
  public int getUseDuration(@NotNull ItemStack itemStack) {
    return this.getActionType().getDurationTicks();
  }

  public static class Properties extends Item.Properties {

    private final ImmutableMultimap.Builder<Attribute, AttributeModifier> attributeModifiers =
        ImmutableMultimap.builder();
    private boolean fireImmunity;
    private boolean enhancesSwimming;

    public Properties attributeModifier(Attribute attribute, AttributeModifier modifier) {
      this.attributeModifiers.put(attribute, modifier);
      return this;
    }

    public Properties fireImmunity() {
      this.fireImmunity = true;
      return this;
    }

    public Properties enhancesSwimming() {
      this.enhancesSwimming = true;
      return this;
    }
  }
}
