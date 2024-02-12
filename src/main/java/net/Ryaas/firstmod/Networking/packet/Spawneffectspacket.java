package net.Ryaas.firstmod.Networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Random;
import java.util.function.Supplier;

public class Spawneffectspacket {
    private final double x, y, z;
    private final int count;
    private final double radius;


    public Spawneffectspacket(double x, double y, double z, int count, double radius) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.count = count;
        this.radius = radius;
    }

    public static void encode(Spawneffectspacket msg, FriendlyByteBuf buffer) {
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
        buffer.writeInt(msg.count);
        buffer.writeDouble(msg.radius);
    }

    public static Spawneffectspacket decode(FriendlyByteBuf buffer) {
        return new Spawneffectspacket(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readInt(), buffer.readDouble());
    }

    public static void handle(Spawneffectspacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            // Execute on the client side
            ClientLevel world = Minecraft.getInstance().level;
            if (world != null) {
                Random random = new Random();
                for (int i = 0; i < msg.count; i++) {
                    double angle = random.nextDouble() * Math.PI * 2;
                    double zAngle = random.nextDouble() * Math.PI;
                    double xOffset = Math.cos(angle) * Math.sin(zAngle) * msg.radius;
                    double yOffset = Math.sin(angle) * Math.sin(zAngle) * msg.radius + 1;
                    double zOffset = Math.cos(zAngle) * msg.radius;

                    world.addParticle(ParticleTypes.END_ROD, msg.x + xOffset, msg.y + yOffset, msg.z + zOffset, 0, 0, 0);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}