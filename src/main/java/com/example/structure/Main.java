package com.example.structure;

import com.example.structure.init.ModEntities;
import com.example.structure.proxy.CommonProxy;
import com.example.structure.world.WorldGenCustomStructure;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;


@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main
{
    public static final String MODID = "test";
    public static final String NAME = "Structure Mod";
    public static final String VERSION = "1.0";

    public static CommonProxy proxy;
    public static Main instance;

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        GeckoLib.initialize();
        logger = event.getModLog();
        //Registering the Structures lmao

        ModEntities.registerEntities();
        GameRegistry.registerWorldGenerator(new WorldGenCustomStructure(), 3);
        proxy.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
