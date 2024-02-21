package net.Ryaas.firstmod.util;

import net.Ryaas.firstmod.Networking.ModNetworking;
import net.Ryaas.firstmod.Networking.packet.SprayPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerHealingManager {
    public static final float RECHARGE_RATE = 1.0F;
    private static final Map<UUID, Float> playerHealingEnergy = new HashMap<>();
    private static final float MAX_HEALING_ENERGY = 100.0f; // Maximum healing energy

    // Initialize player's healing energy to max upon first use or reset
    public static void initializePlayerEnergy(UUID playerId) {
        playerHealingEnergy.put(playerId, MAX_HEALING_ENERGY);
    }

    // Get the current energy level for a player
    public static float getPlayerEnergy(UUID playerId) {
        return playerHealingEnergy.getOrDefault(playerId, 0.0f);
    }

    // Method to visually represent healing using a particle spray
    public static void sendSpray(ServerPlayer player) {
        Vec3 startPosition = player.position();
        int particleCount = 50; // Adjust based on the desired density of the spray
        double coneDepth = 5.0; // This could be interpreted as the reach or spread of the spray
        float yaw = player.getYRot(); // Player's yaw
        float pitch = player.getXRot(); // Player's pitch

        SprayPacket packet = new SprayPacket(startPosition, particleCount, coneDepth, yaw, pitch);
        double radius = 50.0; // Determines how far the spray effect should be visible

        // Send the packet to all players within a certain radius of the player
        ModNetworking.getChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(startPosition.x, startPosition.y, startPosition.z, radius, player.level().dimension())), packet);
    }

    // Use some of the player's healing energy, ensuring it doesn't drop below 0
    public static void useEnergy(UUID playerId, float amount) {
        float currentEnergy = getPlayerEnergy(playerId);
        float newEnergy = Math.max(0.0f, currentEnergy - amount);
        playerHealingEnergy.put(playerId, newEnergy);
    }

    // Recharge player's healing energy, ensuring it doesn't exceed the max
    public static void rechargeEnergy(UUID playerId, float amount) {
        float currentEnergy = getPlayerEnergy(playerId);
        float newEnergy = Math.min(MAX_HEALING_ENERGY, currentEnergy + amount);
        playerHealingEnergy.put(playerId, newEnergy);
    }
}