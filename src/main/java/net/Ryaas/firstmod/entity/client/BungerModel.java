package net.Ryaas.firstmod.entity.client;

import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.entity.custom.Bungerentity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BungerModel extends GeoModel<Bungerentity> {
    @Override
    public ResourceLocation getModelResource(Bungerentity bungerentity) {
        return new ResourceLocation(FirstMod.MOD_ID, "geo/bungerrrr.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Bungerentity bungerentity) {
        return new ResourceLocation(FirstMod.MOD_ID, "textures/entities/bungerrrr.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Bungerentity bungerentity) {
        return new ResourceLocation(FirstMod.MOD_ID, "animations/bungeranims.animation.json");
    }
//    @Override
//    public void setCustomAnimations( Bungerentity animatable, long instanceID, AnimationState<Bungerentity> animationState){
//            CoreGeoBone body = getAnimationProcessor().getBone("body");
//
//            if(body != null) {
//                EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
//
//                body.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
//                body.setRotY(entityData.headPitch() * Mth.DEG_TO_RAD);
//
//            }
//
//        }

}

