package net.Ryaas.firstmod.util;

import net.Ryaas.firstmod.Networking.ModNetworking;
import net.Ryaas.firstmod.Networking.packet.SprayPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerHealingManager {
    public static final float RECHARGE_RATE = 1.0F;
    private static final Map<UUID, Float> playerHealingEnergy = new HashMap<>();
    private static final float MAX_HEALING_ENERGY = 100.0f; // Maximum healing energy

    // Initialize player's healing energy
    public static void initializePlayerEnergy(UUID playerId) {
        playerHealingEnergy.put(playerId, MAX_HEALING_ENERGY);
    }

    // Get the current energy level for a player
    public static float getPlayerEnergy(UUID playerId) {
        return playerHealingEnergy.getOrDefault(playerId, 0.0f);
    }

    public static void sendSpray(ServerPlayer player) {
        // Method parameters (e.g., particle count, spread, and other details) should be decided based on your specific requirements.
        SprayPacket packet = new SprayPacket(player.position(), 50, 5.0); // This needs a correct constructor in SprayPacket for position, particles count, and box size.
        double radius = 50.0; // 50 blocks radius

        // Assuming you have some ModNetworking setup to get your networking channel
        ModNetworking.getChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getX(), player.getY(), player.getZ(), radius, player.level().dimension())), packet);
    }



    // Use some of the player's healing energy
    public static void useEnergy(UUID playerId, float amount) {
        float currentEnergy = getPlayerEnergy(playerId);
        float newEnergy = Math.max(0.0f, currentEnergy - amount);
        playerHealingEnergy.put(playerId, newEnergy);
        System.out.println("Using energy for player " + playerId + ": " + currentEnergy + " -> " + newEnergy); // Debug log
    }

    // Recharge player's healing energy
    public static void rechargeEnergy(UUID playerId, float amount) {
        float currentEnergy = getPlayerEnergy(playerId);
        float newEnergy = Math.min(MAX_HEALING_ENERGY, currentEnergy + amount);
        playerHealingEnergy.put(playerId, newEnergy);
        System.out.println("Recharging energy for player " + playerId + ": " + currentEnergy + " -> " + newEnergy); // Debug log
    }
}
