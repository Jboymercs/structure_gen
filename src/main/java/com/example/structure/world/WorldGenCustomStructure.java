package com.example.structure.world;

import com.example.structure.config.ModConfig;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEnd;
import net.minecraft.world.biome.BiomeOcean;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.lwjgl.Sys;

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

public static final WorldGenStructure END_BOSS_STRUCTURE = new WorldGenStructure("ocean/endboss");

//If you want to use this you can, as of now I have it preset to ONE structure, however if you wish to add more, create what
    //OCEAN_RUINS has and then just throw it in a list below and you can call a random choice at the actual generation of it.
public static WorldGenStructure[] listedStructures = {OCEAN_RUINS};




    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int x = chunkX * 16;
        int z = chunkZ * 16;

        Class<?> BIOMES = Biomes.OCEAN.getBiomeClass();
        Class<?> BIOMES_END = BiomeEnd.class;


        if(world.provider.getDimension() == 0) {
                                                   // Alter the nextInt to change weight, if we really want too we can make this a config option
            if(world.rand.nextInt(24) == 0) {
                // Set to spawn in all types of OCEAN Biomes, specifically for the Overworld only.
                generateBiomeSpecificStructure(OCEAN_RUINS , world, random, x, z,BIOMES );

            }
        }
        if(world.provider.getDimension() == 1) {
            if(canStructureSpawn(chunkX, chunkZ, world, 4)) {
                generateBiomeSpecificStructure(END_BOSS_STRUCTURE, world, random, x, z, BIOMES_END);
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
                    System.out.println("Generation");
                    return true;

                }
            }
        }
        return false;
    }

    public static boolean canStructureSpawn(int chunkX, int chunkZ, World world, int frequency){
        if (frequency <= 0) return false;
        int realFreq= 11 - frequency;
        int maxDistanceBetween = realFreq + 8;

        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= maxDistanceBetween - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= maxDistanceBetween - 1;
        }

        int k = chunkX / maxDistanceBetween;
        int l = chunkZ / maxDistanceBetween;
        Random random = world.setRandomSeed(k, l, 14357617);
        k = k * maxDistanceBetween;
        l = l * maxDistanceBetween;
        k = k + random.nextInt(maxDistanceBetween - 8);
        l = l + random.nextInt(maxDistanceBetween - 8);

        return i == k && j == l;
    }
}
