package net.Ryaas.firstmod.item.client;

import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.item.custom.WizardHatItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WizardHatModel extends GeoModel<WizardHatItem> {
    @Override
    public ResourceLocation getModelResource(WizardHatItem wizardHatItem) {
        return new ResourceLocation(FirstMod.MOD_ID, "geo/wizzyhat.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WizardHatItem wizardHatItem) {
        return new ResourceLocation(FirstMod.MOD_ID, "textures/models/armor/wizardhat.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WizardHatItem wizardHatItem) {
        return new ResourceLocation(FirstMod.MOD_ID, "animations/wizard_hat.animation.json");
    }
}
