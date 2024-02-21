package net.Ryaas.firstmod.Networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

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
                // Correct the calculation of yawRadians and pitchRadians
                double yawRadians = Math.toRadians(msg.yaw);
                double pitchRadians = Math.toRadians(msg.pitch);

                // Directly use Minecraft's pitch orientation
                double dx = -Math.sin(yawRadians) * Math.cos(pitchRadians);
                double dy = -Math.sin(pitchRadians); // Now directly translates pitch without inversion
                double dz = Math.cos(yawRadians) * Math.cos(pitchRadians);
                Vec3 direction = new Vec3(dx, dy, dz);

                for (int i = 0; i < msg.particleCount; i++) {
                    double distance = 0.1 * i; // Example distance multiplier, adjust as needed
                    Vec3 particlePos = msg.center.add(direction.scale(distance));

                    mc.level.addParticle(ParticleTypes.HAPPY_VILLAGER, particlePos.x, particlePos.y + 1, particlePos.z, 0, 0, 0);
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}