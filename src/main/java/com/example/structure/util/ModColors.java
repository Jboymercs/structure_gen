package com.example.structure.util;

import net.minecraft.util.math.Vec3d;

public class ModColors {
    public static final Vec3d WHITE = new Vec3d(1, 1, 1);
    public static final Vec3d MAELSTROM = new Vec3d(0.3, 0.2, 0.4);
    public static final Vec3d RED = new Vec3d(0.9, 0.1, 0.1);
    public static final Vec3d AZURE = new Vec3d(0.2, 0.8, 1);

    public static final Vec3d GREY = new Vec3d(0.5, 0.5, 0.5);

    public static final Vec3d RANDOM_GREY = randomSetColors();

    public static Vec3d variateColor(Vec3d baseColor, float variance) {
        float f = ModRand.getFloat(variance);

        return new Vec3d((float) Math.min(Math.max(0, baseColor.x + f), 1),
                (float) Math.min(Math.max(0, baseColor.y + f), 1),
                (float) Math.min(Math.max(0, baseColor.z + f), 1));
    }

    public static Vec3d randomSetColors() {
        int randomGenerator = ModRand.range(1, 9);
        if(randomGenerator <= 3) {
            return new Vec3d(0.3, 0.3, 0.3);
        }
        if(randomGenerator > 4 && randomGenerator <= 6) {
            return new Vec3d(0.2, 0.2, 0.2);
        }
        else {
            return new Vec3d(0.05, 0.05, 0.05);
        }
    }

    public static int toIntegerColor(int r, int g, int b, int a) {
        int i = r << 16;
        i += g << 8;
        i += b;
        i += a << 24;
        return i;
    }
}
