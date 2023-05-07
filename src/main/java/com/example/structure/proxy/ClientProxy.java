package com.example.structure.proxy;

import com.example.structure.util.handlers.RenderHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }


    @Override
    public void init() {



        //Registers Geckolib Entities
        RenderHandler.registerGeoEntityRenderers();
        super.init();
    }
}
