package net.Ryaas.firstmod.item.custom;

import net.Ryaas.firstmod.entity.client.ModEntities;
import net.Ryaas.firstmod.entity.custom.MageHand;
import net.Ryaas.firstmod.item.helper.BSWeaponUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

@Mod.EventBusSubscriber(modid = "firstmod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MoonlitDaggerItem extends SkillWeapon implements BSWeaponUtil, GeoItem {
    private static final int MAX_HIT_COUNT = 5;
    private static final int MAX_CHARGE_TIME = 72000; // Max charge time, adjust as needed
    private static final int NUMBER_OF_VERSIONS = 5;
    private static final int TIME_PER_VERSION = MAX_CHARGE_TIME / NUMBER_OF_VERSIONS;
    private static final double TARGET_RADIUS = 20.0;

    private static final String HIT_COUNT_KEY = "hitCount";

    private boolean dashing = false;


    public MoonlitDaggerItem(Tier ptier, int pAttackDamage,
                             float pAttackSpeedModifier, Properties properties) {
        super(ptier, pAttackDamage, pAttackSpeedModifier, properties);
    }


    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        CompoundTag entityData = target.getPersistentData();

        int hitCount = entityData.getInt("hitCount");
        System.out.println("Current hit count: " + hitCount);

        entityData.putInt("hitCount", Math.min(hitCount + 1, MAX_HIT_COUNT));

        System.out.println("Updated hit count: " + entityData.getInt("hitCount"));


        return false;
    }

//    @SubscribeEvent
//    public static void onPlayerSwing(PlayerInteractEvent.LeftClickEmpty event) {
////        System.out.println("Player swung the item!");
//        Player player = event.getEntity();
//        Level server = player.getCommandSenderWorld();
//        AABB hitbox = createHitbox(player);
//        if (!player.getCommandSenderWorld().isClientSide) {
//            ModNetworking.sendToServer(new SwingPacket());
//
//
//
//
//            List<Entity> entities = player.level().getEntities(player, hitbox, e -> e instanceof LivingEntity && e != player);
//
//            for (Entity hitEntity : entities) {
//                hitEntity.hurt(damageSources(player).playerAttack(player), 4);
//                spawnParticles(player);
//
//            }
//        }








//    private static void spawnParticles(Player player){
//        AABB hitbox = createHitbox(player);
//        Vec3 center = hitbox.getCenter();
//        Level server = player.getCommandSenderWorld();
//        Vec3 size = new Vec3(hitbox.maxX - hitbox.minX, hitbox.maxY - hitbox.minY, hitbox.maxZ - hitbox.minZ);
//        double length = size.length(); // Calculate the length of the slash
//        Vec3 direction = player.getLookAngle(); // Get the normalized direction of the slash
//
//        for (double i = 0; i < length; i += 0.1) { // Adjust the increment as needed
//            double x = center.x + direction.x * i;
//            double y = center.y + direction.y * i;
//            double z = center.z + direction.z * i;
//            server.addParticle(ParticleTypes.SWEEP_ATTACK, x, y, z, 0, 0, 0);
//        }
//    }

    public static DamageSources damageSources(Player player) {

        return player.level().damageSources();

    }

//    public static AABB createHitbox(Player player) {
//        // Calculate the hitbox position and size based on the player's position and orientation
//        // This is an example and should be adjusted according to your requirements
//        Vec3 lookVec = player.getLookAngle();
//        Vec3 startPos = player.position().add(0, player.getEyeHeight(), 0);
//        Vec3 endPos = startPos.add(lookVec.scale(3));
//        double hitboxSize = 1.0;
//        return new AABB(startPos.subtract(hitboxSize, hitboxSize, hitboxSize), endPos.add(hitboxSize, hitboxSize, hitboxSize));
//    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.startUsingItem(hand); // Start the charge-up

        return InteractionResultHolder.consume(itemStack); // Indicate that the action was successful and the item was used



//        if (!world.isClientSide) {
//            ItemStack itemStack = player.getItemInHand(hand);
//
//            MageHand mageHand = new MageHand(ModEntities.MAGE_HAND.get(), world);
//            Vec3 playerLook = player.getLookAngle();
//
//            // Set position and orientation of the MageHand
//            Vec3 spawnpos = player.position().add(player.getLookAngle().scale(1.0));
//            mageHand.setPos(spawnpos.x, spawnpos.y + 1, spawnpos.z);
//            mageHand.setYRot(player.getYRot());
//            mageHand.setXRot(player.getXRot());
//
//            // Call shoot method to set initial motion
//            System.out.println("Look Vector: " + playerLook);
//            mageHand.shoot(playerLook.x, playerLook.y, playerLook.z, 1.5f, 0.0f); // Adjust speed and inaccuracy as needed
//
//            world.addFreshEntity(mageHand);
//            return InteractionResultHolder.success(itemStack);
//        }
//        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }


    public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        if (entity instanceof Player) {
            Player player = (Player) entity;
            boolean isHoldingMoonlitDagger = player.getMainHandItem() == stack || player.getOffhandItem() == stack;

            if (isHoldingMoonlitDagger) {
                player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20, 0));

            }
        }
    }


    public void onUseTick(Level world, LivingEntity entityLiving, ItemStack stack, int count) {
        super.onUseTick(world, entityLiving, stack, count);

        if (entityLiving instanceof Player) {
            Player player = (Player) entityLiving;

            // Check if the player is sneaking while using the item
            if (player.isShiftKeyDown()) {
                // Apply the effect, for example, slow falling for 1 second (20 ticks)
                player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20, 0));
            }
        }
    }

    public void releaseUsing(ItemStack stack, Level world, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player) {

            Player player = (Player) entityLiving;

            if(player.isShiftKeyDown())
            {
                Vec3 viewangle = player.getLookAngle();
                double dashSpeed = 5.0;
                player.setDeltaMovement(viewangle.x * dashSpeed, viewangle.y * dashSpeed, viewangle.z * dashSpeed);


            }
            else{
                spawnMageHandVersion(world, player);
            }



            // Now you have the charge time, and you can spawn the MageHand accordingly

            // Example: Spawn a larger and more powerful MageHand if charged longer
            // Set position and orientation of the MageHand
//            Vec3 playerLook = player.getLookAngle();



//            Vec3 spawnpos = player.position().add(player.getLookAngle().scale(1.0));
//            mageHand.setPos(spawnpos.x, spawnpos.y + 1, spawnpos.z);
//            mageHand.setYRot(player.getYRot());
//            mageHand.setXRot(player.getXRot());

            // Call shoot method to set initial motion
//            System.out.println("Look Vector: " + playerLook);
//            mageHand.shoot(playerLook.x, playerLook.y, playerLook.z, 1.5f, 0.0f); // Adjust speed and inaccuracy as needed
//
//            world.addFreshEntity(mageHand);
            // Set position, motion, size, and damage based on chargeTime
            // ...


        }
    }




    private PlayState predicate(AnimationState animationState){
        animationState.getController().setAnimation((RawAnimation.begin().then("idle", Animation.LoopType.LOOP)));
        return PlayState.CONTINUE;
    }

    private void spawnMageHandVersion(Level world, Player player) {
        MageHand mageHand;

        Vec3 playerLook = player.getLookAngle();
       double baseSpawnDistance = 2.0;
       double pitch = Math.cos(player.getXRot() * (Math.PI / 180));
        double safeSpawnDistance = baseSpawnDistance / pitch;
        safeSpawnDistance = Math.min(safeSpawnDistance, 1.0);
        Vec3 spawnpos = player.position().add(player.getLookAngle().scale(safeSpawnDistance));
                mageHand = new MageHand(ModEntities.MAGE_HAND.get(), world);
                mageHand.setOwner(player);
                mageHand.setDamage(5.0f);
                mageHand.setPos(spawnpos.x, spawnpos.y + 1, spawnpos.z);
                mageHand.setYRot(player.getYRot());
                mageHand.setXRot(player.getXRot());
                mageHand.shoot(playerLook.x, playerLook.y, playerLook.z, 1.5f, 0.0f);
                world.addFreshEntity(mageHand);
//
//

        }






    @Override
    public int getUseDuration(ItemStack stack) {
        return MAX_CHARGE_TIME;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
}



    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
    }

    @Override
    public double getTick(Object o) {
        return 0;
    }
}











