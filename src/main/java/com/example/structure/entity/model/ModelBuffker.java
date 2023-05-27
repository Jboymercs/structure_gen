package com.example.structure.entity.model;

import com.example.structure.entity.EntityBuffker;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelBuffker extends AnimatedGeoModel<EntityBuffker> {
    @Override
    public ResourceLocation getModelLocation(EntityBuffker entityBuffker) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/buffker/buffker.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBuffker entityBuffker) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/buffker.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityBuffker entityBuffker) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.buffker.json");
    }

    @Override
    public void setLivingAnimations(EntityBuffker entity, Integer uniqueID) {
        super.setLivingAnimations(entity, uniqueID);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
