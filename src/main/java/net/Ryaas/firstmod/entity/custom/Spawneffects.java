package net.Ryaas.firstmod.entity.custom;

import net.Ryaas.firstmod.Networking.ModNetworking;
import net.Ryaas.firstmod.Networking.packet.Spawneffectspacket;
import net.Ryaas.firstmod.entity.client.ModEntities;
import net.Ryaas.firstmod.util.BlackHoleManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
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
    private Player player;

    private int active = 100;
    private Vec3 playerLookVec;

    private int setup1 = 1;



    private int spawnAttack = 1;

    private final Level world;
    public Spawneffects(EntityType<? extends Projectile> entityType, Level world) {
        super(entityType, world);
        this.world = world; // Correct way to set the world.
    }




    public void setup(Vec3 lookVec, UUID playerUUID, Player player) {
        if(lookVec != null){
            this.playerLookVec = lookVec.normalize();
        }

        this.player = player;

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
            setup(playerLookVec, playerUUID, player);
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
            // Create a new black hole entity
            BlackHoleUlt blackHoleEntity = ModEntities.BLACK_HOLE.get().create(world);
            if (blackHoleEntity != null) {
                // Set the black hole's position and initial motion
                blackHoleEntity.setPos(this.getX(), this.getY(), this.getZ());
                if (this.playerLookVec != null) {
                    Vec3 motion = this.playerLookVec.normalize().scale(0.1); // Adjust motion based on the look vector
                    blackHoleEntity.setDeltaMovement(motion);
                }
                blackHoleEntity.setOwner(player); // Assuming 'player' is the entity spawning the black hole

                // Add the black hole entity to the world
                world.addFreshEntity(blackHoleEntity);


                // Associate the black hole with the player in BlackHoleManager
                UUID blackHoleUUID = blackHoleEntity.getUUID(); // Get the black hole's UUID
                BlackHoleManager.getInstance().addBlackHole(this.playerUUID, blackHoleUUID);
            } else {
                // Log an error or take appropriate action if the black hole entity couldn't be created
                System.err.println("Failed to create a BlackHoleUlt entity.");
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

