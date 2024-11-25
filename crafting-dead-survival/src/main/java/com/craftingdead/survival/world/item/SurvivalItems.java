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

package com.craftingdead.survival.world.item;

import com.craftingdead.core.util.FunctionalUtil;
import com.craftingdead.core.world.item.ActionItem;
import com.craftingdead.core.world.item.ArbitraryTooltips;
import com.craftingdead.core.world.item.GrenadeItem;
import com.craftingdead.core.world.item.MeleeWeaponItem;
import com.craftingdead.core.world.item.ToolItem;
import com.craftingdead.survival.CraftingDeadSurvival;
import com.craftingdead.survival.world.action.SurvivalActionTypes;
import com.craftingdead.survival.world.entity.SurvivalEntityTypes;
import com.craftingdead.survival.world.entity.grenade.PipeBomb;
import com.craftingdead.survival.world.item.ConsumableItem.Type;
import com.craftingdead.survival.world.level.block.SurvivalBlocks;
import com.craftingdead.survival.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SurvivalItems {

  public static final DeferredRegister<Item> deferredRegister =
      DeferredRegister.create(ForgeRegistries.ITEMS, CraftingDeadSurvival.ID);

  public static final CreativeModeTab TAB = new CreativeModeTab(CraftingDeadSurvival.ID) {

    @Override
    public ItemStack makeIcon() {
      return new ItemStack(RBI_SYRINGE.get());
    }
  };

  // ================================================================================
  // Loot
  // ================================================================================

  public static final RegistryObject<Item> MILITARY_LOOT_ITEM =
      deferredRegister.register("military_loot",
          () -> new BlockItem(SurvivalBlocks.MILITARY_LOOT.get(), new Item.Properties()
              .rarity(Rarity.EPIC)
              .tab(TAB)));

  public static final RegistryObject<Item> MEDIC_LOOT_ITEM =
      deferredRegister.register("medic_loot",
          () -> new BlockItem(SurvivalBlocks.MEDICAL_LOOT.get(), new Item.Properties()
              .rarity(Rarity.EPIC)
              .tab(TAB)));

  public static final RegistryObject<Item> CIVILIAN_LOOT_ITEM =
      deferredRegister.register("civilian_loot",
          () -> new BlockItem(SurvivalBlocks.CIVILIAN_LOOT.get(), new Item.Properties()
              .rarity(Rarity.EPIC)
              .tab(TAB)));

  public static final RegistryObject<Item> CIVILIAN_RARE_LOOT_ITEM =
      deferredRegister.register("civilian_rare_loot",
          () -> new BlockItem(SurvivalBlocks.RARE_CIVILIAN_LOOT.get(), new Item.Properties()
              .rarity(Rarity.EPIC)
              .tab(TAB)));

  public static final RegistryObject<Item> POLICE_LOOT_ITEM =
      deferredRegister.register("police_loot",
          () -> new BlockItem(SurvivalBlocks.POLICE_LOOT.get(), new Item.Properties()
              .rarity(Rarity.EPIC)
              .tab(TAB)));

  public static final RegistryObject<Item> MILITARY_LOOT_GEN_ITEM =
      deferredRegister.register("military_loot_gen",
          () -> new BlockItem(SurvivalBlocks.MILITARY_LOOT_GENERATOR.get(), new Item.Properties()
              .tab(TAB)));

  public static final RegistryObject<Item> MEDIC_LOOT_GEN_ITEM =
      deferredRegister.register("medic_loot_gen",
          () -> new BlockItem(SurvivalBlocks.MEDICAL_LOOT_GENERATOR.get(), new Item.Properties()
              .tab(TAB)));

  public static final RegistryObject<Item> CIVILIAN_LOOT_GEN_ITEM =
      deferredRegister.register("civilian_loot_gen",
          () -> new BlockItem(SurvivalBlocks.CIVILIAN_LOOT_GENERATOR.get(), new Item.Properties()
              .tab(TAB)));

  public static final RegistryObject<Item> CIVILIAN_RARE_LOOT_GEN_ITEM =
      deferredRegister.register("civilian_rare_loot_gen",
          () -> new BlockItem(SurvivalBlocks.RARE_CIVILIAN_LOOT_GENERATOR.get(),
              new Item.Properties()
                  .tab(TAB)));

  public static final RegistryObject<Item> POLICE_LOOT_GEN_ITEM =
      deferredRegister.register("police_loot_gen",
          () -> new BlockItem(SurvivalBlocks.POLICE_LOOT_GENERATOR.get(), new Item.Properties()
              .tab(TAB)));

  // ================================================================================
  // Supply Drop Radio
  // ================================================================================

  public static final RegistryObject<Item> MEDICAL_DROP_RADIO =
      deferredRegister.register("medical_drop_radio",
          () -> new SupplyDropRadioItem(
              (SupplyDropRadioItem.Properties) new SupplyDropRadioItem.Properties()
                  .setLootTable(BuiltInLootTables.MEDICAL_SUPPLY_DROP)
                  .stacksTo(1)
                  .tab(TAB)));

  public static final RegistryObject<Item> MILITARY_DROP_RADIO =
      deferredRegister.register("military_drop_radio",
          () -> new SupplyDropRadioItem(
              (SupplyDropRadioItem.Properties) new SupplyDropRadioItem.Properties()
                  .setLootTable(BuiltInLootTables.MILITARY_SUPPLY_DROP)
                  .stacksTo(1)
                  .tab(TAB)));

  // ================================================================================
  // Virus
  // ================================================================================

  public static final RegistryObject<GrenadeItem> PIPE_BOMB = deferredRegister.register("pipe_bomb",
      () -> new GrenadeItem((GrenadeItem.Properties) new GrenadeItem.Properties()
          .setGrenadeEntitySupplier(FunctionalUtil.nullsafeFunction(PipeBomb::new, PipeBomb::new))
          .setEnabledSupplier(CraftingDeadSurvival.serverConfig.pipeBombEnabled)
          .stacksTo(3)
          .tab(TAB)));

  public static final RegistryObject<Item> SPLINT = deferredRegister.register("splint",
      () -> new ActionItem(SurvivalActionTypes.USE_SPLINT, new Item.Properties()
          .stacksTo(3)
          .tab(TAB)));

  public static final RegistryObject<Item> RBI_SYRINGE = deferredRegister.register("rbi_syringe",
      () -> new ActionItem(SurvivalActionTypes.USE_RBI_SYRINGE, new ActionItem.Properties()
          .stacksTo(3)
          .tab(TAB)));

  public static final RegistryObject<Item> CURE_SYRINGE = deferredRegister.register("cure_syringe",
      () -> new ActionItem(SurvivalActionTypes.USE_CURE_SYRINGE, new ActionItem.Properties()
          .stacksTo(3)
          .tab(TAB)));

  // ================================================================================
  // Spawn Eggs
  // ================================================================================

  public static final RegistryObject<Item> FAST_ZOMBIE_SPAWN_EGG =
      deferredRegister.register("fast_zombie_spawn_egg",
          () -> new ForgeSpawnEggItem(SurvivalEntityTypes.FAST_ZOMBIE, 0x000000, 0xFFFFFF,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> TANK_ZOMBIE_SPAWN_EGG =
      deferredRegister.register("tank_zombie_spawn_egg",
          () -> new ForgeSpawnEggItem(SurvivalEntityTypes.TANK_ZOMBIE, 0x000000, 0xFFFFFF,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> WEAK_ZOMBIE_SPAWN_EGG =
      deferredRegister.register("weak_zombie_spawn_egg",
          () -> new ForgeSpawnEggItem(SurvivalEntityTypes.WEAK_ZOMBIE, 0x000000, 0xFFFFFF,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> POLICE_ZOMBIE_SPAWN_EGG =
      deferredRegister.register("police_zombie_spawn_egg",
          () -> new ForgeSpawnEggItem(SurvivalEntityTypes.POLICE_ZOMBIE, 0x000000, 0xFFFFFF,
              new Item.Properties().tab(TAB)));

  public static final RegistryObject<Item> DOCTOR_ZOMBIE_SPAWN_EGG =
      deferredRegister.register("doctor_zombie_spawn_egg",
          () -> new ForgeSpawnEggItem(SurvivalEntityTypes.DOCTOR_ZOMBIE, 0x000000, 0xFFFFFF,
              new Item.Properties().tab(TAB)));

  // ================================================================================
  // Consumable Items
  // ================================================================================

  public static final RegistryObject<Item> EMPTY_WATER_BOTTLE =
      deferredRegister.register("empty_water_bottle",
          () -> new Item(new Item.Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> WATER_BOTTLE =
      deferredRegister.register("water_bottle",
          () -> new ConsumableItem(new Properties().tab(TAB).stacksTo(3), 0, 0, 8,
              EMPTY_WATER_BOTTLE, Type.ONLY_DRINK));

  public static final RegistryObject<Item> EMPTY_WATER_CANTEEN =
      deferredRegister.register("empty_water_canteen",
          () -> new ActionItem(SurvivalActionTypes.FILL_WATER_CANTEEN,
              new Item.Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> WATER_CANTEEN =
      deferredRegister.register("water_canteen",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 0, 0, 9,
              EMPTY_WATER_CANTEEN, Type.ONLY_DRINK));

  public static final RegistryObject<Item> EMPTY_FLASK =
      deferredRegister.register("empty_flask",
          () -> new ActionItem(SurvivalActionTypes.FILL_FLASK,
              new Item.Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> FLASK =
      deferredRegister.register("flask",
          () -> new ConsumableItem(new Properties().tab(TAB).stacksTo(3), 0, 0, 7, EMPTY_FLASK,
              Type.ONLY_DRINK));

  public static final RegistryObject<Item> EMPTY_ICED_TEA =
      deferredRegister.register("empty_iced_tea",
          () -> new Item(new Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> ICED_TEA =
      deferredRegister.register("iced_tea",
          () -> new ConsumableItem(new Properties().tab(TAB).stacksTo(3), 0, 0, 4, EMPTY_ICED_TEA,
              Type.ONLY_DRINK));

  public static final RegistryObject<Item> EMPTY_IRON_BREW =
      deferredRegister.register("empty_iron_brew",
          () -> new Item(new Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> IRON_BREW =
      deferredRegister.register("iron_brew",
          () -> new ConsumableItem(new Properties().tab(TAB).stacksTo(3), 0, 0, 4, EMPTY_IRON_BREW,
              Type.ONLY_DRINK));

  public static final RegistryObject<Item> EMPTY_JUICE_POUCH =
      deferredRegister.register("empty_juice_pouch",
          () -> new Item(new Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> JUICE_POUCH =
      deferredRegister.register("juice_pouch",
          () -> new ConsumableItem(new Properties().tab(TAB).stacksTo(3), 0, 0, 4,
              EMPTY_JUICE_POUCH, Type.ONLY_DRINK));

  public static final RegistryObject<Item> EMPTY_LEMON_SODA =
      deferredRegister.register("empty_lemon_soda",
          () -> new Item(new Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> LEMON_SODA =
      deferredRegister.register("lemon_soda",
          () -> new ConsumableItem(new Properties().tab(TAB).stacksTo(3), 0, 0, 4, EMPTY_LEMON_SODA,
              Type.ONLY_DRINK));

  public static final RegistryObject<Item> EMPTY_MILK_CARTON =
      deferredRegister.register("empty_milk_carton",
          () -> new Item(new Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> MILK_CARTON =
      deferredRegister.register("milk_carton",
          () -> new ConsumableItem(new Properties().tab(TAB).stacksTo(3), 0, 0, 6,
              EMPTY_MILK_CARTON, Type.ONLY_DRINK));

  public static final RegistryObject<Item> EMPTY_ORANGE_SODA =
      deferredRegister.register("empty_orange_soda",
          () -> new Item(new Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> ORANGE_SODA =
      deferredRegister.register("orange_soda",
          () -> new ConsumableItem(new Properties().tab(TAB).stacksTo(3), 0, 0, 4,
              EMPTY_ORANGE_SODA, Type.ONLY_DRINK));

  public static final RegistryObject<Item> EMPTY_PEPE_SODA =
      deferredRegister.register("empty_pepe_soda",
          () -> new Item(new Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> PEPE_SODA =
      deferredRegister.register("pepe_soda",
          () -> new ConsumableItem(new Properties().tab(TAB).stacksTo(3), 0, 0, 4, EMPTY_PEPE_SODA,
              Type.ONLY_DRINK));

  public static final RegistryObject<Item> EMPTY_SPRITE =
      deferredRegister.register("empty_sprite",
          () -> new Item(new Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> SPRITE =
      deferredRegister.register("sprite",
          () -> new ConsumableItem(new Properties().tab(TAB).stacksTo(3), 0, 0, 6, EMPTY_SPRITE,
              Type.ONLY_DRINK));

  public static final RegistryObject<Item> EMPTY_ZOMBIE_ENERGY =
      deferredRegister.register("empty_zombie_energy",
          () -> new Item(new Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> ZOMBIE_ENERGY =
      deferredRegister.register("zombie_energy",
          () -> new ConsumableItem(new Properties().tab(TAB).stacksTo(3), 0, 0, 4,
              EMPTY_ZOMBIE_ENERGY, Type.ONLY_DRINK));

  public static final RegistryObject<Item> POWER_BAR =
      deferredRegister.register("power_bar",
          () -> new ConsumableItem(new Properties().tab(TAB).stacksTo(3), 4, 0.3F, 0, null,
              Type.ONLY_FOOD));

  public static final RegistryObject<Item> CANDY_BAR =
      deferredRegister.register("candy_bar",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 6, 0.3F, 0, null,
              Type.ONLY_FOOD));

  public static final RegistryObject<Item> CEREAL =
      deferredRegister.register("cereal",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 10, 0.6F, 0, null,
              Type.ONLY_FOOD));

  public static final RegistryObject<Item> CANNED_SWEETCORN =
      deferredRegister.register("canned_sweetcorn",
          () -> new Item(new Item.Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> OPEN_CANNED_SWEETCORN =
      deferredRegister.register("open_canned_sweetcorn",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 6, 0.6F, 0, null,
              Type.ONLY_FOOD));

  public static final RegistryObject<Item> CANNED_BEANS =
      deferredRegister.register("canned_beans",
          () -> new Item(new Item.Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> OPEN_CANNED_BEANS =
      deferredRegister.register("open_canned_beans",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 8, 0.6F, 0, null,
              Type.ONLY_FOOD));

  public static final RegistryObject<Item> CANNED_TUNA =
      deferredRegister.register("canned_tuna",
          () -> new Item(new Item.Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> OPEN_CANNED_TUNA =
      deferredRegister.register("open_canned_tuna",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 6, 0.6F, 0, null,
              Type.ONLY_FOOD));

  public static final RegistryObject<Item> CANNED_PEACHES =
      deferredRegister.register("canned_peaches",
          () -> new Item(new Item.Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> OPEN_CANNED_PEACHES =
      deferredRegister.register("open_canned_peaches",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 6, 0.6F, 1, null,
              Type.FOOD_AND_DRINK));

  public static final RegistryObject<Item> CANNED_PASTA =
      deferredRegister.register("canned_pasta",
          () -> new Item(new Item.Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> OPEN_CANNED_PASTA =
      deferredRegister.register("open_canned_pasta",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 6, 0.6F, 0, null,
              Type.ONLY_FOOD));

  public static final RegistryObject<Item> CANNED_CORNED_BEEF =
      deferredRegister.register("canned_corned_beef",
          () -> new Item(new Item.Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> OPEN_CANNED_CORNED_BEEF =
      deferredRegister.register("open_canned_corned_beef",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 8, 0.6F, 0, null,
              Type.ONLY_FOOD));

  public static final RegistryObject<Item> CANNED_CUSTARD =
      deferredRegister.register("canned_custard",
          () -> new Item(new Item.Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> OPEN_CANNED_CUSTARD =
      deferredRegister.register("open_canned_custard",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 4, 0.6F, 3, null,
              Type.FOOD_AND_DRINK));

  public static final RegistryObject<Item> CANNED_PICKLES =
      deferredRegister.register("canned_pickles",
          () -> new Item(new Item.Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> OPEN_CANNED_PICKLES =
      deferredRegister.register("open_canned_pickles",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 4, 0.6F, 0, null,
              Type.ONLY_FOOD));

  public static final RegistryObject<Item> CANNED_DOG_FOOD =
      deferredRegister.register("canned_dog_food",
          () -> new Item(new Item.Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> OPEN_CANNED_DOG_FOOD =
      deferredRegister.register("open_canned_dog_food",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 2, 0.6F, 0, null,
              Type.ONLY_FOOD));

  public static final RegistryObject<Item> CANNED_TOMATO_SOUP =
      deferredRegister.register("canned_tomato_soup",
          () -> new Item(new Item.Properties().tab(TAB).stacksTo(3)));

  public static final RegistryObject<Item> OPEN_CANNED_TOMATO_SOUP =
      deferredRegister.register("open_canned_tomato_soup",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 4, 0.6F, 2, null,
              Type.FOOD_AND_DRINK));

  public static final RegistryObject<Item> MRE =
      deferredRegister.register("mre",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 16, 0.6F, 7, null,
              Type.FOOD_AND_DRINK));

  public static final RegistryObject<Item> ORANGE =
      deferredRegister.register("orange",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 5, 0.6F, 2, null,
              Type.FOOD_AND_DRINK));

  public static final RegistryObject<Item> ROTTEN_ORANGE =
      deferredRegister.register("rotten_orange",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 2, 0.6F, 1, null,
              Type.FOOD_AND_DRINK));

  public static final RegistryObject<Item> PEAR =
      deferredRegister.register("pear",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 4, 0.6F, 2, null,
              Type.FOOD_AND_DRINK));

  public static final RegistryObject<Item> ROTTEN_PEAR =
      deferredRegister.register("rotten_pear",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 1, 0.6F, 1, null,
              Type.FOOD_AND_DRINK));

  public static final RegistryObject<Item> RICE_BAG =
      deferredRegister.register("rice_bag",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 8, 0.6F, 0, null,
              Type.ONLY_FOOD));

  public static final RegistryObject<Item> ROTTEN_APPLE =
      deferredRegister.register("rotten_apple",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 1, 0.6F, 1, null,
              Type.FOOD_AND_DRINK));

  public static final RegistryObject<Item> NOODLES =
      deferredRegister.register("noodles",
          () -> new ConsumableItem(new Properties().tab(TAB).stacksTo(3), 6, 0.6F, 3, null,
              Type.FOOD_AND_DRINK));

  public static final RegistryObject<Item> ROTTEN_MELON_SLICE =
      deferredRegister.register("rotten_melon_slice",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 3, 0.6F, 1, null,
              Type.FOOD_AND_DRINK));

  public static final RegistryObject<Item> BLUEBERRY =
      deferredRegister.register("blueberry",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 4, 0.6F, 3, null,
              Type.FOOD_AND_DRINK));

  public static final RegistryObject<Item> ROTTEN_BLUEBERRY =
      deferredRegister.register("rotten_blueberry",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 1, 0.6F, 1, null,
              Type.FOOD_AND_DRINK));

  public static final RegistryObject<Item> RASPBERRY =
      deferredRegister.register("raspberry",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 3, 0.6F, 3, null,
              Type.FOOD_AND_DRINK));

  public static final RegistryObject<Item> ROTTEN_RASPBERRY =
      deferredRegister.register("rotten_raspberry",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 1, 0.6F, 1, null,
              Type.FOOD_AND_DRINK));

  public static final RegistryObject<Item> CHIPS =
      deferredRegister.register("chips",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 3, 0.6F, 0, null,
              Type.ONLY_FOOD));

  public static final RegistryObject<Item> RANCH_CHIPS =
      deferredRegister.register("ranch_chips",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 3, 0.6F, 0, null,
              Type.ONLY_FOOD));

  public static final RegistryObject<Item> CHEESY_CHIPS =
      deferredRegister.register("cheesy_chips",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 3, 0.6F, 0, null,
              Type.ONLY_FOOD));

  public static final RegistryObject<Item> SALTED_CHIPS =
      deferredRegister.register("salted_chips",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 3, 0.6F, 0, null,
              Type.ONLY_FOOD));

  public static final RegistryObject<Item> POPCORN =
      deferredRegister.register("popcorn",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 3, 0.6F, 0, null,
              Type.ONLY_FOOD));

  public static final RegistryObject<Item> NUTTY_CEREAL =
      deferredRegister.register("nutty_cereal",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 10, 0.6F, 0, null,
              Type.ONLY_FOOD));

  public static final RegistryObject<Item> EMERALD_CEREAL =
      deferredRegister.register("emerald_cereal",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 10, 0.6F, 0, null,
              Type.ONLY_FOOD));

  public static final RegistryObject<Item> FLAKE_CEREAL =
      deferredRegister.register("flake_cereal",
          () -> new ConsumableItem(new Item.Properties().tab(TAB).stacksTo(3), 10, 0.6F, 0, null,
              Type.ONLY_FOOD));

  // ================================================================================
  // Tools
  // ================================================================================

  public static final RegistryObject<Item> CAN_OPENER =
      deferredRegister.register("can_opener", () -> new ToolItem(
          new Item.Properties().durability(8).tab(TAB)) {

        @Override
        public boolean hasContainerItem(ItemStack stack) {
          return true;
        }

        @Override
        public ItemStack getContainerItem(ItemStack itemStack) {
          return itemStack.copy();
        }
      });

  public static final RegistryObject<Item> SCREWDRIVER =
      deferredRegister.register("screwdriver", () -> new ToolItem(
          new Item.Properties().durability(4).tab(TAB)) {

        @Override
        public boolean hasContainerItem(ItemStack stack) {
          return true;
        }

        @Override
        public ItemStack getContainerItem(ItemStack itemStack) {
          return itemStack.copy();
        }
      });

  public static final RegistryObject<Item> MULTI_TOOL =
      deferredRegister.register("multi_tool", () -> new MeleeWeaponItem(8, -2.4F,
          new Item.Properties().durability(20).tab(TAB)) {

        @Override
        public boolean hasContainerItem(ItemStack stack) {
          return true;
        }

        @Override
        public ItemStack getContainerItem(ItemStack itemStack) {
          return itemStack.copy();
        }
      });

  static {
    var canOpenerTooltip = new TranslatableComponent("can_opener.information")
        .withStyle(ChatFormatting.GRAY);
    var cannedFoodTooltip = new TranslatableComponent("canned_food.information")
        .withStyle(ChatFormatting.GRAY);
    ArbitraryTooltips.registerTooltip(CAN_OPENER, canOpenerTooltip);
    ArbitraryTooltips.registerTooltip(SCREWDRIVER, canOpenerTooltip);
    ArbitraryTooltips.registerTooltip(MULTI_TOOL, canOpenerTooltip);
    ArbitraryTooltips.registerTooltip(CANNED_SWEETCORN, cannedFoodTooltip);
    ArbitraryTooltips.registerTooltip(CANNED_BEANS, cannedFoodTooltip);
    ArbitraryTooltips.registerTooltip(CANNED_TUNA, cannedFoodTooltip);
    ArbitraryTooltips.registerTooltip(CANNED_PEACHES, cannedFoodTooltip);
    ArbitraryTooltips.registerTooltip(CANNED_PASTA, cannedFoodTooltip);
    ArbitraryTooltips.registerTooltip(CANNED_CORNED_BEEF, cannedFoodTooltip);
    ArbitraryTooltips.registerTooltip(CANNED_CUSTARD, cannedFoodTooltip);
    ArbitraryTooltips.registerTooltip(CANNED_PICKLES, cannedFoodTooltip);
    ArbitraryTooltips.registerTooltip(CANNED_DOG_FOOD, cannedFoodTooltip);
    ArbitraryTooltips.registerTooltip(CANNED_TOMATO_SOUP, cannedFoodTooltip);
    ArbitraryTooltips.registerTooltip(SPLINT,
        new TranslatableComponent("splint.information")
            .withStyle(ChatFormatting.GRAY));
    ArbitraryTooltips.registerTooltip(RBI_SYRINGE,
        new TranslatableComponent("rbi_syringe.information")
            .withStyle(ChatFormatting.GRAY));
    ArbitraryTooltips.registerTooltip(CURE_SYRINGE,
        new TranslatableComponent("cure_syringe.information")
            .withStyle(ChatFormatting.GRAY));
  }
}
