package net.Ryaas.firstmod.entity.client;

import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.entity.custom.Moon;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MoonModel extends GeoModel<Moon> {
    @Override
    public ResourceLocation getModelResource(Moon moon) {
        return new ResourceLocation(FirstMod.MOD_ID, "geo/moon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Moon moon) {
        return new ResourceLocation(FirstMod.MOD_ID, "textures/entities/moon.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Moon moon) {
        return new ResourceLocation(FirstMod.MOD_ID, "animations/moon.animation.json");
    }

}
