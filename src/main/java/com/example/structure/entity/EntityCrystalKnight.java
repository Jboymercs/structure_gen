package com.example.structure.entity;

import com.example.structure.entity.ai.EntityAITimedAttack;
import com.example.structure.entity.util.IAttack;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * The Boss of the mod, Here I will test myself in skills to make something immaculate
 */

public class EntityCrystalKnight extends EntityModBase implements IAnimatable, IAttack {
    private Consumer<EntityLivingBase> prevAttack;
    private final String ANIM_IDLE = "idle";
    private final String ANIM_BLINK = "blink";

    private final String STRIKE_ANIM = "strike";
    private final String CRYSTAL_ANIM = "crystal";
    private final String SPIN_ANIM = "spin";
    private static final DataParameter<Boolean> FIGHT_MODE = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STRIKE_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CRYSTAL_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPIN_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);

    public boolean rangeSwitch;
    public boolean meleeSwitch;

    private AnimationFactory factory = new AnimationFactory(this);

    public EntityCrystalKnight(World worldIn) {
        super(worldIn);
        this.experienceValue = 10;
        this.setSize(0.8f, 2.2f);
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();
        this.setImmovable(false);

    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(FIGHT_MODE, Boolean.valueOf(false));
        this.dataManager.register(STRIKE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(CRYSTAL_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SPIN_ATTACK, Boolean.valueOf(false));

    }

    public void setFightMode(boolean value) {this.dataManager.set(FIGHT_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(FIGHT_MODE);}
    public void setStrikeAttack(boolean value) {this.dataManager.set(STRIKE_ATTACK, Boolean.valueOf(value));}
    public boolean isStrikeAttack() {return this.dataManager.get(STRIKE_ATTACK);}
    public void setCrystalAttack(boolean value){this.dataManager.set(CRYSTAL_ATTACK, Boolean.valueOf(value));}
    public boolean isCrystalAttack() {return this.dataManager.get(CRYSTAL_ATTACK);}
    public void setSpinAttack(boolean value){this.dataManager.set(SPIN_ATTACK,Boolean.valueOf(value));}
    public boolean isSpinAttack() {return this.dataManager.get(SPIN_ATTACK);}

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.34590D);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
    animationData.addAnimationController(new AnimationController(this, "animBlink", 0, this::predicateBlink));
    animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    animationData.addAnimationController(new AnimationController(this, "phaseONE_controller", 0, this::predicateAttack));
    }

    private<E extends IAnimatable>PlayState predicateBlink(AnimationEvent<E> event) {
        //Handles the Eyes, Small movements of the remaining Crystals
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BLINK, true));
        return PlayState.CONTINUE;
    }
    private <E extends IAnimatable>PlayState predicateIdle(AnimationEvent<E> event) {
        //Idle Movements
        if(!this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        }
        return PlayState.CONTINUE;
    }


    private <E extends IAnimatable>PlayState predicateAttack(AnimationEvent<E> event) {
        //Attack Animations
        if(this.isStrikeAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(STRIKE_ANIM, false));
            return PlayState.CONTINUE;
        }
        if(this.isCrystalAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(CRYSTAL_ANIM, false));
            return PlayState.CONTINUE;
        }
        if(this.isSpinAttack()) {

            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAITimedAttack<>(this, 1.0, 20, 14F, 0.4F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }



    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double HealthChange = this.getHealth() / this.getMaxHealth();
        if(!this.isFightMode()) {
            //Begin Attacks REPEATED
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(meleeStrike, summonCrystals));
            double[] weights = {
                    (distance < 3) ? 1/distance : 0,
                    (distance > 3) ? distance * 0.02 : 0

            };
            prevAttack = ModRand.choice(attacks, rand, weights).next();
            System.out.println("Choosing Attack");
            prevAttack.accept(target);

        }

        return 20;
    }

    //Basic Melee Attack
    private final Consumer<EntityLivingBase> meleeStrike = (target) -> {
        this.setFightMode(true);
        this.setStrikeAttack(true);
        addEvent(()-> {
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 1.2, 0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = 7;
            ModUtils.handleAreaImpact(1.0f, (e)-> damage, this, offset, source, 0.4f, 0, false);
        }, 25);
        addEvent(()-> {
            this.setFightMode(false);
            this.setStrikeAttack(false);
        }, 40);
        System.out.println("MeleeAttack");
    };
    //Crystal Ranged Attack
    private final Consumer<EntityLivingBase>summonCrystals = (target) -> {
      this.setFightMode(true);
      this.setCrystalAttack(true);
      addEvent(()-> {
        for(int i = 0; i < 60; i += 5)  {
            addEvent(()-> {
                //Summon Crystals
            }, i);
        }
      }, 40);

      addEvent(()-> this.setFightMode(false), 110);
      addEvent(()-> {this.setCrystalAttack(false);
          System.out.println("Boolean set to false");
      }, 110);
      System.out.println("RangedAttack");
    };
}
