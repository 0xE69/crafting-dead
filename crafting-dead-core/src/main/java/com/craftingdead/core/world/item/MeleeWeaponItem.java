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

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.craftingdead.core.world.item.combatslot.CombatSlot;
import com.craftingdead.core.world.item.combatslot.CombatSlotProvider;
import com.craftingdead.core.world.item.equipment.Equipment;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class MeleeWeaponItem extends ToolItem {

  private final int attackDamage;

  private final Multimap<Attribute, AttributeModifier> attributeModifiers;

  public MeleeWeaponItem(int attackDamage, double attackSpeed, Item.Properties properties) {
    super(properties);
    this.attackDamage = attackDamage;
    this.attributeModifiers = ImmutableMultimap.of(
        Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID,
            "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION),
        Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID,
            "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
  }

  @Override
  public Multimap<Attribute, AttributeModifier> getAttributeModifiers(
      EquipmentSlot equipmentSlot, ItemStack itemStack) {
    return equipmentSlot == EquipmentSlot.MAINHAND
        ? this.attributeModifiers
        : super.getAttributeModifiers(equipmentSlot, itemStack);
  }

  @Override
  public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip,
      @NotNull TooltipFlag flag) {
    tooltip.add(new TranslatableComponent("item.craftingdead.damage").append(" ").append(
            new TranslatableComponent(String.valueOf(this.attackDamage))
                .withStyle(style -> style.withColor(0xBD4444)))
        .withStyle(style -> style.withColor(0x666666)));

    tooltip.add(new TranslatableComponent("item.craftingdead.durability").append(" ").append(
            new TranslatableComponent(String.valueOf(stack.getMaxDamage() - stack.getDamageValue()))
                .withStyle(style -> style.withColor(0xBD4444)))
        .withStyle(style -> style.withColor(0x666666)));
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack itemStack, @Nullable CompoundTag nbt) {
    var combatSlotProvider = LazyOptional.of(() -> CombatSlot.MELEE);
    var equipment = LazyOptional.of(() -> Equipment.forSlot(Equipment.Slot.MELEE));
    return new ICapabilityProvider() {

      @Override
      public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap,
          Direction side) {
        if (cap == CombatSlotProvider.CAPABILITY) {
          return combatSlotProvider.cast();
        }

        if (cap == Equipment.CAPABILITY) {
          return equipment.cast();
        }

        return LazyOptional.empty();
      }
    };
  }
}
