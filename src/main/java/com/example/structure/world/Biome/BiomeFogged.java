package com.example.structure.world.Biome;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeFogged extends Biome {

    private int[] fogColorRGB = new int[]{(int) 255, (int) 255, (int) 255};
    public BiomeFogged(BiomeProperties properties) {
        super(properties);
        this.setFogColor(10, 30, 22);
    }

    /**
     * Sets the biome fog color
     * @param red
     * @param green
     * @param blue
     * @return
     */
    public final BiomeFogged setFogColor(int red, int green, int blue) {
        this.fogColorRGB[0] = red;
        this.fogColorRGB[1] = green;
        this.fogColorRGB[2] = blue;
        return this;
    }

    @SideOnly(Side.CLIENT)
    public float getFogStart(float farPlaneDistance, int mode) {
        return mode == -1 ? 0.0F : farPlaneDistance * 0.5F;
    }

    /**
     * Returns the distance where the fog is fully opaque.
     * @param farPlaneDistance Maximum render distance
     * @return float
     */
    @SideOnly(Side.CLIENT)
    public float getFogEnd(float farPlaneDistance, int mode) {
        return farPlaneDistance;
    }

    /**
     * Returns the fog RGB color.
     * @return int[3]
     */
    @SideOnly(Side.CLIENT)
    public int[] getFogRGB() {
        return this.fogColorRGB;
    }

    /**
     * Called to update the fog range and color
     */
    public void updateFog() {

    }
}
