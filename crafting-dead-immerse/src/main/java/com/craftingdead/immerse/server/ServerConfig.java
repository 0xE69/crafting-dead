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

package com.craftingdead.immerse.server;

import java.util.List;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {

  public final ForgeConfigSpec.ConfigValue<List<? extends String>> gameRotation;
  public final ForgeConfigSpec.EnumValue<NametagMode> nametagMode;

  public ServerConfig(ForgeConfigSpec.Builder builder) {
    builder.push("server");
    {
      this.gameRotation = builder.translation("options.craftingdeadimmerse.server.game_rotation")
          .defineList("game_rotation", ImmutableList.of(), gameName -> true);
    }
    builder.pop();

    builder.push("gameplay");
    {
      this.nametagMode = builder
          .translation("options.craftingdeadimmerse.gameplay.nametag_mode")
          .comment("Allows to define how nametags should be shown to the player.",
              "DEFAULT: Default minecraft behaviour",
              "LOOK: The player must be looking at another player to see it's nametag",
              "HIDE_PLAYER: Hide player nametags",
              "HIDE_ALL: Hide all nametags")
          .defineEnum("nametag_mode", NametagMode.DEFAULT);
    }
    builder.pop();
  }

  public enum NametagMode {
    DEFAULT, LOOK, HIDE_PLAYER, HIDE_ALL
  }
}
