package com.example.structure.entity.model;

import com.example.structure.entity.EntityBuffker;
import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.util.ModReference;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelEnderKnight extends AnimatedGeoModel<EntityEnderKnight> {
    @Override
    public ResourceLocation getModelLocation(EntityEnderKnight entityEnderKnight) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/endknight/geo.endknight.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityEnderKnight entityEnderKnight) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/endknight.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityEnderKnight entityEnderKnight) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.endknight.json");
    }

    @Override
    public void setLivingAnimations(EntityEnderKnight entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("Head");
        IBone rightArm = this.getAnimationProcessor().getBone("RArm");
        IBone leftArm = this.getAnimationProcessor().getBone("LArm");
        IBone rightLeg = this.getAnimationProcessor().getBone("RLegJ");
        IBone leftLeg = this.getAnimationProcessor().getBone("LLegJ");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));

    }
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {

    }
    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
