package net.Ryaas.firstmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.Ryaas.firstmod.FirstMod;
import net.Ryaas.firstmod.entity.custom.MageHand;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.renderer.GeoEntityRenderer;





public class MageHandRenderer extends GeoEntityRenderer<MageHand> {
    public MageHandRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MageHandModel());
        this.shadowRadius = 0.7F;

    }

    @Override
    public ResourceLocation getTextureLocation(MageHand animatable){
        return new ResourceLocation(FirstMod.MOD_ID, "textures/entities/magehand.png");
    }




    @Override
    public void render(MageHand entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Vec3 motion = entity.getDeltaMovement();
        float yaw = (float) Math.toDegrees(Math.atan2(-motion.z, motion.x)) + 90.0f;
        float pitch = (float) Math.toDegrees(Math.atan2(motion.y, Math.sqrt(motion.x * motion.x + motion.z * motion.z)));

        poseStack.pushPose();

        // Apply custom rotation
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));

        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }



    // The method to calculate rotation angles
//    private float[] calculateRotationFromMotion(Vec3 motion) {
//        float xRot = -((float)(Math.atan2(motion.y, motion.x) * 57.2957763671875) - 90.0F);
//        float yRot = -((float)(Math.atan2(motion.z, motion.x) * 57.2957763671875) + 90.0F);
//
//        // Ensure the angles are within the valid range (0 to 360 degrees)
//        xRot = Math.floorMod((int)xRot, 360);
//        yRot = Math.floorMod((int)yRot, 360);
//
//        return new float[]{xRot, yRot};
//    }

    }
