package net.Ryaas.firstmod.Networking.packet;


import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class BlackHolePacket {

    private final double x, y, z; // Position for the particle effect
    private final int count; // Number of particles to spawn

    public BlackHolePacket(double x, double y, double z, int count) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.count = count;
    }

    public static void encode(BlackHolePacket msg, FriendlyByteBuf buffer) {
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
        buffer.writeInt(msg.count);
    }

    public static BlackHolePacket decode(FriendlyByteBuf buffer) {
        return new BlackHolePacket(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readInt());
    }

    public static void handle(BlackHolePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Handle packet on the client side
            // This should be a safe operation since it's called on the client side
            ClientLevel world = Minecraft.getInstance().level;
            if (world != null) {
                double radius = 2.5;
                int particleCount = 50;
                float particleScale = 3;

                for (int i = 0; i < particleCount; i++) {
                    // Random angles for spherical coordinates
                    double theta = Math.random() * 2 * Math.PI;
                    double phi = Math.random() * Math.PI;
                    double x = msg.x + radius * Math.sin(phi) * Math.cos(theta);
                    double y = msg.y + radius * Math.sin(phi) * Math.sin(theta);
                    double z = msg.z + radius * Math.cos(phi);

                    // Color and scale
                    float red = 0.0F;
                    float green = 0.0F;
                    float blue = 0.0F; // Adjust RGB values as needed

                    DustParticleOptions dustOptions = new DustParticleOptions(new Vector3f(red, green, blue), particleScale);

                    world.addParticle(dustOptions, true, x, y, z, 0.0D, 0.0D, 0.0D);

                }
            }

        });
        ctx.get().setPacketHandled(true);
    }
}



