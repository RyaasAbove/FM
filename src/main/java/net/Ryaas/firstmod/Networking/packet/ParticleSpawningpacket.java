package net.Ryaas.firstmod.Networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class ParticleSpawningpacket {
    private final Vec3 position;
    private final float red, green, blue, scale;

    public ParticleSpawningpacket(Vec3 position, float red, float green, float blue, float scale) {
        this.position = position;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.scale = scale;
    }

    public static ParticleSpawningpacket decode(FriendlyByteBuf buf) {
        Vec3 pos = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        float red = buf.readFloat();
        float green = buf.readFloat();
        float blue = buf.readFloat();
        float scale = buf.readFloat();
        return new ParticleSpawningpacket(pos, red, green, blue, scale);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeFloat(red);
        buf.writeFloat(green);
        buf.writeFloat(blue);
        buf.writeFloat(scale);
    }

    public static void handle(ParticleSpawningpacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                DustParticleOptions particleOptions = new DustParticleOptions(new Vector3f(msg.red, msg.green, msg.blue), msg.scale);
                mc.level.addParticle(particleOptions, msg.position.x, msg.position.y, msg.position.z, 0.0D, 0.0D, 0.0D);
            }
        });
        ctx.setPacketHandled(true);
    }
}