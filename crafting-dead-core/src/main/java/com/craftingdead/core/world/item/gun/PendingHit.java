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

package com.craftingdead.core.world.item.gun;

import com.craftingdead.core.world.entity.extension.EntitySnapshot;

public class PendingHit {

  private final byte tickOffset;
  private final EntitySnapshot playerSnapshot;
  private final EntitySnapshot hitSnapshot;
  private final long randomSeed;

  public PendingHit(byte tickOffset, EntitySnapshot playerSnapshot, EntitySnapshot hitSnapshot,
      long randomSeed) {
    this.tickOffset = tickOffset;
    this.playerSnapshot = playerSnapshot;
    this.hitSnapshot = hitSnapshot;
    this.randomSeed = randomSeed;
  }

  public byte getTickOffset() {
    return this.tickOffset;
  }

  public EntitySnapshot getPlayerSnapshot() {
    return this.playerSnapshot;
  }

  public EntitySnapshot getHitSnapshot() {
    return this.hitSnapshot;
  }

  public long getRandomSeed() {
    return this.randomSeed;
  }
}
