package net.Ryaas.firstmod.assisting.Weapon_events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "firstmod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Moonlit_DaggerAttacks {
//        @SubscribeEvent
//        public static void onPlayerSwing(PlayerInteractEvent.LeftClickEmpty event) {
//            System.out.println("Player swung the item!");
//            InteractionHand hand = InteractionHand.MAIN_HAND;
//            Player player = event.getEntity();
//            ItemStack itemStack = player.getItemInHand(hand);
//            Level server = player.getCommandSenderWorld();
//            AABB hitbox = createHitbox(player);
//            if(itemStack.getItem() instanceof MoonlitDaggerItem){
//                spawnParticles(player);
//                if (!player.getCommandSenderWorld().isClientSide) {
//                    ModNetworking.sendToServer(new SwingPacket());
//
//
//
//
//                    List<Entity> entities = player.level().getEntities(player, hitbox, e -> e instanceof LivingEntity && e != player);
//
//                    for (Entity hitEntity : entities) {
//                        hitEntity.hurt(damageSources(player).playerAttack(player), 4);
//
//
//                    }
//                }
//            }
//
//        }
//
//    private static void spawnParticles(Player player){
//        AABB hitbox = createHitbox(player);
//        Vec3 center = hitbox.getCenter();
//        Level server = player.getCommandSenderWorld();
//        Vec3 size = new Vec3(hitbox.maxX - hitbox.minX, hitbox.maxY - hitbox.minY, hitbox.maxZ - hitbox.minZ);
//        double length = size.length(); // Calculate the length of the slash
//        Vec3 direction = size.normalize(); // Get the normalized direction of the slash
//
//        for (double i = 0; i < length; i += 0.1) { // Adjust the increment as needed
//            double x = center.x + direction.x * i;
//            double y = center.y + direction.y * i;
//            double z = center.z + direction.z * i;
//            server.addParticle(ParticleTypes.SWEEP_ATTACK, x, y, z, 0, 0, 0);
//        }
//    }



    public static void sendHitBoxFromPlayer(Player player, double range, float damage, Level level){
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 lookVector = player.getLookAngle();
        Vec3 targetPos = eyePos.add(lookVector.x * range, lookVector.y * range, lookVector.z * range);

        HitResult hitResult = player.pick(range, 1.0F, false);

        if(hitResult.getType() == HitResult.Type.BLOCK){
            BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
            //do something with blockPos here
        }
        else if(hitResult.getType() == HitResult.Type.ENTITY){
            Entity target = ((net.minecraft.world.phys.EntityHitResult) hitResult).getEntity();
            //do something with entity here
            if(player.isInvisible())
            {
                damage = damage * 3;
            }
            target.hurt(level.damageSources().magic(), damage);
        }
    }
    public static void spawnSlash(Level level, LivingEntity target){
        double x = target.getX();
        double y = target.getY() + target.getBbHeight() / 2.0;
        double z = target.getZ();

        level.addParticle(ParticleTypes.HEART, x, y, z, 0.0, 0.0, 0.0);
    }
}


