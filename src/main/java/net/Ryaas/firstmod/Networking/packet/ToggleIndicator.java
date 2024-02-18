package net.Ryaas.firstmod.Networking.packet;

import net.Ryaas.firstmod.util.CooldownManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ToggleIndicator {
    // This packet does not need to carry any data if it's just a toggle

    public void encode(FriendlyByteBuf buf) {
        // No data to encode
    }

    public static ToggleIndicator decode(FriendlyByteBuf buf) {
        return new ToggleIndicator();
    }

    public static void handle(ToggleIndicator packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            if (player != null) {
                UUID playerId = player.getUUID();
                CooldownManager.toggleParticleSpawning(playerId);
                // Optionally, inform the player of the change or synchronize the new state back to the client
            }

        });
        context.setPacketHandled(true);
    }
}

