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

package com.craftingdead.immerse.game.survival;

import com.craftingdead.core.network.SynchedData;
import com.craftingdead.core.world.entity.extension.PlayerExtension;
import com.craftingdead.core.world.entity.extension.PlayerHandler;
import com.craftingdead.immerse.game.GameTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.stats.Stats;
import net.minecraft.resources.ResourceLocation;

public class SurvivalPlayerHandler implements PlayerHandler {

  public static final ResourceLocation EXTENSION_ID = GameTypes.SURVIVAL.getId();

  private static final EntityDataAccessor<Integer> DAYS_SURVIVED =
      new EntityDataAccessor<>(0x00, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Integer> ZOMBIES_KILLED =
      new EntityDataAccessor<>(0x01, EntityDataSerializers.INT);
  private static final EntityDataAccessor<Integer> PLAYERS_KILLED =
      new EntityDataAccessor<>(0x02, EntityDataSerializers.INT);

  private final PlayerExtension<?> player;

  private final SynchedData dataManager = new SynchedData();

  public SurvivalPlayerHandler(PlayerExtension<?> player) {
    this.player = player;
    this.dataManager.register(DAYS_SURVIVED, 0);
    this.dataManager.register(ZOMBIES_KILLED, 0);
    this.dataManager.register(PLAYERS_KILLED, 0);
  }

  @Override
  public void tick() {
    if (this.player.getEntity() instanceof ServerPlayer) {
      int aliveTicks = ((ServerPlayer) this.player.getEntity()).getStats()
          .getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
      this.setDaysSurvived(aliveTicks / 20 / 60 / 20);
    }
  }

  @Override
  public boolean handleKill(Entity target) {
    if (target instanceof Zombie) {
      this.setZombiesKilled(this.getZombiesKilled() + 1);
    } else if (target instanceof ServerPlayer) {
      this.setPlayersKilled(this.getPlayersKilled() + 1);
    }
    return false;
  }

  public int getDaysSurvived() {
    return this.dataManager.get(DAYS_SURVIVED);
  }

  public void setDaysSurvived(int daysSurvived) {
    this.dataManager.set(DAYS_SURVIVED, daysSurvived);
  }

  public int getZombiesKilled() {
    return this.dataManager.get(ZOMBIES_KILLED);
  }

  public void setZombiesKilled(int zombiesKilled) {
    this.dataManager.set(ZOMBIES_KILLED, zombiesKilled);
  }

  public int getPlayersKilled() {
    return this.dataManager.get(PLAYERS_KILLED);
  }

  public void setPlayersKilled(int playersKilled) {
    this.dataManager.set(PLAYERS_KILLED, playersKilled);
  }

  @Override
  public boolean isCombatModeEnabled() {
    return false;
  }

  @Override
  public void copyFrom(PlayerExtension<?> that, boolean wasDeath) {
    if (!wasDeath) {
      that.getHandler(GameTypes.SURVIVAL.getId())
          .map(extension -> (SurvivalPlayerHandler) extension)
          .ifPresent(extension -> {
            this.setDaysSurvived(extension.getDaysSurvived());
            this.setZombiesKilled(extension.getZombiesKilled());
            this.setPlayersKilled(extension.getPlayersKilled());
          });
    }
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    nbt.putInt("zombiesKilled", this.getZombiesKilled());
    nbt.putInt("playersKilled", this.getPlayersKilled());
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    this.setZombiesKilled(nbt.getInt("zombiesKilled"));
    this.setPlayersKilled(nbt.getInt("playersKilled"));
  }

  @Override
  public void encode(FriendlyByteBuf out, boolean writeAll) {
    SynchedData.pack(writeAll
        ? this.dataManager.getAll()
        : this.dataManager.packDirty(), out);
  }

  @Override
  public void decode(FriendlyByteBuf in) {
    this.dataManager.assignValues(SynchedData.unpack(in));
  }

  @Override
  public boolean requiresSync() {
    return this.dataManager.isDirty();
  }
}
