package net.Ryaas.firstmod.Networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class Indicator {
    private final double x, y, z;
    private final int particleType; // 0 for soul fire, 1 for smoke
    private int abilitySet = 0; // Add this line


    public Indicator(double x, double y, double z, int particleType) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.particleType = particleType;
        this.abilitySet = abilitySet;
    }

    public static void encode(Indicator msg, FriendlyByteBuf buffer) {
        buffer.writeDouble(msg.x);
        buffer.writeDouble(msg.y);
        buffer.writeDouble(msg.z);
        buffer.writeInt(msg.particleType);
        buffer.writeInt(msg.abilitySet);
    }

    public static Indicator decode(FriendlyByteBuf buffer) {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        int particleType = buffer.readInt();
        return new Indicator(x, y, z, particleType);
    }

    public static void handle(Indicator msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                ParticleOptions particle;
                switch (msg.abilitySet) {
                    case 1:
                        // Ability set 1 logic
                        particle = determineParticleForSet1(msg.particleType);
                        break;
                    case 2:
                        // Handle other sets as needed
                        particle = determineParticleForSet2(msg.particleType);
                        break;
                    default:
                        particle = ParticleTypes.AMBIENT_ENTITY_EFFECT; // Default case
                        break;
                }
                if (particle != null) {
                    mc.level.addParticle(particle, msg.x, msg.y, msg.z, 0.0D, 0.0D, 0.0D);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
    private static ParticleOptions determineParticleForSet1(int particleType) {
        return switch (particleType) {
            case 0 -> ParticleTypes.SOUL_FIRE_FLAME;
            case 1 -> ParticleTypes.SMOKE;
            default -> null;
        };
    }

    private static ParticleOptions determineParticleForSet2(int particleType) {
        return switch (particleType) {
            // Define particles for ability set 2
            default -> null;
        };
    }
}