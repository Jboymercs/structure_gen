package com.example.structure.util;

import com.example.structure.packets.MessageModParticles;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PacketUtils {


    public static World getWorld() {
        return Minecraft.getMinecraft().world;
    }


    public static void spawnEffect(MessageModParticles message) {
        ParticleManager.spawnEffect(Minecraft.getMinecraft().world, new Vec3d(message.xCoord, message.yCoord, message.zCoord), new Vec3d(message.particleArguments[0], message.particleArguments[1], message.particleArguments[2]));
    }
}
