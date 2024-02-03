package net.Ryaas.firstmod.Networking.packet;

import net.Ryaas.firstmod.item.helper.BSWeaponUtil;
import net.Ryaas.firstmod.util.MarkActivater;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MarkPacketc2s implements BSWeaponUtil {
    private Map<LivingEntity, Vec3> initialPositions = new HashMap<LivingEntity, Vec3>();
    private static final int MAX_HIT_COUNT = 5;
    private double angularOffset = 0.0;
    private static final double TARGET_RADIUS = 20.0;


    private static final String HIT_COUNT_KEY = "hitCount";

    public MarkPacketc2s() {

    }

    public MarkPacketc2s(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public void handler(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Level world = player.getCommandSenderWorld();

            ServerLevel level = (ServerLevel) world;
            MarkActivater.activateMark(player, world);
        });
    }
}
//            System.out.println(player);
//            List<LivingEntity> entities = findEntitiesInRadius(player, level, TARGET_RADIUS);
//
//            for (int i = 0; i < entities.size(); i++) {
//                LivingEntity entity = entities.get(i);
//                CompoundTag data = entity.getPersistentData();
//                System.out.println("Entity type: " + entity.getType());
//                Vec3 velocity = entity.getDeltaMovement();
//                int hitCount = data.getInt("hitCount");
//                if (hitCount == 1) {
//
//                    //Rising proj
//                    entity.invulnerableTime = 0;
//                    MageHand risingShot = new MageHand(ModEntities.MAGE_HAND.get(), world);
//                    Vec3 targetPos = entity.position();
//                    risingShot.setPos(targetPos.x, targetPos.y - 1.0, targetPos.z);
//                    risingShot.setDeltaMovement(new Vec3(0, 3.0, 0));
//                    entity.setDeltaMovement(velocity.x, 5, velocity.z);
//                    world.addFreshEntity(risingShot);
//                    entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 60, 0));
//                    data.putBoolean("HitByRisingProjectile", true);
//
//                    //Slash
//
//
//                    Vec3 entityPos = entity.position();
//
//                    if (entity.getDeltaMovement().y > 0.1) {
//                        double entityHeight = entity.getBbHeight();
//                        double entityWidth = entity.getBbWidth();
//
//                        // Calculate start and end points of the slash
//                        Vec3 startPoint = entityPos.add(-entityWidth / 2, entityHeight / 2, 0);
//                        Vec3 endPoint = entityPos.add(entityWidth / 2, entityHeight / 2, 0);
//
//                        // Number of particles to spawn along the path
//                        int particleCount = 10;
//
//                        for (int j = 0; j < particleCount; j++) {
//                            double t = j / (double) particleCount;
//                            Vec3 particlePos = startPoint.lerp(endPoint, t);
//
//                            world.addParticle(ParticleTypes.SMOKE,
//                                    particlePos.x, particlePos.y, particlePos.z,
//                                    0, 0, 0); // No motion for particles
//                            world.addParticle(ParticleTypes.SMOKE,
//                                    particlePos.x, particlePos.y, particlePos.z,
//                                    0, 0, 0); // No motion for particles
//                            world.addParticle(ParticleTypes.SMOKE,
//                                    particlePos.x, particlePos.y, particlePos.z,
//                                    0, 0, 0); // No motion for particles
//                            world.addParticle(ParticleTypes.SMOKE,
//                                    particlePos.x, particlePos.y, particlePos.z,
//                                    0, 0, 0); // No motion for particles
//                            world.addParticle(ParticleTypes.SMOKE,
//                                    particlePos.x, particlePos.y, particlePos.z,
//                                    0, 0, 0); // No motion for particles
//
//                        }
//
//                    }
//
//
//                    decrementHitCount(entity, HIT_COUNT_KEY);
//                }
//                if (hitCount == 2) {
//                    Vec3 lookVector = player.getLookAngle();
//                    Vec3 startPos = entity.position();
//                    initialPositions.put(entity, startPos);
//                    data.putBoolean("Carried", true);
//
//                    entity.setDeltaMovement(lookVector.x * 7, lookVector.y * 7, lookVector.z * 7);
//
//
//                }
//                if (hitCount == 3) {
//                    entity.invulnerableTime = 0;
//                    createTornadoEffect(entity);
//                    data.putBoolean("Stormshot", true);
//                    spawnNextMageHand(entity);
//                    decrementHitCount(entity, HIT_COUNT_KEY);
//                }
//                if (hitCount == 4) {
//                    Vec3 targetpos = entity.position();
//                    entity.invulnerableTime = 0;
//                    double spawnX = targetpos.x;
//                    double spawnY = targetpos.y; // 31 blocks above
//                    double spawnZ = targetpos.z;
//                    Beamattack beamattack = new Beamattack(ModEntities.BEAM_ATTACK.get(), world);
//                    beamattack.setPos(spawnX, spawnY, spawnZ);
//                    world.addFreshEntity(beamattack);
//                    decrementHitCount(entity, HIT_COUNT_KEY);
//                }
//                if (hitCount == 5) {
//
//                    entity.invulnerableTime = 0;
//                    // Assuming 'target' is the target entity and 'world' is the Level instance
//
//                    Vec3 targetPos = entity.position();
//
//                    Vec3 vec3d = new Vec3(targetPos.x, targetPos.y, targetPos.z).normalize().scale(2.0f);
//
//// Random offsets
//                    double offsetX = (Math.random() - 0.5) * 30; // 30 is the maximum horizontal offset
//                    double offsetY = 50 + Math.random() * 20; // Spawn between 50 and 70 blocks above
//                    double offsetZ = (Math.random() - 0.5) * 30;
//
//// Calculating spawn position
//                    double spawnX = targetPos.x + offsetX;
//                    double spawnY = targetPos.y + offsetY;
//                    double spawnZ = targetPos.z + offsetZ;
//
//// Creating and spawning the entity
//                    Moon moon = new Moon(ModEntities.MOON.get(), world);
//                    moon.setPos(spawnX, spawnY, spawnZ);
//                    world.addFreshEntity(moon);
//                    Vec3 direction = targetPos.subtract(new Vec3(spawnX, spawnY, spawnZ)).normalize();
//                    moon.setDeltaMovement(direction.x * 4, direction.y * 4, direction.z * 4);
//
//
//                }
//
//
//            }
//
//
//            });
//        return true;
//    }


//    private List<LivingEntity> findEntitiesInRadius(Player player, Level level, double radius) {
//        Vec3 playerPos = player.position();
//        AABB box = new AABB(playerPos.x - radius, playerPos.y - radius, playerPos.z - radius,
//                playerPos.x + radius, playerPos.y + radius, playerPos.z + radius);
//        return level.getEntitiesOfClass(LivingEntity.class, box, e -> true);
//    }
//    public static void decrementHitCount(LivingEntity entity, String key) {
//        CompoundTag entityData = entity.getPersistentData();
//        int hitCount = entityData.getInt(key);
//        entityData.putInt(key, Math.max(0, hitCount - 1));
//        entity.addAdditionalSaveData(entityData);
//    }
//
//
//
//    private void spawnNextMageHand(Entity target) {
//        double stormRadius = 5.0;
//        int mageHandCount = 10;
//        double spawnHeight = 15;
//        Level world = target.level();
//        Vec3 centerPos = target.position();
//        if (world != null && !world.isClientSide) {
//            for (int i = 0; i < mageHandCount; i++) {
//                double angle = Math.random() * Math.PI * 2;
//                double distance = Math.random() * stormRadius;
//                double xOffset = Math.cos(angle) * distance;
//                double zOffset = Math.sin(angle) * distance;
//
//                Vec3 spawnPos = centerPos.add(xOffset, spawnHeight, zOffset);
//                MageHand mageHand = new MageHand(ModEntities.MAGE_HAND.get(), world);
//                mageHand.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
//                mageHand.setDeltaMovement(0, -1.0, 0); // Falling straight down
//
//                world.addFreshEntity(mageHand);
//                CompoundTag data = target.getPersistentData();
////            if(data.contains("Stormshot")){
//            }
//        }
//    }
//
//    public void createTornadoEffect(Entity entity) {
//        Level level = Minecraft.getInstance().level;
//        Vec3 entityPos = entity.position();
//        double radius = 2.0; // Radius of the tornado
//        int height = 15; // Height of the tornado
//        int particlesPerLayer = 20;
//        double particleSpawnRate = 2.0; // Number of particles per horizontal layer
//
//
//
//
//        if(level != null){
//            for (int w = 0; w < height; w++) {
//                double yOffset = w * 1.5; // Vertical spacing between layers
//
//                for (int i = 0; i < particlesPerLayer; i++) {
//                    if (Math.random() < particleSpawnRate) {
//
//                        double angle = 2 * Math.PI * i / particlesPerLayer + angularOffset;
//
//                        double xOffset = radius * Math.cos(angle);
//                        double zOffset = radius * Math.sin(angle);
//
//                        double x = entityPos.x + xOffset;
//                        double y = entityPos.y + yOffset;
//                        double z = entityPos.z + zOffset;
//
//                        level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 0, 0, 0);
//                    }
//                }
//            }
//        }
//    }
//
//}
//

