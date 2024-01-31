package net.Ryaas.firstmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.entity.custom.Moon;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MoonRenderer extends GeoEntityRenderer<Moon>{


    public MoonRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MoonModel());


    }

    @Override
    public ResourceLocation getTextureLocation(Moon animatable){
        return new ResourceLocation(FirstMod.MOD_ID, "textures/entities/moon.png");
    }

    @Override
    public void render(Moon entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {



        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);

    }

}
