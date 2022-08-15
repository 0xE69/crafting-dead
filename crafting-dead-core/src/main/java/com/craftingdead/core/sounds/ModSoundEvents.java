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

package com.craftingdead.core.sounds;

import com.craftingdead.core.CraftingDead;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSoundEvents {

  public static final DeferredRegister<SoundEvent> deferredRegister =
      DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CraftingDead.ID);

  public static final RegistryObject<SoundEvent> ACR_SHOOT = register("acr_shoot");
  public static final RegistryObject<SoundEvent> ACR_DISTANT_SHOOT = register("acr_distant_shoot");
  public static final RegistryObject<SoundEvent> ACR_RELOAD = register("acr_reload");
  public static final RegistryObject<SoundEvent> AK47_RELOAD = register("ak47_reload");
  public static final RegistryObject<SoundEvent> AK47_DISTANT_SHOOT =
      register("ak47_distant_shoot");
  public static final RegistryObject<SoundEvent> AK47_SHOOT = register("ak47_shoot");
  public static final RegistryObject<SoundEvent> AS50_RELOAD = register("as50_reload");
  public static final RegistryObject<SoundEvent> AS50_DISTANT_SHOOT = register("as50_distant_shoot");
  public static final RegistryObject<SoundEvent> AS50_SHOOT = register("as50_shoot");
  public static final RegistryObject<SoundEvent> AWP_RELOAD = register("awp_reload");
  public static final RegistryObject<SoundEvent> AWP_DISTANT_SHOOT = register("awp_distant_shoot");
  public static final RegistryObject<SoundEvent> AWP_SHOOT = register("awp_shoot");
  public static final RegistryObject<SoundEvent> BULLET_IMPACT_DIRT =
      register("bullet_impact_dirt");
  public static final RegistryObject<SoundEvent> BULLET_IMPACT_FLESH =
      register("bullet_impact_flesh");
  public static final RegistryObject<SoundEvent> BULLET_IMPACT_GLASS =
      register("bullet_impact_glass");
  public static final RegistryObject<SoundEvent> BULLET_IMPACT_METAL =
      register("bullet_impact_metal");
  public static final RegistryObject<SoundEvent> BULLET_IMPACT_METAL2 =
      register("bullet_impact_metal2");
  public static final RegistryObject<SoundEvent> BULLET_IMPACT_STONE =
      register("bullet_impact_stone");
  public static final RegistryObject<SoundEvent> BULLET_IMPACT_WOOD =
      register("bullet_impact_wood");
  public static final RegistryObject<SoundEvent> CROSSBOW_SHOOT = register("crossbow_shoot");
  public static final RegistryObject<SoundEvent> DESERT_EAGLE_RELOAD =
          register("desert_eagle_reload");
  public static final RegistryObject<SoundEvent> DESERT_EAGLE_DISTANT_SHOOT =
      register("desert_eagle_distant_shoot");
  public static final RegistryObject<SoundEvent> DESERT_EAGLE_SHOOT =
      register("desert_eagle_shoot");
  public static final RegistryObject<SoundEvent> DMR_DISTANT_SHOOT = register("dmr_distant_shoot");
  public static final RegistryObject<SoundEvent> DMR_RELOAD = register("dmr_reload");
  public static final RegistryObject<SoundEvent> DMR_SHOOT = register("dmr_shoot");
  public static final RegistryObject<SoundEvent> DRAGUNOV_DISTANT_SHOOT =
      register("dragunov_distant_shoot");
  public static final RegistryObject<SoundEvent> DRAGUNOV_SHOOT = register("dragunov_shoot");
  public static final RegistryObject<SoundEvent> DRY_FIRE = register("dry_fire");
  public static final RegistryObject<SoundEvent> FN57_DISTANT_SHOOT = register("fn57_distant_shoot");
  public static final RegistryObject<SoundEvent> FN57_RELOAD = register("fn57_reload");
  public static final RegistryObject<SoundEvent> FN57_SHOOT = register("fn57_shoot");
  public static final RegistryObject<SoundEvent> FNFAL_DISTANT_SHOOT =
      register("fnfal_distant_shoot");
  public static final RegistryObject<SoundEvent> FNFAL_RELOAD = register("fnfal_reload");
  public static final RegistryObject<SoundEvent> FNFAL_SHOOT = register("fnfal_shoot");
  public static final RegistryObject<SoundEvent> G18_RELOAD = register("g18_reload");
  public static final RegistryObject<SoundEvent> G18_DISTANT_SHOOT = register("g18_distant_shoot");
  public static final RegistryObject<SoundEvent> G18_SHOOT = register("g18_shoot");
  public static final RegistryObject<SoundEvent> G36C_DISTANT_SHOOT = register("g36c_distant_shoot");
  public static final RegistryObject<SoundEvent> G36C_RELOAD = register("g36c_reload");
  public static final RegistryObject<SoundEvent> G36C_SHOOT = register("g36c_shoot");
  public static final RegistryObject<SoundEvent> HK417_DISTANT_SHOOT = register("hk417_distant_shoot");
  public static final RegistryObject<SoundEvent> HK417_RELOAD = register("hk417_reload");
  public static final RegistryObject<SoundEvent> HK417_SHOOT = register("hk417_shoot");
  public static final RegistryObject<SoundEvent> M107_DISTANT_SHOOT = register("m107_distant_shoot");
  public static final RegistryObject<SoundEvent> M107_RELOAD = register("m107_reload");
  public static final RegistryObject<SoundEvent> M107_SHOOT = register("m107_shoot");
  public static final RegistryObject<SoundEvent> M1911_RELOAD = register("m1911_reload");
  public static final RegistryObject<SoundEvent> M1911_DISTANT_SHOOT =
      register("m1911_distant_shoot");
  public static final RegistryObject<SoundEvent> M1911_SHOOT = register("m1911_shoot");
  public static final RegistryObject<SoundEvent> M1GARAND_DISTANT_SHOOT = register("m1garand_distant_shoot");
  public static final RegistryObject<SoundEvent> M1GARAND_RELOAD = register("m1garand_reload");
  public static final RegistryObject<SoundEvent> M1GARAND_SHOOT = register("m1garand_shoot");
  public static final RegistryObject<SoundEvent> M240B_RELOAD = register("m240b_reload");
  public static final RegistryObject<SoundEvent> M240B_DISTANT_SHOOT =
      register("m240b_distant_shoot");
  public static final RegistryObject<SoundEvent> M240B_SHOOT = register("m240b_shoot");
  public static final RegistryObject<SoundEvent> M4A1_RELOAD = register("m4a1_reload");
  public static final RegistryObject<SoundEvent> M4A1_DISTANT_SHOOT =
      register("m4a1_distant_shoot");
  public static final RegistryObject<SoundEvent> M4A1_SHOOT = register("m4a1_shoot");
  public static final RegistryObject<SoundEvent> M9_DISTANT_SHOOT = register("m9_distant_shoot");
  public static final RegistryObject<SoundEvent> M9_RELOAD = register("m9_reload");
  public static final RegistryObject<SoundEvent> M9_SHOOT = register("m9_shoot");
  public static final RegistryObject<SoundEvent> MAC10_DISTANT_SHOOT =
      register("mac10_distant_shoot");
  public static final RegistryObject<SoundEvent> MAC10_RELOAD = register("mac10_reload");
  public static final RegistryObject<SoundEvent> MAC10_SHOOT = register("mac10_shoot");
  public static final RegistryObject<SoundEvent> MAGNUM_DISTANT_SHOOT = register("magnum_distant_shoot");
  public static final RegistryObject<SoundEvent> MAGNUM_RELOAD = register("magnum_reload");
  public static final RegistryObject<SoundEvent> MAGNUM_SHOOT = register("magnum_shoot");
  public static final RegistryObject<SoundEvent> MINIGUN_BARREL = register("minigun_barrel");
  public static final RegistryObject<SoundEvent> MINIGUN_DISTANT_SHOOT = register("minigun_distant_shoot");
  public static final RegistryObject<SoundEvent> MINIGUN_SHOOT = register("minigun_shoot");
  public static final RegistryObject<SoundEvent> MK48MOD_DISTANT_SHOOT = register("mk48mod_distant_shoot");
  public static final RegistryObject<SoundEvent> MK48MOD_RELOAD = register("mk48mod_reload");
  public static final RegistryObject<SoundEvent> MK48MOD_SHOOT = register("mk48mod_shoot");
  public static final RegistryObject<SoundEvent> MOSSBERG_DISTANT_SHOOT = register("mossberg_distant_shoot");
  public static final RegistryObject<SoundEvent> MOSSBERG_RELOAD = register("mossberg_reload");
  public static final RegistryObject<SoundEvent> MOSSBERG_SHOOT = register("mossberg_shoot");
  public static final RegistryObject<SoundEvent> MP5A5_DISTANT_SHOOT = register("mp5a5_distant_shoot");
  public static final RegistryObject<SoundEvent> MP5A5_RELOAD = register("mp5a5_reload");
  public static final RegistryObject<SoundEvent> MP5A5_SHOOT = register("mp5a5_shoot");
  public static final RegistryObject<SoundEvent> MPT_DISTANT_SHOOT = register("mpt_distant_shoot");
  public static final RegistryObject<SoundEvent> MPT_RELOAD = register("mpt_reload");
  public static final RegistryObject<SoundEvent> MPT_SHOOT = register("mpt_shoot");
  public static final RegistryObject<SoundEvent> P250_DISTANT_SHOOT =
      register("p250_distant_shoot");
  public static final RegistryObject<SoundEvent> P250_RELOAD = register("p250_reload");
  public static final RegistryObject<SoundEvent> P250_SHOOT = register("p250_shoot");
  public static final RegistryObject<SoundEvent> P90_RELOAD = register("p90_reload");
  public static final RegistryObject<SoundEvent> P90_DISTANT_SHOOT = register("p90_distant_shoot");
  public static final RegistryObject<SoundEvent> P90_SHOOT = register("p90_shoot");
  public static final RegistryObject<SoundEvent> RPK_DISTANT_SHOOT = register("rpk_distant_shoot");
  public static final RegistryObject<SoundEvent> RPK_RELOAD = register("rpk_reload");
  public static final RegistryObject<SoundEvent> RPK_SHOOT = register("rpk_shoot");
  public static final RegistryObject<SoundEvent> SCARL_DISTANT_SHOOT = register("scarl_distant_shoot");
  public static final RegistryObject<SoundEvent> SCARL_RELOAD = register("scarl_reload");
  public static final RegistryObject<SoundEvent> SCARL_SHOOT = register("scarl_shoot");
  public static final RegistryObject<SoundEvent> SHOTGUN_RELOAD = register("shotgun_reload");
  public static final RegistryObject<SoundEvent> SILENCED_AK47_SHOOT =
      register("silenced_ak47_shoot");
  public static final RegistryObject<SoundEvent> SILENCED_M240B_SHOOT =
      register("silenced_m240b_shoot");
  public static final RegistryObject<SoundEvent> SILENCED_M4A1_SHOOT =
      register("silenced_m4a1_shoot");
  public static final RegistryObject<SoundEvent> SILENCED_M9_SHOOT = register("silenced_m9_shoot");
  public static final RegistryObject<SoundEvent> SILENCED_MK48MOD_SHOOT =
      register("silenced_mk48mod_shoot");
  public static final RegistryObject<SoundEvent> SILENCED_MP5A5_SHOOT =
      register("silenced_mp5a5_shoot");
  public static final RegistryObject<SoundEvent> SILENCED_P90_SHOOT =
      register("silenced_p90_shoot");
  public static final RegistryObject<SoundEvent> SILENCED_RPK_SHOOT =
      register("silenced_rpk_shoot");
  public static final RegistryObject<SoundEvent> SPORTER22_DISTANT_SHOOT = register("sporter22_distant_shoot");
  public static final RegistryObject<SoundEvent> SPORTER22_RELOAD = register("sporter22_reload");
  public static final RegistryObject<SoundEvent> SPORTER22_SHOOT = register("sporter22_shoot");
  public static final RegistryObject<SoundEvent> TASER_SHOOT = register("taser_shoot");
  public static final RegistryObject<SoundEvent> TOGGLE_FIRE_MODE = register("toggle_fire_mode");
  public static final RegistryObject<SoundEvent> TRENCH_GUN_DISTANT_SHOOT = register("trench_gun_distant_shoot");
  public static final RegistryObject<SoundEvent> TRENCH_GUN_SHOOT = register("trench_gun_shoot");
  public static final RegistryObject<SoundEvent> VECTOR_DISTANT_SHOOT =
      register("vector_distant_shoot");
  public static final RegistryObject<SoundEvent> VECTOR_RELOAD = register("vector_reload");
  public static final RegistryObject<SoundEvent> VECTOR_SHOOT = register("vector_shoot");
  public static final RegistryObject<SoundEvent> SCOPE_ZOOM = register("scope_zoom");
  public static final RegistryObject<SoundEvent> GUN_EQUIP = register("gun_equip");

  private static RegistryObject<SoundEvent> register(String name) {
    ResourceLocation registryName = new ResourceLocation(CraftingDead.ID, name);
    return deferredRegister.register(name, () -> new SoundEvent(registryName));
  }
}
