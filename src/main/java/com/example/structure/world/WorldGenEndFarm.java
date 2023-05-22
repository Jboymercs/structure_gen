package com.example.structure.world;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenEndFarm extends WorldGenEndDungeon{
    public WorldGenEndFarm() {
        super("ocean/endfarm", 0);
    }

    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {
        super.generateStructure(world, pos, Rotation.NONE);
    }
}
