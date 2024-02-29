package net.Ryaas.firstmod.Networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class Swirl {
    private final Vec3 position;
    static float red = 0.68f; // For light blue, the red component is relatively low
    static float green = 0.84f; // Higher green component
    static float blue = 0.90f; // Higher blue component
    static float scale = 1.0f; // Adjust this value for the particle's size

    // Create the DustParticleOptions
    static ParticleOptions particleOptions = new DustParticleOptions(new Vector3f(red, green, blue), scale);
    private final ParticleOptions particleType;

    public Swirl(Vec3 position, ParticleOptions particleType) {
        this.position = position;
        this.particleType = particleType;
    }

    public static Swirl decode(FriendlyByteBuf buf) {
        Vec3 position = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        ParticleOptions particleType = particleOptions; // Example, adjust based on your needs
        return new Swirl(position, particleType);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        // Encoding ParticleType requires more complex logic, often custom registry ID mapping
    }

    public static void handle(Swirl msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            // Client-side particle spawning logic
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && msg.particleType != null) {
                mc.level.addParticle(msg.particleType, msg.position.x, msg.position.y - 1, msg.position.z, 0, 0, 0);
            }
        });
        ctx.setPacketHandled(true);
    }
}
