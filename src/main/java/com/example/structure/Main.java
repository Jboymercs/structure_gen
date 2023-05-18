package com.example.structure;

import com.example.structure.init.ModEntities;
import com.example.structure.proxy.CommonProxy;
import com.example.structure.util.ModReference;
import com.example.structure.world.WorldGenCustomStructure;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;


@Mod(modid = ModReference.MOD_ID, name = ModReference.NAME, version = ModReference.VERSION)
public class Main
{

    @SidedProxy(clientSide = ModReference.CLIENT_PROXY_CLASS, serverSide = ModReference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;
    public static SimpleNetworkWrapper network;
    @Mod.Instance
    public static Main instance;

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        GeckoLib.initialize();
        logger = event.getModLog();

        //Register Entities
        ModEntities.registerEntities();
        //Register World Gen
        GameRegistry.registerWorldGenerator(new WorldGenCustomStructure(), 3);
        //Renders and other misc stuff
        proxy.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
