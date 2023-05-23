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

    @Config.Name("Lamentor Boss Health")
    @Config.Comment("Change the Health of the Lamentor")
    @Config.RequiresMcRestart
    public static float health = 300f;

    @Config.Name("Lamentor Speed")
    @Config.Comment("Change the speed at which the Lamentor attacks in seconds, smaller number means quicker, larger number means slower, warning 0 might be buggy")
    @Config.RangeInt(min = 0, max = 10)
    @Config.RequiresMcRestart
    public static int boss_speed = 1;

    @Config.Name("Lamentor Attack Damage")
    @Config.Comment("Change the Attack Damage of the Lamentor")
    @Config.RequiresMcRestart
    public static float attack_damage= 9f;

    @Config.Name("Lamentor Ranged Crystal Damage")
    @Config.Comment("Change the damage of the flying Crystals")
    @Config.RequiresMcRestart
    public static float crystal_damage= 5f;

    @Config.Name("Lamentor Pierce Damage")
    @Config.Comment("Change the multiplier of the Lamentor's Pierce attacks, base damage * pierce multiplier")
    @Config.RequiresMcRestart
    public static float pierce_multiplier = 1.8f;

    @Config.Name("Lamentor Circle Attack Multiplier")
    @Config.Comment("Change the multiplier of the Lamentor's Circle Attack, base damage * circle attack multiplier")
    @Config.RequiresMcRestart
    public static float circle_multiplier = 2.0f;

    @Config.Name("Lamentor Hammer Attack Multiplier")
    @Config.Comment("Change the multiplier of the Lamentors Hammer Attack, base damage * hammer attack multiplier")
    @Config.RequiresMcRestart
    public static float hammer_multiplier = 1.4f;

    @Config.Name("Lamentor Explosion Size")
    @Config.Comment("Change the size of explosion for the Lamentors Hammer Attack")
    @Config.RangeInt(min = 0, max = 3)
    @Config.RequiresMcRestart
    public static float explosion_size = 2;

    @Config.Name("Ground Crystal Damage")
    @Config.Comment("Change the damage done by Ground Crystals")
    @Config.RequiresMcRestart
    public static float ground_crystal_damage= 4f;

}
