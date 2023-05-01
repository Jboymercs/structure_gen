package com.example.structure.entity;

import com.example.structure.entity.util.IAttack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

/**
 * The Boss of the mod, Here I will test myself in skills to make something immaculate
 */

public class EntityCrystalKnight extends EntityModBase implements IAnimatable, IAttack {


    private AnimationFactory factory = new AnimationFactory(this);

    public EntityCrystalKnight(World worldIn) {
        super(worldIn);
        this.experienceValue = 10;
    }

    @Override
    public void registerControllers(AnimationData animationData) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        return 0;
    }
}
