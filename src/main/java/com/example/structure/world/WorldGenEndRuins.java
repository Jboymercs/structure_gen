package com.example.structure.world;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenEndRuins extends WorldGenEndDungeon{


    public WorldGenEndRuins() {
        super("ocean/endruins", 0);
    }


    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {
        super.generateStructure(world, pos, Rotation.NONE);
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {
        if(function.startsWith("left")) {
            BlockPos pos1 = pos.add(new BlockPos(-4,-7,10));
            new WorldGenEndRuinHouse().generateStructure(world, pos1, Rotation.NONE);
            world.setBlockToAir(pos);
        }
        if(function.startsWith("right")) {
            BlockPos pos1 = pos.add(new BlockPos(-6,-6,-18));
            new WorldGenEndFarm().generateStructure(world, pos1, Rotation.NONE);
            world.setBlockToAir(pos);
        }
    }
}
