package net.Ryaas.firstmod.entity.custom;

import net.Ryaas.firstmod.Networking.ModNetworking;
import net.Ryaas.firstmod.Networking.packet.BlackHolePacket;
import net.Ryaas.firstmod.Networking.packet.Ring1Packet;
import net.Ryaas.firstmod.Networking.packet.Ring2Packet;
import net.Ryaas.firstmod.Networking.packet.Ring3Packet;
import net.Ryaas.firstmod.util.getTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class BlackHoleUlt extends Ranged_Projectiles {
    private final Level world;

    private float rotationAngle = 0.0F;
    private int activetime = 500;

    private boolean isActive = false;
    private int chargeup = 20;

    private Vec3 targetDirection = Vec3.ZERO;
    private UUID ownerUuid;
    private Player owner;
    private ServerLevel serverLevel;



    private double suctionRadius = 5.0;

    private double damageAmount = 2.0; // Example damage amount
    private int damageInterval = 10; // Damage every 20 ticks (1 second at 20 ticks/second)
    private int tickCounter = 0; // Counter to keep track of ticks for damage interval


    public BlackHoleUlt(EntityType<? extends Projectile> entityType, Level world) {
        super(entityType, world);
        this.world = world;
    }

    @Override
    public void trail() {

    }


    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (ownerUuid != null) {
            compound.putUUID("OwnerUUID", ownerUuid);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("OwnerUUID")) {
            ownerUuid = compound.getUUID("OwnerUUID");
        }
    }

    public void setOwner(Player player) {
        this.ownerUuid = player.getUUID();
        this.owner = player;
    }

    // Method to get the owner as a Player object
    public UUID getCreator() {
        if (this.level() instanceof ServerLevel) {
            return ownerUuid;
        }
        return null; // Return null if the owner cannot be found or if this is called on the client side
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
    public BlockPos findDistantBlock(Player player, Level world, int maxDistance) {
        Vec3 startVec = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getViewVector(1.0F);
        Vec3 endVec = startVec.add(lookVec.x * maxDistance, lookVec.y * maxDistance, lookVec.z * maxDistance);

        ClipContext context = new ClipContext(startVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player);
        BlockHitResult hitResult = world.clip(context);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return hitResult.getBlockPos();
        }

        return null; // No block was hit within the distance
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
            Vec3 blackHolePos = new Vec3(this.getX(), this.getY(), this.getZ());

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



            //Suction
            List<Entity> affectedEntities = world.getEntities(
                    this,
                    this.getBoundingBox().inflate(suctionRadius),
                    entity -> entity.isAlive() && !(entity instanceof BlackHoleUlt) // Example condition, adjust as needed
            );


            for (Entity entity : affectedEntities) {
                if(!entity.getUUID().equals(this.ownerUuid)){
                    continue;
                }
                    Vec3 directionToBlackHole = blackHolePos.subtract(entity.position()).normalize();
                    double distance = blackHolePos.distanceTo(entity.position());

                    // Calculate the strength of the pull based on distance (optional)
                    double pullStrength = 5.0 - (distance / suctionRadius);

                    // Apply motion towards the black hole; adjust the factor to control the speed
                    entity.setDeltaMovement(entity.getDeltaMovement().add(directionToBlackHole.scale(pullStrength * 0.1)));


            }
            tickCounter++;

            // Check if it's time to apply damage
            if (tickCounter >= damageInterval) {
                for (Entity entity : affectedEntities) {
                    if(entity instanceof LivingEntity )
                        if (!entity.getUUID().equals(this.ownerUuid)) {
                            continue;
                        }
                        (entity).hurt(damageSources().magic(), (float)damageAmount); // Apply damage


                }
                tickCounter = 0; // Reset counter after applying damage
            }

            if (tickCounter % 20 == 0) {
                destroyBlocksAround();
            }






            activetime--;
            chargeup--;


            if(chargeup <= 0){
                Vec3 motion = this.getDeltaMovement();

                this.setPos(this.getX() + motion.x, this.getY() + motion.y, this.getZ() + motion.z);

                getTarget.toggleActive();

                BlockPos pos = getTarget.getTargetedBlock(owner, 100);



            }
            }







            //Timer
            if(activetime <= 0){

                this.remove(RemovalReason.DISCARDED);
                System.out.println("Removed black hole");
                getTarget.toggleActive();
            }
        }



private void destroyBlocksAround() {
    int radius = 3;
    BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

    for (int x = -radius; x <= radius; x++) {
        for (int y = -radius; y <= radius; y++) {
            for (int z = -radius; z <= radius; z++) {
                mutable.set(this.getX() + x, this.getY() + y, this.getZ() + z);
                BlockState blockState = world.getBlockState(mutable);
                // Check if the block is not air and possibly other conditions
                if (!blockState.isAir()) {
                    // Destroy the block without dropping items
                    world.destroyBlock(mutable, false);
                }
            }
        }
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


