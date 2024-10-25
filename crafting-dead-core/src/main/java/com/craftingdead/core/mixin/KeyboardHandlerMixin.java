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

package com.craftingdead.core.mixin;

import com.craftingdead.core.ServerConfig;
import com.craftingdead.core.world.effect.ModMobEffects;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.item.GunItem;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

  @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
  private void onKeyPress(long windowPointer, int key, int scancode, int action, int modifiers,
      CallbackInfo ci) {

    var minecraft = Minecraft.getInstance();
    var playerExtension = PlayerExtension.get(minecraft.player);

    if (playerExtension == null) {
      return;
    }

    int dropKey = minecraft.options.keyDrop.getKey().getValue();
    if (key == dropKey
        && playerExtension.isHandcuffed()) {
      ci.cancel();
    }

    int shiftKey = minecraft.options.keyShift.getKey().getValue();
    if (key == shiftKey
        && playerExtension.entity().hasEffect(ModMobEffects.PARACHUTE.get())) {
      ci.cancel();
    }

    int offhandKey = minecraft.options.keySwapOffhand.getKey().getValue();
    if (key == offhandKey
        && (playerExtension.entity().getMainHandItem().getItem() instanceof GunItem
        || !ServerConfig.instance.allowOffhandSwap.get())) {
      ci.cancel();
    }

    int togglePerspectiveKey = minecraft.options.keyTogglePerspective.getKey().getValue();
    if (key == togglePerspectiveKey
        && !ServerConfig.instance.allowTogglePerspective.get()) {
      ci.cancel();
    }
  }
}
