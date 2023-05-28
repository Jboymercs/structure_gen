package com.example.structure.entity;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.ai.ActionGolemQuake;
import com.example.structure.entity.ai.EntityAITimedAttack;
import com.example.structure.entity.util.IAttack;
import com.example.structure.entity.util.IPitch;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModReference;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
 * The New Base Mob of the End, can be found rarely in the wild and as well in the dungeons
 */

public class EntityBuffker extends EntityModBase implements IAnimatable, IAttack, IEntityMultiPart, IPitch {

    private final String IDLE_ANIM = "idle";
    private final String MOVING_ARMS_ANIM = "movingArms";
    private final String MOVING_LEGS_ANIM = "movingLegs";
    private final String BLINK_ANIM = "peak";
    private final String SHOOT_ANIM = "shoot";
    private final String SHOCKWAVE_ANIM = "shockWave";

    private Consumer<EntityLivingBase> prevAttack;

    private static final DataParameter<Boolean> FIGHT_MODE = EntityDataManager.createKey(EntityBuffker.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BLINK_MODE = EntityDataManager.createKey(EntityBuffker.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHOOT_ATTACK = EntityDataManager.createKey(EntityBuffker.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHOCKWAVE_ATTACK = EntityDataManager.createKey(EntityBuffker.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityBuffker.class, DataSerializers.FLOAT);

    private final MultiPartEntityPart model = new MultiPartEntityPart(this, "model", 0f, 0f);
    private final MultiPartEntityPart head = new MultiPartEntityPart(this, "head", 1.0f, 1.0f);

    private final MultiPartEntityPart torso = new MultiPartEntityPart(this, "torso", 1.3f, 1.2f);
    private AnimationFactory factory = new AnimationFactory(this);
    private final MultiPartEntityPart[] hitboxParts;

    public EntityBuffker(World worldIn) {
        super(worldIn);
        ACCEPT_TARGET = true;
        this.hitboxParts = new MultiPartEntityPart[]{model, head, torso};
        this.setSize(2.0f, 2.8f);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }


    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(FIGHT_MODE, Boolean.valueOf(false));
        this.dataManager.register(BLINK_MODE, Boolean.valueOf(false));
        this.dataManager.register(SHOOT_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SHOCKWAVE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(LOOK, 0f);


    }

    private void setHitBoxPos(Entity entity, Vec3d offset) {
        Vec3d lookVel = ModUtils.getLookVec(this.getPitch(), this.renderYawOffset);
        Vec3d center = this.getPositionVector().add(ModUtils.yVec(1.2));

        Vec3d position = center.subtract(ModUtils.Y_AXIS.add(ModUtils.getAxisOffset(lookVel, offset)));
        ModUtils.setEntityPosition(entity, position);

    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        Vec3d[] avec3d = new Vec3d[this.hitboxParts.length];
        for (int j = 0; j < this.hitboxParts.length; ++j) {
            avec3d[j] = new Vec3d(this.hitboxParts[j].posX, this.hitboxParts[j].posY, this.hitboxParts[j].posZ);
        }
        this.setHitBoxPos(torso, new Vec3d(0, 0.1, 0));
        this.setHitBoxPos(head, new Vec3d(0, 1.3, 0 ));

        Vec3d knightPos = this.getPositionVector();
        ModUtils.setEntityPosition(model, knightPos);

        for (int l = 0; l < this.hitboxParts.length; ++l) {
            this.hitboxParts[l].prevPosX = avec3d[l].x;
            this.hitboxParts[l].prevPosY = avec3d[l].y;
            this.hitboxParts[l].prevPosZ = avec3d[l].z;
        }
    }

    public void setFightMode(boolean value) {this.dataManager.set(FIGHT_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(FIGHT_MODE);}
    public void setBlinkMode(boolean value) {this.dataManager.set(BLINK_MODE, Boolean.valueOf(value));}
    public boolean isBlinkMode() {return this.dataManager.get(BLINK_MODE);}
    public void setShootAttack(boolean value) {this.dataManager.set(SHOOT_ATTACK, Boolean.valueOf(value));}
    public boolean isShootAttack() {return this.dataManager.get(SHOOT_ATTACK);}
    public void setShockwaveAttack(boolean value) {this.dataManager.set(SHOCKWAVE_ATTACK, Boolean.valueOf(value));}
    public boolean isShockWaveAttack() {return this.dataManager.get(SHOCKWAVE_ATTACK);}




    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ModConfig.constructor_health);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.8D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8.0D);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(4, new EntityAITimedAttack<>(this, 1.0, constructor_cooldown_one, 10F, 0.3f));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityCrystalKnight>(this, EntityCrystalKnight.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));

    }
    public static int constructor_cooldown_one = ModConfig.constructor_speed_one * 20;
    public static int constructor_cooldown_two = ModConfig.constructor_speed_two * 20;

    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        if(!this.isFightMode()) {
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(mortarFire, groundSlam));
            double[] weights = {
                    (distance > 7) ? distance * 0.02 : 0, // Mortar Attack
                    (distance < 7) ? 1/distance : 0 // Ground Slam

            };

            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);
        }
        return (prevAttack == mortarFire) ? constructor_cooldown_two : constructor_cooldown_one;
    }

    private final Consumer<EntityLivingBase> mortarFire = (target) -> {
        this.setFightMode(true);
        this.setShootAttack(true);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_SHULKER_OPEN, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f), 5);
        addEvent(()-> {
            //Left Hand
        for(int i = 0; i < 20; i += 10) {
            addEvent(()-> {
                this.playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 1.0f, 0.8f);
                EntityShulkerBullet projectile = new EntityShulkerBullet(this.world, this, target, EnumFacing.Axis.Y);
                Vec3d startPosition = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.6, 0.8)));
                Vec3d velocity = target.getPositionVector().subtract(startPosition).normalize().scale(0.5);
                ModUtils.setEntityVelocity(projectile, velocity);
                projectile.setPosition(startPosition.x, startPosition.y, startPosition.z);
                this.world.spawnEntity(projectile);


            }, i);
        }
        }, 20);
        addEvent(()-> {
            //Right Hand
            for(int i = 0; i < 20; i += 10) {
                addEvent(()-> {
                    this.playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 1.0f, 0.8f);
                    EntityShulkerBullet projectile = new EntityShulkerBullet(this.world, this, target, EnumFacing.Axis.Y);
                    Vec3d startPosition = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1, 1.6, -0.8)));
                    Vec3d velocity = target.getPositionVector().subtract(startPosition).normalize().scale(0.5);
                    ModUtils.setEntityVelocity(projectile, velocity);
                    projectile.setPosition(startPosition.x, startPosition.y, startPosition.z);
                    this.world.spawnEntity(projectile);
                }, i);
            }
        }, 25);

        addEvent(()-> this.playSound(SoundEvents.ENTITY_SHULKER_CLOSE, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f), 65);
        addEvent(()-> {
        this.setFightMode(false);
        this.setShootAttack(false);
        }, 70);
    };

    private final Consumer<EntityLivingBase> groundSlam = (target) -> {
    this.setFightMode(true);
    this.setShockwaveAttack(true);
    this.setImmovable(true);
    //Attack Status
        addEvent(()-> this.playSound(SoundEvents.ENTITY_SHULKER_OPEN, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f), 3);
    addEvent(()-> {
        new ActionGolemQuake().performAction(this, target);
        this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.7f, 1.0f / rand.nextFloat() * 0.4f + 0.4f);
    }, 25);
        addEvent(()-> this.playSound(SoundEvents.ENTITY_SHULKER_CLOSE, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f), 46);
    addEvent(()-> {
        this.setImmovable(false);
    this.setShockwaveAttack(false);
    this.setFightMode(false);
    }, 50);
    };

    public int blinkCoolDown = 0;

    @Override
    public void onUpdate() {
        super.onUpdate();
        EntityLivingBase target = this.getAttackTarget();
        //Random Peak Animation that plays
        if(rand.nextInt(5) == 0 && blinkCoolDown > 120 && target == null) {

            this.setBlinkMode(true);
            addEvent(()-> this.playSound(SoundEvents.ENTITY_SHULKER_OPEN, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f), 10);
            addEvent(()-> this.playSound(SoundEvents.ENTITY_SHULKER_CLOSE, 1.0f, 1.0f / rand.nextFloat() * 0.4f + 0.4f), 25);
            addEvent(()-> this.setBlinkMode(false), 30);
            blinkCoolDown = 0;
        } else {
            blinkCoolDown++;
        }

    }

    @Override
    public boolean getCanSpawnHere() {
            return super.getCanSpawnHere();
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "animPeak", 0, this::predicatePeak));
        animationData.addAnimationController(new AnimationController(this, "moveIdle", 0, this::predicateIdle));
        animationData.addAnimationController(new AnimationController(this, "armsMover", 0, this::predicateArms));
        animationData.addAnimationController(new AnimationController(this, "attackHandler", 0, this::predicateAttack));
    }
    //The Peak animation shulkers do
    private<E extends IAnimatable> PlayState predicatePeak(AnimationEvent<E> event) {
        if(this.isBlinkMode() && !this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(BLINK_ANIM, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    //Handles movement of the Legs and Idle Animation
    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        if(!this.isShockWaveAttack()) {
            if (event.isMoving()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(MOVING_LEGS_ANIM, true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(IDLE_ANIM, true));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    //Handles specifically movement of the Arms when moving and not attacking
    private <E extends IAnimatable>PlayState predicateArms(AnimationEvent<E> event) {
        if(event.isMoving() && !this.isFightMode()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(MOVING_ARMS_ANIM, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState predicateAttack(AnimationEvent<E> event) {
        if (this.isShootAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(SHOOT_ANIM, false));
            return PlayState.CONTINUE;
        }
        if(this.isShockWaveAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(SHOCKWAVE_ANIM, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private static final ResourceLocation LOOT = new ResourceLocation(ModReference.MOD_ID, "constructor");

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT;
    }
    public void setPosition(BlockPos pos) {
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
    }
    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }


    @Override
    public World getWorld() {
        return null;
    }

    public boolean damageConstructor;

    @Override
    public boolean attackEntityFromPart(MultiPartEntityPart multiPartEntityPart, DamageSource damageSource, float damage) {
        if(multiPartEntityPart == this.head  && this.isFightMode()) {
            this.damageConstructor = true;
            return this.attackEntityFrom(damageSource, damage);

        }
        if (damage > 0.0F && !damageSource.isUnblockable()) {
        if (!damageSource.isProjectile()) {
            Entity entity = damageSource.getImmediateSource();

            if (entity instanceof EntityLivingBase) {
                this.blockUsingShield((EntityLivingBase) entity);
            }

             }
            this.playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED, 1.0f, 0.6f + ModRand.getFloat(0.2f));
            return false;
        }
        return false;
    }



    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        if(!this.damageConstructor && !source.isUnblockable()) {
            return false;

        }
        this.damageConstructor = false;
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public Entity[] getParts() {
        return this.hitboxParts;
    }

    public void facePosition(BlockPos pos, float maxPitchIncrease, float maxYawIncrease) {
        double d0 = posX - this.posX;
        double d1 = posZ - this.posZ;

        double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1);
        float f = (float)(MathHelper.atan2(d1, d0) * (180D / Math.PI)) - 90.0F;
        float f1 = (float)(-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
        this.rotationPitch = this.updateRotations(this.rotationPitch, f1, maxPitchIncrease);
        this.rotationYaw = this.updateRotations(this.rotationYaw, f, maxYawIncrease);
    }

    private float updateRotations(float angle, float targetAngle, float maxIncrease)
    {
        float f = MathHelper.wrapDegrees(targetAngle - angle);

        if (f > maxIncrease)
        {
            f = maxIncrease;
        }

        if (f < -maxIncrease)
        {
            f = -maxIncrease;
        }

        return angle + f;
    }

    @Override
    public void setPitch(Vec3d look) {
        float prevLook = this.getPitch();
        float newLook = (float) ModUtils.toPitch(look);
        float deltaLook = 5;
        float clampedLook = MathHelper.clamp(newLook, prevLook - deltaLook, prevLook + deltaLook);
        this.dataManager.set(LOOK, clampedLook);
    }

    @Override
    public float getPitch() {
        return this.dataManager == null ? 0 : this.dataManager.get(LOOK);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SHULKER_AMBIENT;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_SHULKER_HURT;
    }
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SHULKER_DEATH;
    }
}
