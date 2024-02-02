package net.Ryaas.firstmod.Networking.packet;

import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.util.Telekinesis;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

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

                // Your logic here
                Telekinesis telekinesis = FirstMod.getGameLogicManager().getTelekinesisHandler().getOrCreateTelekinesis(player);
                telekinesis.activateAbility(player);

        });
        ctx.setPacketHandled(true);
    }
}
