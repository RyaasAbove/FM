package net.Ryaas.firstmod.entity.custom;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

import java.util.Optional;

public abstract class Ranged_Projectiles extends Projectile {
    protected static final int DURATION = 300;
    protected int age;
    protected float damage;
    protected float radius;



    public abstract void trail();

    public abstract void impactparticles(double x, double y, double z);

    public abstract float getSpeed();

    public abstract Optional<SoundEvent> getImpactSound();


    protected Ranged_Projectiles(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

//    public void shoot(Vec3 rotation) {
//        Vec3 adjustedRotation = rotation.normalize().scale(getSpeed());
//        this.setDeltaMovement(adjustedRotation.x, adjustedRotation.y, adjustedRotation.z);
//    }


}
