package net.Ryaas.firstmod.entity.custom;

import net.Ryaas.firstmod.Networking.ModNetworking;
import net.Ryaas.firstmod.Networking.packet.BlackHolePacket;
import net.Ryaas.firstmod.Networking.packet.Ring1Packet;
import net.Ryaas.firstmod.Networking.packet.Ring2Packet;
import net.Ryaas.firstmod.Networking.packet.Ring3Packet;
import net.Ryaas.firstmod.util.getTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.Optional;


public class BlackHoleUlt extends Ranged_Projectiles {
    private final Level world;

    private float rotationAngle = 0.0F;
    private int activetime = 500;

    private boolean isActive = false;
    private int chargeup = 20;

    private Vec3 targetDirection = Vec3.ZERO;


    public BlackHoleUlt(EntityType<? extends Projectile> entityType, Level world) {
        super(entityType, world);
        this.world = world;
    }

    @Override
    public void trail() {

    }

    @Override
    public void impactparticles(double x, double y, double z) {

    }

    public void setTargetDirection(Vec3 direction) {
        this.targetDirection = direction;
    }

    public void moveToTarget(BlockPos targetPos) {


        Vec3 currentPosition = new Vec3(this.getX(), this.getY(), this.getZ());
        Vec3 targetPosition = Vec3.atCenterOf(targetPos);
        Vec3 direction = targetPosition.subtract(currentPosition).normalize();

        // Example movement logic - adjust as needed for your entity
        this.setDeltaMovement(direction.scale(0.5)); // Move towards the target
        // Update the black hole's position or motion vector based on your game mechanics
    }



    public void updateFollowingDirection(Vec3 lookVec) {
        this.targetDirection = lookVec;
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


    // Existing implementation...

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }


    @Override
    public void tick() {
        super.tick();
        if (!this.world.isClientSide) { // Example: Send packet every second (20 ticks)
            if (targetDirection.lengthSqr() > 0) {
                Vec3 newMotion = targetDirection.normalize().scale(0.1); // Scale represents speed
                this.setDeltaMovement(newMotion);
            }
            // Define the particle spawning parameters
            double x = this.getX();
            double y = this.getY();
            double z = this.getZ();
            int particleCount = 50; // Example count, adjust as needed

            // Create a new BlackHolePacket with your parameters
            BlackHolePacket packet = new BlackHolePacket(x, y, z, particleCount);

            // Send the packet to all players within a certain range
            ModNetworking.getChannel().send(PacketDistributor.NEAR.with(() ->
                            new PacketDistributor.TargetPoint(x, y, z, 50, this.world.dimension())),
                    packet);
            Ring1Packet ring1Packet = new Ring1Packet(x, y, z, particleCount, this.rotationAngle);
            ModNetworking.getChannel().send(PacketDistributor.NEAR.with(() ->
                            new PacketDistributor.TargetPoint(x, y, z, 50, this.world.dimension())),
                    ring1Packet);

            Ring2Packet ring2Packet = new Ring2Packet(x, y, z, particleCount, this.rotationAngle);
            ModNetworking.getChannel().send(PacketDistributor.NEAR.with(() ->
                            new PacketDistributor.TargetPoint(x, y, z, 50, this.world.dimension())),
                    ring2Packet);

            Ring3Packet ring3Packet = new Ring3Packet(x, y, z, particleCount, this.rotationAngle);
            ModNetworking.getChannel().send(PacketDistributor.NEAR.with(() ->
                            new PacketDistributor.TargetPoint(x, y, z, 50, this.world.dimension())),
                    ring3Packet);



            activetime--;
            chargeup--;


            if(chargeup <= 0){
                Vec3 motion = this.getDeltaMovement();

                this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);
                getTarget.toggleActive();

            }
            }







            //Timer
            if(activetime <= 0){

                this.remove(RemovalReason.DISCARDED);
                System.out.println("Removed black hole");
                getTarget.toggleActive();
            }
        }
    public void updateDirection(Vec3 lookVec) {
        // Normalize the look vector to ensure it has unit length, then scale to desired speed.
        Vec3 normalizedLookVec = lookVec.normalize();
        double speed = 0.1; // Example speed value, adjust as needed.

        // Set the entity's motion. You might need to adjust this based on how you handle entity physics.
        this.setDeltaMovement(normalizedLookVec.scale(speed));

        // Optional: If you want the entity to instantly face the direction it's moving towards,
        // you can update its rotation here as well. This example assumes a method to do so.
        // updateRotationBasedOnLookVec(normalizedLookVec);


    }
    private void updateRotationBasedOnLookVec(Vec3 lookVec) {
        // Calculate the yaw (rotationYaw) and pitch (rotationPitch) based on the lookVec.
        // This is a simplified example; actual implementation may vary based on your needs.
        float yaw = (float) Math.toDegrees(Math.atan2(lookVec.z, lookVec.x)) - 90.0F;
        float pitch = (float) Math.toDegrees(Math.atan2(lookVec.y, Math.sqrt(lookVec.x * lookVec.x + lookVec.z * lookVec.z)));

        this.setYRot(yaw); // Set entity yaw (rotation around the vertical axis)
        this.setXRot(pitch); // Set entity pitch (rotation around the horizontal axis)

        // Adjust these methods to match your entity's methods for setting rotation.
    }
}


