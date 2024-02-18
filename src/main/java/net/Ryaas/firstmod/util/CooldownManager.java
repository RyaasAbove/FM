package net.Ryaas.firstmod.util;

import net.Ryaas.firstmod.Networking.ModNetworking;
import net.Ryaas.firstmod.Networking.packet.ToggleIndicator;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {
    private static final HashMap<UUID, Boolean> shouldSpawnParticles = new HashMap<>();
    private static final HashMap<UUID, Integer> playerParticleTypes = new HashMap<>();




    private static final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public static void setCooldown(Player player, long cooldownTicks) {
        cooldowns.put(player.getUUID(), player.level().getGameTime() + cooldownTicks);
    }

    public static boolean hasCooldown(Player player) {
        Long cooldownEnd = cooldowns.getOrDefault(player.getUUID(), 0L);
        return player.level().getGameTime() < cooldownEnd;
    }

    private static final HashMap<UUID, Boolean> particleToggle = new HashMap<>();

    // Toggle whether particles should spawn for a player
    public static void toggleParticleSpawning(UUID playerID) {
        boolean currentState = shouldSpawnParticles.getOrDefault(playerID, true); // Default to true if not set
        shouldSpawnParticles.put(playerID, !currentState);
    }

    // Check if particles should spawn for a player
    public static boolean shouldSpawnParticles(UUID playerID) {
        return shouldSpawnParticles.getOrDefault(playerID, true); // Default to true
    }
    // Server-side toggle method
    public static void toggleParticleSpawning(ServerPlayer player) {
        UUID playerID = player.getUUID();
        boolean newVisibility = !shouldSpawnParticles.getOrDefault(playerID, true);
        shouldSpawnParticles.put(playerID, newVisibility);
        // Send packet to client to update visibility state
        ModNetworking.getChannel().send(PacketDistributor.PLAYER.with(() -> player), new ToggleIndicator());
    }

    public static void setParticleType(UUID playerID, int particleType) {
        playerParticleTypes.put(playerID, particleType);
    }

    // Retrieve the particle type for a player
    public static int getParticleType(UUID playerID) {
        return playerParticleTypes.getOrDefault(playerID, 0); // Default to 0 or some default particle type
    }
}