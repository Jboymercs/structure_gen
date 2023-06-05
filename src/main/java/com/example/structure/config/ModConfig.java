package com.example.structure.config;


import com.example.structure.util.ModReference;
import net.minecraftforge.common.config.Config;

@Config(modid = ModReference.MOD_ID, name = ModReference.NAME)
public class ModConfig {
    @Config.Name("Lamented Islands Structure Frequency")
    @Config.Comment("Raises and Lowers Frequency of Structure Spawns, Higher means more frequent")
    @Config.RangeInt(min = 0, max = 48)
    @Config.RequiresMcRestart
    public static int structureFrequency = 25;

    @Config.Name("Lamentor Boss Health")
    @Config.Comment("Change the Health of the Lamentor")
    @Config.RequiresMcRestart
    public static float health = 300f;

    @Config.Name("Lamentor Attack Speed")
    @Config.Comment("Change the speed at which the Lamentor attacks in seconds, smaller number means quicker, larger number means slower, warning 0 might be buggy")
    @Config.RangeInt(min = 0, max = 10)
    @Config.RequiresMcRestart
    public static int boss_speed = 1;

    @Config.Name("Lamentor Attack Damage")
    @Config.Comment("Change the Attack Damage of the Lamentor")
    @Config.RequiresMcRestart
    public static double attack_damage= 9D;

    @Config.Name("Lamentor Ranged Crystal Damage")
    @Config.Comment("Change the damage of the flying Crystals")
    @Config.RequiresMcRestart
    public static float crystal_damage= 4.0f;

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

    @Config.Name("Constructor Health")
    @Config.Comment("Change the Health of the Constructor")
    @Config.RequiresMcRestart
    public static float constructor_health = 40f;

    @Config.Name("Constructor Attack Speed Value 1")
    @Config.Comment("Constructor Speed #1, this value in seconds, smaller means quicker, larger means slower speed of attacks")
    @Config.RequiresMcRestart
    public static int constructor_speed_one = 2;

    @Config.Name("Constructor Attack Speed Value 2")
    @Config.Comment("Constructor Speed #2, this value in seconds, smaller means quicker, larger means slower speed of attacks. This second value is used for how long after a ranged attack")
    @Config.RequiresMcRestart
    public static int constructor_speed_two = 10;

    @Config.Name("Constructor ShockWave Damage")
    @Config.Comment("damage dealt by the Constructor when doing it's ground shock wave attack as a mutliplier, base damage * shockwave damage")
    @Config.RequiresMcRestart
    public static float constructor_shockwave_damage= 0.5f;

    @Config.Name("Constructor Structure Spawn Chance")
    @Config.Comment("Change the chance of how many Constructors will spawn on the Lamented Islands dungeon")
    @Config.RangeInt(min = 0, max = 1)
    public static float structure_spawns = 0.3f;

    @Config.Name("Constructor Natural Spawn Weight")
    @Config.Comment("Change the weights at which the Constructors naturally spawn in the End, or disable it entirely, lower value means less spawns, higher means more common")
    @Config.RequiresMcRestart
    public static int constructor_weights = 1;

    @Config.Name("Lamented Islands Structure Enabled/Disabled")
    @Config.Comment("Change the value to enable the structure or disable the Lamented Islands spawn, true or false value only")
    @Config.RequiresMcRestart
    public static boolean does_structure_spawn = true;

    @Config.Name("Lamented Sword Damage")
    @Config.Comment("Change the damage done by the Lamented Sword")
    @Config.RequiresMcRestart
    public static float sword_damage = 7.0f;

    @Config.Name("Lamented Sword Dash Damage")
    @Config.Comment("Change the damage done by the dash ability upon initially dashing to nearby entities")
    @Config.RequiresMcRestart
    public static float sword_dash_damage = 4.0f;

    @Config.Name("Lamented Sword Cooldown")
    @Config.Comment("Change the cooldown time for using the dash ability on the Lamented Sword, in seconds")
    @Config.RequiresMcRestart
    public static int sword_cooldown = 3;

    @Config.Name("Lamented Sword Dash Velocity")
    @Config.Comment("Change the Velocity of the player when using the dash ability")
    @Config.RequiresMcRestart
    public static float sword_velocity = 2.4f;

    @Config.Name("Lamented Eye Cooldown")
    @Config.Comment("Change the cooldown Period of the Lamented Eye, in seconds")
    @Config.RequiresMcRestart
    public static int eye_cooldown = 8;

    @Config.Name("Lamentor Legacy texture")
    @Config.Comment("For those that prefer the legacy model and texture of the Lamentor")
    @Config.RequiresMcRestart
    public static boolean lamenter_legacy_texture = false;

    @Config.Name("Constructor Center Island Spawns Enabled/Disabled")
    @Config.Comment("Allow Constructors to spawn in the center island during generation of the Lamented Islands")
    @Config.RequiresMcRestart
    public static boolean constructor_center_spawn = true;

    @Config.Name("Mini Islands Spawn above Lamented Islands Enabled/Disabled")
    @Config.Comment("Allow mini-islands to spawn above the dungeon, this is to make it easier to counteract levitation, if you want it to be harder set it too false")
    @Config.RequiresMcRestart
    public static boolean miniIslands_spawn = true;

    @Config.Name("Mini-Island Chest Spawns Chance")
    @Config.Comment("Change the chance of loot spawns for the mini-islands, higher number means lesser chance")
    @Config.RangeInt(min = 0, max = 5)
    @Config.RequiresMcRestart
    public static int miniIslandLootChance = 3;

    @Config.Name("Lamented Islands Chest Spawn Chance")
    @Config.Comment("Change the chance of loot spawns for the Lamented Islands, higher number means lesser chance")
    @Config.RangeInt(min = 0, max = 5)
    @Config.RequiresMcRestart
    public static int lamentedIslandsLootChance = 2;

    @Config.Name("Scaling Attack Factor For the Lamentor")
    @Config.Comment("This value raises the attack damage of the Lamentor the lower it's health is, to incentivize further punishment in not dodging the bosses moves later in the fight")
    @Config.RequiresMcRestart
    public static double lamentor_scaled_attack = 0.4f;
}
