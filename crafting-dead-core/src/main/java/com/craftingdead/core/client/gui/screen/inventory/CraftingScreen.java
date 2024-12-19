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

package com.craftingdead.core.client.gui.screen.inventory;

import com.craftingdead.core.CraftingDead;
import com.craftingdead.core.network.NetworkChannel;
import com.craftingdead.core.network.message.play.OpenEquipmentMenuMessage;
import com.craftingdead.core.world.inventory.CraftingMenu;
import com.craftingdead.core.world.item.ModItems;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CraftingScreen extends EffectRenderingInventoryScreen<CraftingMenu> {

  private static final ResourceLocation CRAFTING =
      new ResourceLocation(CraftingDead.ID, "textures/gui/container/crafting.png");
  private static final ResourceLocation EQUIPMENT =
      new ResourceLocation(CraftingDead.ID, "textures/gui/container/equipment.png");

  private int oldMouseX;
  private int oldMouseY;

  public CraftingScreen(CraftingMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
  }

  @Override
  public void init() {
    super.init();
  }

  /**
   * Main render method, called every frame.
   */
  @Override
  public void render(@NotNull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    super.render(matrixStack, mouseX, mouseY, partialTicks);
    this.renderTooltip(matrixStack, mouseX, mouseY);
    this.oldMouseX = mouseX;
    this.oldMouseY = mouseY;
  }

  /**
   * Renders the background of the crafting screen.
   */
  @Override
  protected void renderBg(@NotNull PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
    this.renderBackground(poseStack);
    RenderSystem.setShaderTexture(0, CRAFTING);
    this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

    RenderSystem.setShaderTexture(0, EQUIPMENT);
    this.blit(poseStack, this.leftPos, this.topPos - 28, 183, 36, 28, 31);
    this.blit(poseStack, this.leftPos + 30, this.topPos - 28, 211, 0, 29, 35);

    this.renderFakeItems();
    this.renderInteractiveTooltips(poseStack, mouseX, mouseY);
    this.renderPlayerEntity();
  }

  /**
   * Prevents rendering labels on the screen.
   */
  @Override
  protected void renderLabels(@NotNull PoseStack matrixStack, int x, int y) {}

  /**
   * Renders fake items in the GUI.
   */
  private void renderFakeItems() {
    this.itemRenderer.renderAndDecorateFakeItem(new ItemStack(ModItems.MEDIUM_BLUE_BACKPACK.get()),
        this.leftPos + 6, this.topPos - 20);
    this.itemRenderer.renderAndDecorateFakeItem(new ItemStack(ModItems.PICKAXE.get()),
        this.leftPos + 36, this.topPos - 22);
  }

  /**
   * Renders tooltips for interactive elements based on mouse position.
   */
  private void renderInteractiveTooltips(PoseStack poseStack, int mouseX, int mouseY) {
    if (this.isMouseOver(this.leftPos, this.topPos - 28, 29, 28, mouseX, mouseY)) {
      this.renderTooltip(poseStack, new TranslatableComponent("inventory_inventory.information"), mouseX, mouseY);
    }
    if (this.isMouseOver(this.leftPos + 30, this.topPos - 28, 29, 28, mouseX, mouseY)) {
      this.renderTooltip(poseStack, new TranslatableComponent("inventory_crafting.information"), mouseX, mouseY);
    }
  }

  /**
   * Renders the player's 3D model in the crafting screen.
   */
  private void renderPlayerEntity() {
    if (this.minecraft != null && this.minecraft.player != null) {
      InventoryScreen.renderEntityInInventory(
          this.leftPos + 35, this.topPos + 72, 30,
          (this.leftPos + 35) - this.oldMouseX,
          (this.topPos + 75 - 50) - this.oldMouseY,
          this.minecraft.player
      );
    }
  }

  /**
   * Handles mouse click events.
   */
  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (this.isMouseOver(this.leftPos, this.topPos - 28, 29, 28, mouseX, mouseY)) {
      handleEquipmentMenuOpen();
      return true;
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  /**
   * Checks if the mouse is over a given rectangular area.
   */
  private boolean isMouseOver(int x, int y, int width, int height, double mouseX, double mouseY) {
    return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
  }

  /**
   * Opens the equipment menu when the appropriate area is clicked.
   */
  private void handleEquipmentMenuOpen() {
    if (this.minecraft != null && this.minecraft.player != null) {
      this.minecraft.player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.2F, 1.0F);
    }
    NetworkChannel.PLAY.getSimpleChannel().sendToServer(new OpenEquipmentMenuMessage());
  }

  /**
   * Called every tick to update the container.
   */
  @Override
  protected void containerTick() {
    super.containerTick();
    this.menu.updateResult();
  }
}

