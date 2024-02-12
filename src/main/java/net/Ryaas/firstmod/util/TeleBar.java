//package net.Ryaas.firstmod.util;
//
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.*;
//import net.minecraft.client.Minecraft;
//import net.minecraft.resources.ResourceLocation;
//import org.joml.Matrix4f;
//
//public class TeleBar{
//
//
//public void drawChargeBar(PoseStack poseStack, int currentCharge, int maxCharge) {
//    Minecraft mc = Minecraft.getInstance();
//    int screenWidth = mc.getWindow().getGuiScaledWidth();
//    int screenHeight = mc.getWindow().getGuiScaledHeight();
//
//    // Texture for the charge bar
//    ResourceLocation CHARGE_TEXTURE = new ResourceLocation("yourmodid", "textures/gui/charge_bar.png");
//
//    // Position and size of the charge bar
//    int x = screenWidth / 2 - 91; // Example position
//    int y = screenHeight - 49; // Example position
//    int width = 182; // Full width of the bar
//    int height = 5; // Height of the bar
//
//    // Calculate the width of the filled portion based on current charge
//    int filledWidth = (int) (((float) currentCharge / maxCharge) * width);
//
//    RenderSystem.setShaderTexture(0, CHARGE_TEXTURE);
//    RenderSystem.enableBlend();
//
//    // Drawing the background (empty part of the charge bar)
//    drawTexturedQuad(poseStack.last().pose(), x, y, 0, 0, width, height, width, height);
//
//    // Drawing the filled part of the charge bar
//    drawTexturedQuad(poseStack.last().pose(), x, y, 0, 0, filledWidth, height, width, height);
//
//    RenderSystem.disableBlend();
//}
//
//    private void drawTexturedQuad(Matrix4f matrix, int x, int y, int minU, int minV, int width, int height, int textureWidth, int textureHeight) {
//        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
//        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        bufferBuilder.vertex(matrix, x, y + height, 0).uv((float)minU / textureWidth, ((float)minV + height) / textureHeight).endVertex();
//        bufferBuilder.vertex(matrix, x + width, y + height, 0).uv(((float)minU + width) / textureWidth, ((float)minV + height) / textureHeight).endVertex();
//        bufferBuilder.vertex(matrix, x + width, y, 0).uv(((float)minU + width) / textureWidth, (float)minV / textureHeight).endVertex();
//        bufferBuilder.vertex(matrix, x, y, 0).uv((float)minU / textureWidth, (float)minV / textureHeight).endVertex();
//
//        // You actually draw the quad here
//        bufferBuilder.end();
//        BufferUploader.end(bufferBuilder); }
//}