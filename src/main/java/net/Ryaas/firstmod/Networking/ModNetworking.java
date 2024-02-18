package net.Ryaas.firstmod.Networking;

import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.Networking.packet.*;
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

        net.messageBuilder(SpeedOne.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SpeedOne::decode)
                .encoder(SpeedOne::encode)
                .consumerMainThread(SpeedOne::handle)
                .add();

        net.messageBuilder(Spawneffectspacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(Spawneffectspacket::decode)
                .encoder(Spawneffectspacket::encode)
                .consumerMainThread(Spawneffectspacket::handle)
                .add();

        net.messageBuilder(BlackHolePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(BlackHolePacket::decode)
                .encoder(BlackHolePacket::encode)
                .consumerMainThread(BlackHolePacket::handle)
                .add();


        net.messageBuilder(Ring1Packet.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(Ring1Packet::decode)
                .encoder(Ring1Packet::encode)
                .consumerMainThread(Ring1Packet::handle)
                .add();

        net.messageBuilder(Ring2Packet.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(Ring2Packet::decode)
                .encoder(Ring2Packet::encode)
                .consumerMainThread(Ring2Packet::handle)
                .add();

        net.messageBuilder(Ring3Packet.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(Ring3Packet::decode)
                .encoder(Ring3Packet::encode)
                .consumerMainThread(Ring3Packet::handle)
                .add();


        net.messageBuilder(UltimateAbility.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(UltimateAbility::decode)
                .encoder(UltimateAbility::encode)
                .consumerMainThread(UltimateAbility::handle)
                .add();

        net.messageBuilder(BlackHoleTargetUpdatePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(BlackHoleTargetUpdatePacket::decode)
                .encoder(BlackHoleTargetUpdatePacket::encode)
                .consumerMainThread(BlackHoleTargetUpdatePacket::handle)
                .add();

        net.messageBuilder(Indicator.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(Indicator::decode)
                .encoder(Indicator::encode)
                .consumerMainThread(Indicator::handle)
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
