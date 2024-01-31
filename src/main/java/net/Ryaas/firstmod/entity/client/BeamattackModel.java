package net.Ryaas.firstmod.entity.client;

import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.entity.custom.Beamattack;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BeamattackModel extends GeoModel<Beamattack> {
    @Override
    public ResourceLocation getModelResource(Beamattack beamattack) {
        return new ResourceLocation(FirstMod.MOD_ID, "geo/beamattack.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Beamattack beamattack) {
        return new ResourceLocation(FirstMod.MOD_ID, "textures/entities/beam.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Beamattack beamattack) {
        return new ResourceLocation(FirstMod.MOD_ID, "animations/beam.animation.json");
    }

}
