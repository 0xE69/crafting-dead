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

package com.craftingdead.survival.world.action;

import com.craftingdead.core.world.action.ActionType;
import com.craftingdead.core.world.action.ActionTypes;
import com.craftingdead.core.world.action.TargetSelector;
import com.craftingdead.core.world.action.item.BlockItemActionType;
import com.craftingdead.core.world.action.item.EntityItemActionType;
import com.craftingdead.core.world.action.item.ItemActionType;
import com.craftingdead.core.world.item.ModItems;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.world.effect.SurvivalMobEffects;
import com.craftingdead.survival.world.item.SurvivalItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SurvivalActionTypes {

  public static final DeferredRegister<ActionType<?>> deferredRegister =
      DeferredRegister.create(ActionTypes.REGISTRY_KEY, CraftingDeadSurvival.ID);

  public static final RegistryObject<EntityItemActionType<?>> USE_SPLINT =
      deferredRegister.register("use_splint",
          () -> EntityItemActionType
              .builder(TargetSelector.SELF_OR_OTHERS.hasEffect(SurvivalMobEffects.BROKEN_LEG))
              .forItem(SurvivalItems.SPLINT)
              .build());

  public static final RegistryObject<EntityItemActionType<?>> USE_SYRINGE_ON_ZOMBIE =
      deferredRegister.register("use_syringe_on_zombie",
          () -> EntityItemActionType.builder(TargetSelector.OTHERS_ONLY.ofEntityType(Zombie.class))
              .forItem(ModItems.SYRINGE)
              .duration(16)
              .customAction((performer, target) -> target.entity().hurt(
                  DamageSource.mobAttack(target.entity()), 2.0F), 0.25F)
              .resultItem(SurvivalItems.RBI_SYRINGE)
              .build());

  public static final RegistryObject<EntityItemActionType<?>> USE_CURE_SYRINGE =
      deferredRegister.register("use_cure_syringe",
          () -> EntityItemActionType.builder(TargetSelector.SELF_OR_OTHERS)
              .forItem(SurvivalItems.CURE_SYRINGE)
              .duration(16)
              .resultItem(ModItems.SYRINGE)
              .build());

  public static final RegistryObject<EntityItemActionType<?>> USE_RBI_SYRINGE =
      deferredRegister.register("use_rbi_syringe",
          () -> EntityItemActionType.builder(TargetSelector.SELF_OR_OTHERS)
              .forItem(SurvivalItems.RBI_SYRINGE)
              .duration(16)
              .effect(() -> new MobEffectInstance(SurvivalMobEffects.INFECTION.get(), 9999999))
              .resultItem(ModItems.SYRINGE)
              .build());

  public static final RegistryObject<ItemActionType<?>> FILL_WATER_CANTEEN =
      deferredRegister.register("fill_water_canteen",
          () -> BlockItemActionType.builder()
              .durationSeconds(3)
              .forFluid(FluidTags.WATER)
              .forItem(SurvivalItems.EMPTY_WATER_CANTEEN)
              .finishSound(SoundEvents.BOTTLE_FILL)
              .resultItem(SurvivalItems.WATER_CANTEEN)
              .consumeItemInCreative(true)
              .build());
}
