package com.example.structure.util.handlers;


import com.example.structure.init.ModItems;
import com.example.structure.items.CrystalBallItem;
import com.example.structure.items.Items;
import com.example.structure.renderer.RenderCrystalBall;
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
        System.out.println("Item Registry Initialized");
        Items.CRYSTAL_BALL_ITEM = registerItem(new CrystalBallItem(),  "crystalball");
        event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
    }

    public static <T extends Item> T registerItem(T item, String name) {
        registerItem(item, new ResourceLocation(ModReference.MOD_ID, name));
        return item;
    }

    public static <T extends Item> T registerItem(T item, ResourceLocation name) {
        itemRegistry.register(item.setRegistryName(name).setUnlocalizedName(name.toString().replace(":", ".")));
        return item;
    }





    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onModelRegister(ModelRegistryEvent event) {
        System.out.println("Model Registry");
        ModelLoader.setCustomModelResourceLocation(Items.CRYSTAL_BALL_ITEM, 0, new ModelResourceLocation(ModReference.MOD_ID + ":crystalball","inventory"));
        Items.CRYSTAL_BALL_ITEM.setTileEntityItemStackRenderer(new RenderCrystalBall());
        for (Item item : ModItems.ITEMS) {
            if (item instanceof IHasModel) {
                ((IHasModel) item).registerModels();
            }
        }

    }
}