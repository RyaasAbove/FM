package net.Ryaas.firstmod.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class passiveManager {
    // Check if the player has ability set 1
    public static boolean hasAbilitySetOne(Player player) {
        CompoundTag playerData = player.getPersistentData();
        // Assuming you store this under player's PersistentData in a specific CompoundTag
        // Adjust "AbilitySet" and "1" according to your actual data structure
        return playerData.getInt("AbilitySet") == 1;
    }

    // Method to add XP to a player slowly
    public static void addXpSlowly(Player player) {
        player.giveExperiencePoints(1); // Adjust the amount of XP as needed
    }
}

