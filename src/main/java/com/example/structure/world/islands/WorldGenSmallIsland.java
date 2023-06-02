package com.example.structure.world.islands;

import com.example.structure.world.WorldGenEndDungeon;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenSmallIsland extends WorldGenEndDungeon {

    public WorldGenSmallIsland() {
        super("ocean/smolisland", 0);
    }

    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {
        super.generateStructure(world, pos, Rotation.NONE);
    }
}
