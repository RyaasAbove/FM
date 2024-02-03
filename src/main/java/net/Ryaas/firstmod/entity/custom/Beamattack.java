package net.Ryaas.firstmod.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
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

public class Beamattack extends Ranged_Projectiles implements  GeoEntity{

        private final Level world;
        private int active = 60;

        private static final String HIT_COUNT_KEY = "hitCount";
        private float damage = 5;
        private boolean charged = false;

    public void setDamage(float damage){
        this.damage = damage;
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

    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);


        Entity target = hitResult.getEntity();
        Vec3 hitPosition = hitResult.getLocation();

        // Implement logic for what happens when the projectile hits an entity
        if (target instanceof LivingEntity) {
            // Example: Deal damage to the entity
            LivingEntity livingEntity = (LivingEntity) target;
            livingEntity.hurt(damageSources().magic(), damage);
            livingEntity.invulnerableTime = 1;
        }
    }
    








        private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);



        public Beamattack(EntityType<? extends Projectile> entityType, Level world) {
            super(entityType, world);
            this.world = world;
            this.setNoGravity(true);
            this.setBoundingBox(new AABB(-1 / 2, 0, -1 / 2, 1 / 2, 1, 1 / 2));
            // WIDTH, HEIGHT, and LENGTH are the dimensions of your entity

        }

    @Override
    public void trail() {

    }

    @Override
    public void tick() {
        super.tick();

        if (world != null && !this.world.isClientSide) {

        }
        active--;

        AABB collisionBox = this.getBoundingBox();
        List<Entity> nearbyEntities = this.world.getEntitiesOfClass(Entity.class, collisionBox.inflate(0.5), e -> e != this && e.isAlive());

        for (Entity entity : nearbyEntities) {
            if (collisionBox.intersects(entity.getBoundingBox())) {
                // Collision detected, handle it
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
    public float getSpeed() {
        return 0;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.empty();
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));


    }

    private PlayState predicate(AnimationState<Beamattack> beamattackAnimationState) {
//        if (beamattackAnimationState.isMoving()) {
//            beamattackAnimationState.getController().setAnimation(RawAnimation.begin().then("Fistproj", Animation.LoopType.LOOP));
//            return PlayState.CONTINUE;
//        }
//        beamattackAnimationState.getController().setAnimation(RawAnimation.begin().then("Fistproj", Animation.LoopType.LOOP));
//        return PlayState.CONTINUE;
        if(!charged){
            beamattackAnimationState.getController().setAnimation(RawAnimation.begin().then("Charge", Animation.LoopType.PLAY_ONCE));
            charged = true;
        }
        else{

            beamattackAnimationState.getController().setAnimation(RawAnimation.begin().then("Beam", Animation.LoopType.PLAY_ONCE));

        }
        return PlayState.CONTINUE;

    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
