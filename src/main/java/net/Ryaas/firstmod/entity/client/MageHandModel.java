package net.Ryaas.firstmod.entity.client;

import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.entity.custom.MageHand;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MageHandModel extends GeoModel<MageHand> {
    @Override
    public ResourceLocation getModelResource(MageHand mageHand) {
        return new ResourceLocation(FirstMod.MOD_ID, "geo/magehandaurageo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MageHand mageHand) {
        return new ResourceLocation(FirstMod.MOD_ID, "textures/entities/magehand.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MageHand mageHand) {
        return new ResourceLocation(FirstMod.MOD_ID, "animations/magehandproj.animation.json");
    }

}
