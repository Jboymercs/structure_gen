package com.example.structure.proxy;

import com.example.structure.Main;
import com.example.structure.packets.MessageModParticles;
import com.example.structure.util.ModReference;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {

    public void registerItemRenderer(Item item, int meta, String id) {
    }


    public void setCustomState(Block block, IStateMapper mapper) {
    }

    public void init() {
        Main.network = NetworkRegistry.INSTANCE.newSimpleChannel(ModReference.CHANNEL_NETWORK_NAME);
        int packetID = 0;
        Main.network.registerMessage(MessageModParticles.MessageHandler.class, MessageModParticles.class, packetID++, Side.CLIENT);
    }
}
