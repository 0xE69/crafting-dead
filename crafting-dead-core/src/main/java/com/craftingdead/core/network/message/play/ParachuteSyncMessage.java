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

package com.craftingdead.core.network.message.play;

import com.craftingdead.core.world.effect.ModMobEffects;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

public class ParachuteSyncMessage {

  private final int entityId;
  private final boolean hasParachute;

  public ParachuteSyncMessage(int entityId, boolean hasParachute) {
    this.entityId = entityId;
    this.hasParachute = hasParachute;
  }

  public static void encode(ParachuteSyncMessage packet, FriendlyByteBuf buf) {
    buf.writeInt(packet.entityId);
    buf.writeBoolean(packet.hasParachute);
  }

  public static ParachuteSyncMessage decode(FriendlyByteBuf buf) {
    return new ParachuteSyncMessage(buf.readInt(), buf.readBoolean());
  }

  public static void handle(ParachuteSyncMessage packet, Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> {
      assert Minecraft.getInstance().level != null;
      Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId);
      if (entity instanceof LivingEntity livingEntity) {
        if (packet.hasParachute) {
          livingEntity.addEffect(new MobEffectInstance(ModMobEffects.PARACHUTE.get()));
        } else {
          livingEntity.removeEffect(ModMobEffects.PARACHUTE.get());
        }
      }
    });
    context.get().setPacketHandled(true);
  }
}
