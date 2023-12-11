package com.example.structure.renderer;

import com.example.structure.entity.tileentity.TileEntityAltar;
import com.example.structure.model.ModelAltar;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class RenderAltarTile extends GeoBlockRenderer<TileEntityAltar> {
    public RenderAltarTile() {
        super(new ModelAltar());
    }
}
