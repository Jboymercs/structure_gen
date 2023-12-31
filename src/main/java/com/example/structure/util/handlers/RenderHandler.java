package com.example.structure.util.handlers;

import com.example.structure.entity.*;
import com.example.structure.entity.endking.*;
import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.knighthouse.EntityEnderShield;
import com.example.structure.entity.knighthouse.EntityHealAura;
import com.example.structure.entity.knighthouse.EntityKnightLord;
import com.example.structure.entity.render.*;
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
        //Shulker COnstructor
        RenderingRegistry.registerEntityRenderingHandler(EntityBuffker.class, RenderBuffker::new);
        //Ground Crystal - Utility
        RenderingRegistry.registerEntityRenderingHandler(EntityGroundCrystal.class, RenderGroundCrystal::new);
        //Crystal Ball - Utility
        registerProjectileRenderer(EntityCrystalSpikeSmall.class);
        //Idle Entity - Utility
        registerProjectileRenderer(EntityExplosion.class);
        //Quake
        registerProjectileRenderer(ProjectileQuake.class);
        //Ender Knight
        registerModEntityRenderer(EntityEnderKnight.class, RenderEnderKnight::new);
        //End King
        RenderingRegistry.registerEntityRenderingHandler(EntityEndKing.class, RenderEntityKing::new);
        //Red Crystal
        RenderingRegistry.registerEntityRenderingHandler(EntityRedCrystal.class, RenderRedCrystal::new);
        //Red Sword
        registerProjectileRenderer(ProjectileSpinSword.class);
        //Fire ball
        RenderingRegistry.registerEntityRenderingHandler(EntityFireBall.class, RenderFireball::new);
        //Nuclear Explosion
        RenderingRegistry.registerEntityRenderingHandler(EntityNuclearExplosion.class, RenderNuclearExplosion::new);
        //End Bug
        RenderingRegistry.registerEntityRenderingHandler(EntityEndBug.class, RenderEndBug::new);
        //Ender Mage
        RenderingRegistry.registerEntityRenderingHandler(EntityEnderMage.class, RenderEnderMage::new);
        //Heal Aura
        RenderingRegistry.registerEntityRenderingHandler(EntityHealAura.class, RenderHealingAura::new);
        //Ender Shield
        RenderingRegistry.registerEntityRenderingHandler(EntityEnderShield.class, RenderEnderShield::new);
        //Ender Lord
        RenderingRegistry.registerEntityRenderingHandler(EntityKnightLord.class, RenderKnightLord::new);
    }
}
