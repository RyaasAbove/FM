package net.Ryaas.firstmod.Networking.packet;

import net.Ryaas.firstmod.entity.client.ModEntities;
import net.Ryaas.firstmod.entity.custom.Spawneffects;
import net.Ryaas.firstmod.util.CooldownManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UltimateAbility {
    private final Vec3 pos; // Store the precise position
    private final Vec3 lookVec; // The look vector for direction

    public UltimateAbility(Vec3 pos, Vec3 lookVec) {
        this.pos = pos;
        this.lookVec = lookVec;
    }

    public static void encode(UltimateAbility message, FriendlyByteBuf buffer) {
        buffer.writeDouble(message.pos.x);
        buffer.writeDouble(message.pos.y);
        buffer.writeDouble(message.pos.z);
        buffer.writeDouble(message.lookVec.x);
        buffer.writeDouble(message.lookVec.y);
        buffer.writeDouble(message.lookVec.z);
    }

    public static UltimateAbility decode(FriendlyByteBuf buffer) {
        Vec3 pos = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        Vec3 lookVec = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        return new UltimateAbility(pos, lookVec);
    }

    public static void handle(UltimateAbility message, Supplier<NetworkEvent.Context> ctx) {
        System.out.println("Ult packet called");
        ServerPlayer player = ctx.get().getSender();
        if (player == null) return; // Check for null player first

        ctx.get().enqueueWork(() -> {
            CompoundTag playerData = player.getPersistentData();
            // Check if the player has the "AbilitySet" key and it's set to 1
            if (playerData.contains("AbilitySet") && playerData.getInt("AbilitySet") == 1) {


                if (CooldownManager.hasCooldown(player)) {
                    if (player.level().isClientSide) {
                        player.level().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 1.0F);

                    }

                } else {
                    // Activate the Black Hole

                    ServerLevel serverWorld = player.serverLevel(); // Correct way to get the server world

                    Spawneffects spawneffects = ModEntities.SPAWN_EFFECTS.get().create(serverWorld);
                    if (spawneffects != null) {
                        spawneffects.setPos(message.pos.x, message.pos.y, message.pos.z);
                        spawneffects.setup(message.lookVec, player.getUUID(), player); // Correct order of parameters
                        serverWorld.addFreshEntity(spawneffects);
                        CooldownManager.setCooldown(player, 20 * 60); // 1 minute cooldown

                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}