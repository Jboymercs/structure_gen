package com.example.structure.entity.endking;

import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.Projectile;
import com.example.structure.entity.ai.EntityAITimedAttack;
import com.example.structure.entity.endking.EndKingAction.*;
import com.example.structure.entity.util.IAttack;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
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
import java.util.function.Supplier;

public class EntityEndKing extends EntityAbstractEndKing implements IAnimatable, IAttack {

    private final String ANIM_IDLE_LOWER = "idle_lower";
    private final String ANIM_IDLE_UPPER = "idle_upper";
    private final String ANIM_WALK_LOWER = "walk_lower";
    private final String ANIM_WALK_UPPER = "walk_upper";
    private final String ANIM_SWEEP_LEAP = "sweepLeap";
    private final String ANIM_FIRE_BALL = "fireBall";
    private final String SUMMON_AOE_CRYSTALS = "crystals";



    private Consumer<EntityLivingBase> prevAttack;

    public EntityEndKing(World world) {
        super(world);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "arms_controller", 0, this::predicateArms));
        animationData.addAnimationController(new AnimationController(this, "attack_controller", 0, this::predicateAttacks));
    }



    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isFullBodyUsage()) {

            if(event.isMoving()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_LOWER, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_LOWER, true));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private<E extends IAnimatable> PlayState predicateArms(AnimationEvent<E> event) {
        if(!this.isSwingingArms() && !this.isFullBodyUsage()) {

            if(event.isMoving()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_WALK_UPPER, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE_UPPER, true));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    private <E extends IAnimatable> PlayState predicateAttacks(AnimationEvent<E> event) {
        if(this.isFightMode()) {
            if(this.isLeapSweepAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SWEEP_LEAP, false));
            }
            if(this.isSummonCrystalsAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(SUMMON_AOE_CRYSTALS, false));
            }
            if(this.isSummonFireBallsAttack()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_FIRE_BALL, false));
            }
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAITimedAttack<>(this, 1.0, 60, 24.0f, 0.4f));
    }

    private AnimationFactory factory = new AnimationFactory(this);

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    Supplier<Projectile> projectileSupplierSpinSword = () -> new ProjectileSpinSword(world, this, 6.0f);
    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double HealthChange = this.getHealth() / this.getMaxHealth();
        if(!this.isFightMode()) {
            //attacks
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(sweepLeap, strafeBack, crystalSelfAOE, projectileSwords, throwFireball));
            //weights
            double[] weights = {
                    (distance < 24 && prevAttack != sweepLeap) ? distance * 0.02 : 0, //LeapAttack
                    (distance < 8 && prevAttack != strafeBack) ? 1/distance : 0, //DashRandom
                    (distance < 7 && prevAttack != crystalSelfAOE) ? 1/distance : 0,  //Crystal Self AOE
                    (distance > 5 && !hasSwordsNearby) ? distance * 0.02 : 0, // Projectile Swords Attack
                    (distance > 8 && prevAttack != throwFireball) ? distance * 0.02 : 0 //Throw Fireball Attack
            };
            prevAttack = ModRand.choice(attacks, rand, weights).next();
            prevAttack.accept(target);
        }
        return 60;
    }

    //Leap Attack Quick
    private final Consumer<EntityLivingBase> sweepLeap = (target) -> {
        this.setImmovable(true);
      this.setFightMode(true);
      this.setFullBodyUsage(true);
      this.setLeapSweepAttack(true);
      this.ActionDashForward(target);
      addEvent(()-> this.setImmovable(false), 10);
      addEvent(()-> {

          for(int i = 0; i < 20; i += 5) {
              addEvent(()-> {
                  Vec3d offset = this.getPositionVector().add(ModUtils.yVec(1.5f));
                  DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                  float damage = 8.0f;
                  ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.6f, 0, false);
              }, i);
          }

      }, 15);
      addEvent(()-> {
        this.setFightMode(false);
        this.setFullBodyUsage(false);
        this.setLeapSweepAttack(false);
      }, 30);
    };

    //Random Dash Attack
    private final Consumer<EntityLivingBase> strafeBack = (target) -> {
      this.setFightMode(true);
        this.ActionDashRandom(target);
      addEvent(()-> this.setFightMode(false), 40);

    };

    private final Consumer<EntityLivingBase> strafeForward = (target)-> {
      this.setFightMode(true);
      this.ActionDashForward(target);
      addEvent(()-> this.setFightMode(false), 40);
    };

    //SElf AOE Attack
    private final Consumer<EntityLivingBase> crystalSelfAOE = (target)-> {
      this.setFightMode(true);
      this.setImmovable(true);
      this.setSummonCrystalsAttack(true);

      addEvent(()-> new ActionAOESimple().performAction(this, target), 20);

      addEvent(()-> this.setSummonCrystalsAttack(false), 23);
      addEvent(()-> this.setImmovable(false),23);
      //Random Dash Chance
      if(randomDashChance() >= 6) {
          addEvent(()-> this.ActionDashRandom(target), 23);
          addEvent(()-> this.setFightMode(false), 63);
      } else {
          addEvent(()-> this.setFightMode(false), 23);
      }

    };

    //Projectile Swords Attack
    private final Consumer<EntityLivingBase> projectileSwords = (target) -> {
      this.setFightMode(true);
      new ActionHoldSwordAttack(projectileSupplierSpinSword, 2.0f).performAction(this, target);

      //Random Chance for Dashing
      addEvent(()-> {
          if(randomDashChance() >= 6) {
              this.ActionDashRandom(target);
          }
      }, 40);

      addEvent(()-> this.setFightMode(false), 80);
    };

    Supplier<EntityFireBall> fireBallSupplier = () -> new EntityFireBall(world);
    //Throw Fireball
    private final Consumer<EntityLivingBase> throwFireball = (target)-> {
      this.setFightMode(true);
      this.setSummonFireballsAttack(true);
      this.setFullBodyUsage(true);
      this.setImmovable(true);
    new ActionThrowFireball(fireBallSupplier, 2.5f).performAction(this, target);

        addEvent(()-> this.setImmovable(false), 35);
        addEvent(()-> this.setFullBodyUsage(false), 35);
        addEvent(()-> this.setSummonFireballsAttack(false), 35);
      addEvent(()-> this.setFightMode(false), 80);
    };

    public void ActionDashBack(EntityLivingBase target) {
            //Random Dash Distance
            int randomDeterminedDistance = ModRand.range(4, 6);
            Vec3d currentPos = this.getPositionVector().add(ModUtils.yVec(getEyeHeight()));
            Vec3d enemyPos = target.getPositionVector().add(ModUtils.yVec(1));
            Vec3d predictedPos = currentPos.subtract(enemyPos).normalize();
            Vec3d fakePoint = new Vec3d(predictedPos.x + 8, predictedPos.y, predictedPos.z + 8);


            AtomicReference<Vec3d> teleportPos = new AtomicReference<>(currentPos);

            ModUtils.lineCallback(currentPos.add(predictedPos),currentPos.scale(randomDeterminedDistance), randomDeterminedDistance * 2, (pos, r) -> {
                if(this.damageViable) {
                    Vec3d offset = this.getPositionVector().add(ModUtils.yVec(1.5));
                    DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                    float damage = this.getAttack();
                    ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.6f, 2, false);
                }
                boolean safeLanding = ModUtils.cubePoints(0, -2, 0, 1, 0, 1).stream()
                        .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                                .isSideSolid(world, new BlockPos(pos.add(off)).down(), EnumFacing.UP));
                boolean notOpen = ModUtils.cubePoints(0, 1, 0, 1, 3, 1).stream()
                        .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                                .causesSuffocation());

                if (safeLanding && !notOpen) {
                    teleportPos.set(pos);
                }
            });

        this.chargeDir = teleportPos.get();
        this.setPositionAndUpdate(chargeDir.x, chargeDir.y, chargeDir.z);
    }

    public void ActionDashForward(EntityLivingBase target) {
        setPhaseMode(true);
        addEvent(()-> setPhaseMode(false), 5);
        addEvent(()-> setPhaseMode(true), 10);
        addEvent(()-> {
            this.playSound(ModSoundHandler.KING_DASH, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
            int randomDeterminedDistance = ModRand.range(4, 6);
            Vec3d enemyPos = target.getPositionVector().add(ModUtils.yVec(1));

            Vec3d startPos = this.getPositionVector().add(ModUtils.yVec(getEyeHeight()));

            Vec3d dir = enemyPos.subtract(startPos).normalize();

            AtomicReference<Vec3d> teleportPos = new AtomicReference<>(enemyPos);

            ModUtils.lineCallback(enemyPos.add(dir),enemyPos.scale(randomDeterminedDistance), randomDeterminedDistance * 2, (pos, r) -> {

                boolean safeLanding = ModUtils.cubePoints(0, -2, 0, 1, 0, 1).stream()
                        .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                                .isSideSolid(world, new BlockPos(pos.add(off)).down(), EnumFacing.UP));
                boolean notOpen = ModUtils.cubePoints(0, 1, 0, 1, 3, 1).stream()
                        .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                                .causesSuffocation());

                if (safeLanding && !notOpen) {
                    teleportPos.set(pos);
                }
            });
            this.chargeDir = teleportPos.get();
            this.setPositionAndUpdate(chargeDir.x, chargeDir.y, chargeDir.z);

        }, 15);
        addEvent(()-> setPhaseMode(false), 25);
        addEvent(()-> {setPhaseMode(true);
                    this.damageViable = false;
            }   , 30);
        addEvent(()-> setPhaseMode(false), 35);
    }


    //RandomDashGenerator
    public void ActionDashRandom(EntityLivingBase target) {
        setPhaseMode(true);
        addEvent(()-> setPhaseMode(false), 5);
        addEvent(()-> setPhaseMode(true), 10);
        addEvent(()-> {
            this.playSound(ModSoundHandler.KING_DASH, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
            int randomDeterminedDistance = ModRand.range(4, 6);
            Vec3d startPos = this.getPositionVector().add(ModUtils.yVec(1));
            Vec3d randomPos = new Vec3d(startPos.x + randomDashDirGenerator(), startPos.y, startPos.z + randomDeterminedDistance);
            Vec3d dir = randomPos.subtract(startPos).normalize();
            AtomicReference<Vec3d> teleportPos = new AtomicReference<>(randomPos);
            ModUtils.lineCallback(randomPos.add(dir),randomPos.scale(randomDeterminedDistance), randomDeterminedDistance * 2, (pos, r) -> {
                boolean safeLanding = ModUtils.cubePoints(0, -2, 0, 1, 0, 1).stream()
                        .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                                .isSideSolid(world, new BlockPos(pos.add(off)).down(), EnumFacing.UP));
                boolean notOpen = ModUtils.cubePoints(0, 1, 0, 1, 3, 1).stream()
                        .anyMatch(off -> world.getBlockState(new BlockPos(pos.add(off)))
                                .causesSuffocation());

                if (safeLanding && !notOpen) {
                    teleportPos.set(pos);
                }
            });
            this.chargeDir = teleportPos.get();
            this.setPositionAndUpdate(chargeDir.x, chargeDir.y, chargeDir.z);
        }, 15);
        addEvent(()-> setPhaseMode(false), 35);
        addEvent(()-> setPhaseMode(true), 40);
        addEvent(()-> setPhaseMode(false), 45);
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }
    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }
}
