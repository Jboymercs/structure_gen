package com.example.structure.util;

import net.minecraft.util.math.Vec3d;

public class ModColors {
    public static final Vec3d WHITE = new Vec3d(1, 1, 1);
    public static final Vec3d MAELSTROM = new Vec3d(0.3, 0.2, 0.4);


    public static Vec3d variateColor(Vec3d baseColor, float variance) {
        float f = ModRand.getFloat(variance);

        return new Vec3d((float) Math.min(Math.max(0, baseColor.x + f), 1),
                (float) Math.min(Math.max(0, baseColor.y + f), 1),
                (float) Math.min(Math.max(0, baseColor.z + f), 1));
    }

    public static int toIntegerColor(int r, int g, int b, int a) {
        int i = r << 16;
        i += g << 8;
        i += b;
        i += a << 24;
        return i;
    }
}
