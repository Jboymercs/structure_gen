package com.example.structure.entity.render;

import com.example.structure.Main;
import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.entity.EntityEndBug;
import com.example.structure.entity.model.ModelEndBug;
import com.example.structure.entity.render.geo.GeoGlowingLayer;
import com.example.structure.entity.util.LayerGenericGlow;
import com.example.structure.util.ModReference;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.function.Function;

public class RenderEndBug extends GeoEntityRenderer<EntityEndBug> {

    protected ResourceLocation TEXTURE_LIGHT = new ResourceLocation(ModReference.MOD_ID, "textures/entity/bug_light.png");
    protected ResourceLocation MODEL_LOC = new ResourceLocation(ModReference.MOD_ID, "geo/entity/endbug/geo.bug.json");

    public RenderEndBug(RenderManager renderManager) {
        super(renderManager, new ModelEndBug());

    }


    @Override
    public void doRender(EntityEndBug entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();


    }



}
