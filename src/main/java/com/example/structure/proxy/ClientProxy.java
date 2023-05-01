package com.example.structure.proxy;

import com.example.structure.util.handlers.RenderHandler;

public class ClientProxy extends CommonProxy {


    @Override
    public void init() {

        //Registers Geckolib Entities
        RenderHandler.registerGeoEntityRenderers();
        super.init();
    }
}
