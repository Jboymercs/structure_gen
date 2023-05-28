package com.example.structure.event_handler;

import com.example.structure.entity.EntityModBase;
import com.example.structure.items.tools.ISweepAttackOverride;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityEventHandler
{
    @SubscribeEvent
    public static void onAttackEntity(LivingAttackEvent event) {
        boolean isTargetable = !EntityModBase.CAN_TARGET.apply(event.getEntityLiving());

        if(isTargetable) {
            event.setCanceled(true);
        }
    }




}
