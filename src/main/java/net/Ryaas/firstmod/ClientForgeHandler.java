package net.Ryaas.firstmod;

import net.Ryaas.firstmod.Networking.ModNetworking;
import net.Ryaas.firstmod.Networking.packet.*;
import net.Ryaas.firstmod.assisting.KeyBinding;
import net.Ryaas.firstmod.util.getTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = FirstMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeHandler {

    private static UUID blackHoleId; // The ID of the black hole, set when spawning it.
    static boolean blackHoleIsActive = false;
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event){


        if(KeyBinding.INSTANCE.MARK_KEY.consumeClick()){

            ModNetworking.sendToServer(new MarkPacketc2s());

        }
        if(KeyBinding.INSTANCE.PRIMARY.consumeClick()){

            ModNetworking.sendToServer(new MainAbility());

        }
        if(KeyBinding.INSTANCE.SECONDARY.isDown()){

            ModNetworking.sendToServer(new SecondaryAbility());

        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || event.phase != TickEvent.Phase.START) return;

        if (KeyBinding.INSTANCE.ULTIMATE.consumeClick()) {
            // Activate the black hole and send the initial packet to spawn it.
            Vec3 lookVec = mc.player.getLookAngle();
            double distance = 3.0; // Distance in front of the player to spawn the black hole.
            Vec3 pos = mc.player.position().add(lookVec.scale(distance));

            // Here, you would need a mechanism to obtain and set the blackHoleId
            // possibly returned or managed by the server upon creating the black hole.
            // For demonstration, we're focusing on updating an existing black hole's look vector.
            blackHoleIsActive = true;

            // Send the ultimate ability packet, assuming it spawns the black hole.
            ModNetworking.getChannel().sendToServer(new UltimateAbility(pos, lookVec));
        }

        // Send the player's look vector to update the black hole's direction.
        if (mc.player == null) return;

// Check if the player is looking at a block

            BlockHitResult blockHitResult = (BlockHitResult) mc.hitResult;
            BlockPos blockPos = getTarget.getTargetedBlock(mc.player, 100);

            // Now, you have the block position the player is looking at.
            // Send this position to the server to update the black hole's target.
            if(blockPos != null){
                ModNetworking.sendToServer(new BlackHoleTargetUpdatePacket(blockPos));

            }
        }

        }



