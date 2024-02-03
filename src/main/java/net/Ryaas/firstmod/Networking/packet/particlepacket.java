package net.Ryaas.firstmod.Networking.packet;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class particlepacket {
    private final Vec3 start;
    private final Vec3 direction;
    private static Level world;
    private final double distance;

    public particlepacket(Vec3 start, Vec3 direction, double distance) {
        this.start = start;
        this.direction = direction;
        this.distance = distance;
    }

    public static void encode(particlepacket msg, FriendlyByteBuf buffer) {
        buffer.writeDouble(msg.start.x);
        buffer.writeDouble(msg.start.y);
        buffer.writeDouble(msg.start.z);
        buffer.writeDouble(msg.direction.x);
        buffer.writeDouble(msg.direction.y);
        buffer.writeDouble(msg.direction.z);
        buffer.writeDouble(msg.distance);
    }

    public static particlepacket decode(FriendlyByteBuf buffer) {
        Vec3 start = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        Vec3 direction = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        double distance = buffer.readDouble();
        return new particlepacket(start, direction, distance);
    }

    public static void handle(particlepacket message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            // Assuming Minecraft.getInstance().level is accessible and not null at this point.
            Vec3 pos = message.start;
            for (double d = 0; d <= message.distance; d += 0.5) {
                Vec3 particlePos = pos.add(message.direction.normalize().scale(d));
                // Replace with your client-side way to spawn particles
                // This might need adjustment based on how you access the Minecraft instance or the world
                spawnParticle(particlePos);
            }
        });
        ctx.setPacketHandled(true);
    }

    // Example spawnParticle method, adjust for actual client-side particle spawning
    private static void spawnParticle(Vec3 particlePos) {
        if(world.isClientSide){
            world.addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                    particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);

        }
        // Directly using Minecraft.getInstance() might not work in all contexts, adjust as necessary
         }
}
