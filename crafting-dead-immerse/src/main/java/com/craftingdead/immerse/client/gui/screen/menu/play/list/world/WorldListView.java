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

package com.craftingdead.immerse.client.gui.screen.menu.play.list.world;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.KeyFrames;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import com.craftingdead.immerse.client.gui.screen.Theme;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.storage.LevelStorageException;
import sm0keysa1m0n.bliss.Animation;
import sm0keysa1m0n.bliss.minecraft.view.MinecraftViewScreen;
import sm0keysa1m0n.bliss.style.Percentage;
import sm0keysa1m0n.bliss.style.States;
import sm0keysa1m0n.bliss.view.ParentView;
import sm0keysa1m0n.bliss.view.View;

public class WorldListView extends ParentView {

  private static final Logger logger = LogUtils.getLogger();

  private final ParentView listView;

  @Nullable
  private WorldItemView selectedItem;

  private final View playButton;
  private final View editButton;
  private final View deleteButton;
  private final View recreateButton;

  public WorldListView() {
    super(new Properties());

    this.listView = new ParentView(new Properties().styleClasses("list-content"));
    this.loadWorlds();

    this.playButton = Theme.createGreenButton(
        new TranslatableComponent("view.world_list.button.play"),
        () -> this.getSelectedItem().ifPresent(WorldItemView::joinWorld));
    this.playButton.setEnabled(false);

    var createButton = Theme.createBlueButton(
        new TranslatableComponent("view.world_list.button.create"),
        () -> ((MinecraftViewScreen) this.getScreen()).keepOpenAndSetScreen(
            CreateWorldScreen.createFresh((MinecraftViewScreen) this.getScreen())));

    this.editButton = Theme.createBlueButton(
        new TranslatableComponent("view.world_list.button.edit"),
        () -> this.getSelectedItem().ifPresent(WorldItemView::editWorld));
    this.editButton.setEnabled(false);

    this.deleteButton = Theme.createRedButton(
        new TranslatableComponent("view.world_list.button.delete"),
        () -> this.getSelectedItem().ifPresent(WorldItemView::deleteWorld));
    this.deleteButton.setEnabled(false);

    this.recreateButton = Theme.createBlueButton(
        new TranslatableComponent("view.world_list.button.recreate"),
        () -> this.getSelectedItem().ifPresent(WorldItemView::recreateWorld));
    this.recreateButton.setEnabled(false);

    var controls = new ParentView(new Properties().styleClasses("list-controls"));

    var firstRow = new ParentView(new Properties());
    firstRow.addChild(this.playButton);
    firstRow.addChild(createButton);

    var secondRow = new ParentView(new Properties());
    secondRow.addChild(this.deleteButton);
    secondRow.addChild(this.editButton);
    secondRow.addChild(this.recreateButton);

    controls.addChild(firstRow);
    controls.addChild(secondRow);

    this.addChild(this.listView);
    this.addChild(controls);

  }

  @Override
  public void added() {
    super.added();
    int delay = 0;
    for (var view : this.listView.getChildren()) {
      new Animator.Builder()
          .addTarget(Animation.forProperty(view.getStyle().opacity)
              .keyFrames(new KeyFrames.Builder<>(Percentage.ZERO)
                  .addFrame(Percentage.ONE_HUNDRED)
                  .build())
              .build())
          .setStartDelay(delay, TimeUnit.MILLISECONDS)
          .setDuration(250L, TimeUnit.MILLISECONDS)
          .build()
          .start();
      delay += 150;
    }
  }

  public void setSelectedItem(@Nullable WorldItemView selectedItem) {
    if (this.selectedItem == selectedItem) {
      return;
    }
    if (this.selectedItem != null) {
      this.selectedItem.getStyleManager().removeState(States.CHECKED);
      this.selectedItem.getStyleManager().notifyListeners();
    }
    this.selectedItem = selectedItem;
    if (selectedItem != null) {
      this.selectedItem.getStyleManager().addState(States.CHECKED);
      this.selectedItem.getStyleManager().notifyListeners();
    }
    var enabled = this.selectedItem != null;
    this.playButton.setEnabled(enabled);
    this.editButton.setEnabled(enabled);
    this.deleteButton.setEnabled(enabled);
    this.recreateButton.setEnabled(enabled);
  }

  private void loadWorlds() {
    try {
      var levelList = Minecraft.getInstance().getLevelSource().getLevelList();
      Collections.sort(levelList);
      for (var levelSummary : levelList) {
        this.listView.addChild(new WorldItemView(levelSummary, this));
      }
    } catch (LevelStorageException e) {
      logger.error("Unable to load save list", e);
    }
  }

  public void reloadWorlds() {
    this.listView.clearChildren();
    this.setSelectedItem(null);
    this.loadWorlds();
  }

  private Optional<WorldItemView> getSelectedItem() {
    return Optional.ofNullable(this.selectedItem);
  }
}
