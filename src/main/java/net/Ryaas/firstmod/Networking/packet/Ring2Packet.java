package net.Ryaas.firstmod.Networking.packet;


import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class Ring2Packet {

    private final double x, y, z; // Position for the particle effect
    private final int count; // Number of particles to spawn
    private final float rotationAngle; // Rotation angle for the particle effect


    public Ring2Packet(double x, double y, double z, int count, float rotationAngle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.count = count;
        this.rotationAngle = rotationAngle;
    }

    public static void encode(Ring2Packet msg, FriendlyByteBuf buffer) {
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
        buffer.writeInt(msg.count);
        buffer.writeFloat(msg.rotationAngle);

    }

    public static Ring2Packet decode(FriendlyByteBuf buffer) {
        return new Ring2Packet(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readInt(), buffer.readFloat());
    }

    public static void handle(Ring2Packet msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Handle packet on the client side
            // This should be a safe operation since it's called on the client side
            ClientLevel world = Minecraft.getInstance().level;
            if (world != null) {
                double radius = 2.5;
                int particleCount = 32;
                float particleScale = 5;



                float red2 = 1.0F; // Yellow color component
                float green2 = .6570F; // Yellow color component
                float blue2 = 0.0F; // Yellow color component
                DustParticleOptions dustParticleOptions = new DustParticleOptions(new Vector3f(red2, green2, blue2), particleScale);

                for (int j = 0; j < particleCount; j++) {
                    radius = 4;
                    double angle = 2 * Math.PI * j / particleCount + msg.rotationAngle;
                    double xr = msg.x + radius * Math.cos(angle);
                    double zr = msg.z + radius * Math.sin(angle);
                    double yr = msg.y; // Keep Y coordinate constant to make the ring horizontal
                    radius = 4.5;
                    double xr1 = msg.x + radius  * Math.cos(angle);
                    double zr1 = msg.z + radius  * Math.sin(angle);
                    double yr1 = msg.y; // Keep Y coordinate constant to make the ring horizontal
                    radius = 5.0;
                    double xr2 = msg.x + radius  * Math.cos(angle);
                    double zr2 = msg.z + radius  * Math.sin(angle);
                    double yr2 = msg.y; // Keep Y coordinate constant to make the ring horizontal

                    radius = 5.5;
                    double xr3 = msg.x + radius  * Math.cos(angle);
                    double zr3 = msg.z + radius  * Math.sin(angle);
                    double yr3 = msg.y; // Keep Y coordinate constant to make the ring horizontal


                    world.addParticle(dustParticleOptions, true, xr, yr, zr, 0.0D, 0.0D, 0.0D);
                    world.addParticle(dustParticleOptions, true, xr1, yr1, zr1, 0.0D, 0.0D, 0.0D);
                    world.addParticle(dustParticleOptions, true, xr2, yr2, zr2, 0.0D, 0.0D, 0.0D);
                    world.addParticle(dustParticleOptions, true, xr3, yr3, zr3, 0.0D, 0.0D, 0.0D);


                }
            }


        });
        ctx.get().setPacketHandled(true);
    }
}



