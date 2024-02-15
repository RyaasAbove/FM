package net.Ryaas.firstmod.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlackHoleManager {
    private static BlackHoleManager instance;
    // Change to map player UUIDs to Black Hole entity UUIDs
    private final Map<UUID, UUID> playerToBlackHoleMap = new HashMap<>();

    public static UUID playerUuid; // Assuming static for global access; adjust based on your design




    private BlackHoleManager() {}

    public static synchronized BlackHoleManager getInstance() {
        if (instance == null) {
            instance = new BlackHoleManager();
        }
        return instance;
    }

    public static void setPlayer(Player player) {
        playerUuid = player.getUUID();
    }

    // Adjust parameter types to accept and store UUIDs
    public void addBlackHole(UUID playerUUID, UUID blackHoleUUID) {
        playerToBlackHoleMap.put(playerUUID, blackHoleUUID);
    }
    public static UUID getPlayerUuid() {
        return playerUuid;
    }

    // If you need to get the PlayerEntity from the UUID (server-side only)
    public static Player getPlayer(ServerLevel serverLevel) {
        return serverLevel.getServer().getPlayerList().getPlayer(playerUuid);
    }

    // Change method name and return type to reflect that it returns a UUID
    public UUID getBlackHoleUUIDByPlayer(UUID playerUUID) {
        return playerToBlackHoleMap.get(playerUUID);
    }


    public void removeBlackHole(UUID playerUUID) {
        playerToBlackHoleMap.remove(playerUUID);
    }
}