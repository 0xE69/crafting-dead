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
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ModAxeItem extends AxeItem {

  private final float attackDamage;

  public ModAxeItem(Tier tier, float attackDamage, float attackSpeed,
      Properties properties) {
    super(tier, attackDamage, attackSpeed, properties);
    this.attackDamage = attackDamage;
  }

  @Override
  public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip,
      @NotNull TooltipFlag flag) {
    tooltip.add(new TranslatableComponent("item.craftingdead.damage").append(" ").append(
            new TranslatableComponent(String.valueOf((int) this.attackDamage))
                .withStyle(style -> style.withColor(0xBD4444)))
        .withStyle(style -> style.withColor(0x666666)));

    tooltip.add(new TranslatableComponent("item.craftingdead.durability").append(" ").append(
            new TranslatableComponent(String.valueOf(stack.getMaxDamage() - stack.getDamageValue()))
                .withStyle(style -> style.withColor(0xBD4444)))
        .withStyle(style -> style.withColor(0x666666)));
  }
}
