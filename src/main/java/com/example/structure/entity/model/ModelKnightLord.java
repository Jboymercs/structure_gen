package com.example.structure.entity.model;

import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.knighthouse.EntityKnightLord;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ModelKnightLord extends AnimatedGeoModel<EntityKnightLord> {
    @Override
    public ResourceLocation getModelLocation(EntityKnightLord entityKnightLord) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/endlord/geo.endlord.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityKnightLord entityKnightLord) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/endlord.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityKnightLord entityKnightLord) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.endlord.json");
    }

    @Override
    public void setLivingAnimations(EntityKnightLord entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("HeadJoint");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));


    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
