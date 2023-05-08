package com.example.structure.util.handlers;

import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.entity.EntityCrystalSpikeSmall;
import com.example.structure.entity.EntityGroundCrystal;
import com.example.structure.entity.render.RenderCrystalBoss;
import com.example.structure.entity.render.RenderGroundCrystal;
import com.example.structure.entity.render.RenderModEntity;
import com.example.structure.entity.render.RenderProjectile;
import com.example.structure.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.function.Function;

public class RenderHandler {


    private static <T extends Entity, U extends ModelBase, V extends RenderModEntity> void registerModEntityRenderer(Class<T> entityClass, U model, String... textures) {
        registerModEntityRenderer(entityClass, (manager) -> new RenderModEntity(manager, model, textures));
    }

    private static <T extends Entity, U extends ModelBase, V extends RenderModEntity> void registerModEntityRenderer(Class<T> entityClass, Function<RenderManager, Render<? super T>> renderClass) {
        RenderingRegistry.registerEntityRenderingHandler(entityClass, new IRenderFactory<T>() {
            @Override
            public Render<? super T> createRenderFor(RenderManager manager) {
                return renderClass.apply(manager);
            }
        });
    }

    private static <T extends Entity> void registerProjectileRenderer(Class<T> projectileClass) {
        registerProjectileRenderer(projectileClass, null);
    }

    /**
     * Makes a projectile render with the given item
     *
     * @param projectileClass
     */
    private static <T extends Entity> void registerProjectileRenderer(Class<T> projectileClass, Item item) {
        RenderingRegistry.registerEntityRenderingHandler(projectileClass, new IRenderFactory<T>() {
            @Override
            public Render<? super T> createRenderFor(RenderManager manager) {
                return new RenderProjectile<T>(manager, Minecraft.getMinecraft().getRenderItem(), item);
            }
        });
    }

    public static void registerGeoEntityRenderers() {
        //Crystal Knight Boss
        RenderingRegistry.registerEntityRenderingHandler(EntityCrystalKnight.class, RenderCrystalBoss::new);
        //Ground Crystal - Utility
        RenderingRegistry.registerEntityRenderingHandler(EntityGroundCrystal.class, RenderGroundCrystal::new);
        //Crystal Ball - Utility
        registerProjectileRenderer(EntityCrystalSpikeSmall.class);
    }
}
