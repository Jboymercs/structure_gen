package com.example.structure.world;

import com.example.structure.config.ModConfig;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEnd;
import net.minecraft.world.biome.BiomeEndDecorator;
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

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int x = chunkX * 16;
        int z = chunkZ * 16;

        if(world.provider.getDimension() == 1) {
            if (ModConfig.does_structure_spawn) {
                if (canStructureSpawn(chunkX, chunkZ, world, ModConfig.structureFrequency)) {

                    new WorldGenEndBossArena().generateStructure(world, new BlockPos(x, 90, z), Rotation.NONE);
                }
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

    public static boolean canStructureSpawn(int chunkX, int chunkZ, World world, int frequency){
        if (frequency <= 0) return false;
        int realFreq= 48 - frequency;
        int maxDistanceBetween = realFreq + 36;

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
