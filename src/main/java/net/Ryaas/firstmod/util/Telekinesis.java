package net.Ryaas.firstmod.util;

import net.Ryaas.firstmod.Networking.ModNetworking;
import net.Ryaas.firstmod.Networking.packet.particlepacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;


public class Telekinesis {
    private Entity heldEntity;
    private MinecraftServer server;
    private ServerPlayer player;
    private TelekinesisHandler handler = ModGameLogicManager.getTelekinesisHandler();

    public static TelekinesisHandler telekinesisHandler;
    public Telekinesis(MinecraftServer server, TelekinesisHandler handler) {
        this.server = server;
        this.handler = handler;

    }


    private final Set<UUID> playersWithActiveAbility = new HashSet<>();
    private final Map<UUID, Entity> heldEntities = new HashMap<>();


    public void activateAbility(ServerPlayer player) {
        UUID playerUUID = player.getUUID();
        if (!playersWithActiveAbility.contains(playerUUID)) {
            Entity target = getLookedAtEntity(player);
            if (target != null) {
                System.out.println("found mob");
                heldEntities.put(playerUUID, target);
                target.setNoGravity(true);
                playersWithActiveAbility.add(playerUUID);
            }
        } else {
            deactivateAbility(player);
        }
    }

    public void updateHeldEntities() {
        if(this.server != null)
        heldEntities.forEach((playerUUID, entity) -> {
            ServerPlayer player = server.getPlayerList().getPlayer(playerUUID);
            if (player != null && entity.isAlive()) {
                Vec3 lookVec = player.getLookAngle();
                Vec3 eyePosition = player.getEyePosition(1.0F);
                double distanceInFront = 2.0; // Distance in front of the player
                Vec3 newPosition = eyePosition.add(lookVec.scale(distanceInFront));
                entity.setPos(newPosition.x, newPosition.y, newPosition.z);
                System.out.println("updating pos");
            }
        });
    }

    public void deactivateAbility(ServerPlayer player) {
        UUID playerUUID = player.getUUID();
        Entity entity = heldEntities.remove(playerUUID);
        if (entity != null) {
            entity.setNoGravity(false);
        }
        playersWithActiveAbility.remove(playerUUID);
    }


    private Entity getLookedAtEntity(Player player) {


        // Sending to all clients
        List<Entity> nearbyEntities = player.level().getEntities(player, player.getBoundingBox().inflate(5.0)); // 5.0 is the search radius



        Vec3 eyePosition = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getLookAngle();
        Vec3 rayTraceEnd = eyePosition.add(lookVec.scale(5.0)); // 5 blocks in front of the player
        AABB searchArea = new AABB(eyePosition, rayTraceEnd);
        sendRayTracePacket(player, eyePosition, lookVec, 5.0); //makes the debug ray trace particles
        for (Entity entity : nearbyEntities) {
            AABB entityBoundingBox = entity.getBoundingBox();
            Optional<Vec3> hit = entityBoundingBox.clip(eyePosition, rayTraceEnd);
            if (hit.isPresent()) {
                // Found an entity that intersects with the ray
                return entity;
            }
        }

        return null;
    }
    private void sendRayTracePacket(Player player, Vec3 start, Vec3 direction, double distance) {
        if (!player.level().isClientSide) { // Ensure this is server-side
            particlepacket packet = new particlepacket(start, direction, distance);
            // Assuming you have a way to send to a specific player. Adjust as necessary.
            ModNetworking.getChannel().send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), packet);
        }
    }

    // Additional utility methods as needed
}