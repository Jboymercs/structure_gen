package com.example.structure.world;

import com.example.structure.config.ModConfig;
import com.example.structure.util.ModReference;
import com.example.structure.world.islands.WorldGenSmallIslandTwo;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenEndTower extends WorldGenEndDungeon{

    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "end_dungeon");
    public WorldGenEndTower() {
        super("ocean/endtower", 0);
    }

    @Override
    public void generateStructure(World world, BlockPos pos, Rotation rotation) {
        super.generateStructure(world, pos, Rotation.NONE);
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random random) {
        if(function.startsWith("chest")) {
                TileEntity tileEntity = world.getTileEntity(pos.down());
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                if (tileEntity instanceof TileEntityChest) {
                    TileEntityChest chest = (TileEntityChest) tileEntity;
                    chest.setLootTable(LOOT, random.nextLong());
                }

        }
        if(function.startsWith("island")) {
            if(random.nextFloat() > 0.4 && ModConfig.miniIslands_spawn) {
                BlockPos pos1 = pos.add(new BlockPos(5, random.nextInt(6), -5));
                new WorldGenSmallIslandTwo().generateStructure(world, pos1, Rotation.NONE);
                world.setBlockToAir(pos);
            } else {
                world.setBlockToAir(pos);
            }
        }
    }
}
