package com.example.structure.proxy;

import com.example.structure.event_handler.ClientRender;
import com.example.structure.util.handlers.RenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getMinecraft().world;
    }


    @Override
    public void init() {



        //Registers Geckolib Entities
        RenderHandler.registerGeoEntityRenderers();
        super.init();
    }


    @SideOnly(Side.CLIENT)
    public static float getClientEffect(int selector, float defaultVal) {
        switch (selector) {
            case 1: return ClientRender.SCREEN_SHAKE;
            default: return defaultVal;
        }
    }
}
