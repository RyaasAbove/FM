package net.Ryaas.firstmod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.util.CooldownManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.UUID;

public class UltimateHud {
    private static final ResourceLocation ULT_RECHARGED = new ResourceLocation(FirstMod.MOD_ID,
            "textures/gui/ult_recharged.png");
    private static final ResourceLocation ULT_RECHARGING = new ResourceLocation(FirstMod.MOD_ID,
            "textures/gui/ult_recharging.png");


    public static final IGuiOverlay GUI_ULT = ((gui, guiGraphics, partialTick, width, height) ->{
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.player == null){
            return;
        }
        // Example check, replace with your actual logic
        boolean isRecharged = checkUltimateRecharged(minecraft.player.getUUID());

        int x = width / 2 + 88;
        int y = height - 19;

        ResourceLocation texture = isRecharged ? ULT_RECHARGING : ULT_RECHARGED;


        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); // Full color and opacity
        RenderSystem.setShaderTexture(0, isRecharged ? ULT_RECHARGED : ULT_RECHARGING);




        guiGraphics.blit(texture, x, y, 0, 0, 16, 16, 16, 16);



    });

    private static boolean checkUltimateRecharged(UUID playerUuid) {
        // Implement your logic here to check if the ultimate ability is recharged
        // This might involve checking a cooldown manager or player data
        return CooldownManager.hasCooldown(Minecraft.getInstance().player); // You need to implement CooldownManager or use existing logic
    }


}
