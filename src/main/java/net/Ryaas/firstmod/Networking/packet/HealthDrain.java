package net.Ryaas.firstmod.Networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.Random;
import java.util.function.Supplier;

public class HealthDrain {
    private final Vec3 startPosition;
    private final Vec3 endPosition;

    // Constructor
    public HealthDrain(Vec3 startPosition, Vec3 endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    // Decoder
    public static HealthDrain decode(FriendlyByteBuf buf) {
        Vec3 startPosition = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        Vec3 endPosition = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        return new HealthDrain(startPosition, endPosition);
    }

    // Encoder
    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(startPosition.x);
        buf.writeDouble(startPosition.y);
        buf.writeDouble(startPosition.z);
        buf.writeDouble(endPosition.x);
        buf.writeDouble(endPosition.y);
        buf.writeDouble(endPosition.z);
    }

    // Handler
    public static void handle(HealthDrain packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                final Random random = new Random();
                // Apply elevation here if not already adjusted when creating the packet
                Vec3 direction = packet.endPosition.add(0, 1, 0).subtract(packet.startPosition.add(0, 1, 0)).normalize();
                final double distance = packet.startPosition.distanceTo(packet.endPosition);
                final int steps = Math.max(5, (int) (distance * 3));

                for (int i = 0; i <= steps; i++) {
                    double offsetX = (random.nextDouble() - 0.5) * 0.5;
                    double offsetY = (random.nextDouble() - 0.5) * 0.5;
                    double offsetZ = (random.nextDouble() - 0.5) * 0.5;
                    Vec3 randomStart = packet.startPosition.add(offsetX, offsetY + 0.5, offsetZ); // Elevate start position

                    double progress = (double) i / steps;
                    Vec3 currentPos = randomStart.add(direction.scale(distance * progress));
                    mc.level.addParticle(ParticleTypes.WITCH, currentPos.x, currentPos.y, currentPos.z, 0, 0, 0);
                }
            }
        });
        context.setPacketHandled(true);
    }
}