package com.example.structure.world;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityBuffker;
import com.example.structure.entity.tileentity.MobSpawnerLogic;
import com.example.structure.entity.tileentity.tileEntityMobSpawner;
import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModEntities;
import com.example.structure.world.islands.WorldGenSmallIsland;
import com.example.structure.world.islands.WorldGenSmallIslandThree;
import com.example.structure.world.islands.WorldGenSmallIslandTwo;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenEndBossArena extends WorldGenEndDungeon{



    public WorldGenEndBossArena() {
        super("ocean/endboss", 0);
    }

    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {
        super.generateStructure(world, pos, Rotation.NONE);
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {
        if(function.startsWith("east")) {
           BlockPos pos1 = pos.add(new BlockPos(10,-9,-15));
           new WorldGenEndRuins().generateStructure(world, pos1, Rotation.NONE);
           world.setBlockToAir(pos);
        }
        if(function.startsWith("west")) {
            BlockPos pos1 = pos.add(new BlockPos(-22, -9, -13));
            new WorldGenEndRuinsW().generateStructure(world, pos1, Rotation.CLOCKWISE_180);
            world.setBlockToAir(pos);
        }
        if(function.startsWith("south")) {
            BlockPos pos1 = pos.add(new BlockPos(-9,-8,10));
            new WorldGenEndTower().generateStructure(world, pos1, Rotation.NONE);
            world.setBlockToAir(pos);
        }
        if(function.startsWith("north")) {
            BlockPos pos1 = pos.add(new BlockPos(-19,-7,-30));
            new WorldGenEndWalkway().generateStructure(world, pos1, Rotation.CLOCKWISE_90);
            world.setBlockToAir(pos);
        }
        if(function.startsWith("mob")) {
            if(random.nextFloat() > ModConfig.structure_spawns && ModConfig.constructor_center_spawn) {
                world.setBlockState(pos, ModBlocks.DISAPPEARING_SPAWNER.getDefaultState(), 2);
                TileEntity tileentity = world.getTileEntity(pos);
                if(tileentity instanceof tileEntityMobSpawner) {
                    ((tileEntityMobSpawner) tileentity).getSpawnerBaseLogic().setData(
                            new MobSpawnerLogic.MobSpawnData[]{
                                    new MobSpawnerLogic.MobSpawnData(ModEntities.getID(EntityBuffker.class),  1)
                            },
                            new int[]{1},
                            1,
                            24);
                }
            }
            else {
                world.setBlockToAir(pos);
            }
        }
        if(function.startsWith("island")) {
            if(random.nextFloat() > 0.4 && ModConfig.miniIslands_spawn) {
                BlockPos pos1 = pos.add(new BlockPos(-5, random.nextInt(5) + 5, -5));
                new WorldGenSmallIsland().generateStructure(world, pos1, Rotation.NONE);
                world.setBlockToAir(pos);
            } else {
                world.setBlockToAir(pos);
            }
        }
        if(function.startsWith("island2") ) {
            if(random.nextFloat() > 0.6 && ModConfig.miniIslands_spawn) {
                BlockPos pos1 = pos.add(new BlockPos(-5, random.nextInt(9) + 5, -5));
                new WorldGenSmallIslandTwo().generateStructure(world,pos1, Rotation.NONE );
                world.setBlockToAir(pos);
            } else {
                world.setBlockToAir(pos);
            }
        }
        if(function.startsWith("island3")) {
            if(random.nextFloat() > 0.2 && ModConfig.miniIslands_spawn) {
                BlockPos pos1 = pos.add(new BlockPos(-5, random.nextInt(12) + 5, -5));
                new WorldGenSmallIslandThree().generateStructure(world, pos1, Rotation.NONE);
                world.setBlockToAir(pos);
            } else {
                world.setBlockToAir(pos);
            }

        }

    }
}
