package net.Ryaas.firstmod.Networking.packet;

import net.Ryaas.firstmod.util.Draining;
import net.Ryaas.firstmod.util.ModGameLogicManager;
import net.Ryaas.firstmod.util.Telekinesis;
import net.Ryaas.firstmod.util.TelekinesisHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SecondaryAbility {
//    private final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
//    private final Set<UUID> playersWithActiveAbility = new HashSet<>();
//    private final Map<UUID, Entity> heldEntities = new HashMap<>();
//

    public SecondaryAbility(){

    }

    public SecondaryAbility(FriendlyByteBuf buf){

    }

    public void toBytes(FriendlyByteBuf buf){

    }



    public static void handle(SecondaryAbility message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if(player != null) {
                CompoundTag playerData = player.getPersistentData();
                // Check if the player has the "AbilitySet" key and it's set to 1
                if (playerData.contains("AbilitySet") && playerData.getInt("AbilitySet") == 1) {

                    TelekinesisHandler handler = ModGameLogicManager.getTelekinesisHandler();

                    // Correctly retrieve the Telekinesis instance for the player
                    Telekinesis telekinesis = handler.getOrCreateTelekinesis(player);
                    if (telekinesis != null) {
                        // Adjust the entity distance based on whether the player is shifting
                        boolean increaseDistance = player.isShiftKeyDown();
                        telekinesis.adjustEntityDistance(player, increaseDistance);
                    }
                }
                if (playerData.contains("AbilitySet") && playerData.getInt("AbilitySet") == 2) {
                    Draining.drainHealthFromTargets(player, 10.0, 2.0f, 1.0f, 0.1);

                }
            }


            // Your logic here


        });
        ctx.setPacketHandled(true);
    }
}
