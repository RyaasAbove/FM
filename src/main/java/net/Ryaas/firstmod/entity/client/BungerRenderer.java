package net.Ryaas.firstmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.entity.custom.Bungerentity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BungerRenderer extends GeoEntityRenderer<Bungerentity> {
    public BungerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BungerModel());
    }

    @Override
    public ResourceLocation getTextureLocation(Bungerentity animatable){
        return new ResourceLocation(FirstMod.MOD_ID, "textures/entities/bunger.png");
    }

    @Override
    public void  render(Bungerentity entity, float partialTick, float entityYaw, PoseStack poseStack,
                        MultiBufferSource bufferSource, int packedLight){
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
 