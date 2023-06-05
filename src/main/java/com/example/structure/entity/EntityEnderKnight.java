package com.example.structure.entity;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.ai.EntityAITimedAttack;
import com.example.structure.entity.model.ModelEnderKnight;
import com.example.structure.entity.util.IAttack;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class EntityEnderKnight extends EntityModBase implements IAnimatable, IAttack {

    private Vec3d chargeDir;

    private final String ANIM_IDLE = "idle";

    private final String ANIM_WALKING_ARMS = "walkingArms";
    private final String ANIM_WALKING_LEGS = "walkingLegs";
    private final String ANIM_RUNNING_ARMS = "runningArms";
    private final String ANIM_RUNNING_LEGS = "runningLegs";
    private final String ANIM_STRIKE_ONE = "strike1";
    private final String ANIM_DASH = "dash";
    private static final DataParameter<Boolean> FIGHT_MODE = EntityDataManager.createKey(EntityEnderKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STRIKE_ATTACK = EntityDataManager.createKey(EntityEnderKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> RUNNING_CHECK = EntityDataManager.createKey(EntityEnderKnight.class, DataSerializers.BOOLEAN);
     private static final DataParameter<Boolean> DASH_ATTACK = EntityDataManager.createKey(EntityEnderKnight.class, DataSerializers.BOOLEAN);

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
        this.dataManager.register(DASH_ATTACK, Boolean.valueOf(false));
    }
    public void setFightMode(boolean value) {this.dataManager.set(FIGHT_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(FIGHT_MODE);}
    public void setStrikeAttack(boolean value) {this.dataManager.set(STRIKE_ATTACK, Boolean.valueOf(value));}
    public boolean isStrikeAttack() {return this.dataManager.get(STRIKE_ATTACK);}
    public void setRunningCheck(boolean value) {
        this.dataManager.set(RUNNING_CHECK, Boolean.valueOf(value));
    }
    public boolean isRunningCheck(){return this.dataManager.get(RUNNING_CHECK);}
    public void setDashAttack(boolean value) {this.dataManager.set(DASH_ATTACK, Boolean.valueOf(value));}
    public boolean isDashAttack() {return this.dataManager.get(DASH_ATTACK);}

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        animationData.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        animationData.addAnimationController(new AnimationController(this, "fight_controller", 0, this::predicateAttack));
    }

    public int dashMeter = 0;
    @Override
    public void onUpdate() {
        super.onUpdate();
        EntityLivingBase target = this.getAttackTarget();
        //This setups up the timer for dashing without being the main brains of the startAttackMethod
        if(!world.isRemote && target != null && !this.isBeingRidden()) {

            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            double distance = Math.sqrt(distSq);

            if(distance > 3 && distance < 10 && !this.isFightMode() && dashMeter > 140) {
                dashAttack.accept(target);
                dashMeter = 0;
            }
            if(!this.isDashAttack()) {
                dashMeter++;
            }
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(10D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(14.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAITimedAttack<>(this, 1.5, 40, 3F, 0.3f));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));

    }
    private <E extends IAnimatable>PlayState predicateArms(AnimationEvent<E> event) {
       // if(!(event.getLimbSwingAmount() > -0.02F && event.getLimbSwingAmount() < 0.02F && event.getLimbSwingAmount() < -0.40F && event.getLimbSwingAmount() > 0.40F)) {
          //  event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALKING_ARMS, true));
       // }
        if(!(event.getLimbSwingAmount() > -0.40F && event.getLimbSwingAmount() < 0.40F) && !this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_RUNNING_ARMS, true));
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }
    private <E extends IAnimatable>PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() > -0.02F && event.getLimbSwingAmount() < 0.02F && event.getLimbSwingAmount() < -0.40F && event.getLimbSwingAmount() > 0.40F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALKING_LEGS, true));
            return PlayState.CONTINUE;
        }
        if(!(event.getLimbSwingAmount() > -0.40F && event.getLimbSwingAmount() < 0.40F)) {
           event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_RUNNING_LEGS, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {

         if(event.getLimbSwingAmount() > -0.02F && event.getLimbSwingAmount() < 0.02F) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        if(this.isStrikeAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STRIKE_ONE, false));
            return PlayState.CONTINUE;
        }
        if(this.isDashAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DASH, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if(id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.yVec(0.5)), ModColors.MAELSTROM, Vec3d.ZERO);
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.yVec(0.9)), ModColors.MAELSTROM, Vec3d.ZERO);
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.yVec(1.3)), ModColors.MAELSTROM, Vec3d.ZERO);
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.yVec(1.7)), ModColors.MAELSTROM, Vec3d.ZERO);
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.yVec(2.0)), ModColors.MAELSTROM, Vec3d.ZERO);
        }
    }

    //Particle Call
    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if(dashParticles) {

        world.setEntityState(this, ModUtils.PARTICLE_BYTE);

        }
    }

    private Consumer<EntityLivingBase> prevAttack;

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(regularStrike));
            double[] weights = {
                    (distance < 4) ? 1/distance : 0 // Ground Slam
            };
            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }
        return 40;
    }
    private final Consumer<EntityLivingBase> regularStrike = (target) -> {
        this.setFightMode(true);
        this.setStrikeAttack(true);
        addEvent(()-> {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0,1.3,0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = 7.0f;
            ModUtils.handleAreaImpact(1.0f, (e)-> damage, this, offset, source, 0.4f, 0, false);
        }, 10);

        addEvent(()-> {
            this.setFightMode(false);
            this.setStrikeAttack(false);
        }, 15);
    };

    protected boolean dashParticles = false;

    private final Consumer<EntityLivingBase> dashAttack = (target) -> {
      this.setFightMode(true);
      this.setDashAttack(true);

      addEvent(()-> {
          //Used for the Initial Position this will dash too
          Vec3d targetedPos = getAttackTarget().getPositionVector().add(ModUtils.yVec(1));
          Vec3d startPos = this.getPositionVector().add(ModUtils.yVec(getEyeHeight()));
          Vec3d dir = targetedPos.subtract(startPos).normalize();
          AtomicReference<Vec3d> teleportPos = new AtomicReference<>(targetedPos);
          int maxDistance = 5;
        addEvent(()-> {
            dashParticles = true;
            ModUtils.lineCallback(targetedPos.add(dir), targetedPos.add(dir.scale(maxDistance)), maxDistance * 2, (pos , i) -> {
                boolean safeLanding = ModUtils.cubePoints(0, -2, 0, 1, 0, 1).stream()
                        .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                                .isSideSolid(world, new BlockPos(pos.add(off)).down(), EnumFacing.UP));
                boolean notOpen = ModUtils.cubePoints(0, 1, 0, 1, 3, 1).stream()
                        .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                                .causesSuffocation());
                if(safeLanding && !notOpen) {
                    teleportPos.set(pos);
                }

                this.chargeDir = teleportPos.get();
                this.setPositionAndUpdate(chargeDir.x, chargeDir.y, chargeDir.z);

            });
        }, 5);

        addEvent(()-> {
            for(int i = 0; i < 10; i += 2) {
                addEvent(()-> {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5,1.2,0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = 4.0f;
                    ModUtils.handleAreaImpact(2.0f, (e)-> damage, this, offset, source, 0.6f, 0, false);
                }, 5);
            }
        }, 5);
      }, 10);

    addEvent(()-> dashParticles = false, 25);

      addEvent(()-> {

          this.setFightMode(false);
          this.setDashAttack(false);
      }, 40);
    };

}