package com.example.structure.world;

import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeOcean;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WorldGenCustomStructure implements IWorldGenerator {

    //Used for lists
public static class lunkerToothStructure extends WorldGenStructure {

    public lunkerToothStructure(String structureName) {
        super("structures/" + structureName);
    }
}

//handles the data markers
public static final WorldGenStructure OCEAN_RUINS = new WorldGenStructure("ocean/ocean_ruins") {
    @Override
    protected void handleDataMarker(String function, BlockPos pos, World worldIn, Random rand) {
        //This is to handle the structure block data marker
        if(function.startsWith("loot")) {
            worldIn.setBlockState(pos, Blocks.CHEST.getDefaultState());
            TileEntity tileEntity = worldIn.getTileEntity(pos);

            if(tileEntity instanceof TileEntityChest) {
                //Insert Loot Table here
            }
        }
    }
};

//If you want to use this you can, as of now I have it preset to ONE structure, however if you wish to add more, create what
    //OCEAN_RUINS has and then just throw it in a list below and you can call a random choice at the actual generation of it.
public static WorldGenStructure[] listedStructures = {OCEAN_RUINS};



    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int x = chunkX * 16;
        int z = chunkZ * 16;
        Class<?> BIOMES = Biomes.OCEAN.getBiomeClass();

        if(world.provider.getDimension() == 0) {
            int i = 2;                                          // Alter the nextInt to change weight, if we really want too we can make this a config option
            if(chunkX % i == 0 && chunkZ % i == 0 && world.rand.nextInt(24) == 0) {
                // Set to spawn in all types of OCEAN Biomes, specifically for the Overworld only.
                generateBiomeSpecificStructure(OCEAN_RUINS , world, random, x, z,BIOMES );


            }
        }
    }

    private boolean generateBiomeSpecificStructure(WorldGenStructure generator, World world, Random rand, int x, int z, Class<?>... classes) {
        ArrayList<Class<?>> classesList = new ArrayList<Class<?>>(Arrays.asList(classes));

        x += 8;
        z += 8;
        int y = generator.getYGenHeight(world, x, z);
        BlockPos pos = new BlockPos(x, y, z);

        Class<?> biome = world.provider.getBiomeForCoords(pos).getClass();

        if (y > -1 && (world.getWorldType() != WorldType.FLAT || world.provider.getDimension() != 0)) {
            if (classesList.contains(biome)) {
                if (rand.nextFloat() > generator.getAttempts()) {
                    generator.generate(world, rand, pos);
                    return true;
                }
            }
        }
        return false;
    }
}
