package net.Ryaas.firstmod.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.*;


public class Telekinesis {


    private final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    private final Set<UUID> playersWithActiveAbility = new HashSet<>();
    private final Map<UUID, Entity> heldEntities = new HashMap<>();


    public void activateAbility(ServerPlayer player) {
        UUID playerUUID = player.getUUID();
        System.out.println("activated ability");
        if (playersWithActiveAbility.contains(playerUUID)) {
            // The ability is already active, so deactivate it instead
            deactivateAbility(player);
        }
        else{
            // Logic to activate the ability (pick up an entity)
            Entity target = getLookedAtEntity(player);
            if (target != null) {
                System.out.println("found entity");
                heldEntities.put(player.getUUID(), target);
                target.setNoGravity(true);
                playersWithActiveAbility.add(playerUUID);
        }
        }
    }

    public void updateHeldEntities() {

        // Logic to update entity position based on player's look direction
        heldEntities.forEach((uuid, entity) -> {
            ServerPlayer player = server.getPlayerList().getPlayer(uuid);
            if (player != null) {
                Vec3 lookVec = player.getLookAngle();
                Vec3 newPosition = player.position().add(lookVec.scale(2.0)); // For example, 2 blocks in front
                entity.setPos(newPosition.x, newPosition.y + 1.0, newPosition.z); // Adjust Y as needed
            }
        });


    }

    public void deactivateAbility(ServerPlayer player) {
        UUID playerUUID = player.getUUID();
        // Retrieve the entity held by the player, if any
        Entity entity = heldEntities.remove(player.getUUID());

        // If an entity was being held, revert the telekinesis effects on it
        if (entity != null) {
            entity.setNoGravity(false); // Re-enable gravity

            // If you've made other changes to the entity, revert them here
            // For example, if you've set some custom data tags, clear or reset them
            playersWithActiveAbility.remove(playerUUID);
        }

    }
    private Entity getLookedAtEntity(Player player) {
        // Implement ray tracing to find targeted entity
        Vec3 eyePosition = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getLookAngle();
        Vec3 rayTraceEnd = eyePosition.add(lookVec.scale(5.0)); // 5 blocks in front of player

        HitResult hit = player.level().clip(new ClipContext(eyePosition, rayTraceEnd, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

        if (hit.getType() == HitResult.Type.ENTITY) {
            // Directly looking at an entity
            EntityHitResult entityHit = (EntityHitResult) hit;
            return entityHit.getEntity();
        } else if (hit.getType() == HitResult.Type.BLOCK) {
            // Check for entities around the block hit
            AABB searchArea = new AABB(hit.getLocation(), hit.getLocation()).inflate(1.0);
            return player.level().getNearestEntity(LivingEntity.class, TargetingConditions.forCombat(), player, hit.getLocation().x, hit.getLocation().y, hit.getLocation().z, searchArea);
        }

        return null;
    }

    // Additional utility methods as needed
}