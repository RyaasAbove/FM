package net.Ryaas.firstmod.Networking;

import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.Networking.packet.MainAbility;
import net.Ryaas.firstmod.Networking.packet.MarkPacketc2s;
import net.Ryaas.firstmod.Networking.packet.SecondaryAbility;
import net.Ryaas.firstmod.Networking.packet.particlepacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetworking {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(FirstMod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
        INSTANCE = net;

        net.messageBuilder(MarkPacketc2s.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MarkPacketc2s::new)
                .encoder(MarkPacketc2s::toBytes)
                .consumerMainThread(MarkPacketc2s::handler)
                .add();

        net.messageBuilder(MainAbility.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MainAbility::new)
                .encoder(MainAbility::toBytes)
                .consumerMainThread(MainAbility::handle)
                .add();

        net.messageBuilder(SecondaryAbility.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SecondaryAbility::new)
                .encoder(SecondaryAbility::toBytes)
                .consumerMainThread(SecondaryAbility::handle)
                .add();

        net.messageBuilder(particlepacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(particlepacket::decode)
                .encoder(particlepacket::encode)
                .consumerMainThread(particlepacket::handle)
                .add();





    }
    public static <MSG> void sendToServer(MSG message){
        INSTANCE.sendToServer(message);
    }

public static <MSG> void sendToPlayer(MSG message, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
}
    public static SimpleChannel getChannel() {
        return INSTANCE;
    }

}
