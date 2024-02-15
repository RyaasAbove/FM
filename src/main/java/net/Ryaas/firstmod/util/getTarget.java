package net.Ryaas.firstmod.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class getTarget {

    // It's more typical to manage state like this outside utility methods if possible,
    // especially if it's meant to be toggled from somewhere like an item usage or a keybind.
    private static boolean isActive = false;

    // Since this is a utility method, it shouldn't directly send packets.
    // Instead, return the BlockPos and handle the sending elsewhere, making the utility method more versatile.
    public static BlockPos getTargetedBlock(Player player, double maxDistance) {

        if(player != null){
            Vec3 startVec = player.getEyePosition(1.0F);
            Vec3 lookVec = player.getViewVector(1.0F);
            Vec3 endVec = startVec.add(lookVec.x * maxDistance, lookVec.y * maxDistance, lookVec.z * maxDistance);

            // Perform ray tracing with extended distance
            HitResult hitResult = player.level().clip(new ClipContext(startVec, endVec, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                return blockHitResult.getBlockPos();
            }
        }


        return null;
    }

    // A toggle method is fine, but consider if there's a better way to manage this state,
    // like directly setting it based on specific events (item use, ability activation, etc.).
    public static void toggleActive() {
        isActive = !isActive;
    }

    // Getter for isActive if needed elsewhere
    public static boolean isActive() {
        return isActive;
    }
}
