package com.example.structure.entity.render;

import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.endking.EntityEndKing;
import com.example.structure.entity.model.ModelEndKing;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderEntityKing extends GeoEntityRenderer<EntityEndKing> {
    public RenderEntityKing(RenderManager renderManager) {
        super(renderManager, new ModelEndKing());
    }
    @Override
    public void doRender(EntityEndKing entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
