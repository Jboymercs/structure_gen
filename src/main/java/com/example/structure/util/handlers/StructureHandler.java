package com.example.structure.util.handlers;

import com.example.structure.world.Biome.altardungeon.AltarTemplate;
import com.example.structure.world.Biome.altardungeon.MapGenAltarDungeon;
import com.example.structure.world.Biome.bridgestructure.BridgeStructureTemplate;
import com.example.structure.world.Biome.bridgestructure.MapGenBridgeStructure;
import net.minecraft.world.gen.structure.MapGenStructureIO;

public class StructureHandler {

    public static void handleStructureRegistries() {
        MapGenStructureIO.registerStructure(MapGenAltarDungeon.Start.class, "Altar Dungeon");
        MapGenStructureIO.registerStructureComponent(AltarTemplate.class, "ADP");
        MapGenStructureIO.registerStructure(MapGenBridgeStructure.Start.class, "Bridge Ruins");
        MapGenStructureIO.registerStructureComponent(BridgeStructureTemplate.class, "BSP");
    }
}
