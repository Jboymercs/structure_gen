package com.example.structure.entity;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.ai.EntityAITimedAttack;
import com.example.structure.entity.ai.EntityAITimedKnight;
import com.example.structure.entity.knighthouse.EntityKnightBase;
import com.example.structure.entity.knighthouse.EntityKnightLord;
import com.example.structure.entity.model.ModelEnderKnight;
import com.example.structure.entity.util.IAttack;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
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

public class EntityEnderKnight extends EntityKnightBase implements IAnimatable, IAttack {

    private Vec3d chargeDir;

    public boolean isRandomGetAway = false;
    private final String ANIM_IDLE = "idle";

    private final String ANIM_WALKING_ARMS = "walk_upper";
    private final String ANIM_WALKING_LEGS = "walk_lower";

    private final String ANIM_STRIKE_ONE = "swing_upper";
    private final String ANIM_STRIKE_TWO = "swing_upper_two";

    private final String ANIM_STRIKE_THREE = "swing_upper_three";
    private final String ANIM_DASH = "dash";
    private final String ANIM_DEATH = "death";
    private final String ANIM_INTERACT_ONE = "interact_1";
    private final String ANIM_INTERACT_TWO = "interact_2";


    private static final DataParameter<Boolean> STRIKE_ATTACK = EntityDataManager.createKey(EntityEnderKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> RUNNING_CHECK = EntityDataManager.createKey(EntityEnderKnight.class, DataSerializers.BOOLEAN);
     private static final DataParameter<Boolean> DASH_ATTACK = EntityDataManager.createKey(EntityEnderKnight.class, DataSerializers.BOOLEAN);

     private static final DataParameter<Boolean> SECOND_STRIKE = EntityDataManager.createKey(EntityEnderKnight.class, DataSerializers.BOOLEAN);

     private static final DataParameter<Boolean> THIRD_STRIKE = EntityDataManager.createKey(EntityEnderKnight.class, DataSerializers.BOOLEAN);
     private static final DataParameter<Boolean> DEATH_KNIGHT = EntityDataManager.createKey(EntityEnderKnight.class, DataSerializers.BOOLEAN);

    private AnimationFactory factory = new AnimationFactory(this);

    public EntityEnderKnight(World worldIn) {
        super(worldIn);
        this.setSize(0.8f, 2.0f);
    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(STRIKE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(RUNNING_CHECK, Boolean.valueOf(false));
        this.dataManager.register(DASH_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SECOND_STRIKE, Boolean.valueOf(false));
        this.dataManager.register(THIRD_STRIKE, Boolean.valueOf(false));
        this.dataManager.register(DEATH_KNIGHT, Boolean.valueOf(false));
    }

    public void setStrikeAttack(boolean value) {this.dataManager.set(STRIKE_ATTACK, Boolean.valueOf(value));}
    public boolean isStrikeAttack() {return this.dataManager.get(STRIKE_ATTACK);}
    public void setRunningCheck(boolean value) {
        this.dataManager.set(RUNNING_CHECK, Boolean.valueOf(value));
    }
    public boolean isRunningCheck(){return this.dataManager.get(RUNNING_CHECK);}
    public void setDashAttack(boolean value) {this.dataManager.set(DASH_ATTACK, Boolean.valueOf(value));}
    public boolean isDashAttack() {return this.dataManager.get(DASH_ATTACK);}

    public void setSecondStrike(boolean value) {this.dataManager.set(SECOND_STRIKE, Boolean.valueOf(value));}
    public boolean isSecondStrike() {return this.dataManager.get(SECOND_STRIKE);}

    public void setThirdStrike(boolean value) {this.dataManager.set(THIRD_STRIKE, Boolean.valueOf(value));}
    public boolean isThirdStrike() {return this.dataManager.get(THIRD_STRIKE);}
    public void setDeathKnight(boolean value) {this.dataManager.set(DEATH_KNIGHT, Boolean.valueOf(value));}
    public boolean isDeathKnight() {return this.dataManager.get(DEATH_KNIGHT);}

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        animationData.addAnimationController(new AnimationController(this, "legs_controller", 0, this::predicateLegs));
        animationData.addAnimationController(new AnimationController(this, "fight_controller", 0, this::predicateAttack));
        animationData.addAnimationController(new AnimationController(this, "death_controller", 0, this::predicateDeath));

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

            if(distance > 3 && distance < 10 && !this.isFightMode() && dashMeter > 140 && !this.isRandomGetAway && !this.isDeathKnight()) {
                dashAttack.accept(target);
                dashMeter = 0;
            }
            if(!this.isDashAttack()) {
                dashMeter++;
            }

            if(this.isRandomGetAway) {
                double d0 = (this.posX - target.posX) * 0.035;
                double d1 = (this.posY - target.posY) * 0.01;
                double d2 = (this.posZ - target.posZ) * 0.035;
                this.addVelocity(d0, d1, d2);
            }


        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(35D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(14.0D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAITimedKnight<>(this, 1.5, 10, 3F, 0.2f));
    }

    private<E extends IAnimatable> PlayState predicateDeath(AnimationEvent<E> event) {
        if(this.isDeathKnight()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DEATH, false));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private <E extends IAnimatable>PlayState predicateArms(AnimationEvent<E> event) {
    if(!this.isFightMode() && !this.isDeathKnight()) {
        if (!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALKING_ARMS, true));
            return PlayState.CONTINUE;
        }
    }
        return PlayState.STOP;
    }
    private <E extends IAnimatable>PlayState predicateLegs(AnimationEvent<E> event) {
        if(!(event.getLimbSwingAmount() > -0.10F && event.getLimbSwingAmount() < 0.10F) && !this.isDeathKnight()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALKING_LEGS, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isFightMode() && !this.isDeathKnight()) {
            if (event.getLimbSwingAmount() > -0.09F && event.getLimbSwingAmount() < 0.09F) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }



    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        if(!this.isDeathKnight()) {
            if (this.isStrikeAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STRIKE_ONE, false));
                return PlayState.CONTINUE;
            }
            if (this.isSecondStrike()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STRIKE_TWO, false));
                return PlayState.CONTINUE;
            }
            if (this.isDashAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_DASH, false));
                return PlayState.CONTINUE;
            }
            if (this.isThirdStrike()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_STRIKE_THREE, false));
                return PlayState.CONTINUE;
            }
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
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.yVec(0.5)), ModColors.RED, Vec3d.ZERO);
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.yVec(0.9)), ModColors.RED, Vec3d.ZERO);
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.yVec(1.3)), ModColors.RED, Vec3d.ZERO);
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.yVec(1.7)), ModColors.RED, Vec3d.ZERO);
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.yVec(2.0)), ModColors.RED, Vec3d.ZERO);
        }

        if(id == ModUtils.SECOND_PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.5, 0))), ModColors.RED, new Vec3d(ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1));
        }
    }

    //Particle Call
    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if(dashParticles) {
        world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }

        if(this.isDeathKnight() || this.isMarkedForUnholy()) {
            if(rand.nextInt(2) == 0) {
                world.setEntityState(this, ModUtils.SECOND_PARTICLE_BYTE);
            }
        }
    }

    private Consumer<EntityLivingBase> prevAttack;

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode() && !this.isDeathKnight()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(regularStrike, strikeTwo, strikeThree, randomGetBack));
            double[] weights = {
                    (distance < 4 && prevAttack != regularStrike) ? 1/distance : 0, // Main Strike
                    (distance < 4 && prevAttack != strikeTwo) ? 1/distance : 0, //Alt STrike
                    (distance < 4 && prevAttack != strikeThree) ? 1/distance : 0, //Alt STrike 2
                    (distance < 4 && prevAttack != randomGetBack) ? 1/distance : 0
            };
            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }
        return 10;
    }

    private final Consumer<EntityLivingBase> randomGetBack = (target) -> {
      this.setFightMode(true);
      this.isRandomGetAway = true;
      addEvent(()-> {
          this.setFightMode(false);
          this.isRandomGetAway = false;
      }, 25);
    };
    private final Consumer<EntityLivingBase> strikeThree = (target) -> {
        this.setFightMode(true);
    this.setThirdStrike(true);
        addEvent(()-> {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5,1.3,0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
            float damage = 5.0f;
            ModUtils.handleAreaImpact(1.0f, (e)-> damage, this, offset, source, 0.4f, 0, false);
        }, 12);

        addEvent(()-> {
            this.setFightMode(false);
            this.setThirdStrike(false);
        }, 21);
    };
    private final Consumer<EntityLivingBase> strikeTwo = (target) -> {
    this.setFightMode(true);
    this.setSecondStrike(true);
        addEvent(()-> {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0,1.3,0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = 7.0f;
            ModUtils.handleAreaImpact(1.0f, (e)-> damage, this, offset, source, 0.4f, 0, false);
        }, 10);

        addEvent(()-> {
            this.setFightMode(false);
            this.setSecondStrike(false);
        }, 21);
    };

    private final Consumer<EntityLivingBase> regularStrike = (target) -> {
        this.setFightMode(true);
        this.setStrikeAttack(true);
        addEvent(()-> {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0,1.3,0)));
            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
            float damage = 7.0f;
            ModUtils.handleAreaImpact(1.0f, (e)-> damage, this, offset, source, 0.4f, 0, false);
        }, 12);

        addEvent(()-> {
            this.setFightMode(false);
            this.setStrikeAttack(false);
        }, 21);
    };

    protected boolean dashParticles = false;

    private final Consumer<EntityLivingBase> dashAttack = (target) -> {
      this.setFightMode(true);
      this.setDashAttack(true);

      addEvent(()-> {
          //Used for the Initial Position this will dash too
          Vec3d targetedPos = target.getPositionVector().add(ModUtils.yVec(1));
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
        }, 10);

        addEvent(()-> {
            for(int i = 0; i < 10; i += 2) {
                addEvent(()-> {
                    Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.5,1.2,0)));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = 4.0f;
                    ModUtils.handleAreaImpact(2.0f, (e)-> damage, this, offset, source, 0.6f, 0, false);
                }, i);
            }
        }, 5);
      }, 0);

    addEvent(()-> dashParticles = false, 22);

      addEvent(()-> {

          this.setFightMode(false);
          this.setDashAttack(false);
      }, 22);
    };

    @Override
    public void onDeath(DamageSource cause) {
        this.setHealth(0.0001f);
        if(world.rand.nextInt(6) == 0 || this.isMarkedForUnholy()) {
            this.setDeathKnight(true);
            this.setImmovable(true);
        }
        if(this.isDeathKnight()) {
            addEvent(()-> {
                this.setImmovable(false);
                this.setDeathKnight(false);
                this.setDead();
                if(!world.isRemote) {
                    EntityKnightLord lord = new EntityKnightLord(this.world);
                    lord.copyLocationAndAnglesFrom(this);
                    this.world.spawnEntity(lord);
                }
            }, 50);

        } else {
            this.setDead();
        }
        super.onDeath(cause);
    }

}
