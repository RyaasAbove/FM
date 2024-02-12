package net.Ryaas.firstmod.util;

import net.Ryaas.firstmod.Networking.ModNetworking;
import net.Ryaas.firstmod.Networking.packet.SpeedOne;
import net.Ryaas.firstmod.Networking.packet.particlepacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;

import static net.Ryaas.firstmod.item.custom.MoonlitDaggerItem.damageSources;
import static net.Ryaas.firstmod.util.ChargeManager.getPlayerCharge;


public class Telekinesis {
    private Entity heldEntity;
    private Vec3 lastPosition;
    private MinecraftServer server;
    double distanceInFront = 2.0;
    private ServerPlayer player;
    private int charge_max;
    private int current_charge;
    private int usage;
    private TelekinesisHandler handler = ModGameLogicManager.getTelekinesisHandler();

    public static TelekinesisHandler telekinesisHandler;
    public Telekinesis(MinecraftServer server, TelekinesisHandler handler) {
        this.server = server;
        this.handler = handler;

    }


    private final Set<UUID> playersWithActiveAbility = new HashSet<>();
    private final Map<UUID, List<Entity>> heldEntities = new HashMap<>();
    private final Map<UUID, Vec3> lastPositions = new HashMap<>();
    private Map<UUID, Integer> drainCooldowns = new HashMap<>();



    public void activateAbility(ServerPlayer player) {
        UUID playerUUID = player.getUUID();
        int currentCharge = getPlayerCharge(player);
        if (!playersWithActiveAbility.contains(playerUUID)) {
            Entity target = getLookedAtEntity(player);
            if (target != null) {
                List<Entity> targets = new ArrayList<>(); // Initialize a new list to store the target and any additional entities
                targets.add(target); // Add the initially targeted entity

                // If the player is shifting, include nearby entities within a 1-block radius
                if (player.isShiftKeyDown()) {
                    AABB areaAroundTarget = target.getBoundingBox().inflate(1.0); // Define a 1-block radius around the target
                    List<Entity> nearbyEntities = player.level().getEntities(target, areaAroundTarget,
                            e -> e != player && e.isAlive() && e != target); // Exclude the player and the target itself

                    targets.addAll(nearbyEntities); // Add all found entities to the targets list
                }

                heldEntities.put(playerUUID, targets); // Associate the list of targets with the player's UUID
                targets.forEach(entity -> entity.setNoGravity(true)); // Apply noGravity to each entity
                playersWithActiveAbility.add(playerUUID); // Mark the ability as active for this player
                ChargeManager.setPlayerCharge(player, ChargeManager.getPlayerCharge(player) - usage); // Deduct charge on ability use

            }
        } else if (playersWithActiveAbility.contains(playerUUID)) {
            deactivateAbility(player);
        }
        else {
            //Do something when out of charge, like flash the bar red
            System.out.println("out of charge");
        }
    }

    public void updateHeldEntities() {
        if(this.server != null)
            heldEntities.forEach((playerUUID, entities) -> {
                ServerPlayer player = server.getPlayerList().getPlayer(playerUUID);
                if (player != null && !entities.isEmpty()) {

                entities.forEach(entity -> {
                    UUID entityUUID = entity.getUUID();
                    Vec3 currentPosition = entity.position();
                    Vec3 lastPosition = lastPositions.getOrDefault(entityUUID, currentPosition);

                    // Calculate velocity
                    Vec3 velocity = currentPosition.subtract(lastPosition);
                    lastPositions.put(entityUUID, currentPosition);
                    if (lastPosition == null) {
                        lastPosition = entity.position();
                    } else {

                        // Calculate the velocity vector based on last and current position


//                    entity.setDeltaMovement(velocity);
                        lastPosition = currentPosition;
                    }

                    if (player != null && entity.isAlive()) {
                        Vec3 lookVec = player.getLookAngle();
                        Vec3 eyePosition = player.getEyePosition(1.0F);
                        checkAndApplyCollisionDamage(entity, player);
                        // Distance in front of the player
                        Vec3 newPosition = eyePosition.add(lookVec.scale(distanceInFront));
                        entity.setPos(newPosition.x, newPosition.y - 0.5, newPosition.z);
//                        System.out.println("updating pos");

                        AABB entityBox = entity.getBoundingBox();
                        int count = 10; // Adjust the count based on desired density

                        // Generate particles around the entity
                        for (int i = 0; i < count; i++) {
                            double x = entityBox.minX + (entityBox.maxX - entityBox.minX) * Math.random();
                            double y = entityBox.minY + (entityBox.maxY - entityBox.minY) * Math.random();
                            double z = entityBox.minZ + (entityBox.maxZ - entityBox.minZ) * Math.random();

                            // Optionally, you can adjust the y-coordinate to ensure particles spawn above the ground
                            y = Math.max(y, player.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int)x, (int)z) + 0.1);

                            // Create and send the packet for each particle
                            SpeedOne speedOnePacket = new SpeedOne(x, y, z, 1, ParticleTypes.SOUL_FIRE_FLAME); // Sending one particle at a time for better distribution control
                            ModNetworking.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), speedOnePacket);
                            }
                        }


                });
            }
        });
    }


    private void spawnSoulParticles(ServerPlayer player, List<Entity> entities, int particleCount) {
        for (Entity entity : entities) {
            AABB entityBox = entity.getBoundingBox();
            int count = 10; // Adjust the count based on desired density

            // Calculate particle spawning logic here based on particleCount
            // You can distribute the particles around the entity
            // Example: Distribute particles evenly around the entity's bounding box

            // Generate particles based on particleCount
            for (int i = 0; i < particleCount; i++) {
                double x = entityBox.minX + (entityBox.maxX - entityBox.minX) * Math.random();
                double y = entityBox.minY + (entityBox.maxY - entityBox.minY) * Math.random();
                double z = entityBox.minZ + (entityBox.maxZ - entityBox.minZ) * Math.random();

                // Create and send the packet for each particle
                SpeedOne speedOnePacket = new SpeedOne(x, y, z, 1, ParticleTypes.SOUL_FIRE_FLAME);
                ModNetworking.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), speedOnePacket);
            }
        }
    }





    public void adjustEntityDistance(ServerPlayer player, boolean increase) {
        UUID playerUUID = player.getUUID();
        if (playersWithActiveAbility.contains(playerUUID)) {
            if (increase) {
                distanceInFront += 0.5; // Increase distance
            } else {
                distanceInFront -= 0.5; // Decrease distance
            }
            // Ensure the distanceModifier stays within a reasonable range
            distanceInFront = Math.max(1.0f, Math.min(distanceInFront, 10.0f));
        }
    }
    public void checkAndApplyCollisionDamage(Entity heldEntity, ServerPlayer player) {
        if (player != null) {
            Vec3 currentPos = heldEntity.position();
            Vec3 velocity = heldEntity.getDeltaMovement();
            AABB collisionBox = heldEntity.getBoundingBox().expandTowards(velocity).inflate(0.25); // Expand search area based on velocity and a buffer

            // Find nearby entities excluding the player holding the entity and the held entity itself
            List<Entity> nearbyEntities = player.level().getEntities(heldEntity, collisionBox, entity ->
                    entity != player && entity.isAlive() && entity != heldEntity);

            for (Entity targetEntity : nearbyEntities) {
                // Check if the target entity is also being held
                boolean isTargetEntityHeld = heldEntities.values().stream()
                        .anyMatch(entities -> entities.contains(targetEntity));

                // Apply collision damage only if the target entity is not held
                if (!isTargetEntityHeld) {
                    applyCollisionDamage(heldEntity, targetEntity, velocity, player);
                }
            }
        }
    }


    public void applyCollisionDamage(Entity heldEntity, Entity targetEntity, Vec3 velocity, Player player) {
        float damageAmount = calculateDamageBasedOnVelocity(velocity); // Implement this based on your needs
        targetEntity.hurt(damageSources(player).generic(), damageAmount); // Use an appropriate DamageSource

        // Optionally, apply knockback or other effects based on the collision
        Vec3 knockbackDirection = velocity.normalize();
        targetEntity.setDeltaMovement(knockbackDirection.scale(0.5)); // Example knockback
    }

    public float calculateDamageBasedOnVelocity(Vec3 velocity) {
        double speed = velocity.length();
        final float minSpeedThreshold = 1f; // Minimum speed required to inflict damage

        // Check if the speed is below the threshold
        if (speed <= minSpeedThreshold) {
            return 0; // No damage if the entity is moving too slowly
        }

        // Damage calculation formula as before
        final float baseDamage = 2.0f;
        final float speedMultiplier = 1.5f;
        final float maxDamage = 20.0f;

        float damage = (float) (baseDamage + (speed - minSpeedThreshold) * speedMultiplier);
        damage = Math.min(damage, maxDamage);

        return damage;
    }
    private List<Entity> findNearbyEntities(ServerPlayer player) {
        AABB area = player.getBoundingBox().expandTowards(5, 5, 5); // Example area
        return player.level().getEntities(player, area, e -> e != player && e.isAlive() /*&&  any other conditions */);
    }


    public void deactivateAbility(ServerPlayer player) {
        UUID playerUUID = player.getUUID();
        List<Entity> entities = heldEntities.remove(playerUUID);
        if (entities != null) {
            entities.forEach(entity -> {
                UUID entityUUID = entity.getUUID();
                Vec3 lastPosition = lastPositions.get(entityUUID);
                if (lastPosition != null) {
                    Vec3 currentPosition = entity.position();
                    Vec3 velocity = currentPosition.subtract(lastPosition);

                    // Apply calculated velocity as momentum
                    entity.setDeltaMovement(velocity);

                    // Remove from lastPositions map
                    lastPositions.remove(entityUUID);
                }
                entity.setNoGravity(false);
            });
        }
        playersWithActiveAbility.remove(playerUUID);
    }




    public static void updateChargeForAdvancements(ServerPlayer player) {
        int additionalCharge = 0; // Calculate based on advancements

        // Assume additionalCharge is now calculated
        ChargeManager.setPlayerCharge(player, additionalCharge);
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