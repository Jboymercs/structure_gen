package com.example.structure.proxy;

import com.example.structure.Main;
import com.example.structure.packets.MessageModParticles;
import com.example.structure.util.ModReference;
import com.example.structure.world.Biome.WorldProviderEndEE;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {

    public void registerItemRenderer(Item item, int meta, String id) {
    }


    public void setCustomState(Block block, IStateMapper mapper) {
    }
    public EntityPlayer getClientPlayer() {
        return null;
    }

    public World getClientWorld() {
        return null;
    }
    public void init() {
        addnewBiome();
        Main.network = NetworkRegistry.INSTANCE.newSimpleChannel(ModReference.CHANNEL_NETWORK_NAME);
        int packetID = 0;
        Main.network.registerMessage(MessageModParticles.MessageHandler.class, MessageModParticles.class, packetID++, Side.CLIENT);
    }

    public void addnewBiome() {
        DimensionManager.unregisterDimension(1);
        DimensionType endBiomes = DimensionType.register("End", "_end", 1, WorldProviderEndEE.class, false);
        DimensionManager.registerDimension(1, endBiomes);
    }
}
