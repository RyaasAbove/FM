package net.Ryaas.firstmod.Networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.Random;
import java.util.function.Supplier;

public class SprayPacket {
    private final Vec3 center;
    private final int particleCount;
    private final double boxSize;
    // Assuming you pass the yaw and pitch directly to the packet for simplicity
    private final float yaw;
    private final float pitch;

    // Constructor used when creating the packet to send
    public SprayPacket(Vec3 center, int particleCount, double boxSize, float yaw, float pitch) {
        this.center = center;
        this.particleCount = particleCount;
        this.boxSize = boxSize;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    // Constructor used when decoding the packet
    public static SprayPacket decode(FriendlyByteBuf buf) {
        Vec3 center = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        int particleCount = buf.readInt();
        double boxSize = buf.readDouble();
        float yaw = buf.readFloat();
        float pitch = buf.readFloat();
        return new SprayPacket(center, particleCount, boxSize, yaw, pitch);
    }

    // Method for encoding the packet data
    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(center.x);
        buf.writeDouble(center.y);
        buf.writeDouble(center.z);
        buf.writeInt(particleCount);
        buf.writeDouble(boxSize);
        buf.writeFloat(yaw);
        buf.writeFloat(pitch);
    }

    // The packet handler that is called on the client side
    public static void handle(SprayPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();

        ctx.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {


                final Random random = new Random();

                double yawRadians = Math.toRadians(msg.yaw);
                double pitchRadians = Math.toRadians(msg.pitch);

                // Main direction of the beam
                double dx = -Math.sin(yawRadians) * Math.cos(pitchRadians);
                double dy = -Math.sin(pitchRadians);
                double dz = Math.cos(yawRadians) * Math.cos(pitchRadians);
                Vec3 direction = new Vec3(dx, dy, dz);

                // Calculate a right vector perpendicular to the direction
                Vec3 up = new Vec3(0, 1, 0); // World up vector
                Vec3 right = direction.cross(up).normalize(); // Perpendicular right vector
                Vec3 perp = right.cross(direction).normalize(); // Perpendicular vector in the plane of direction and up

                for (int i = 0; i < msg.particleCount; i++) {
                    double distance = 0.1 * i;
                    Vec3 basePos = msg.center.add(direction.scale(distance));

                    // Adding randomness perpendicular to the direction for a wider beam
                    double spread = 0.5; // Adjust this value to control the beam's width
                    double offsetX = (random.nextDouble() - 0.5) * spread;
                    double offsetZ = (random.nextDouble() - 0.5) * spread;

                    Vec3 particlePos = basePos.add(right.scale(offsetX)).add(perp.scale(offsetZ));

                    mc.level.addParticle(ParticleTypes.GLOW, particlePos.x, particlePos.y + 1, particlePos.z, 0, 0, 0);
                }
            }

        });
        ctx.setPacketHandled(true);
    }
}