package com.example.structure.world;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenEndRuinsW extends WorldGenEndDungeon{

    public WorldGenEndRuinsW() {
        super("ocean/endruins", 0);
    }

    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {
        super.generateStructure(world, pos, Rotation.CLOCKWISE_180);
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {
        if(function.startsWith("left")) {
        BlockPos pos1 = pos.add(new BlockPos(-12,-7,-22));
        new WorldGenRuinBridge().generateStructure(world, pos1, Rotation.NONE);
        world.setBlockToAir(pos);
        }
        if(function.startsWith("right")) {
            BlockPos pos1 = pos.add(new BlockPos(-5,-6,10));
            new WorldGenCircle().generateStructure(world, pos1, Rotation.NONE);
            world.setBlockToAir(pos);
        }
    }
}
