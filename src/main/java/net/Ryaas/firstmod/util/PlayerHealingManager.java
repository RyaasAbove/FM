package net.Ryaas.firstmod.util;

import net.Ryaas.firstmod.Networking.ModNetworking;
import net.Ryaas.firstmod.Networking.packet.Swirl;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerHealingManager {
    public static final float RECHARGE_RATE = 1.0F;
    private static final Map<UUID, Float> playerHealingEnergy = new HashMap<>();
    private static final Map<UUID, Boolean> healingAbilityActive = new HashMap<>(); // Track active state of healing ability
    private static final float MAX_HEALING_ENERGY = 100.0f; // Maximum healing energy

    private static float HEAL_AMOUNT = 0.25f;
    private static Random random = new Random();


    private static Vec3 previousControlPoint = null; // Store the last used control point for interpolation



    // Initialize player's healing energy to max upon first use or reset
    public static void initializePlayerEnergy(UUID playerId) {
        playerHealingEnergy.put(playerId, MAX_HEALING_ENERGY);
    }

    // Get the current energy level for a player
    public static float getPlayerEnergy(UUID playerId) {
        return playerHealingEnergy.getOrDefault(playerId, 0.0f);
    }

    // Toggle the healing ability state for a player
    public static void toggleHealingAbility(UUID playerId) {
        boolean isActive = healingAbilityActive.getOrDefault(playerId, false);
        healingAbilityActive.put(playerId, !isActive); // Toggle the state

        // Optionally initialize energy if not already done
        playerHealingEnergy.putIfAbsent(playerId, MAX_HEALING_ENERGY);
    }

    // Check if the healing ability is active for a player
    public static boolean isHealingAbilityActive(UUID playerId) {
        return healingAbilityActive.getOrDefault(playerId, false);
    }

    // Method to visually represent healing using a particle spray
//    public static void sendSpray(ServerPlayer player) {
//        if (!isHealingAbilityActive(player.getUUID())) return; // Check if ability is active before spraying
//
//        Vec3 startPosition = player.position();
//        int particleCount = 50; // Adjust based on the desired density of the spray
//        double coneDepth = 10.0; // This could be interpreted as the reach or spread of the spray
//        float yaw = player.getYRot(); // Player's yaw
//        float pitch = player.getXRot(); // Player's pitch
//
//        SprayPacket packet = new SprayPacket(startPosition, particleCount, coneDepth, yaw, pitch);
//        double radius = 50.0; // Determines how far the spray effect should be visible
//
//        // Send the packet to all players within a certain radius of the player
//        ModNetworking.getChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(startPosition.x, startPosition.y, startPosition.z, radius, player.level().dimension())), packet);
//    }

    // Use some of the player's healing energy, ensuring it doesn't drop below 0
    public static void useEnergy(UUID playerId, float amount) {
        float currentEnergy = getPlayerEnergy(playerId);
        float newEnergy = Math.max(0.0f, currentEnergy - amount);
        playerHealingEnergy.put(playerId, newEnergy);
    }

    // Recharge player's healing energy, ensuring it doesn't exceed the max
    public static void rechargeEnergy(UUID playerId, float amount) {
        float currentEnergy = getPlayerEnergy(playerId);
        float newEnergy = Math.min(MAX_HEALING_ENERGY, currentEnergy + amount);
        playerHealingEnergy.put(playerId, newEnergy);
    }

    public static void applyContinuousHealing(ServerPlayer player) {
        if (!isHealingAbilityActive(player.getUUID())) {
            return; // Exit if the healing ability is not active
        }

        // Define the parameters for the cone in which entities will be healed
        final double coneRadius = 5.0; // The radius of the cone at its farthest point from the player
        final double coneDepth = 10.0; // How far in front of the player the cone extends
        final Vec3 startPosition = player.position().add(0, player.getEyeHeight(), 0); // Starting point of the cone
        final Vec3 lookVec = player.getLookAngle(); // The direction the player is looking

        // Calculate the end position of the cone based on its depth
        Vec3 endPosition = startPosition.add(lookVec.scale(coneDepth));
        AABB areaOfEffect = new AABB(startPosition, endPosition).inflate(coneRadius);

        // Find entities within the cone
        List<LivingEntity> entitiesToHeal = player.level().getEntitiesOfClass(LivingEntity.class, areaOfEffect)
                .stream()
                .filter(e -> e != player) // Exclude the player
                .filter(e -> isInCone(e.position().subtract(startPosition), lookVec, coneDepth, Math.toRadians(45))) // 45 degrees for example
                .collect(Collectors.toList());

        // Apply healing to each entity
        for (LivingEntity entity : entitiesToHeal) {
            entity.heal(HEAL_AMOUNT); // HEAL_AMOUNT defines how much each entity is healed
            // Optionally, send a visual effect here
        }
    }

    // Helper method to determine if an entity is within the defined cone
    private static boolean isInCone(Vec3 posVec, Vec3 lookVec, double coneDepth, double coneAngleRad) {
        double distanceSquared = posVec.lengthSqr();
        if (distanceSquared > coneDepth * coneDepth) return false; // Beyond cone depth

        double dot = posVec.normalize().dot(lookVec);
        double angle = Math.acos(dot);
        return angle <= coneAngleRad;
    }

//    public static void createAndSendSwirlingBeam(ServerPlayer player) {
//        Vec3 start = player.getEyePosition(1.0f);
//        Vec3 end = start.add(player.getLookAngle().scale(10.0)); // Beam extends 10 blocks forward
//        float red = 0.68f; // For light blue, the red component is relatively low
//        float green = 0.84f; // Higher green component
//        float blue = 0.91f; // Higher blue component
//        float scale = 1.0f; // Adjust this value for the particle's size
//        final int particleCount = 50; // Total particles along the beam
//        ParticleOptions swirlParticleType = new DustParticleOptions(new Vector3f(red, green, blue), scale);
//
//        Vec3 direction = end.subtract(start).normalize();
//        double distance = start.distanceTo(end);
//
//        for (int i = 0; i < particleCount; i++) {
//            double ratio = (double) i / particleCount;
//            Vec3 position = start.add(direction.scale(distance * ratio));
//
//            double angle = 2 * Math.PI * ratio * 5; // Swirls around the beam
//            double offsetX = Math.cos(angle) * 2.5; // Swirl radius
//            double offsetY = Math.sin(angle) * 0.5;
//
//            Vec3 swirlPosition = position.add(new Vec3(offsetX, offsetY, 0));
//
//            // Directly spawn the swirl particles server-side
//            ModNetworking.getChannel().send(PacketDistributor.PLAYER.with(() -> player),
//                    new Swirl(swirlPosition, swirlParticleType));
//        }
//
//    }
    private static Vec3 calculateSwirlPosition(Vec3 start, Vec3 end, double ratio) {
        // Calculation for swirling effect around the beam
        Vec3 direction = end.subtract(start).normalize();
        double distance = start.distanceTo(end);
        double angle = 2 * Math.PI * ratio * 5; // Swirls around the beam
        double offsetX = Math.cos(angle) * 0.5; // Swirl radius
        double offsetY = Math.sin(angle) * 0.5;
        return start.add(direction.scale(distance * ratio)).add(new Vec3(offsetX, offsetY, 0));
    }


    //Tendrils
// Method to create tendrils from player to nearby entities
    public static void createHealingTendrils(ServerPlayer player) {
        List<LivingEntity> targets = findTargets(player, 9.5); // Implement this to find nearby entities
        targets.forEach(target -> spawnCurvedTendril(player, target));
    }

    private static void spawnTendril(ServerPlayer player, LivingEntity target) {
        Vec3 start = player.getEyePosition(1.0F); // Starting point from the player
        Vec3 end = target.position().add(0, target.getBbHeight() * 1, 0); // Targeting the midpoint of the entity

        final int steps = 20; // Number of points along the tendril for particle spawning

        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            Vec3 point = start.add(end.subtract(start).scale(t)); // Linear interpolation for points along the tendril

            // Adjust this to match the constructor of your Swirl packet
            // Assuming Swirl packet takes Vec3 position and a ParticleOptions type
            Swirl swirlPacket = new Swirl(point, ParticleTypes.EFFECT); // Example particle type, replace with actual ParticleOptions if needed

            // Sending the Swirl packet to all nearby players including the player itself
            ModNetworking.getChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(point.x, point.y, point.z, 50, player.level().dimension())), swirlPacket);
        }
    }
    private static Vec3 generateControlPoint(Vec3 start, Vec3 end, double t) {
        // Calculate the base midpoint for the control point
        Vec3 midPoint = start.add(end.subtract(start).scale(0.5));

        // Define the oscillation parameters
        double lateralOscillationAmplitude = 2.0; // Amplitude of left-right movement
        double verticalOscillationAmplitude = 2.0; // Amplitude of up-down movement
        double oscillationFrequency = 2.0; // Frequency of the oscillation

        // Calculate lateral and vertical offsets based on a sine wave
        double lateralOffset = Math.sin(t * Math.PI * oscillationFrequency) * lateralOscillationAmplitude;
        double verticalOffset = Math.cos(t * Math.PI * oscillationFrequency) * verticalOscillationAmplitude;

        // Adjust control point P1 with oscillation in both lateral and vertical directions
        Vec3 P1 = new Vec3(midPoint.x + lateralOffset,
                midPoint.y + 5.0 + verticalOffset, // Add vertical oscillation
                midPoint.z);

        return P1;
    }

    private static void spawnCurvedTendril(ServerPlayer player, LivingEntity target) {
        Vec3 P0 = player.getEyePosition(2.0F);
        P0 = P0.add(0,1.0,0);
        Vec3 P2 = target.position().add(0, target.getBbHeight() * 1, 0);



        // Control point P1 - halfway between P0 and P2 but raised above and slightly towards the target for curvature
        Vec3 midPoint = P0.add(P2.subtract(P0).scale(0.5));
        Vec3 P1 = new Vec3(midPoint.x, midPoint.y + 5.0, midPoint.z); // Adjust the +5.0 to control the height/curvature

        final int steps = 50;

        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            Vec3 pointOnCurve = calculateBezierPoint(t, P0, P1, P2);

            // Send the particle packet for this point
            ModNetworking.getChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pointOnCurve.x, pointOnCurve.y, pointOnCurve.z, 50, player.level().dimension())),
                    new Swirl(pointOnCurve, ParticleTypes.EFFECT));
        }
    }

    //Tendril curve
    private static Vec3 calculateBezierPoint(double t, Vec3 P0, Vec3 P1, Vec3 P2) {
        double u = 1 - t;
        double tt = t * t;
        double uu = u * u;

        Vec3 point = P0.scale(uu); // initial point
        point = point.add(P1.scale(2 * u * t));
        point = point.add(P2.scale(tt));

        return point;
    }

    public static List<LivingEntity> findTargets(Player player, double range) {
        Vec3 playerPos = player.position();
        Vec3 viewDirection = player.getViewVector(1.0F);
        List<LivingEntity> targets = player.level().getEntitiesOfClass(LivingEntity.class, new AABB(playerPos.x - range, playerPos.y - range, playerPos.z - range, playerPos.x + range, playerPos.y + range, playerPos.z + range),
                entity -> (entity != player && entity.isAlive())); // Ensure the target is not the player and is alive

        // Filter targets to only those in front of the player
        targets.removeIf(target -> {
            Vec3 targetDirection = target.position().subtract(playerPos).normalize();
            double angle = viewDirection.dot(targetDirection); // Dot product gives cos(theta) where theta is the angle between the vectors
            return angle <= Math.cos(Math.toRadians(60)); // Adjust the angle threshold as needed, e.g., 60 degrees
        });

        return targets;
    }
}
