package com.example.structure.util.handlers;


import com.example.structure.init.ModBlocks;
import com.example.structure.init.ModItems;
import com.example.structure.items.CrystalBallItem;
import com.example.structure.items.Items;
import com.example.structure.renderer.RenderBossSword;
import com.example.structure.renderer.RenderCrystalBall;
import com.example.structure.renderer.RenderSpinSword;
import com.example.structure.util.IHasModel;
import com.example.structure.util.ModReference;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import software.bernie.geckolib3.GeckoLib;


@Mod.EventBusSubscriber
public class RegistryHandler {

    private static IForgeRegistry<Item> itemRegistry;



    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        itemRegistry = event.getRegistry();
        event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
    }


    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegister(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(ModItems.CRYSTAL_BALL, 0, new ModelResourceLocation(ModReference.MOD_ID + ":crystalball","inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.BOSS_SWORD, 0, new ModelResourceLocation(ModReference.MOD_ID + ":sword", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ModItems.SPIN_SWORD_ITEM, 0, new ModelResourceLocation(ModReference.MOD_ID + ":spinsword", "inventory"));
        ModItems.CRYSTAL_BALL.setTileEntityItemStackRenderer(new RenderCrystalBall());
        ModItems.BOSS_SWORD.setTileEntityItemStackRenderer(new RenderBossSword());
        ModItems.SPIN_SWORD_ITEM.setTileEntityItemStackRenderer(new RenderSpinSword());
        for (Item item : ModItems.ITEMS) {
            if (item instanceof IHasModel) {
                ((IHasModel) item).registerModels();
            }
        }
        for (Block block : ModBlocks.BLOCKS) {
            if (block instanceof IHasModel) {
                ((IHasModel) block).registerModels();
            }
        }

    }
}
