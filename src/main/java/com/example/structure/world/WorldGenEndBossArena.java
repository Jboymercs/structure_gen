package com.example.structure.world;

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
    }
}
