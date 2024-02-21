package net.Ryaas.firstmod.util;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.nbt.CompoundTag;

@Mod.EventBusSubscriber(modid = "firstmod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerDataPersistence {

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        // Check if this is a death clone, not a dimension change clone
        if (event.isWasDeath()) {
            CompoundTag oldPlayerData = event.getOriginal().getPersistentData();
            CompoundTag newPlayerData = event.getEntity().getPersistentData();

            // Check if the old player had the "AbilitySet" data
            if (oldPlayerData.contains("AbilitySet")) {
                int abilitySet = oldPlayerData.getInt("AbilitySet");
                // Copy the "AbilitySet" data to the new player
                newPlayerData.putInt("AbilitySet", abilitySet);

            }
        }
    }
}