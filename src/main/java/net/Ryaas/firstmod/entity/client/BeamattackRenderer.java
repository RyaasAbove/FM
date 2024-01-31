package net.Ryaas.firstmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.entity.custom.Beamattack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BeamattackRenderer extends GeoEntityRenderer<Beamattack>{


        public BeamattackRenderer(EntityRendererProvider.Context renderManager) {
            super(renderManager, new BeamattackModel());


        }

        @Override
        public ResourceLocation getTextureLocation(Beamattack animatable){
            return new ResourceLocation(FirstMod.MOD_ID, "textures/entities/beam.png");
        }

    @Override
    public void render(Beamattack entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {



        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);

    }

    }
