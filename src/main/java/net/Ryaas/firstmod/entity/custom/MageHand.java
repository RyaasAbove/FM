package net.Ryaas.firstmod.entity.custom;

import net.Ryaas.firstmod.entity.client.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.*;

public class MageHand extends Ranged_Projectiles implements GeoEntity {
    private final Level world;
    private int active = 100;
    private UUID ownerUUID;

    private static final String HIT_COUNT_KEY = "hitCount";
   private float damage = 5;

    private Map<LivingEntity, Vec3> initialPositions = new HashMap<LivingEntity, Vec3>();

    private Set<UUID> hitEntities = new HashSet<>();
    private double angularOffset = 0.0;

    private int count = 0;





    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);



    public MageHand(EntityType<? extends Projectile> entityType, Level world) {
        super(entityType, world);
        this.world = world;
        this.setNoGravity(true);
        this.setBoundingBox(new AABB(-1 / 2, 0, -1 / 2, 1 / 2, 1, 1 / 2));
        // WIDTH, HEIGHT, and LENGTH are the dimensions of your entity

    }

    public void setOwner(Player player){
        this.ownerUUID = player.getUUID();
    }


    @Override
    public void trail() {
//        generateTrailParticles();

    }

    public void setDamage(float damage){
        this.damage = damage;
    }

    private void spawnSoulFireParticles() {
        double x;
        double y;
        double z;

        Vec3 position = this.position();
        x = position.x;
        y = position.y;
        z = position.z;

        // Spawn particles at regular intervals
        for (int i = 0; i < 10; i++) {
            double offsetX = random.nextGaussian() * 0.2;
            double offsetY = random.nextGaussian() * 0.2;
            double offsetZ = random.nextGaussian() * 0.2;

            if(world != null && world.isClientSide){
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, offsetX, offsetY, offsetZ);

            }

             }
    }

    private void generateTrailParticles(Entity entity) {

        Vec3 vec3 = this.position();


    }



    @Override
    public void impactparticles(double x, double y, double z) {
        spawnSoulFireParticles();

    }

    @Override
    public float getSpeed() {
        return 1;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.empty();
    }

    @Override
    protected void defineSynchedData() {

    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vec3d = new Vec3(x, y, z).normalize().scale(velocity);


        this.setDeltaMovement(vec3d.x * velocity, vec3d.y * velocity, vec3d.z * velocity);

        // Calculate the horizontal distance squared
        float horizontalDistanceSq = (float)(vec3d.x * vec3d.x + vec3d.z * vec3d.z);

        this.setYRot((float)(Mth.atan2(vec3d.x, vec3d.z) * (180F / Math.PI)));
        this.setXRot((float)(Mth.atan2(vec3d.y, Math.sqrt(horizontalDistanceSq)) * (180F / Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();

    }


    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);




        Entity target = hitResult.getEntity();
        Vec3 hitPosition = hitResult.getLocation();

        // Implement logic for what happens when the projectile hits an entity
        if (target instanceof LivingEntity  && (!target.getUUID().equals(ownerUUID))) {
            // Example: Deal damage to the entity
            LivingEntity livingEntity = (LivingEntity) target;
            livingEntity.hurt(damageSources().magic(), damage);
            hitEntities.add(livingEntity.getUUID());
            Vec3 motion = this.getDeltaMovement();
            double upforce = 3.0;
            double knockbackStrength = 1.0; // Adjust this value as needed
            livingEntity.knockback(knockbackStrength, -motion.x, -motion.z);
            CompoundTag data = livingEntity.getPersistentData();

            Vec3 velocity = livingEntity.getDeltaMovement();
            int hitCount = data.getInt("hitCount");
//

        }

        // Spawn impact particles at the hit position
        impactparticles(hitPosition.x, hitPosition.y, hitPosition.z);

        // Perform any other actions on hitting an entity
    }

    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);


        BlockPos target = hitResult.getBlockPos();
        Vec3 hitPosition = hitResult.getLocation();


    }

    public static void decrementHitCount(LivingEntity entity, String key) {
        CompoundTag entityData = entity.getPersistentData();
        int hitCount = entityData.getInt(key);
        entityData.putInt(key, Math.max(0, hitCount - 1));
        entity.addAdditionalSaveData(entityData);
    }

//

    private void spawnNextMageHand(Entity target) {
        double stormRadius = 5.0;
        int mageHandCount = 10;
        double spawnHeight = 15;
        Level world = target.level();
        Vec3 centerPos = target.position();
        if(world != null && !world.isClientSide){
            for (int i = 0; i < mageHandCount; i++) {
                double angle = Math.random() * Math.PI * 2;
                double distance = Math.random() * stormRadius;
                double xOffset = Math.cos(angle) * distance;
                double zOffset = Math.sin(angle) * distance;

                Vec3 spawnPos = centerPos.add(xOffset, spawnHeight, zOffset);
                MageHand mageHand = new MageHand(ModEntities.MAGE_HAND.get(), world);
                mageHand.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                mageHand.setDeltaMovement(0, -1.0, 0); // Falling straight down

                world.addFreshEntity(mageHand);
                CompoundTag data = target.getPersistentData();
//
        }


        }
    }
    @Override
    public void tick(){
        super.tick();





        Vec3 currentMotion = this.getDeltaMovement();
        this.setPos(this.getX() + currentMotion.x, this.getY() + currentMotion.y, this.getZ() + currentMotion.z);
        if(world != null && !world.isClientSide  ){


            //contact
            if (!currentMotion.equals(Vec3.ZERO)) {
             this.setYRot((float) Math.toDegrees(Math.atan2(-currentMotion.z, currentMotion.x)) - 90.0f);
             this.setXRot((float) Math.toDegrees(Math.atan2(currentMotion.y, Math.sqrt(currentMotion.x * currentMotion.x + currentMotion.z * currentMotion.z))));
            }
            active--;


            AABB collisionBox = this.getBoundingBox();
            List<Entity> nearbyEntities = world.getEntitiesOfClass(Entity.class, collisionBox.inflate(0.5), e -> e != this && e.isAlive());

            for (Entity entity : nearbyEntities) {
                if (collisionBox.intersects(entity.getBoundingBox())) {
                    // Collision detected, handle it
                    onHitEntity(new EntityHitResult(entity));
                    // Remove this line if you want to check for collisions with multiple entities per tick
                }


                CompoundTag data = entity.getPersistentData();
                if (data.contains("HitByRisingProjectile")) {
                    if (entity.invulnerableTime == 0) {
                        activateSlash(entity);
                    }

                }

                if (data.contains("Carried")) {
                    initialPositions.entrySet().removeIf(entry -> {
                        Entity target = entry.getKey();
                        Vec3 startPosition = entry.getValue();
                        Vec3 currentPosition = target.position();

                        double distanceTraveled = currentPosition.distanceToSqr(startPosition);
                        if (distanceTraveled > 10.0) { // z blocks distance squared
                            target.hurt(damageSources().magic(), 5.0F); // Apply damage
                            world.addParticle(ParticleTypes.LARGE_SMOKE,
                                    currentPosition.x, currentPosition.y, currentPosition.z,
                                    0, 0, 0); // No motion for particles
                            world.addParticle(ParticleTypes.LARGE_SMOKE,
                                    currentPosition.x, currentPosition.y, currentPosition.z,
                                    0, 0, 0); // No motion for particles
                            world.addParticle(ParticleTypes.LARGE_SMOKE,
                                    currentPosition.x, currentMotion.y, currentMotion.z,
                                    0, 0, 0); // No motion for particles
                            world.addParticle(ParticleTypes.LARGE_SMOKE,
                                    currentPosition.x, currentPosition.y, currentPosition.z,
                                    0, 0, 0); // No motion for particles
                            world.addParticle(ParticleTypes.LARGE_SMOKE,
                                    currentPosition.x, currentPosition.y, currentPosition.z,
                                    0, 0, 0); // No motion for particles

                            System.out.println("shot");

                            return true; // Remove the entry to prevent continuous damage
                        }
                        return false;
                    });
                }
            }

            angularOffset += 0.1; // Adjust this value to change the spin speed

// Optional: To keep the angular offset within a reasonable range
            if (angularOffset > 2 * Math.PI) {
                angularOffset -= 2 * Math.PI;
            }




            //Timer
            if(active <= 0){

                this.remove(RemovalReason.DISCARDED);
            }
        }
    }
    protected void collidedWithBlock(BlockState blockState, BlockPos blockPos,Entity entity) {
        // Apply damage to the entity
        entity.hurt(damageSources().inWall(), 5.0F);  // Damage amount can be adjusted
        if(world != null && world.isClientSide  ){


        world.addParticle(ParticleTypes.EXPLOSION, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0, 0, 0);

        // Additional logic for collision with a block
        }

    }
    public void createTornadoEffect(Entity entity) {

        Vec3 entityPos = entity.position();
        double radius = 2.0; // Radius of the tornado
        int height = 15; // Height of the tornado
        int particlesPerLayer = 20;
        double particleSpawnRate = 2.0; // Number of particles per horizontal layer




        if(world != null && world.isClientSide){
            for (int w = 0; w < height; w++) {
                double yOffset = w * 1.5; // Vertical spacing between layers

                for (int i = 0; i < particlesPerLayer; i++) {
                    if (Math.random() < particleSpawnRate) {

                        double angle = 2 * Math.PI * i / particlesPerLayer + angularOffset;

                        double xOffset = radius * Math.cos(angle);
                        double zOffset = radius * Math.sin(angle);

                        double x = entityPos.x + xOffset;
                        double y = entityPos.y + yOffset;
                        double z = entityPos.z + zOffset;

                        world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 0, 0, 0);
                    }
                }
            }
        }
    }



    public void activateSlash(Entity entity){
        entity.invulnerableTime = 0;

        Vec3 entityPos = entity.position();

        if(entity.getDeltaMovement().y > 0.1) {
            double entityHeight = entity.getBbHeight();
            double entityWidth = entity.getBbWidth();

            // Calculate start and end points of the slash
            Vec3 startPoint = entityPos.add(-entityWidth / 2, entityHeight / 2, 0);
            Vec3 endPoint = entityPos.add(entityWidth / 2, entityHeight / 2, 0);
            if (world.isClientSide && world != null) {
                // Number of particles to spawn along the path
                int particleCount = 10;

                for (int i = 0; i < particleCount; i++) {
                    double t = i / (double) particleCount;
                    Vec3 particlePos = startPoint.lerp(endPoint, t);

                    world.addParticle(ParticleTypes.LARGE_SMOKE,
                            particlePos.x, particlePos.y, particlePos.z,
                            0, 0, 0); // No motion for particles
                    world.addParticle(ParticleTypes.LARGE_SMOKE,
                            particlePos.x, particlePos.y, particlePos.z,
                            0, 0, 0); // No motion for particles
                    world.addParticle(ParticleTypes.LARGE_SMOKE,
                            particlePos.x, particlePos.y, particlePos.z,
                            0, 0, 0); // No motion for particles
                    world.addParticle(ParticleTypes.LARGE_SMOKE,
                            particlePos.x, particlePos.y, particlePos.z,
                            0, 0, 0); // No motion for particles
                    world.addParticle(ParticleTypes.LARGE_SMOKE,
                            particlePos.x, particlePos.y, particlePos.z,
                            0, 0, 0); // No motion for particles

                }
                entity.hurt(damageSources().magic(), damage);
            }


        }

    }








    @Override
    public double getTick(Object o) {
        return 0;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));


    }

    private PlayState predicate(AnimationState<MageHand> mageHandAnimationState) {

        if (world.isClientSide && world != null) {
            if (mageHandAnimationState.isMoving()) {
                mageHandAnimationState.getController().setAnimation(RawAnimation.begin().then("Fistproj", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            }

            mageHandAnimationState.getController().setAnimation(RawAnimation.begin().then("Fistproj", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        return PlayState.CONTINUE;
    }

        @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


}





