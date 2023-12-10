package com.example.structure.init;

import com.example.structure.blocks.*;
import com.example.structure.blocks.slab.BlockDoubleSlab;
import com.example.structure.blocks.slab.BlockHalfSlab;
import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.entity.EntityExplosion;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
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
    public static final Block RED_CRYSTAL = new BlockCrystal("red_crystal", Material.GLASS, ModItems.RED_CRYSTAL_ITEM).setHardness(WOOD_HARDNESS).setResistance(WOOD_RESISTANCE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block PURPLE_CRYSTAL = new BlockCrystal("purple_crystal", Material.GLASS, ModItems.PURPLE_CRYSTAL_ITEM).setHardness(WOOD_HARDNESS).setResistance(WOOD_RESISTANCE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block RED_CRYSTAL_TOP = new BlockCrystalTopBase("red_crystal_top", Material.GLASS, ModItems.RED_CRYSTAL_ITEM).setHardness(WOOD_HARDNESS).setResistance(WOOD_RESISTANCE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block END_ASH = new BlockAsh("end_ash", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block PURPLE_CRYSTAL_TOP = new BlockCrystalTopBase("purple_crystal_top", Material.GLASS, ModItems.PURPLE_CRYSTAL_ITEM).setHardness(WOOD_HARDNESS).setResistance(WOOD_RESISTANCE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block END_BARRIER = new BlockBarrier("end_barrier", Material.BARRIER);
    public static final Block END_ASH_SKULL = new BlockBase("ash_skull", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block BROWN_END_STONE = new BlockBase("brown_stone", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block BROWN_END_BRICK = new BlockBase("brown_brick", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block BROWN_SMOOTH_STONE = new BlockBase("brown_smooth", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

    public static final Block DOOR_ACTIVATOR = new BlockDungeonDoor("end_door", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

    public static final Block LIGHTING_UPDATER = new BlockLightingUpdater("lighting_updater", Material.AIR).setLightLevel(0.1f);

    public static final Block RED_LAMP = new BlockLamp("red_lamp", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setLightLevel(1.0f).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);


    public static final Block END_ASH_DOOR_ACTIVATE = new BlockDoorDeactivation("door_on", Material.ROCK, ModItems.RED_CRYSTAL_ITEM).setResistance(OBSIDIAN_RESISTANCE).setHardness(OBSIDIAN_HARDNESS);
    public static final Block END_ASH_DOOR = new BlockDoorActivation("ash_door", ModItems.RED_CRYSTAL_ITEM);
    public static final Block END_ASH_CHISLE = new BlockBase("ash_chisle", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

    public static final Block END_KEY_BLOCK = new BlockKey("key_block", ModItems.END_KEY, ((world, pos) -> new EntityExplosion(world, pos.getX(), pos.getY(), pos.getZ(), null)));
    public static final Block ASH_BRICK = new BlockBase("ash_brick", Material.ROCK, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block ASH_TRAP_FLOOR = new GroundCrystalTrapBlock("ash_trap_floor", Material.ROCK).setResistance(STONE_RESISTANCE).setHardness(STONE_HARDNESS).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final BlockSlab ASH_BRICK_DOUBLE = new BlockDoubleSlab("ash_brick_double", Material.ROCK, CreativeTabs.BUILDING_BLOCKS, ModBlocks.ASH_BRICK_HALF, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE);
    public static final BlockSlab ASH_BRICK_HALF = new BlockHalfSlab("ash_brick_half", Material.ROCK, CreativeTabs.BUILDING_BLOCKS, ModBlocks.ASH_BRICK_HALF, ModBlocks.ASH_BRICK_DOUBLE, STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE);
    public static final Block ASH_BRICK_STAIRS = new BlockStairBase("ash_brick_stairs", ASH_BRICK.getDefaultState(), STONE_HARDNESS, STONE_RESISTANCE, SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static final Block ASH_BRICK_PILLAR = new BlockPillarBase("ash_brick_pillar", Material.ROCK).setHardness(STONE_HARDNESS).setResistance(STONE_RESISTANCE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

    public static final Block DISAPPEARING_SPAWNER = new BlockDisappearingSpawner("disappearing_spawner", Material.ROCK);
}
