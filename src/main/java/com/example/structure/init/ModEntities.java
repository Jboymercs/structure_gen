package com.example.structure.init;

import com.example.structure.Main;
import com.example.structure.config.ModConfig;
import com.example.structure.entity.*;
import com.example.structure.entity.endking.*;
import com.example.structure.entity.knighthouse.EntityEnderMage;
import com.example.structure.entity.knighthouse.EntityEnderShield;
import com.example.structure.entity.knighthouse.EntityHealAura;
import com.example.structure.entity.knighthouse.EntityKnightLord;
import com.example.structure.entity.tileentity.*;
import com.example.structure.util.ModReference;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

public class ModEntities {

    private static final Map<Class<? extends Entity>, String> ID_MAP = new HashMap<>();

    private static int ENTITY_START_ID = 123;
    private static int PROJECTILE_START_ID = 230;
    private static int PARTICLE_START_ID = 500;

    public static Vec3i end_mobs = new Vec3i(0x833d91, 0xd8d295, 0);


    public static void registerEntities() {
        //Crystal Boss
        registerEntityWithID("crystal_boss", EntityCrystalKnight.class, ENTITY_START_ID++, 50, end_mobs);
        //Shulker Constructor
        registerEntityWithID("buffker", EntityBuffker.class, ENTITY_START_ID++, 50, end_mobs);
        //Crystal Ball - Utility
        registerEntity("crystal_ball", EntityCrystalSpikeSmall.class, ENTITY_START_ID++, 50);
        //Ground Crystal - Utility
        registerEntity("crytsal_ground", EntityGroundCrystal.class, ENTITY_START_ID++, 50);
        //IdleEntity
        registerEntity("entity_idle", EntityExplosion.class, ENTITY_START_ID++, 50);
        //Tile Entity
        registerTileEntity(TileEntityUpdater.class, "updater");
        //Quake - Utility
        registerEntity("projectile_quake", ProjectileQuake.class, ENTITY_START_ID++, 50);
        //Tile Entity - Utility
        registerTileEntity(TileEntityDisappearingSpawner.class, "disappearing_spawner_entity");
        //Tile Entity - Utility
        registerTileEntity(TileEntityDoorStart.class, "door");
        //Tile Entity - Utility
        registerTileEntity(TileEntityDeactivate.class, "door_deactivate");
        //Tile Entity - Utility
        registerTileEntity(TileEntityActivate.class, "door_activate");
        //Tile Entity - Utility
        registerTileEntity(TileEntityTrap.class, "floor_trap");
        //Altar - Tile Entity
        registerTileEntity(TileEntityAltar.class, "altar");
        //End King
        registerEntityWithID("end_king", EntityEndKing.class, ENTITY_START_ID++, 50, end_mobs);
        //Red Crystal
        registerEntity("red_crystal", EntityRedCrystal.class, ENTITY_START_ID++, 50);
        //SpinSword Projectile
        registerEntity("red_sword", ProjectileSpinSword.class, ENTITY_START_ID++, 60);
        //Fireball Entity
        registerEntity("fire_ball_red", EntityFireBall.class, ENTITY_START_ID++, 60);
        //Nuclear Explosion
        registerEntity("nuke", EntityNuclearExplosion.class, ENTITY_START_ID++, 60);
        //End Bug
        registerEntityWithID("end_bug", EntityEndBug.class, ENTITY_START_ID++, 60, end_mobs);
        //Ender Knight
        registerEntityWithID("end_knight", EntityEnderKnight.class, ENTITY_START_ID++, 50, end_mobs);
        //Ender Shield
        registerEntityWithID("end_shield", EntityEnderShield.class, ENTITY_START_ID++, 60, end_mobs);
        //Ender Mage
        registerEntityWithID("end_mage", EntityEnderMage.class, ENTITY_START_ID++, 60, end_mobs);
        //Ender Sword Ultra
        registerEntityWithID("end_lord", EntityKnightLord.class, ENTITY_START_ID++, 60, end_mobs);
        //Snatcher
        registerEntityWithID("snatcher", EntitySnatcher.class, ENTITY_START_ID++, 70, end_mobs);
        //Heal Aura
        registerEntity("heal_aura", EntityHealAura.class, ENTITY_START_ID++, 60);
    }

    public static void RegisterEntitySpawns() {
        spawnRate(EntityBuffker.class, EnumCreatureType.MONSTER, ModConfig.constructor_weights, 1 ,2, BiomeDictionary.Type.END);
    }



    public static String getID(Class<? extends Entity> entity) {
        if (ID_MAP.containsKey(entity)) {
            return ModReference.MOD_ID + ":" + ID_MAP.get(entity);
        }
        throw new IllegalArgumentException("Mapping of an entity has not be registered for the maelstrom mod spawner system.");
    }

    private static void registerEntityWithID(String name, Class<? extends Entity> entity, int id, int range, Vec3i eggColor) {
        EntityRegistry.registerModEntity(new ResourceLocation(ModReference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, true, eggColor.getX(), eggColor.getY());
        ID_MAP.put(entity, name);
    }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, Vec3i eggColor) {
        EntityRegistry.registerModEntity(new ResourceLocation(ModReference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, true, eggColor.getX(), eggColor.getY());
    }

    private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range) {
        EntityRegistry.registerModEntity(new ResourceLocation(ModReference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, true);
    }

    private static void registerFastProjectile(String name, Class<? extends Entity> entity, int id, int range) {
        EntityRegistry.registerModEntity(new ResourceLocation(ModReference.MOD_ID + ":" + name), entity, name, id, Main.instance, range, 1, false);
    }

    private static void registerTileEntity(Class<? extends TileEntity> entity, String name) {
        GameRegistry.registerTileEntity(entity, new ResourceLocation(ModReference.MOD_ID + ":" + name));
    }

    private static void spawnRate(Class<? extends EntityLiving> entityClass, EnumCreatureType creatureType, int weight, int min, int max, BiomeDictionary.Type biomesAllowed) {
        for(Biome biome: BiomeDictionary.getBiomes(biomesAllowed)) {
            if(biome != null && weight > 0) {
                EntityRegistry.addSpawn(entityClass, weight, min, max, creatureType, biome);

            }
        }
    }

}
