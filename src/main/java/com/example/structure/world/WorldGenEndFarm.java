package com.example.structure.world;

import com.example.structure.config.ModConfig;
import com.example.structure.world.islands.WorldGenSmallIslandThree;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenEndFarm extends WorldGenEndDungeon{
    public WorldGenEndFarm() {
        super("ocean/endfarm", 0);
    }

    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {
        super.generateStructure(world, pos, Rotation.NONE);
    }
    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {
        if(function.startsWith("island")) {
            if(random.nextFloat() > 0.3 && ModConfig.miniIslands_spawn) {
                BlockPos pos1 = pos.add(new BlockPos(-5, random.nextInt(5) + 5, -5));
                new WorldGenSmallIslandThree().generateStructure(world, pos1, Rotation.NONE);
                world.setBlockToAir(pos);
            } else {
                world.setBlockToAir(pos);
            }
        }
    }
}
