package net.Ryaas.firstmod.Networking.packet;

import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.util.PlayerHealingManager;
import net.Ryaas.firstmod.util.Telekinesis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class MainAbility {
//    private final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
//    private final Set<UUID> playersWithActiveAbility = new HashSet<>();
//    private final Map<UUID, Entity> heldEntities = new HashMap<>();
//

    public MainAbility(){

    }

    public MainAbility(FriendlyByteBuf buf){

    }

    public void toBytes(FriendlyByteBuf buf){

    }



    public static void handle(MainAbility message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if(player != null) {
                CompoundTag playerData = player.getPersistentData();
                // Check if the player has the "AbilitySet" key and it's set to 1
                //WIZZZZZZY
                if (playerData.contains("AbilitySet") && playerData.getInt("AbilitySet") == 1) {


                    // Your logic here
                    Telekinesis telekinesis = FirstMod.getGameLogicManager().getTelekinesisHandler().getOrCreateTelekinesis(player);
                    telekinesis.activateAbility(player);
                }

                //HEALINGGGGGG
                else {

                    if (PlayerHealingManager.getPlayerEnergy(player.getUUID()) >= 10.0f) { // Requires 10 energy to use)
                        Vec3 lookVec = player.getLookAngle();
                        Vec3 startVec = player.position().add(0, player.getEyeHeight(), 0).add(lookVec.scale(0.5)); // Start in front of the player


                        for (int i = 0; i < 10; i++) {

                            Vec3 particlePos = startVec.add(lookVec.scale(i * 0.1)); // Adjust scale for distance
                            PlayerHealingManager.sendSpray(player);
                            // New: Detect and heal entities
                            AABB areaOfEffect = new AABB(particlePos.x - 0.5, particlePos.y - 0.5, particlePos.z - 0.5, particlePos.x + 0.5, particlePos.y + 0.5, particlePos.z + 0.5);
                            List<Entity> entities = player.level().getEntities(player, areaOfEffect);
                            for (Entity entity : entities) {
                                if (entity instanceof LivingEntity) {
                                    LivingEntity livingEntity = (LivingEntity) entity;
                                    livingEntity.heal(1.0F); // Heal amount, adjust as needed
                                }
                            }
                        }
                    }
                }
            }







        });
        ctx.setPacketHandled(true);
    }
}
