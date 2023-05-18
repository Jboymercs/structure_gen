package com.example.structure.config;


import com.example.structure.util.ModReference;
import net.minecraftforge.common.config.Config;

@Config(modid = ModReference.MOD_ID, name = ModReference.NAME)
public class ModConfig {
    @Config.Name("End Boss Structure Frequency")
    @Config.Comment("Raises and Lowers Frequency of Structure Spawns, Higher means more frequent")
    @Config.RangeInt(min = 0, max = 10)
    @Config.RequiresMcRestart
    public static int structureFrequency = 4;
}
