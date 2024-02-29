package net.Ryaas.firstmod.Networking.packet;

import net.Ryaas.firstmod.Networking.ModNetworking;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.joml.Vector3f;

import java.util.*;

@Mod.EventBusSubscriber
public class HealingDome {
    private static final Map<UUID, Integer> activeDomes = new HashMap<>();

    // Method to activate the dome for a player
    public static void activateDome(ServerPlayer player, int durationSeconds) {
        // Convert duration from seconds to game ticks (20 ticks per second)
        int durationTicks = durationSeconds * 20;
        activeDomes.put(player.getUUID(), durationTicks);

    }

    // Check if the dome is active for a specific player
    public static boolean isActiveForPlayer(ServerPlayer player) {
        return activeDomes.containsKey(player.getUUID());
    }

    // Update the dome duration for a player, and remove it if expired
    public static void updateDomeForPlayer(ServerPlayer player) {
        UUID playerId = player.getUUID();
        if (activeDomes.containsKey(playerId)) {
            int ticksLeft = activeDomes.get(playerId) - 1;
            if (ticksLeft <= 0) {
                activeDomes.remove(playerId);
            } else {
                activeDomes.put(playerId, ticksLeft);
            }
        }
    }

    public static void onServerTick(TickEvent.ServerTickEvent event) {
        System.out.println("tick");
        if (event.phase != TickEvent.Phase.START) {
            return; // Only process at the start of each tick
        }

        Iterator<Map.Entry<UUID, Integer>> iterator = activeDomes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Integer> entry = iterator.next();
            ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(entry.getKey());
            if (player == null) {
                // Player is not online or doesn't exist, remove dome
                iterator.remove();
                continue;
            }

            int ticksLeft = entry.getValue() - 1;
            if (ticksLeft <= 0) {
                // Dome duration has expired
                iterator.remove(); // Remove the dome since its duration has expired
            } else {
                // Dome is still active, update the remaining duration
                entry.setValue(ticksLeft);

                // Check if the current tick is a multiple of 4 to spawn dome particles
                if (ticksLeft % 4 == 0) {
                    spawnDomeParticles(player);
                }
            }
        }
    }

    public static void spawnDomeParticles(ServerPlayer player) {
        System.out.println("Spawn particles called");
        ServerLevel level = player.serverLevel();
        Vec3 playerPos = player.position();
        double radius = 10.0; // Example radius for the dome
        int particles = 200; // Total number of particles in the dome

        // Calculate positions for the dome particles
        List<Vec3> particlePositions = calculateDomePositions(playerPos, radius, particles);


        // Light blue color components
        float red = 0.68F;
        float green = 0.84F;
        float blue = 0.91F;
        float scale = 1.0F; // Adjust the particle's scale as needed

        // Iterate over each position and send the ParticleSpawningpacket for each
        particlePositions.forEach(pos -> {
            // Create the DustParticleOptions for light blue dust
            DustParticleOptions particleOptions = new DustParticleOptions(new Vector3f(red, green, blue), scale);

            // Convert DustParticleOptions to a form that can be sent over the network
            // Note: You may need to adjust this if ParticleSpawningpacket does not directly accept DustParticleOptions
            ParticleSpawningpacket packet = new ParticleSpawningpacket(pos,  red, green, blue, scale);

            // Send packet to all players within a certain radius, including the player itself
            ModNetworking.getChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.x, pos.y, pos.z, 50, level.dimension())), packet);
        });
        // Apply regeneration effect to all entities within the dome
        AABB domeArea = new AABB(playerPos.subtract(radius, radius, radius), playerPos.add(radius, radius, radius));
        List<LivingEntity> entitiesWithinDome = level.getEntitiesOfClass(LivingEntity.class, domeArea, entity -> entity != player);
        entitiesWithinDome.forEach(entity -> {
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 10)); // 5 seconds of regeneration II
        });
    }

    private static List<Vec3> calculateDomePositions(Vec3 center, double radius, int particles) {
        List<Vec3> positions = new ArrayList<>();

        // Generate random positions within the sphere
        for (int i = 0; i < particles; i++) {
            double u = Math.random();
            double v = Math.random();
            double theta = 2 * Math.PI * u;
            double phi = Math.acos(2 * v - 1);

            double x = center.x() + (radius * Math.sin(phi) * Math.cos(theta));
            double y = center.y() + (radius * Math.sin(phi) * Math.sin(theta));
            double z = center.z() + (radius * Math.cos(phi));

            positions.add(new Vec3(x, y, z));
        }

        return positions;
    }
}