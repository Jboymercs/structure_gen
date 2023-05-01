package com.example.structure.entity.model;

import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelCrystalBoss extends AnimatedGeoModel<EntityCrystalKnight> {
    @Override
    public ResourceLocation getModelLocation(EntityCrystalKnight entityCrystalKnight) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/crystalknight/");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCrystalKnight entityCrystalKnight) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityCrystalKnight entityCrystalKnight) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/");
    }

    @Override
    public void setLivingAnimations(EntityCrystalKnight entity, Integer uniqueID) {
        super.setLivingAnimations(entity, uniqueID);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
