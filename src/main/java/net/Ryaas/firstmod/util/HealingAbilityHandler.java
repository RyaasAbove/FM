package net.Ryaas.firstmod.util;

import net.Ryaas.firstmod.Networking.ModNetworking;
import net.Ryaas.firstmod.Networking.packet.SprayPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.stream.Collectors;

public class HealingAbilityHandler {
    private static final float HEALING_COST = 10.0f; // Energy cost to use the healing ability
    private static final float HEAL_AMOUNT = 1.0F; // The amount of health to heal

    // Method to activate the healing ability for a player
    public static void activateHealing(ServerPlayer player) {
        // Check if the player has enough energy
        if (PlayerHealingManager.getPlayerEnergy(player.getUUID()) >= 10.0f) {
            PlayerHealingManager.useEnergy(player.getUUID(), 10.0f); // Deduct energy cost for healing

            // Define the healing logic
            List<LivingEntity> healedEntities = healEntitiesAroundPlayer(player);

            // Assuming a method that sends a particle effect packet to all players nearby
            // for each entity that was healed
            for (LivingEntity healedEntity : healedEntities) {
                // Example: Send particles to visualize healing at the entity's location
                sendHealingParticles(player.level(), healedEntity.position());
            }
        }
    }

    // Method to send a packet to the client to trigger particle effects
    public static void sendHealingParticles(Level level, Vec3 position) {
        // Implementation depends on your packet system
        // This would create a packet with the position and send it to all nearby players
        SprayPacket packet = new SprayPacket(position, 50, 5.0); // Example parameters
        PacketDistributor.TargetPoint targetPoint = new PacketDistributor.TargetPoint(position.x, position.y, position.z, 50, level.dimension());
        ModNetworking.getChannel().send(PacketDistributor.NEAR.with(() -> targetPoint), packet);
    }

    // Method to heal entities around the player
    private static List<LivingEntity> healEntitiesAroundPlayer(ServerPlayer player) {
        final double coneRadius = 5.0; // Radius of the cone at its furthest extent
        final double coneDepth = 10.0; // How far the cone extends
        final Vec3 startPosition = player.position().add(0, player.getEyeHeight(), 0);
        final Vec3 lookVec = player.getLookAngle();

        // Define the bounding box based on the cone's specifications
        Vec3 endPosition = startPosition.add(lookVec.scale(coneDepth));
        AABB areaOfEffect = new AABB(startPosition, endPosition).inflate(coneRadius);

        // Filter entities within the bounding box and then further by cone shape
        List<LivingEntity> entitiesToHeal = player.level().getEntitiesOfClass(LivingEntity.class, areaOfEffect)
                .stream()
                .filter(e -> e instanceof LivingEntity && !e.equals(player))
                .filter(e -> isInCone(e.position().subtract(startPosition), lookVec, coneDepth, Math.toRadians(coneRadius)))
                .collect(Collectors.toList());

        // Apply healing
        entitiesToHeal.forEach(e -> {
            e.heal(HEAL_AMOUNT);


        // If entities were healed, send particle effects

            sendHealingParticles(player.level(), e.position());
         });
        return entitiesToHeal;
    }
    // Helper method to check if a position is within the cone
    private static boolean isInCone(Vec3 posVec, Vec3 lookVec, double coneDepth, double coneAngleRad) {
        double distanceSquared = posVec.lengthSqr();
        if (distanceSquared > coneDepth * coneDepth) return false; // Beyond cone depth

        double dot = posVec.normalize().dot(lookVec);
        double angle = Math.acos(dot);
        return angle <= coneAngleRad;
    }
}

