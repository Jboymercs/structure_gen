package com.example.structure.init;

import com.example.structure.config.ModConfig;
import com.example.structure.items.*;
import com.example.structure.items.armor.ItemSlideBoots;
import com.example.structure.items.armor.ModArmorBase;
import com.example.structure.items.tools.ToolBossSword;
import com.example.structure.items.tools.ToolRedSword;
import com.example.structure.util.ModReference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

import java.util.ArrayList;
import java.util.List;

public class ModItems {


    private static final Item.ToolMaterial SWORD = EnumHelper.addToolMaterial("rare_sword", 2, 100, 8.0f, ModConfig.sword_damage, 20);
    private static final Item.ToolMaterial RED_SWORD = EnumHelper.addToolMaterial("unholy", 2, 800, 8.0f, 6.0F, 40);

    private static final ItemArmor.ArmorMaterial ARMOR = EnumHelper.addArmorMaterial("skate", ModReference.MOD_ID + ":skate", 200, new int[]{3, 6, 8, 3}, 16, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0);
    private static final ItemArmor.ArmorMaterial DARK_ARMOR = EnumHelper.addArmorMaterial("dark", ModReference.MOD_ID + ":dark", 800, new int[]{3, 6, 8, 3}, 20, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 2);

    public static final List<Item> ITEMS = new ArrayList<Item>();

    //Guide Book
    public static final Item EE_BOOK = new ItemModBook("info_book", "guide", ModCreativeTabs.ITEMS);
    //Lamented Islands Items
    public static final Item END_KEY = new ItemKey("key", "dimensional_key", ModCreativeTabs.ITEMS);
    public static final Item LAMENTED_EYE = new ItemLamentedEye("lamented", "lamented_eye", ModCreativeTabs.ITEMS);
    public static final Item BOSS_SWORD = new ToolBossSword("sword", "sword_desc", SWORD);
    public static final Item PURPLE_CRYSTAL_ITEM = new ItemPurpleCrystal("purple_crystal_item", "purple_crystal", ModCreativeTabs.ITEMS);
    //Ash Wastelands Items
    public static  final Item RED_CRYSTAL_ITEM = new ItemRedCrystal("red_crystal_item", "red_crystal",ModCreativeTabs.ITEMS);
    public static final Item INFUSED_CRYSTAL = new ItemInfusedCrystal("infused_crystal", "infuse", ModCreativeTabs.ITEMS);
    public static final Item INFUSION_CORE = new ItemInfusionCore("infusion_core", "core", ModCreativeTabs.ITEMS);
    public static final Item STALKER_HIDE = new ItemBase("stalker_hide", ModCreativeTabs.ITEMS);
    public static final Item PARASITE_CARAPACE = new ItemBase("carapace", ModCreativeTabs.ITEMS);
    public static final Item DARK_INGOT = new ItemBase("dark_ingot", ModCreativeTabs.ITEMS);
    public static final Item KNIGHT_SWORD = new ToolRedSword("red_sword", "unholy_sword_desc", RED_SWORD);
    public static final Item DARK_HELMET = new ModArmorBase("dark_helmet", DARK_ARMOR, 1, EntityEquipmentSlot.HEAD, "dark");
    public static final Item DARK_CHESTPLATE = new ModArmorBase("dark_chestplate", DARK_ARMOR, 1, EntityEquipmentSlot.CHEST, "dark");


    //Misc.

   // public static Item ALTAR;

    public static final Item INVISIBLE = new ItemBase("invisible", null);
    public static final Item CRYSTAL_BALL = new CrystalBallItem("crystalball", null);

    public static final Item SPIN_SWORD_ITEM = new SpinSwordItem("spinsword", null);



   //public static final Item SKATE_BOOTS = new ItemSlideBoots("skate_boots", ARMOR, 1, EntityEquipmentSlot.FEET,  "skate");
    public ModItems() {


    }
}
