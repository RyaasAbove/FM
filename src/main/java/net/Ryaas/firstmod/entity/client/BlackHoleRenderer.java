package net.Ryaas.firstmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.Ryaas.firstmod.FirstMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class BlackHoleRenderer extends EntityRenderer<Entity> {


    public BlackHoleRenderer(EntityRendererProvider.Context renderManager) {

            super(renderManager);


    }



    @Override
    public void render(Entity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        // Custom rendering code here
        poseStack.pushPose();

        // Example: Render a glowing orb or simple shape
        // This is just a placeholder to illustrate where you might begin custom rendering.
        // Actual rendering code would involve drawing vertices or using particle effects.

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return new ResourceLocation(FirstMod.MOD_ID, "textures/items/amulet.png");
    }


}