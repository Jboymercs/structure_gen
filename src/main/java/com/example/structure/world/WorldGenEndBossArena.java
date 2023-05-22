package com.example.structure.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenEndBossArena extends WorldGenEndDungeon{



    public WorldGenEndBossArena() {
        super("ocean/endboss", 35);
    }

    @Override
    public boolean generate(World worldIn, Random random, BlockPos blockPos) {
        return super.generate(worldIn, random, blockPos.add(new BlockPos(0,yOffset,0)));
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {
        if(function.startsWith("east")) {
           BlockPos pos1 = pos.add(new BlockPos(10,-9,-10));
           new WorldGenEndRuins().generate(world, random, pos1);
           world.setBlockToAir(pos);
        }
    }
}
