package com.example.structure.entity.render;

import com.example.structure.entity.EntityModBase;
import com.example.structure.renderer.ITarget;
import com.example.structure.util.ModReference;
import com.example.structure.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.Optional;

public class RenderModEntity <T extends EntityLiving> extends RenderLiving<T> {

    public String[] TEXTURES;
    private ResourceLocation DEATH_TEXTURES;

    public <U extends ModelBase> RenderModEntity(RenderManager rendermanagerIn, String textures, U modelClass) {
        this(rendermanagerIn, modelClass, new String[]{textures});
    }

    public <U extends ModelBase> RenderModEntity(RenderManager rendermanagerIn, U modelClass, String... textures) {
        super(rendermanagerIn, modelClass, 0.5f);
        if (textures.length == 0) {
            throw new IllegalArgumentException("Must provide at least one texture to render an entity.");
        }
        this.TEXTURES = textures;
        this.DEATH_TEXTURES = new ResourceLocation(String.format("%s:textures/entity/disintegration_%d_%d.png", ModReference.MOD_ID, modelClass.textureWidth, modelClass.textureHeight));
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        String texture = TEXTURES[0];


        return new ResourceLocation(ModReference.MOD_ID + ":textures/entity/" + texture);
    }

    @Override
    protected void applyRotations(T entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
        if (entityLiving instanceof EntityModBase && Minecraft.getMinecraft().getFramebuffer().isStencilEnabled() && GL11.glGetInteger(GL11.GL_STENCIL_BITS) > 0) {
            GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
        } else {
            super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
        }
    }

    @Override
    protected void renderModel(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        if (entity instanceof EntityModBase) {
            if (entity.getHealth() <= 0 && Minecraft.getMinecraft().getFramebuffer().isStencilEnabled() && GL11.glGetInteger(GL11.GL_STENCIL_BITS) > 0) {
                float f = entity.deathTime / (15f); // The alpha value required to render a pixel

                // Use the stencil buffer to first record where to draw with the disintegration texture,
                // and then the entity render only renders where the stencil buffer has drawn
                int stencilVal = 42;
                int maskPass = 0xff;
                GL11.glEnable(GL11.GL_STENCIL_TEST);
                GlStateManager.clear(GL11.GL_STENCIL_BUFFER_BIT);
                GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
                GL11.glStencilFunc(GL11.GL_ALWAYS, stencilVal, maskPass); // Everything passes the stencil test
                GL11.glStencilMask(0xFF); // Enable writing to stencil buffer

                // Disable color to keep from the disintegration render from affecting the actual visual output
                GlStateManager.colorMask(false, false, false, false);
                GlStateManager.depthMask(false);

                // Use the alpha function to make more and more pixels get cut off as alpha threshold gets larger
                GlStateManager.enableAlpha();
                GlStateManager.alphaFunc(GL11.GL_GREATER, f);

                // Write the disintegrated mob to the stencil buffer
                this.bindTexture(DEATH_TEXTURES);
                this.mainModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

                GL11.glEnable(GL11.GL_COLOR_BUFFER_BIT);
                // Disable stencil buffer and enable rendering again
                GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.depthMask(true);

                // Apply the stencil filter I think
                GL11.glStencilFunc(GL11.GL_NOTEQUAL, 0, maskPass);

                // Return alpha function to what it was before (probably what it was before)
                GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);

                super.renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

                GL11.glDisable(GL11.GL_STENCIL_TEST);
            } else {
                super.renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            }
        } else {
            super.renderModel(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        }
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!entity.isInvisible()) {
            // The blending here allows for rendering of translucent textures
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();

        } else {
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    @Override
    public boolean shouldRender(@Nonnull T livingEntity, @Nonnull ICamera camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntity, camera, camX, camY, camZ))
        {
            return true;
        }

        if (livingEntity instanceof ITarget) {
            Optional<Vec3d> optional = ((ITarget) livingEntity).getTarget();
            if(optional.isPresent()) {
                Vec3d end = optional.get();
                Vec3d start = livingEntity.getPositionEyes(1);
                return camera.isBoundingBoxInFrustum(ModUtils.makeBox(start, end));
            }
        }

        return false;
    }
}
