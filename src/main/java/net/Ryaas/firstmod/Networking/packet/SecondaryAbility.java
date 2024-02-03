package net.Ryaas.firstmod.Networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

            // Your logic here
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 60, 0));

        });
        ctx.setPacketHandled(true);
    }
}
