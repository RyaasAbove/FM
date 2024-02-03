package net.Ryaas.firstmod.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.Optional;

public class Moon extends Ranged_Projectiles implements GeoEntity {

    private final Level world;
    private boolean hasCollided = false;
    private int stopTimer = 0;
    private int active = 60;

    private static final String HIT_COUNT_KEY = "hitCount";
    private float damage = 100;
    private Vec3 prevMotion = Vec3.ZERO;
    private boolean charged = false;

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);



    public void setDamage(float damage){
        this.damage = damage;
    }

    public Moon(EntityType<? extends Projectile> entityType, Level world) {
        super(entityType, world);
        this.world = world;
        this.setNoGravity(true);
        this.setBoundingBox(new AABB(-5 / 2, 0, -5 / 2, 5 / 2, 5, 5 / 2));
        // WIDTH, HEIGHT, and LENGTH are the dimensions of your entity

    }


    @Override
    public void trail() {

    }

    @Override
    public void impactparticles(double x, double y, double z) {
        spawnSoulFireParticles();

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

    @Override
    public float getSpeed() {
        return 0;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.empty();
    }

    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);


        Entity target = hitResult.getEntity();
        Vec3 hitPosition = hitResult.getLocation();

        // Implement logic for what happens when the projectile hits an entity
        if (target instanceof LivingEntity) {
            // Example: Deal damage to the entity
            LivingEntity livingEntity = (LivingEntity) target;
            livingEntity.hurt(damageSources().magic(), damage);
//            livingEntity.invulnerableTime = 1;
            int numberOfParticles = 75;
            if(world != null && world.isClientSide){
                for (int i = 0; i < numberOfParticles; i++) {
                    // Randomize the particle position a bit for natural effect
                    double offsetX = random.nextGaussian() * 2;
                    double offsetY = random.nextGaussian() * 1;
                    double offsetZ = random.nextGaussian() * 2;

                    // Spawn the particles
                    world.addParticle(ParticleTypes.EXPLOSION,
                            hitPosition.x + offsetX,
                            hitPosition.y + offsetY,
                            hitPosition.z + offsetZ,
                            0, 0, 0);
            }


            }
        }
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


    @Override
    public void tick() {
        super.tick();

        if (hasCollided) {
            Vec3 vec3d = new Vec3(0, -.05, 0).normalize().scale(1);
            stopTimer++;
            if (stopTimer >= 1) { // 20 ticks = 1 second
                // Perform the action after stopping for 1 second


                this.setDeltaMovement(vec3d.x, vec3d.y, vec3d.z);
                final int numparticles = 45;
                final double radius = 11;


                Vec3 moonPos = this.position();
                if(world != null && world.isClientSide){
                    for (int i = 0; i < numparticles; i++) {
                        double angle = 2 * Math.PI * i / numparticles;
                        double xOffset = Math.cos(angle) * radius;
                        double zOffset = Math.sin(angle) * radius;


                        double particleX = moonPos.x + xOffset;
                        double particleY = moonPos.y; // Adjust if you want the particles higher or lower
                        double particleZ = moonPos.z + zOffset;


                        world.addParticle(ParticleTypes.EXPLOSION, particleX, particleY, particleZ, 0, 0, 0);

                    }

                }
            }
        }


        final int numberOfParticlesWidth = 15; // Number of particles across the width
        final int numberOfParticlesHeight = 15; // Number of particles across the height
        final double width = 15.0; // Width of the trail
        final double height = 15.0; // Height of the trail

        Vec3 entityPos = this.position();
        Vec3 motionVec = this.getDeltaMovement();

        // Calculate a vector perpendicular to the entity's motion
        Vec3 perpendicularVec = new Vec3(-motionVec.z, 0, motionVec.x).normalize();

        if(world != null && world.isClientSide){
            for (int i = 0; i < numberOfParticlesWidth; i++) {
                for (int j = 0; j < numberOfParticlesHeight; j++) {
                    double factorWidth = (i / (double) numberOfParticlesWidth) - 0.5;
                    double factorHeight = (j / (double) numberOfParticlesHeight) - 0.5;

                    Vec3 particlePos = entityPos.add(perpendicularVec.scale(factorWidth * width))
                            .add(0, factorHeight * height, 0);

                    // Spawn the particle
                    world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                            particlePos.x, particlePos.y, particlePos.z,
                            0, 0.05, 0); // Slight upward motion

                }
        }
        }




        Vec3 currentMotion = this.getDeltaMovement();
        this.setPos(this.getX() + currentMotion.x, this.getY() + currentMotion.y, this.getZ() + currentMotion.z);


        if (world != null && !this.world.isClientSide) {



            this.setYRot((float) Math.toDegrees(Math.atan2(-currentMotion.z, currentMotion.x)) - 90.0f);
            this.setXRot((float) Math.toDegrees(Math.atan2(currentMotion.y, Math.sqrt(currentMotion.x * currentMotion.x + currentMotion.z * currentMotion.z))));
        }
        active--;

        AABB collisionBox = this.getBoundingBox();
        List<Entity> nearbyEntities = this.world.getEntitiesOfClass(Entity.class, collisionBox.inflate(0.5), e -> e != this && e.isAlive());

        for (Entity entity : nearbyEntities) {
            if (collisionBox.intersects(entity.getBoundingBox())) {
                // Collision detected, handle it

                hasCollided = true;
                onHitEntity(new EntityHitResult(entity));
                // Remove this line if you want to check for collisions with multiple entities per tick
            }


        }
        //Timer
        if (active <= 0) {

            this.remove(RemovalReason.DISCARDED);
        }
    }


    @Override
    protected void defineSynchedData() {

    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));


    }


    private PlayState predicate(AnimationState<Moon> moonAnimationState) {
        if(world != null && world.isClientSide){
            if (moonAnimationState.isMoving()) {
                moonAnimationState.getController().setAnimation(RawAnimation.begin().then("boom", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            }
            else{
                moonAnimationState.getController().setAnimation(RawAnimation.begin().then("Crumble", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            }
        }


        return PlayState.CONTINUE;

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
