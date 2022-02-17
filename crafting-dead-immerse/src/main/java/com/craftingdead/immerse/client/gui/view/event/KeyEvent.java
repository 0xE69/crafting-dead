/*
 * Crafting Dead
 * Copyright (C) 2021  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.gui.view.event;

import net.minecraftforge.eventbus.api.Event;

public class KeyEvent extends Event {

  private final int key;
  private final int scancode;
  private final int action;
  private final int mods;

  public KeyEvent(int key, int scancode, int action, int mods) {
    this.key = key;
    this.scancode = scancode;
    this.action = action;
    this.mods = mods;
  }

  public int getKey() {
    return this.key;
  }

  public int getScancode() {
    return this.scancode;
  }

  public int getAction() {
    return this.action;
  }

  public int getMods() {
    return this.mods;
  }
}

