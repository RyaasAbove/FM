package net.Ryaas.firstmod.util;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class RDModifier {
    public void updatePlayerReachDistance(Player player) {
        CompoundTag playerData = player.getPersistentData();
        CompoundTag data = playerData.getCompound(Player.PERSISTED_NBT_TAG); // Ensures we're working within the player's persistent data

        // Assuming "reachModifier" is the key for your specific integer
        int reachModifier = data.getInt("reachModifier");

        if (reachModifier == 1) {
            // Code to increase reach distance to 30
        } else {
            // Reset reach distance or leave it at default
        }
    }

}