package net.Ryaas.firstmod.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ModGameLogicManager {
    private static TelekinesisHandler telekinesisHandler = new TelekinesisHandler();


    public static TelekinesisHandler getTelekinesisHandler() {
        return telekinesisHandler;
    }
    public static void setTelekinesisHandler(TelekinesisHandler handler) {
        telekinesisHandler = handler;
    }

    public static List<Entity> findTargetsInFrontOfPlayer(ServerPlayer player, double range) {
        Vec3 lookVec = player.getLookAngle();
        Vec3 startVec = player.getEyePosition(1.0F); // Starting from the player's eye position for accuracy
        Vec3 endVec = startVec.add(lookVec.scale(range)); // Extending the look vector by the range

        AABB targetingBox = new AABB(startVec, endVec).inflate(0.5, 0.5, 0.5); // Create a box around the vector, inflate by 0.5 for width/height

        // Find all entities within the box, excluding the player
        List<Entity> targets = player.level().getEntities(player, targetingBox, entity -> entity != player && entity.isAlive());

        return targets;
    }
}
