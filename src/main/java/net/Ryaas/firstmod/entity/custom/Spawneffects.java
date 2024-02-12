package net.Ryaas.firstmod.entity.custom;

import net.Ryaas.firstmod.Networking.ModNetworking;
import net.Ryaas.firstmod.Networking.packet.Spawneffectspacket;
import net.Ryaas.firstmod.entity.client.ModEntities;
import net.Ryaas.firstmod.util.BlackHoleManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class Spawneffects extends Ranged_Projectiles {



    private int tickCount = 0; // A counter to track ticks since the cloud started forming
    private final int duration = 20 * 5; // Duration of the effect in ticks (e.g., 5 seconds)
    private final Random random = new Random();
    private UUID playerUUID;

    private int active = 100;
    private Vec3 playerLookVec;

    private int setup1 = 1;



    private int spawnAttack = 1;

    private final Level world;
    public Spawneffects(EntityType<? extends Projectile> entityType, Level world) {
        super(entityType, world);
        this.world = world; // Correct way to set the world.
    }




    public void setup(Vec3 lookVec, UUID playerUUID) {
        if(lookVec != null){
            this.playerLookVec = lookVec.normalize();
        }

        this.playerUUID = playerUUID;
    }

    @Override
    public void trail() {

    }

    @Override
    public void impactparticles(double x, double y, double z) {

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
    public void tick() {
        super.tick();
        if(setup1 == 1){
            setup(playerLookVec, playerUUID);
            setup1--;
        }


        if (world != null && !world.isClientSide) {
            // Check if the effect should still be running
            if (tickCount < duration) {
                // Calculate the radius for this tick. It starts large and decreases over time.
                double radius = 2.5 * (1 - (double) tickCount / duration);

                // Calculate the number of particles to spawn based on the remaining duration
                int particleCount = 5; // You can adjust this based on the desired density

                // Spawn the particles
                spawnEndRodParticleCloud(this.world, this.getX(), this.getY(), this.getZ(), particleCount, radius);

                tickCount++;
            }

            active--;


            //Timer
            if (active <= 0) {


                this.remove(RemovalReason.DISCARDED);
                System.out.println("Removed Spawn effects");
                spawnBlackHole();
            }
        }
    }

    public void spawnBlackHole() {
        if (!world.isClientSide) {
            BlackHoleUlt blackHoleEntity = ModEntities.BLACK_HOLE.get().create(world);
            assert blackHoleEntity != null;
            UUID blackHoleUUID = blackHoleEntity.getUUID(); // Assuming getUUID() retrieves the entity's UUID
            if (blackHoleEntity != null) {
                blackHoleEntity.setPos(this.getX(), this.getY(), this.getZ()); // Set spawn position
                if (this.playerLookVec != null) {
                    Vec3 motion = this.playerLookVec.normalize().scale(0.1); // Adjust the scale factor as needed
                    blackHoleEntity.setDeltaMovement(motion);
                    world.addFreshEntity(blackHoleEntity);

                    // Use the playerUUID to add the black hole to the manager
                    BlackHoleManager.getInstance().addBlackHole(this.playerUUID, blackHoleUUID);
                }
            }
        }
    }

    public void spawnEndRodParticleCloud(Level world, double x, double y, double z, int particleCount, double radius) {
        if (!world.isClientSide) { // Ensure this runs only on the server side

            Spawneffectspacket packet = new Spawneffectspacket(x, y, z, particleCount, radius);
            ModNetworking.getChannel().send(PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(x, y, z, 50, world.dimension())), packet);
        }
    }
}

