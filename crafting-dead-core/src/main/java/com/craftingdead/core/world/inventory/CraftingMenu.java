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

package com.craftingdead.core.world.inventory;

import com.craftingdead.core.world.entity.extension.PlayerExtension;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public class CraftingMenu extends AbstractContainerMenu {

  private final PlayerExtension<?> player;
  private final CraftingContainer craftingGrid;
  private final ResultContainer resultSlot;

  public CraftingMenu(int id, Inventory inventory) {
    this(id, PlayerExtension.getOrThrow(inventory.player));
  }

  public CraftingMenu(int id, PlayerExtension<?> player) {
    super(ModMenuTypes.CRAFTING.get(), id);
    this.player = player;
    this.craftingGrid = new CraftingContainer(this, 2, 2);
    this.resultSlot = new ResultContainer();

    this.setupCraftingSlots();
    this.setupPlayerInventorySlots(player.entity().getInventory());
  }

  /**
   * Adds crafting grid and result slots to the menu.
   */
  private void setupCraftingSlots() {
    final int slotSize = 18;

    // Crafting Grid (2x2)
    for (int y = 0; y < 2; ++y) {
      for (int x = 0; x < 2; ++x) {
        this.addSlot(new Slot(this.craftingGrid, x + y * 2, 83 + x * slotSize, 27 + y * slotSize));
      }
    }

    // Result Slot
    this.addSlot(new Slot(this.resultSlot, 0, 141, 36) {
      @Override
      public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
      }

      @Override
      public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
        super.onTake(player, stack);
        handleCraftingResultTaken();
      }
    });
  }

  /**
   * Adds inventory and hotbar slots to the menu.
   *
   * @param inventory The player's inventory.
   */
  private void setupPlayerInventorySlots(Inventory inventory) {
    final int slotSize = 18;

    // Main Inventory
    for (int y = 0; y < 3; ++y) {
      for (int x = 0; x < 9; ++x) {
        this.addSlot(new Slot(inventory, x + (y + 1) * 9, 8 + x * slotSize, 84 + y * slotSize));
      }
    }

    // Hotbar
    for (int x = 0; x < 9; ++x) {
      this.addSlot(new Slot(inventory, x, 8 + x * slotSize, 142));
    }
  }

  /**
   * Slot Index Mapping:
   * - Crafting Grid Slots: 0 to 3
   *   These slots are used for the crafting ingredients.
   * - Result Slot: 4
   *   This slot contains the crafted item output.
   * - Inventory Slots: 5 to 31
   *   These slots are part of the player's main inventory.
   * - Hotbar Slots: 32 to 40
   *   These slots are the player's quick access hotbar.
   */

  @Override
  public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
    var itemstack = ItemStack.EMPTY;
    var slot = this.slots.get(index);

    if (slot.hasItem()) {
      var stack = slot.getItem();
      itemstack = stack.copy();

      if (index == 4) { // RESULT_SLOT
        if (!this.moveItemStackTo(stack, 32, 41,
            false)) { // HOTBAR_SLOT_START to HOTBAR_SLOT_END + 1
          if (!this.moveItemStackTo(stack, 5, 32, false)) { // INV_SLOT_START to INV_SLOT_END + 1
            return ItemStack.EMPTY;
          }
        }
        slot.onTake(player, stack);
      } else if (index >= 5 && index <= 31) { // Inventory Slots
        if (!this.moveItemStackTo(stack, 0, 4, false)) { // CRAFT_SLOT_START to CRAFT_SLOT_END + 1
          return ItemStack.EMPTY;
        }
      } else if (index >= 32 && index <= 40) { // Hotbar Slots
        if (!this.moveItemStackTo(stack, 0, 4, false)) { // CRAFT_SLOT_START to CRAFT_SLOT_END + 1
          if (!this.moveItemStackTo(stack, 5, 32, false)) { // INV_SLOT_START to INV_SLOT_END + 1
            return ItemStack.EMPTY;
          }
        }
      } else if (index >= 0 && index <= 3) { // Crafting Grid Slots
        if (!this.moveItemStackTo(stack, 32, 41,
            false)) { // HOTBAR_SLOT_START to HOTBAR_SLOT_END + 1
          if (!this.moveItemStackTo(stack, 5, 32, false)) { // INV_SLOT_START to INV_SLOT_END + 1
            return ItemStack.EMPTY;
          }
        }
      }

      if (stack.isEmpty()) {
        slot.set(ItemStack.EMPTY);
      } else {
        slot.setChanged();
      }

      if (stack.getCount() == itemstack.getCount()) {
        return ItemStack.EMPTY;
      }

      slot.onTake(player, stack);
    }

    return itemstack;
  }

  @Override
  public void slotsChanged(@NotNull Container container) {
    this.updateResult();
  }

  @Override
  public void removed(@NotNull Player player) {
    super.removed(player);
    this.returnItemsToPlayerInventory();
  }

  @Override
  public boolean stillValid(@NotNull Player player) {
    return true;
  }

  /**
   * Updates the result slot with the current crafting recipe's output.
   */
  public void updateResult() {
    if (player.entity().getLevel().isClientSide()) {
      return;
    }

    var level = player.entity().getLevel();
    var recipeManager = level.getRecipeManager();
    var recipeOpt = recipeManager.getRecipeFor(RecipeType.CRAFTING, craftingGrid, level);

    if (recipeOpt.isPresent()) {
      var recipe = recipeOpt.get();
      var result = recipe.assemble(craftingGrid);
      this.resultSlot.setItem(0, result);
    } else {
      this.resultSlot.setItem(0, ItemStack.EMPTY);
    }

    this.resultSlot.setChanged();
    this.broadcastChanges();
  }

  /**
   * Handles updating the crafting grid when the result is taken.
   */
  private void handleCraftingResultTaken() {
    var level = player.entity().getLevel();
    var recipeManager = level.getRecipeManager();
    var recipeOpt = recipeManager.getRecipeFor(RecipeType.CRAFTING, craftingGrid, level);

    if (recipeOpt.isPresent()) {
      var recipe = recipeOpt.get();
      var remainingItems = recipe.getRemainingItems(craftingGrid);

      for (int i = 0; i < remainingItems.size(); i++) {
        var current = craftingGrid.getItem(i);
        var remaining = remainingItems.get(i);

        if (!current.isEmpty()) {
          if (!remaining.isEmpty()) {
            craftingGrid.setItem(i, remaining.copy());
          } else {
            craftingGrid.removeItem(i, 1);
          }
        }
      }

      updateResult();
    }
  }

  /**
   * Returns any remaining crafting grid items to the player's inventory when the menu is closed.
   */
  private void returnItemsToPlayerInventory() {
    for (int i = 0; i < craftingGrid.getContainerSize(); i++) {
      var itemstack = craftingGrid.getItem(i);
      if (!itemstack.isEmpty()) {
        player.entity().getInventory().placeItemBackInInventory(itemstack);
      }
    }
  }
}
