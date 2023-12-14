package com.example.structure;


import com.example.structure.entity.tileentity.TileEntityAltar;
import com.example.structure.init.ModEntities;
import com.example.structure.proxy.CommonProxy;
import com.example.structure.renderer.RenderAltarTile;
import com.example.structure.util.ModReference;
import com.example.structure.util.handlers.BiomeRegister;
import com.example.structure.util.handlers.FogHandler;
import com.example.structure.util.handlers.ModSoundHandler;
import com.example.structure.world.WorldGenCustomStructure;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;


@Mod(modid = ModReference.MOD_ID, name = ModReference.NAME, version = ModReference.VERSION)
public class Main
        /**
         * I'd like to give a huge thank you to Barribob, for the spectacular work done on Maelstrom and this project
         * exsisting cause of it. Credit to a lot of the code modified from Maelstrom source. Yes, I am revamping that mod but
         * Barribob still deserves the credit.
         *
         * Credits also go to - UnOriginal for the structure method used in this project, and a few other functions used
         *
         * FakeDrayn for sound design, and animation work
         *
         * Lastly, this mod is for the ModJam 2023 Summer for the 1.12.2 Modded Coalition Discord Server
         * link - https://discord.gg/Hmvek4Axrv
         */
{

    @SidedProxy(clientSide = ModReference.CLIENT_PROXY_CLASS, serverSide = ModReference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;
    public static SimpleNetworkWrapper network;

    public static final Logger LOGGER = LogManager.getLogger(ModReference.MOD_ID);
    @Mod.Instance
    public static Main instance;

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        GeckoLib.initialize();
        logger = event.getModLog();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
        //Register Entities
        ModEntities.registerEntities();
        ModEntities.RegisterEntitySpawns();
        //Register World Gen
        GameRegistry.registerWorldGenerator(new WorldGenCustomStructure(), 3);
        //Register Fog
        handleClientFog();
        proxy.init();
    }

    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void registerRenderers(FMLPreInitializationEvent event) {
        //Specific for blocks handling geckolib data
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAltar.class, new RenderAltarTile());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        BiomeRegister.registerBiomes();
        ModSoundHandler.registerSounds();
    }

    public static void handleClientFog() {
        MinecraftForge.EVENT_BUS.register(new FogHandler());
    }
}
