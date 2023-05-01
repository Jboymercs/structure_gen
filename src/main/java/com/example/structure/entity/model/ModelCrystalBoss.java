package com.example.structure.entity.model;

import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelCrystalBoss extends AnimatedGeoModel<EntityCrystalKnight> {
    @Override
    public ResourceLocation getModelLocation(EntityCrystalKnight entityCrystalKnight) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/entity/crystalknight/geo.lamentor.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCrystalKnight entityCrystalKnight) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/entitylamentor.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityCrystalKnight entityCrystalKnight) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.lamentor.json");
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
