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

    // Constructor used when creating the packet to send
    public SprayPacket(Vec3 center, int particleCount, double boxSize) {
        this.center = center;
        this.particleCount = particleCount;
        this.boxSize = boxSize;
    }

    // Constructor used when decoding the packet
    public static SprayPacket decode(FriendlyByteBuf buf) {
        Vec3 center = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        int particleCount = buf.readInt();
        double boxSize = buf.readDouble();
        return new SprayPacket(center, particleCount, boxSize);
    }

    // Method for encoding the packet data
    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(center.x);
        buf.writeDouble(center.y);
        buf.writeDouble(center.z);
        buf.writeInt(particleCount);
        buf.writeDouble(boxSize);
    }

    // The packet handler that is called on the client side
    public static void handle(SprayPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                final Random random = new Random();
                for (int i = 0; i < msg.particleCount; i++) {
                    double x = msg.center.x + (random.nextDouble() - 0.5) * msg.boxSize;
                    double y = msg.center.y + (random.nextDouble() - 0.5) * msg.boxSize;
                    double z = msg.center.z + (random.nextDouble() - 0.5) * msg.boxSize;

                    mc.level.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0, 0);
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}