package net.Ryaas.firstmod.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlackHoleManager {
    private static BlackHoleManager instance;
    // Change to map player UUIDs to Black Hole entity UUIDs
    private final Map<UUID, UUID> playerToBlackHoleMap = new HashMap<>();



    private BlackHoleManager() {}

    public static synchronized BlackHoleManager getInstance() {
        if (instance == null) {
            instance = new BlackHoleManager();
        }
        return instance;
    }

    // Adjust parameter types to accept and store UUIDs
    public void addBlackHole(UUID playerUUID, UUID blackHoleUUID) {
        playerToBlackHoleMap.put(playerUUID, blackHoleUUID);
    }

    // Change method name and return type to reflect that it returns a UUID
    public UUID getBlackHoleUUIDByPlayer(UUID playerUUID) {
        return playerToBlackHoleMap.get(playerUUID);
    }


    public void removeBlackHole(UUID playerUUID) {
        playerToBlackHoleMap.remove(playerUUID);
    }
}