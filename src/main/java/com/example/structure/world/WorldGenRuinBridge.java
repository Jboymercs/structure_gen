package com.example.structure.world;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenRuinBridge extends WorldGenEndDungeon{
    public WorldGenRuinBridge() {
        super("ocean/endruinbridge", 0);
    }

    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {
        super.generateStructure(world, pos, Rotation.CLOCKWISE_180);
    }

}
