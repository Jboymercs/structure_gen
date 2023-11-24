package com.example.structure.entity.endking.EndKingAction;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.IAction;
import com.example.structure.entity.endking.EntityRedCrystal;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class ActionEncirclingAOE implements IAction {
    @Override
    public void performAction(EntityModBase actor, EntityLivingBase target) {
        ModUtils.circleCallback(5, 8, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(target.getPositionVector());
            EntityRedCrystal spike = new EntityRedCrystal(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        });
        target.playSound(SoundEvents.EVOCATION_FANGS_ATTACK, 1.0f, 1.0f);

        actor.addEvent(()-> ModUtils.circleCallback(4, 6, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(target.getPositionVector());
            EntityRedCrystal spike = new EntityRedCrystal(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        }), 20);
        actor.addEvent(()-> target.playSound(SoundEvents.EVOCATION_FANGS_ATTACK, 1.0f, 1.0f), 20);

        actor.addEvent(()-> ModUtils.circleCallback(3, 3, (pos)-> {
            pos = new Vec3d(pos.x, 0, pos.y).add(target.getPositionVector());
            EntityRedCrystal spike = new EntityRedCrystal(actor.world);
            spike.setPosition(pos.x, pos.y, pos.z);
            actor.world.spawnEntity(spike);
        }), 40);
        actor.addEvent(()-> target.playSound(SoundEvents.EVOCATION_FANGS_ATTACK, 1.0f, 1.0f), 40);
    }
}
