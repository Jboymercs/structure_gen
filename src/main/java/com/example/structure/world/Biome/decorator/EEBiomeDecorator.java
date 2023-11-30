package com.example.structure.world.Biome.decorator;

import com.example.structure.world.Biome.generation.WorldGenAshSpikes;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEndDecorator;

import java.util.Random;

public class EEBiomeDecorator extends BiomeEndDecorator {


    public EEBiomeDecorator()
    {
    }

    @Override
    protected void genDecorations(Biome biomeIn, World worldIn, Random random)
    {
        super.genDecorations(biomeIn, worldIn, random);
    }
}
