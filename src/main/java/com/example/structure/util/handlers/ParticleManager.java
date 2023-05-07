package com.example.structure.util.handlers;

import com.example.structure.util.ModColors;
import com.example.structure.util.ModParticle;
import com.example.structure.util.ModRand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ParticleManager {

    public static void spawnCrystalWisps(World worldIn, Vec3d pos, Vec3d color, Vec3d motion) {
        ModParticle modParticle = new ModParticle(worldIn, pos, motion, 2, ModRand.range(10, 20), false);
        modParticle.setParticleTextureRange(0, 6, 2);
        spawnParticleWithColor(modParticle, color);
    }

    private static void spawnParticleWithColor(Particle particle, Vec3d baseColor) {
        baseColor = ModColors.variateColor(baseColor, 0.2f);
        particle.setRBGColorF((float) baseColor.x, (float) baseColor.y, (float) baseColor.z);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }
}
