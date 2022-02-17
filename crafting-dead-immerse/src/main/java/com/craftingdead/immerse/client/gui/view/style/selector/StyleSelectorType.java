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

package com.craftingdead.immerse.client.gui.view.style.selector;

public enum StyleSelectorType {

  WILDCARD(0),
  TYPE(1),
  CLASS(1_000),
  ID(1_000_000),
  PSEUDOCLASS(1_000),
  STRUCTURAL_PSEUDOCLASS(1_000);

  final int specificity;

  private StyleSelectorType(int specificity) {
    this.specificity = specificity;
  }

  public int getSpecificity() {
    return this.specificity;
  }
}
