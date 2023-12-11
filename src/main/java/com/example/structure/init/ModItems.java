package com.example.structure.init;

import com.example.structure.config.ModConfig;
import com.example.structure.items.*;
import com.example.structure.items.tools.ToolBossSword;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    private static final Item.ToolMaterial SWORD = EnumHelper.addToolMaterial("rare_sword", 2, 100, 8.0f, ModConfig.sword_damage, 20);


    public static final List<Item> ITEMS = new ArrayList<Item>();
    public static final Item INVISIBLE = new ItemBase("invisible", null);

    public static final Item END_KEY = new ItemKey("key", "dimensional_key", CreativeTabs.MISC);

    public static final Item LAMENTED_EYE = new ItemLamentedEye("lamented", "lamented_eye", CreativeTabs.MISC);

    public static final Item BOSS_SWORD = new ToolBossSword("sword", "sword_desc", SWORD);
    public static  final Item RED_CRYSTAL_ITEM = new ItemRedCrystal("red_crystal_item", "red_crystal",CreativeTabs.MATERIALS);

    public static final Item PURPLE_CRYSTAL_ITEM = new ItemPurpleCrystal("purple_crystal_item", "purple_crystal", CreativeTabs.MATERIALS);

    public static final Item INFUSED_CRYSTAL = new ItemInfusedCrystal("infused_crystal", "infuse", CreativeTabs.MATERIALS);

    public static final Item INFUSION_CORE = new ItemInfusionCore("infusion_core", "core", CreativeTabs.MATERIALS);

    public static Item ALTAR;

    public static final Item CRYSTAL_BALL = new CrystalBallItem("crystalball", null);

    public static final Item SPIN_SWORD_ITEM = new SpinSwordItem("spinsword", null);
    public ModItems() {


    }
}
