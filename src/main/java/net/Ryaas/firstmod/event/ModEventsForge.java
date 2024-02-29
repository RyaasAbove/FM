package net.Ryaas.firstmod.event;

import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.Networking.ModNetworking;
import net.Ryaas.firstmod.Networking.packet.HealingDome;
import net.Ryaas.firstmod.Networking.packet.Indicator;
import net.Ryaas.firstmod.util.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = FirstMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEventsForge {
    public static final ModGameLogicManager gameLogicManager = FirstMod.getGameLogicManager(); // Example method to get the manager
    public static TelekinesisHandler telekinesisHandler;

    public static void setTelekinesisHandler(TelekinesisHandler handler) {
        telekinesisHandler = handler;
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        TelekinesisHandler telekinesisHandler = new TelekinesisHandler();

        // If you need to store the handler for later use
        ModGameLogicManager.setTelekinesisHandler(telekinesisHandler);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            TelekinesisHandler handler = ModGameLogicManager.getTelekinesisHandler();

            if (handler != null) {
                handler.updateAllTelekinesis();

            }

            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            for (ServerLevel level : server.getAllLevels()) {
                for (ServerPlayer player : level.players()) {
                    CompoundTag playerData = player.getPersistentData();

                    if (passiveManager.hasAbilitySetOne(player) && server.getTickCount() % 20 == 0) {
                        passiveManager.addXpSlowly(player);
                    }
                    if (passiveManager.hasAbilitySetTwo(player) && server.getTickCount() % 20 == 0) {
                        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 53, 1, false, false, true));
                    }

                    // Only recharge energy for players with "AbilitySet" set to 2
                    if (playerData.contains("AbilitySet") && playerData.getInt("AbilitySet") == 2) {
                        UUID playerId = player.getUUID();
                        PlayerHealingManager.rechargeEnergy(playerId, PlayerHealingManager.RECHARGE_RATE / 20.0f);
                    }
                    if (PlayerHealingManager.isHealingAbilityActive(player.getUUID())) {

                        PlayerHealingManager.applyContinuousHealing(player);
//
//                        PlayerHealingManager.sendSpray(player);
//
//                        PlayerHealingManager.createAndSendSwirlingBeam(player);
                        PlayerHealingManager.createHealingTendrils(player);
                    }
                    for (ServerLevel level1: server.getAllLevels()) {
                        for (ServerPlayer player1: level.players()) {

                            // Check if the player has the "AbilitySet" key set to 2
                            if (playerData.contains("AbilitySet") && playerData.getInt("AbilitySet") == 2) {
                                // Check if a dome is active for this player
                                if (HealingDome.isActiveForPlayer(player)) {
                                    // Decrement the dome's active time and spawn particles if necessary
                                    HealingDome.updateDomeForPlayer(player);

                                    // If it's the right tick, spawn dome particles
                                    if (server.getTickCount() % 4 == 0) {
                                        HealingDome.spawnDomeParticles(player);
                                    }
                                }
                            }


                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        if (player != null) {
            ChargeManager.initializePlayerCharge(player);
        }


        CompoundTag playerData = player.getPersistentData();

        // Check if the "AbilitySet" is set to 2
        if (playerData.contains("AbilitySet") && playerData.getInt("AbilitySet") == 2) {
            UUID playerId = player.getUUID();
            PlayerHealingManager.initializePlayerEnergy(playerId);
        }
    }

    private static void spawnCooldownIndicatorParticles(ServerPlayer player) {
        boolean isCooldownActive = !CooldownManager.hasCooldown(player);
        ParticleOptions particle = isCooldownActive ? ParticleTypes.SMOKE : ParticleTypes.SOUL_FIRE_FLAME;

        // Spawn particles around the player
        if (isCooldownActive) {
            // Cooldown is ready, spawn soul fire particles
            Indicator packet = new Indicator(player.getX(), player.getY() + 1.0, player.getZ(), 0);
            ModNetworking.getChannel().send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), packet);
        } else {

            Indicator packet = new Indicator(player.getX()+0.25, player.getY() + 1.5, player.getZ(), 1);
            ModNetworking.getChannel().send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), packet);

        }


    }
    private void applyRegeneration(ServerPlayer player) {
        // Check if the player already has the effect and its level
        if (!player.hasEffect(MobEffects.REGENERATION) || player.getEffect(MobEffects.REGENERATION).getAmplifier() < 1) {
            // Apply a minor regeneration effect for 5 seconds with amplifier 0 (Regeneration I)
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0, false, false, true));
        }
    }
}