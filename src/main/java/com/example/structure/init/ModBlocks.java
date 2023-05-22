package com.example.structure.init;

import com.example.structure.blocks.BlockBase;
import com.example.structure.blocks.BlockKey;
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


    public static final Block LAMENTED_END_STONE = new BlockBase("lamented_end_stone", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.SLIME).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

    public static final Block END_KEY_BLOCK = new BlockKey("key_block", ModItems.END_KEY, ((world, pos) -> new EntityExplosion(world, pos.getX(), pos.getY(), pos.getZ(), null)));
}
