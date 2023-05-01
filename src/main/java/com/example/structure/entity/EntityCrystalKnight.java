package com.example.structure.entity;

import com.example.structure.entity.util.IAttack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

/**
 * The Boss of the mod, Here I will test myself in skills to make something immaculate
 */

public class EntityCrystalKnight extends EntityModBase implements IAnimatable, IAttack {
    private final String ANIM_IDLE = "idle";
    private final String ANIM_BLINK = "blink";

    private AnimationFactory factory = new AnimationFactory(this);

    public EntityCrystalKnight(World worldIn) {
        super(worldIn);
        this.experienceValue = 10;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
    animationData.addAnimationController(new AnimationController(this, "animBlink", 0, this::predicateBlink));
    animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    }

    private<E extends IAnimatable>PlayState predicateBlink(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BLINK, true));
        return PlayState.CONTINUE;
    }
    private <E extends IAnimatable>PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        return PlayState.CONTINUE;
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
