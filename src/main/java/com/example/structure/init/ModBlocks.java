package com.example.structure.init;

import com.example.structure.blocks.*;
import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.entity.EntityExplosion;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {
    public static final List<Block> BLOCKS = new ArrayList<Block>();
    public static final float STONE_HARDNESS = 1.7f;
    public static final float STONE_RESISTANCE = 10f;
    public static final float BRICK_HARDNESS = 2.0f;
    public static final float WOOD_HARDNESS = 1.5f;
    public static final float WOOD_RESISTANCE = 5.0f;
    public static final float PLANTS_HARDNESS = 0.2f;
    public static final float PLANTS_RESISTANCE = 2.0f;
    public static final float ORE_HARDNESS = 3.0F;
    public static final float OBSIDIAN_HARDNESS = 50;
    public static final float OBSIDIAN_RESISTANCE = 2000;


    public static final Block LAMENTED_END_STONE = new BlockBase("lamented_end_stone", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block RED_CRYSTAL = new BlockBase("red_crystal", Material.GLASS, WOOD_HARDNESS, WOOD_RESISTANCE, SoundType.GLASS).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block RED_CRYSTAL_TOP = new BlockCrystalTopBase("red_crystal_top", Material.GLASS).setHardness(WOOD_HARDNESS).setResistance(WOOD_RESISTANCE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block END_ASH = new BlockAsh("end_ash", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

    public static final Block END_KEY_BLOCK = new BlockKey("key_block", ModItems.END_KEY, ((world, pos) -> new EntityExplosion(world, pos.getX(), pos.getY(), pos.getZ(), null)));

    public static final Block DISAPPEARING_SPAWNER = new BlockDisappearingSpawner("disappearing_spawner", Material.ROCK);
}
