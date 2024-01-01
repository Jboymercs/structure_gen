package com.example.structure.entity.render;

import com.example.structure.entity.EntitySnatcher;
import com.example.structure.entity.knighthouse.EntityEnderShield;
import com.example.structure.entity.model.ModelSnatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import scala.reflect.internal.Mode;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderSnatcher extends GeoEntityRenderer<EntitySnatcher> {
    public RenderSnatcher(RenderManager renderManager) {
        super(renderManager, new ModelSnatcher());
    }

    @Override
    public void doRender(EntitySnatcher entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
    }
}
