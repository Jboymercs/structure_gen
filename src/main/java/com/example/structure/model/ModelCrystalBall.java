package com.example.structure.model;

import com.example.structure.entity.EntityCrystalSpikeSmall;
import com.example.structure.items.CrystalBallItem;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelCrystalBall extends AnimatedGeoModel<CrystalBallItem> {

    public ModelCrystalBall() {

    }
    @Override
    public ResourceLocation getModelLocation(CrystalBallItem entityCrystalSpikeSmall) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.crystal_ball.json");
    }

    @Override
    public ResourceLocation getTextureLocation(CrystalBallItem entityCrystalSpikeSmall) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/entity/crystalball.png");
    }

@Override
    public ResourceLocation getAnimationFileLocation(CrystalBallItem entityCrystalSpikeSmall) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.crystal.json");
    }
}
