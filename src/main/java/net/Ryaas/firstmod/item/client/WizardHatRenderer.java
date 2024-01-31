package net.Ryaas.firstmod.item.client;

import net.Ryaas.firstmod.item.custom.WizardHatItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class WizardHatRenderer extends GeoArmorRenderer<WizardHatItem> {
    public WizardHatRenderer() {
        super(new WizardHatModel());
    }
}
