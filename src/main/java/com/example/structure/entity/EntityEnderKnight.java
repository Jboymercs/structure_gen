package com.example.structure.entity;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.ai.EntityAITimedAttack;
import com.example.structure.entity.model.ModelEnderKnight;
import com.example.structure.entity.util.IAttack;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EntityEnderKnight extends EntityModBase implements IAnimatable, IAttack {

    private final String ANIM_IDLE = "idle";

    private final String ANIM_WALKING_ARMS = "walkingArms";
    private final String ANIM_WALKING_LEGS = "walkingLegs";
    private final String ANIM_RUNNING_ARMS = "runningArms";
    private final String ANIM_RUNNING_LEGS = "runningLegs";
    private final String ANIM_STRIKE_ONE = "strike1";
    private static final DataParameter<Boolean> FIGHT_MODE = EntityDataManager.createKey(EntityEnderKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STRIKE_ATTACK = EntityDataManager.createKey(EntityEnderKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> RUNNING_CHECK = EntityDataManager.createKey(EntityEnderKnight.class, DataSerializers.BOOLEAN);

    private AnimationFactory factory = new AnimationFactory(this);

    public EntityEnderKnight(World worldIn) {
        super(worldIn);
        this.setSize(0.8f, 2.3f);
    }
    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(FIGHT_MODE, Boolean.valueOf(false));
        this.dataManager.register(STRIKE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(RUNNING_CHECK, Boolean.valueOf(false));
    }
    public void setFightMode(boolean value) {this.dataManager.set(FIGHT_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(FIGHT_MODE);}
    public void setStrikeAttack(boolean value) {this.dataManager.set(STRIKE_ATTACK, Boolean.valueOf(value));}
    public boolean isStrikeAttack() {return this.dataManager.get(STRIKE_ATTACK);}
    public void setRunningCheck(boolean value) {
        this.dataManager.set(RUNNING_CHECK, Boolean.valueOf(value));
    }
    public boolean isRunningCheck(){return this.dataManager.get(RUNNING_CHECK);}

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        animationData.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(10D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(14.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAITimedAttack<>(this, 1.5, 40, 2F, 0.3f));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));

    }
    private <E extends IAnimatable>PlayState predicateArms(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F) && !this.isFightMode() && !(event.getLimbSwingAmount() < -0.80F && event.getLimbSwingAmount() > 0.80F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALKING_ARMS, true));
            return PlayState.CONTINUE;
        }
        if(!(event.getLimbSwingAmount() > -0.80F && event.getLimbSwingAmount() < 0.80F) && !this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_RUNNING_ARMS, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private <E extends IAnimatable>PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F) && !(event.getLimbSwingAmount() < -0.80F && event.getLimbSwingAmount() > 0.80F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALKING_LEGS, true));
            return PlayState.STOP;
        }
        if(!(event.getLimbSwingAmount() > -0.80F && event.getLimbSwingAmount() < 0.80F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_RUNNING_LEGS, true));
            return PlayState.STOP;
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {

         if(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
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
