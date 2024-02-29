package net.Ryaas.firstmod.util;

import net.Ryaas.firstmod.Networking.ModNetworking;
import net.Ryaas.firstmod.Networking.packet.HealthDrain;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Random;

import static net.Ryaas.firstmod.item.custom.MoonlitDaggerItem.damageSources;

public class Draining {
    public static void drainHealthFromTargets(ServerPlayer player, double range, float drainAmount, float maxHeal, double healChance) {
        List<Entity> targets = ModGameLogicManager.findTargetsInFrontOfPlayer(player, range);
        Random random = new Random();
        float totalHealed = 0.0f; // Track the total amount healed

        for (Entity target : targets) {
            if (target instanceof LivingEntity) {
                LivingEntity livingTarget = (LivingEntity) target;
                float healthToDrain = Math.min(drainAmount, livingTarget.getHealth());
                livingTarget.hurt(damageSources(player).magic(), healthToDrain);
                triggerDrainingParticles(player, target);

                // Check if the player gets healed based on the healChance
                if (random.nextDouble() < healChance) {
                    // Only heal up to maxHeal amount
                    float healAmount = Math.min(healthToDrain, maxHeal - totalHealed);
                    player.heal(healAmount);
                    totalHealed += healAmount;

                    if (totalHealed >= maxHeal) {
                        break; // Stop if max healing reached
                    }
                }
            }
        }
    }

    public static void triggerDrainingParticles(ServerPlayer player, Entity target) {
        Vec3 targetPos = target.position();
        Vec3 playerPos = player.position();
        HealthDrain packet = new HealthDrain(targetPos.add(0, 0.5, 0), playerPos);

        // Sending packet to all nearby players including the player performing the drain
        ModNetworking.getChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(player.getX(), player.getY() + 1, player.getZ(), 50, player.level().dimension())), packet);
    }
}


