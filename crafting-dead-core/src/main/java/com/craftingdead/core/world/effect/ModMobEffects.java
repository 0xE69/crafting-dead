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

package com.craftingdead.core.world.effect;

import com.craftingdead.core.CraftingDead;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMobEffects {

  public static final DeferredRegister<MobEffect> deferredRegister =
      DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CraftingDead.ID);

  public static final RegistryObject<MobEffect> SCUBA =
      deferredRegister.register("scuba", ScubaMobEffect::new);

  public static final RegistryObject<MobEffect> FLASH_BLINDNESS =
      deferredRegister.register("flash_blindness", FlashBlindnessMobEffect::new);

  public static final RegistryObject<MobEffect> ADRENALINE =
      deferredRegister.register("adrenaline", AdrenalineMobEffect::new);

  public static final RegistryObject<MobEffect> BLEEDING =
      deferredRegister.register("bleeding", BleedingMobEffect::new);

  public static final RegistryObject<MobEffect> PARACHUTE =
      deferredRegister.register("parachute", ParachuteMobEffect::new);

  /**
   * If the potion effect is not present, the potion effect is applied. Otherwise, overrides the
   * potion effect if its duration is longer than the current instance.
   *
   * @return <code>true</code> if the effect was applied. <code>false</code> otherwise.
   */
  public static boolean applyOrOverrideIfLonger(LivingEntity target, MobEffectInstance effect) {
    MobEffectInstance currentEffect = target.getEffect(effect.getEffect());
    if (currentEffect == null || currentEffect.getDuration() < effect.getDuration()) {
      target.removeEffect(effect.getEffect());
      return target.addEffect(effect);
    }
    return false;
  }
}
