package com.example.structure.model;

import com.example.structure.items.tools.ToolBossSword;
import com.example.structure.util.ModReference;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelSword extends AnimatedGeoModel<ToolBossSword> {
    @Override
    public ResourceLocation getModelLocation(ToolBossSword toolBossSword) {
        return new ResourceLocation(ModReference.MOD_ID, "geo/item/geo.sword.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ToolBossSword toolBossSword) {
        return new ResourceLocation(ModReference.MOD_ID, "textures/item/sword.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ToolBossSword toolBossSword) {
        return new ResourceLocation(ModReference.MOD_ID, "animations/animation.sword.json");
    }
}
