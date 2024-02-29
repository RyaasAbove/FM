package net.Ryaas.firstmod.util;

import net.Ryaas.firstmod.Networking.ModNetworking;
import net.Ryaas.firstmod.Networking.packet.SprayPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HealingAbilityHandler {
    private static final float HEALING_COST = 10.0f;
    private static final float HEAL_AMOUNT = 5.0F;
    private static final Map<Player, Boolean> healingActive = new HashMap<>();


    public static void activateHealing(ServerPlayer player) {
        if (PlayerHealingManager.getPlayerEnergy(player.getUUID()) >= HEALING_COST) {
            PlayerHealingManager.useEnergy(player.getUUID(), HEALING_COST);
            List<LivingEntity> healedEntities = healEntitiesAroundPlayer(player);

            healedEntities.forEach(healedEntity -> {
                sendHealingBeam(player.level(), player.position().add(0, player.getEyeHeight(), 0), healedEntity.position(), player);
                healedEntity.heal(HEAL_AMOUNT);
            });
        }
    }
    public static boolean toggleHealing(Player player) {
        boolean isActive = healingActive.getOrDefault(player, false);
        healingActive.put(player, !isActive);
        return !isActive;
    }

    public static void sendHealingBeam(Level level, Vec3 start, Vec3 end, ServerPlayer player) {
        final int particleCount = 30; // Adjust based on desired visual density
        Vec3 direction = end.subtract(start).normalize();
        double distance = start.distanceTo(end);

        for (int i = 0; i < particleCount; i++) {
            double ratio = (double) i / (particleCount - 1); // Ensure the last particle is at the target
            Vec3 position = start.add(direction.scale(distance * ratio));
            spawnParticleAtPosition(level, position, player);
        }
    }

    public static void spawnParticleAtPosition(Level level, Vec3 position, ServerPlayer player) {
        // Adjust particle type and behavior as needed
        SprayPacket packet = new SprayPacket(position, 1, 0.0f, player.getYRot(), player.getXRot());
        PacketDistributor.TargetPoint targetPoint = new PacketDistributor.TargetPoint(position.x, position.y, position.z, 50, level.dimension());
        ModNetworking.getChannel().send(PacketDistributor.NEAR.with(() -> targetPoint), packet);
    }

    private static List<LivingEntity> healEntitiesAroundPlayer(ServerPlayer player) {
        final double coneRadius = 5.0;
        final double coneDepth = 10.0;
        Vec3 startPosition = player.position().add(0, player.getEyeHeight(), 0);
        Vec3 lookVec = player.getLookAngle();
        Vec3 endPosition = startPosition.add(lookVec.scale(coneDepth));
        AABB areaOfEffect = new AABB(startPosition, endPosition).inflate(coneRadius);

        return player.level().getEntitiesOfClass(LivingEntity.class, areaOfEffect)
                .stream()
                .filter(e -> e instanceof LivingEntity && !e.equals(player))
                .filter(e -> isInCone(e.position().subtract(startPosition), lookVec, coneDepth, Math.toRadians(coneRadius)))
                .collect(Collectors.toList());
    }

    private static boolean isInCone(Vec3 posVec, Vec3 lookVec, double coneDepth, double coneAngleRad) {
        double distanceSquared = posVec.lengthSqr();
        if (distanceSquared > coneDepth * coneDepth) return false;
        double dot = posVec.normalize().dot(lookVec);
        return Math.acos(dot) <= coneAngleRad;
    }
}
