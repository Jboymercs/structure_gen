package com.example.structure.entity.model;

import com.example.structure.entity.EntityBuffker;
import com.example.structure.entity.EntityEndBug;
import com.example.structure.entity.animation.IAnimatedEntity;
import com.example.structure.entity.animation.ModelAnimator;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelEndBug extends AnimatedGeoModel<EntityEndBug> {
    private ModelAnimator animator;

    @Override
    public ResourceLocation getModelLocation(EntityEndBug entityEndBug) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/endbug/geo.bug.json");

    }

    @Override
    public ResourceLocation getTextureLocation(EntityEndBug entityEndBug) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/bug.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityEndBug entityEndBug) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.bug.json");
    }

    @Override
    public void setLivingAnimations(EntityEndBug entity, Integer uniqueID, AnimationEvent customPredicate) {
        animator = ModelAnimator.create();
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("HeadMoveJ");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));



    }





    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
