package net.Ryaas.firstmod.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TelekinesisHandler {
    private final Map<UUID, Telekinesis> playerTelekinesisMap = new ConcurrentHashMap<>();
    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    private final Map<UUID, Entity> heldEntities = new HashMap<>();

    public TelekinesisHandler() {}

    public Telekinesis getOrCreateTelekinesis(ServerPlayer player) {
        try {
            return playerTelekinesisMap.computeIfAbsent(player.getUUID(), k -> new Telekinesis(server, this));
        } catch (Exception e) {
            return null; // Or handle this scenario appropriately
        }
    }
    public Entity removeHeldEntity(UUID playerUUID) {
        Entity entity = heldEntities.remove(playerUUID);
        if (entity != null) {
            entity.setNoGravity(false);
        }
        return entity;
    }

    public Telekinesis getTelekinesis(ServerPlayer player) {
        return playerTelekinesisMap.get(player.getUUID());
    }

    public void addHeldEntity(UUID playerUUID, Entity entity) {
        heldEntities.put(playerUUID, entity);
    }

    public void updateAllTelekinesis() {
        playerTelekinesisMap.values().forEach(Telekinesis::updateHeldEntities);
    }

    public void updateHeldEntities() {

        heldEntities.forEach((uuid, entity) -> {
            ServerPlayer player = server.getPlayerList().getPlayer(uuid);
            if (player != null) {
                Vec3 lookVec = player.getLookAngle();
                Vec3 newPosition = player.position().add(lookVec.scale(2.0)); // Example: 2 blocks in front

                // Update the entity's position
                // Note: Consider using teleport instead of setPos for instant movement without physics interference
                entity.setPos(newPosition.x, newPosition.y + 1.0, newPosition.z); // Adjust Y as needed

            }



        });


    }

    public void removePlayerTelekinesis(ServerPlayer player) {
        playerTelekinesisMap.remove(player.getUUID());
    }
}