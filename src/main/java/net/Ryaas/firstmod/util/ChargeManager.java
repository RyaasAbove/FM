package net.Ryaas.firstmod.util;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.UUID;

public class ChargeManager {
    private static final HashMap<UUID, Integer> playerCharges = new HashMap<>();
    private static final int MAX_CHARGE = 50; // Default max charge, can be modified per player
    private int REPLENISH_RATE = 1;
    private static final HashMap<UUID, Integer> playerReplenishRates = new HashMap<>();

    public static void setPlayerCharge(ServerPlayer player, int charge) {
        playerCharges.put(player.getUUID(), charge);
    }

    public static int getPlayerCharge(ServerPlayer player) {
        return playerCharges.getOrDefault(player.getUUID(), 0);
    }

    public static void increaseMaxCharge(ServerPlayer player, int amount) {
        // Correctly increase the maximum charge capacity
        int currentMaxCharge = getPlayerMaxCharge(player);
        setPlayerMaxCharge(player, currentMaxCharge + amount);
    }

    // Method to call when checking advancements
    public static void updateChargeForAdvancements(ServerPlayer player) {
        if (player.server == null) return;

        int completedAdvancements = 0;
        for (Advancement advancement : player.server.getAdvancements().getAllAdvancements()) {
            AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
            if (progress.isDone()) {
                completedAdvancements++;
            }
        }

        int additionalMaxCharge = completedAdvancements * 10; // Modify the MAX_CHARGE based on completed advancements
        increaseMaxCharge(player, additionalMaxCharge);
    }


    private static final HashMap<UUID, Integer> playerMaxCharges = new HashMap<>();

    public static void setPlayerMaxCharge(ServerPlayer player, int maxCharge) {
        playerMaxCharges.put(player.getUUID(), maxCharge);
    }
    public static void RestoreCharges() {
        for (UUID playerId : playerCharges.keySet()) {
            int currentCharge = playerCharges.getOrDefault(playerId, 0);
            int maxCharge = playerMaxCharges.getOrDefault(playerId, 100); // Assume a default max charge
            int replenishRate = playerReplenishRates.getOrDefault(playerId, 1); // Default replenish rate if not set

            int newCharge = Math.min(currentCharge + replenishRate, maxCharge);
            playerCharges.put(playerId, newCharge);
        }
    }


    public static int getPlayerMaxCharge(ServerPlayer player) {
        return playerMaxCharges.getOrDefault(player.getUUID(), MAX_CHARGE);
    }
    public static void initializePlayerCharge(ServerPlayer player) {
        setPlayerCharge(player, 0); // Initialize with 0 charge
        setPlayerMaxCharge(player, MAX_CHARGE); // Initialize with base max charge
        updateChargeForAdvancements(player); // Then, adjust for advancements
    }

    // Example method that is called when the specific item is used
//    public void onItemUse(ServerPlayer player) {
//        // Adjust the player's replenish rate
//        ChargeManager.setPlayerReplenishRate(player, 2); // Example: Increase the replenish rate
//
//        // Optionally, schedule a task to reset the rate after a certain time
//        // This part would depend on how you manage scheduled tasks or events in your mod
//    }
}