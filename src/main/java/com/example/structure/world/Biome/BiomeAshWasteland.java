package com.example.structure.world.Biome;

import com.example.structure.init.ModBlocks;
import com.example.structure.util.IBiomeMisty;
import com.example.structure.util.ModRand;
import com.example.structure.world.Biome.decorator.EEBiomeDecorator;
import com.example.structure.world.Biome.generation.WorldGenAshHeights;
import com.example.structure.world.Biome.generation.WorldGenAshSpikes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BiomeAshWasteland extends BiomeFogged{


    private float fogRangeInterpolateStart = 0.0F;
    private float fogRangeInterpolateEnd = 0.0F;
    public static BiomeProperties properties = new BiomeProperties("Ash Wastelands");
    private static final IBlockState AIR = Blocks.AIR.getDefaultState();

    public int spikesPerChunk = 3;

    public WorldGenAshSpikes spikes = new WorldGenAshSpikes();
    public WorldGenerator ashHeights = new WorldGenAshHeights();
    private static final IBlockState END_FLOOR = ModBlocks.END_ASH.getDefaultState();
    private Random random;
    public BiomeAshWasteland() {
        super(properties.setBaseHeight(0.3f).setHeightVariation(0.8f));
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.topBlock = END_FLOOR;
        this.fillerBlock = END_FLOOR;
        random = new Random();
        this.setFogColor(10, 30, 22);



    }


    @SideOnly(Side.CLIENT)
    @Override
    public int getSkyColorByTemp(float currentTemperature)
    {
        currentTemperature = MathHelper.clamp(Math.abs(1.25F - currentTemperature), 0.0F, 1.0F);
        int r = 140;
        int g = (int)(185 + currentTemperature * 15);
        int b = (int)(215 - currentTemperature * 5);
        return r << 16 | g << 8 | b;
    }


    @Override
    public BiomeDecorator createBiomeDecorator()
    {
        return new EEBiomeDecorator();
    }

    public void decorate(World world, Random rand, BlockPos pos)
    {
        if(rand.nextInt(2) == 0) {
            int yHieght = getEndSurfaceHeight(world, pos.add(16, 0, 16), 50, 70);
            if(yHieght > 0) {
                ashHeights.generate(world, rand, pos.add(ModRand.range(1, 16), yHieght + 1, ModRand.range(1, 16)));
            }
        }
        for (int k2 = 0; k2 < this.spikesPerChunk; ++k2)
        {
            int l6 = random.nextInt(16) + 8;
            int k10 = random.nextInt(16) + 8;
            int yHieght = getEndSurfaceHeight(world, pos.add(16, 0, 16), 50, 70);
            if(yHieght > 0) {
                this.spikes.generate(world, random, pos.add(l6, yHieght, k10));
            }

        }
    }


    private int getEndSurfaceHeight(World world, BlockPos pos, int min, int max)
    {
        int maxY = max;
        int minY = min;
        int currentY = maxY;

        while(currentY >= minY)
        {
            if(!world.isAirBlock(pos.add(0, currentY, 0)))
                return currentY;
            currentY--;
        }
        return 0;
    }





}
