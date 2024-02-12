package net.Ryaas.firstmod.Networking.packet;


import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class SpeedOne {
    private final double x, y, z;
    private final int count;
    ParticleType<?> myParticleType = ParticleTypes.SOUL_FIRE_FLAME; // Example particle type
    ResourceLocation particleTypeLocation = ForgeRegistries.PARTICLE_TYPES.getKey(myParticleType);


    public SpeedOne(double x, double y, double z, int count, ParticleType<?> particleType) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.count = count;
        this.myParticleType = particleType;
    }

    public static void encode(SpeedOne msg, FriendlyByteBuf buffer) {
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
        buffer.writeInt(msg.count);
        buffer.writeResourceLocation(ForgeRegistries.PARTICLE_TYPES.getKey(msg.myParticleType)); // Correctly encode the ResourceLocation
    }

    public static SpeedOne decode(FriendlyByteBuf buffer) {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        int count = buffer.readInt();
        ResourceLocation particleTypeId = buffer.readResourceLocation(); // Decode into ResourceLocation
        ParticleType<?> particleType = ForgeRegistries.PARTICLE_TYPES.getValue(particleTypeId); // Convert back to ParticleType
        return new SpeedOne(x, y, z, count, particleType);
    }


    public static void handle(SpeedOne msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;
            if (world == null) return;

            ParticleType<?> particleType = ForgeRegistries.PARTICLE_TYPES.getValue(msg.particleTypeLocation);
            if (particleType != null) {
                for (int i = 0; i < msg.count; i++) {
                    world.addParticle(ParticleTypes.SOUL, msg.x, msg.y, msg.z, 0, 0, 0);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
