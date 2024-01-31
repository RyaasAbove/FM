package net.Ryaas.firstmod.util;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TelekinesisHandler {
    private Map<UUID, Telekinesis> playerTelekinesisMap = new HashMap<>();

    // Private constructor to prevent external instantiation
    public TelekinesisHandler() {}

    public Telekinesis getOrCreateTelekinesis(ServerPlayer player) {
        return playerTelekinesisMap.computeIfAbsent(player.getUUID(), k -> new Telekinesis());
    }

    public void updateAll() {
        playerTelekinesisMap.values().forEach(Telekinesis::updateHeldEntities);
    }

    // Other methods...
}