package net.Ryaas.firstmod.Networking.packet;

import net.Ryaas.firstmod.entity.custom.BlackHoleUlt;
import net.Ryaas.firstmod.util.BlackHoleManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class BlackHoleTargetUpdatePacket {
    private BlockPos targetPos;

    public BlackHoleTargetUpdatePacket(BlockPos targetPos) {
        this.targetPos = targetPos;
    }

    public static void encode(BlackHoleTargetUpdatePacket msg, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(msg.targetPos);
    }

    public static BlackHoleTargetUpdatePacket decode(FriendlyByteBuf buffer) {
        BlockPos targetPos = buffer.readBlockPos();
        return new BlackHoleTargetUpdatePacket(targetPos);
    }

    public static void handle(BlackHoleTargetUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer player = ctx.get().getSender();
        if (player == null) return;

        ctx.get().enqueueWork(() -> {
            UUID blackHoleUUID = BlackHoleManager.getInstance().getBlackHoleUUIDByPlayer(player.getUUID());
            if (blackHoleUUID != null) {
                Entity entity = ((ServerLevel) player.level()).getEntity(blackHoleUUID);
                if (entity instanceof BlackHoleUlt) {
                    BlackHoleUlt blackHole = (BlackHoleUlt) entity;
                    System.out.println(msg.targetPos);
                    blackHole.moveToTarget(msg.targetPos);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}