//package net.Ryaas.firstmod.util;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.Ryaas.firstmod.FirstMod;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.client.renderer.GameRenderer;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Player;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//import java.util.UUID;
//
//@Mod.EventBusSubscriber(modid = FirstMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
//
//public class GuiHandler {
//    @SubscribeEvent
//    public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
//        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
//            Minecraft minecraft = Minecraft.getInstance();
//            Player player = minecraft.player;
//
//            if (player == null) return; // Ensure we have a player
//
//            boolean isRecharged = checkUltimateRecharged(player.getUUID()); // Your method to check status
//            ResourceLocation texture = isRecharged ? ULT_RECHARGED_TEXTURE : ULT_RECHARGING_TEXTURE;
//
//            renderUltimateIndicator(event.getMatrixStack(), texture);
//        }
//    }
//
//    private static void renderUltimateIndicator(PoseStack poseStack, ResourceLocation texture) {
//        Minecraft minecraft = Minecraft.getInstance();
//        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
//        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
//
//        int x = (screenWidth / 2) - 8;
//        int y = screenHeight - 20;
//
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderTexture(0, texture);
//        GuiGraphics.blit(poseStack, x, y, 0, 0, 16, 16, 16, 16);
//    }
//
//    private static boolean checkUltimateRecharged(UUID playerUuid) {
//        // Your logic here
//        return false; // Placeholder
//    }
//}
//
