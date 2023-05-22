package com.example.structure.entity;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.ai.*;
import com.example.structure.entity.util.IAttack;
import com.example.structure.entity.util.TimedAttackIniator;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.advancements.critereon.LevitationTrigger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * The Boss of the mod, Here I will test myself in skills to make something immaculate
 */

public class EntityCrystalKnight extends EntityModBase implements IAnimatable, IAttack {

    private final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.NOTCHED_6));
    private Consumer<EntityLivingBase> prevAttack;
    private final String ANIM_IDLE = "idle";
    private final String ANIM_BLINK = "blink";

    private final String STRIKE_ANIM = "strike";
    private final String CRYSTAL_ANIM = "crystal";
    private final String PIERCE_DASH_ANIM = "pierce";
    private final String SPIN_BEGIN_ANIM = "spinBegin";
    private final String SPIN_ANIM = "spin";
    private final String SPIN_ATTACK_ANIM = "spinAttack";
    private final String GROUND_CRYSTALS_ANIM = "groundCrystals";

    private final String HAMMER_BEGIN_ANIM = "hammerBegin";
    private final String HAMMER_TRAVEL_ANIM = "hammerTravel";
    private final String HAMMER_ATTACK_ANIM = "hammerAttack";
    private final String MULTI_ATTACK_ANIM = "multiple";
    private final String SUMMON_SHULKERS_ANIM = "shulker";
    private final String RANGED_HAMMER_ANIM = "hammerProjectile";
    private final String SUMMON_BOSS_ANIM = "summon";
    private final String DEATH_BOSS_ANIM = "death";
    private static final DataParameter<Boolean> FIGHT_MODE = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> STRIKE_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CRYSTAL_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPIN_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPIN_START = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SPIN_CYCLE = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> PIERCE_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAMMER_START = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAMMER_CYCLE = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAMMER_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> MULTI_PIERCE_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SHULKER_ATTACK = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAMMER_PROJECTILE = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SUMMON_BOOLEAN = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DEATH_BOOLEAN = EntityDataManager.createKey(EntityCrystalKnight.class, DataSerializers.BOOLEAN);

    public boolean rangeSwitch;
    public boolean meleeSwitch;

    public boolean targetFloating = false;

    public boolean startFlameSpawns = false;

    public int SwitchCoolDown;

    private AnimationFactory factory = new AnimationFactory(this);

    public EntityCrystalKnight(World worldIn) {
        super(worldIn);
        this.setImmovable(true);
        this.experienceValue = 10;
        this.setSize(0.8f, 2.2f);
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();

        this.meleeSwitch = true;
        this.moveHelper = new EntityFlyMoveHelper(this);
        this.navigator = new PathNavigateFlying(this, worldIn);
        if(!world.isRemote) {
            initBossAI();
        }

        this.setAlive(true);
        if(this.isSummoned()) {
            addEvent(()-> this.playSound(ModSoundHandler.BOSS_SUMMON, 1.5f, 1.0f), 50);
            addEvent(()-> this.setAlive(false), 70);
        }

    }

    @Override
    public void entityInit() {
        super.entityInit();
        this.dataManager.register(FIGHT_MODE, Boolean.valueOf(false));
        this.dataManager.register(STRIKE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(CRYSTAL_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SPIN_START, Boolean.valueOf(false));
        this.dataManager.register(SPIN_CYCLE, Boolean.valueOf(false));
        this.dataManager.register(SPIN_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(PIERCE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(HAMMER_PROJECTILE, Boolean.valueOf(false));
        //Phase Two
        this.dataManager.register(HAMMER_START, Boolean.valueOf(false));
        this.dataManager.register(HAMMER_CYCLE, Boolean.valueOf(false));
        this.dataManager.register(HAMMER_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(MULTI_PIERCE_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SHULKER_ATTACK, Boolean.valueOf(false));
        //States
        this.dataManager.register(DEATH_BOOLEAN, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_BOOLEAN, Boolean.valueOf(false));


    }


    public void setFightMode(boolean value) {this.dataManager.set(FIGHT_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(FIGHT_MODE);}
    public void setStrikeAttack(boolean value) {this.dataManager.set(STRIKE_ATTACK, Boolean.valueOf(value));}
    public boolean isStrikeAttack() {return this.dataManager.get(STRIKE_ATTACK);}
    public void setCrystalAttack(boolean value){this.dataManager.set(CRYSTAL_ATTACK, Boolean.valueOf(value));}
    public boolean isCrystalAttack() {return this.dataManager.get(CRYSTAL_ATTACK);}
    public void setSpinAttack(boolean value){this.dataManager.set(SPIN_ATTACK,Boolean.valueOf(value));}
    public boolean isSpinAttack() {return this.dataManager.get(SPIN_ATTACK);}
    public void setSpinStart(boolean value) {
        this.dataManager.set(SPIN_START, Boolean.valueOf(value));
    }
    public boolean isSpinStart() {
        return this.dataManager.get(SPIN_START);
    }
    public void setSpinCycle(boolean value) {
        this.dataManager.set(SPIN_CYCLE, Boolean.valueOf(value));
    }
    public boolean isSpinCycle() {
        return this.dataManager.get(SPIN_CYCLE);
    }
    public void setPierceAttack(boolean value) {
        this.dataManager.set(PIERCE_ATTACK, Boolean.valueOf(value));
    }
    public boolean isPierceAttack() {
        return this.dataManager.get(PIERCE_ATTACK);
    }
    public void setSummonAttack(boolean value) {
        this.dataManager.set(SUMMON_ATTACK, Boolean.valueOf(value));
    }
    public boolean isSummonAttack() {
        return this.dataManager.get(SUMMON_ATTACK);
    }
    public void setHammerStart(Boolean value) {
        this.dataManager.set(HAMMER_START, Boolean.valueOf(value));
    }
    public boolean isHammerStart() {
        return this.dataManager.get(HAMMER_START);
    }
    public void setHammerCycle(Boolean value) {
        this.dataManager.set(HAMMER_CYCLE, Boolean.valueOf(value));
    }
    public boolean isHammerCycle() {
        return this.dataManager.get(HAMMER_CYCLE);
    }
    public void setHammerAttack(Boolean value) {
        this.dataManager.set(HAMMER_ATTACK, Boolean.valueOf(value));
    }
    public boolean isHammerAttack() {
        return this.dataManager.get(HAMMER_ATTACK);
    }
    public void setMultiPierceAttack(Boolean value) {
        this.dataManager.set(MULTI_PIERCE_ATTACK, Boolean.valueOf(value));
    }
    public boolean isMultiPierceAttack() {
        return this.dataManager.get(MULTI_PIERCE_ATTACK);
    }
    public void setShulkerAttack(Boolean value) {
        this.dataManager.set(SHULKER_ATTACK, Boolean.valueOf(value));
    }
    public boolean isShulkerAttack() {
        return this.dataManager.get(SHULKER_ATTACK);
    }

    public void setHammerProjectile(Boolean value) {
        this.dataManager.set(HAMMER_PROJECTILE, Boolean.valueOf(value));
    }
    public boolean isHammerProjectile() {
        return this.dataManager.get(HAMMER_PROJECTILE);
    }
    public void setAlive(Boolean value) {
        this.dataManager.set(SUMMON_BOOLEAN, Boolean.valueOf(value));
    }
    public boolean isSummoned() {
        return this.dataManager.get(SUMMON_BOOLEAN);
    }
    public void setDeadAnim(Boolean value) {
        this.dataManager.set(DEATH_BOOLEAN, Boolean.valueOf(value));
    }
    public boolean isDeathAnim() {
        return this.dataManager.get(DEATH_BOOLEAN);
    }


    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(ModConfig.health);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.34590D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(2.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(18.0D);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        if(meleeSwitch && SwitchCoolDown == 500) {
            rangeSwitch = true;
            SwitchCoolDown = 0;
            meleeSwitch = false;
        }
        if(rangeSwitch && SwitchCoolDown == 500) {
            meleeSwitch = true;
            SwitchCoolDown = 0;
            rangeSwitch = false;
        }
        else {
            SwitchCoolDown++;
        }

        if (this.isShulkerAttack()) {
            this.motionY = 0;
        }
        if(targetFloating) {
            EntityLivingBase target = this.getAttackTarget();
            if(target != null) {
                target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 40, 3));
            }
        }

        if(this.isSummoned() || this.isDeathAnim()) {
            this.setImmovable(true);
        } else {
            this.setImmovable(false);
        }
        //Allows boss to destory blocks while quick dashing
        if(this.isSpinCycle() || this.isPierceAttack() || this.isMultiPierceAttack()) {
            AxisAlignedBB box = getEntityBoundingBox().grow(1.25, 0.1, 1.25).offset(0, 0.1, 0);
            ModUtils.destroyBlocksInAABB(box, world, this);
        }

    }
    //Particle Call
    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();

        if(rand.nextInt(5)== 0) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }
        if(startFlameSpawns) {
            if(rand.nextInt(2) == 0) {
                world.setEntityState(this, ModUtils.THIRD_PARTICLE_BYTE);
            }
        }
        if(this.isSpinCycle()) {
            if(rand.nextInt(2) == 0) {
                world.setEntityState(this, ModUtils.FOURTH_PARTICLE_BYTE);
            }
        }
    }
    //Particle Handler for the boss
    @Override
    public void handleStatusUpdate(byte id) {
        if(id == ModUtils.PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 1.5, 0))), ModColors.WHITE, new Vec3d(ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1,ModRand.getFloat(1) * 0.1));
        }
        if(id == ModUtils.THIRD_PARTICLE_BYTE) {
            ParticleManager.spawnBrightFlames(world, rand, this.getPositionVector(), ModRand.randVec().scale(0.3f));
        }
        if(id == ModUtils.FOURTH_PARTICLE_BYTE) {
            ParticleManager.spawnColoredSmoke(world, getPositionVector(), ModColors.WHITE, new Vec3d(rand.nextFloat() * 0.2,1,rand.nextFloat() * 0.2));
        }

        super.handleStatusUpdate(id);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        EntityLivingBase target = this.getAttackTarget();
        if(target != null && !this.isBeingRidden() && !meleeSwitch) {
            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            double distance = Math.sqrt(distSq);
            if(distance < 12) {
                double d0 = (this.posX - target.posX) * 0.015;
                double d1 = (this.posY - target.posY) * 0.01;
                double d2 = (this.posZ - target.posZ) * 0.015;
                this.addVelocity(d0, d1, d2);
            }
        }
    //Spin Cycle
        if(this.isSpinCycle() && target != null) {
            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            double distance = Math.sqrt(distSq);
            if (distance > 2) {
            meleeSwitch = true;
            rangeSwitch = false;
            double d0 = (target.posX - this.posX) * 0.005;
            double d1 = (target.posY - this.posY) * 0.009;
            double d2 = (target.posZ - this.posZ) * 0.005;
            this.addVelocity(d0, d1, d2);
        }
            else {
                this.setSpinCycle(false);
                this.setFightMode(false);
            }
        }
        //Hammer Cycle
        if(this.isHammerCycle() && target != null) {
            double distSq = this.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            double distance = Math.sqrt(distSq);
            meleeSwitch = true;
            rangeSwitch = false;
            if(distance > 3) {
                double d0 = (target.posX - this.posX) * 0.015;
                double d1 = (target.posY - this.posY) * 0.009;
                double d2 = (target.posZ - this.posZ) * 0.015;
                this.addVelocity(d0, d1, d2);
            } else {
                this.setHammerCycle(false);
                this.setFightMode(false);
            }
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
    animationData.addAnimationController(new AnimationController(this, "animBlink", 0, this::predicateBlink));
    animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    animationData.addAnimationController(new AnimationController(this, "phaseONE_controller", 0, this::predicateAttack));
    animationData.addAnimationController(new AnimationController(this, "SD_controller", 0, this::predicateStates));
    }

    private<E extends IAnimatable>PlayState predicateBlink(AnimationEvent<E> event) {
        //Handles the Eyes, Small movements of the remaining Crystals
        if(!this.isDeathAnim() && !this.isSummoned()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_BLINK, true));
        }
        return PlayState.CONTINUE;
    }
    private <E extends IAnimatable> PlayState predicateStates(AnimationEvent<E> event) {
        if(this.isSummoned()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(SUMMON_BOSS_ANIM, false));
            return PlayState.CONTINUE;
        }
        if(this.isDeathAnim()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(DEATH_BOSS_ANIM, false));
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        return PlayState.STOP;
    }
    private <E extends IAnimatable>PlayState predicateIdle(AnimationEvent<E> event) {
        //Idle Movements
        if(!this.isFightMode() && !this.isDeathAnim() && !this.isSummoned()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        }

        return PlayState.CONTINUE;
    }


    private <E extends IAnimatable>PlayState predicateAttack(AnimationEvent<E> event) {
        //Attack Animations
        //Simple Strike
        if(this.isStrikeAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(STRIKE_ANIM, false));
            return PlayState.CONTINUE;
        }
        //Summon Small Crystals
        if(this.isCrystalAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(CRYSTAL_ANIM, false));
            return PlayState.CONTINUE;
        }
        // Spin Start Anim
        if(this.isSpinStart()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(SPIN_BEGIN_ANIM, false));
            return PlayState.CONTINUE;
        }
        // Spin Cycle End - Attack
        if(this.isSpinAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(SPIN_ATTACK_ANIM, false));
            return PlayState.CONTINUE;
        }
        //Pierce Attack
        if(this.isPierceAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(PIERCE_DASH_ANIM, false));
            return PlayState.CONTINUE;
        }
        //Spin Loop Anim
        if(this.isSpinCycle()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(SPIN_ANIM, true));
            return PlayState.CONTINUE;
        }
        //Summon Ground Crystals
        if(this.isSummonAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(GROUND_CRYSTALS_ANIM, false));
            return PlayState.CONTINUE;
        }
        //Hammer Start Anim
        if(this.isHammerStart()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(HAMMER_BEGIN_ANIM, false));
            return PlayState.CONTINUE;
        }
        //Hammer Travel Cycle Anim
        if(this.isHammerCycle()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(HAMMER_TRAVEL_ANIM, true));
            return PlayState.CONTINUE;
        }
        //Hammer Attack Anim
        if(this.isHammerAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(HAMMER_ATTACK_ANIM, false));
            return PlayState.CONTINUE;
        }
        //Multiple Pierce Anims
        if(this.isMultiPierceAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(MULTI_ATTACK_ANIM, false));
            return PlayState.CONTINUE;
        }
        //Summon Shulkers Anim
        if(this.isShulkerAttack()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(SUMMON_SHULKERS_ANIM, false));
            return PlayState.CONTINUE;
        }
        //HammerProjectileAttack
        if(this.isHammerProjectile()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(RANGED_HAMMER_ANIM, false));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));

    }

    public void initBossAI() {
        float attackDistance = 14;
        this.tasks.addTask(4, new EntityAerialTimedAttack(this, attackDistance, 3, 30, new TimedAttackIniator<>(this, 20)));
    }

    public void teleportTarget(double x, double y, double z) {
        this.setPosition(x , y, z);

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    Supplier<Projectile> crystalBallProjectile = () -> new EntityCrystalSpikeSmall(world, this, 5.0f, null);



    @Override
    public int startAttack(EntityLivingBase target, float distanceSq, boolean strafingBackwards) {
        double distance = Math.sqrt(distanceSq);
        double HealthChange = this.getHealth() / this.getMaxHealth();
        if(!this.isFightMode() && !this.isDeathAnim() && !this.isSummoned()) {
            //Begin Attacks REPEATED
            List<Consumer<EntityLivingBase>> attacks = new ArrayList<>(Arrays.asList(meleeStrike, summonCrystals, dashPierce, circleDash, circleAttack, summonGroundCrystals, hammerSLAM, hammerExplosion, animeStrike, summonShulkers, hammerProjectile));
            double[] weights = {
                    //Phase One Abilities
                    (distance < 3) ? 1/distance : 0, //Melee Strike
                    (distance > 10 && prevAttack != summonCrystals) ? distance * 0.02 : 0, //Summon Crystal Projectiles
                    (distance < 8 && prevAttack != dashPierce) ? 1/distance : 0, // Dash Pierce
                    (distance > 10 && prevAttack == summonCrystals) ? distance * 0.02 : 0, //CircleDash
                    (prevAttack == circleDash) ?  100 : 0, //CircleAttack
                    (distance < 8 && prevAttack != summonGroundCrystals) ? 1/distance : 0, //SummonGroundCrystals
                    //Phase Two Abilities Added

                    (distance < 9 && prevAttack != hammerSLAM && HealthChange < 0.5) ? 1/distance : 0, //Hammer Slam Attack
                    (prevAttack == hammerSLAM) ? 100 : 0, // 2 Part Hammer Attack
                    (distance < 9 && prevAttack != animeStrike && HealthChange < 0.60) ? 1/distance : 0, //Summon Shulkers
                    (distance > 10 && HealthChange < 0.75) ? distance * 0.02 : 0, // Summon Shulkers
                    (distance > 7) ? distance * 0.02 : 0 //Hammer Projectile Attack
                    //Possibly one more Attack

            };
            prevAttack = ModRand.choice(attacks, rand, weights).next();

            prevAttack.accept(target);

        }


        return (prevAttack == circleDash || prevAttack == hammerSLAM) ? 0 : 20;
    }

    //Basic Melee Attack
    private final Consumer<EntityLivingBase> meleeStrike = (target) -> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            this.setStrikeAttack(true);
            addEvent(() -> {
                this.playSound(ModSoundHandler.BOSS_DRAW_SWORD, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
            }, 5);
            addEvent(() -> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.2, 1.2, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = 7;
                ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.4f, 0, false);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            }, 25);
            addEvent(() -> {
                this.setFightMode(false);
                this.setStrikeAttack(false);
            }, 40);
        }
    };
    //Crystal Ranged Attack
    private final Consumer<EntityLivingBase>summonCrystals = (target) -> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            this.setCrystalAttack(true);
            addEvent(() -> {
                this.playSound(ModSoundHandler.BOSS_CAST_AMBIENT, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
            }, 25);
            addEvent(() -> {
                for (int i = 0; i < 60; i += 4) {
                    addEvent(() -> {
                        this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.4f, 0.8f + ModRand.getFloat(0.2F));
                        float damage = 5f;
                        EntityCrystalSpikeSmall projectile = new EntityCrystalSpikeSmall(this.world, this, damage, null);
                        Vec3d pos = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(ModRand.getFloat(2), 3, ModRand.getFloat(2))));
                        Vec3d targetPos = new Vec3d(target.posX + ModRand.getFloat(2) - 1, target.posY, target.posZ + ModRand.getFloat(2) - 1);
                        Vec3d velocity = targetPos.subtract(pos).normalize().scale(0.55f);
                        projectile.setPosition(pos.x, pos.y, pos.z);
                        projectile.setTravelRange(30f);
                        ModUtils.setEntityVelocity(projectile, velocity);
                        world.spawnEntity(projectile);
                    }, i);
                }
            }, 40);

            addEvent(() -> this.setFightMode(false), 110);
            addEvent(() -> {
                this.setCrystalAttack(false);

            }, 110);
        }
    };
    // Dash Pierce
    private final Consumer<EntityLivingBase> dashPierce = (target)-> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            this.setPierceAttack(true);
            addEvent(() -> {
                this.playSound(ModSoundHandler.BOSS_DRAW_SWORD, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
            }, 21);
            addEvent(() -> {
                Vec3d targetPosition = target.getPositionVector();
                addEvent(() -> {
                    startFlameSpawns = true;
                    ModUtils.leapTowards(this, targetPosition, 0.9f, 0.0f);
                    this.playSound(ModSoundHandler.BOSS_DASH, 0.7f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
                }, 17);

                addEvent(() -> {
                    for (int i = 0; i < 20; i += 5) {
                        addEvent(() -> {
                            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0.5, 0.75, 0)));
                            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).disablesShields().directEntity(this).build();
                            float damage = 7;
                            ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.7f, 0, false);
                        }, i);
                    }
                }, 17);
            }, 30);

            addEvent(() -> startFlameSpawns = false, 67);
            addEvent(() -> {
                this.setPierceAttack(false);
                this.setFightMode(false);
            }, 80);
        }
    };
    //Circle Dash
    private final Consumer<EntityLivingBase> circleDash = (target) -> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            this.setSpinStart(true);
            addEvent(() -> {
                this.playSound(ModSoundHandler.BOSS_DRAW_SWORD, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
            }, 12);
            addEvent(() -> {
                this.setSpinStart(false);
                this.setSpinCycle(true);
            }, 30);
            addEvent(() -> {

                    for (int i = 0; i < 110; i += 5) {
                        addEvent(() -> {
                            if(this.isSpinCycle()) {
                                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 0.6f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
                            }
                        }, i);
                    }

            }, 30);
            addEvent(() -> {
                if (this.isSpinCycle()) {
                    this.setSpinCycle(false);
                    this.setFightMode(false);
                }
            }, 150);
        }
    };
    //Circle Attack - A Continuation of the Circle Dash
    private final Consumer<EntityLivingBase> circleAttack = (target)-> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            this.setSpinAttack(true);
            addEvent(() -> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 0.75, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = 14;
                ModUtils.handleAreaImpact(1.5f, (e) -> damage, this, offset, source, 0.7f, 0, false);
                this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
            }, 4);

            addEvent(() -> {
                this.setFightMode(false);
                this.setSpinAttack(false);
            }, 30);
        }
    };


    //Summon Ground Crystals
    private final Consumer<EntityLivingBase> summonGroundCrystals = (target) -> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            this.setSummonAttack(true);
            addEvent(() -> {

            }, 10);
            addEvent(() -> {
                Vec3d targetPosition = target.getPositionVector();
                Vec3d throwerPosition = this.getPositionVector();
                Vec3d dir = targetPosition.subtract(throwerPosition).normalize();
                AtomicReference<Vec3d> spawnPos = new AtomicReference<>(throwerPosition);

                for (int t = 0; t < 7; t += 1) {
                    int additive = t;
                    addEvent(() -> {

                        ModUtils.lineCallback(throwerPosition.add(dir), throwerPosition.add(dir.scale(additive)), additive * 2, (pos, r) -> {
                            spawnPos.set(pos);
                        });
                        Vec3d initPos = spawnPos.get();
                        EntityGroundCrystal crystal = new EntityGroundCrystal(this.world);
                        BlockPos blockPos = new BlockPos(initPos.x, initPos.y, initPos.z);
                        crystal.setPosition(blockPos);
                        this.world.spawnEntity(crystal);

                    }, t);
                }
            }, 20);


            addEvent(() -> this.setFightMode(false), 40);
            addEvent(() -> this.setSummonAttack(false), 40);
        }
    };
    //Hammer Slam Attack
    private Consumer<EntityLivingBase> hammerSLAM = (target)-> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            this.setHammerStart(true);
            addEvent(() -> {
                this.playSound(ModSoundHandler.BOSS_DRAW_HAMMER, 0.9f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
            }, 12);
            addEvent(() -> {
                this.setHammerStart(false);
                this.setHammerCycle(true);
                //Set's into a continuation where until the requirements are met, the cycle will continue
            }, 40);

            addEvent(() -> {
                if (isHammerCycle()) {
                    this.setHammerCycle(false);
                    this.setFightMode(false);
                }
            }, 200);
        }
    };
    //Hammer PT 2 Attack
    private Consumer<EntityLivingBase> hammerExplosion = (target) -> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            this.setHammerAttack(true);
            addEvent(() -> {
                Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(2.0, 0, 0)));
                DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                float damage = 7;
                float explostionFactor = 2;
                ModUtils.handleAreaImpact(2.0f, (e) -> damage, this, offset, source, 0.9f, 1, false);
                this.world.newExplosion(this, offset.x, offset.y, offset.z, explostionFactor, true, true);
            }, 20);

            addEvent(() -> this.setFightMode(false), 40);
            addEvent(() -> this.setHammerAttack(false), 35);
        }
    };
    //Anime Pierce Strike
    private Consumer<EntityLivingBase> animeStrike = (target)-> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            targetFloating = true;
            //Repeat Attack Times 3
            addEvent(() -> {
                this.meleeSwitch = true;
                this.rangeSwitch = false;
                new ActionAerialTeleport(ModColors.AZURE).performAction(this, target);
                this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.3F));
                addEvent(() -> {
                    this.setMultiPierceAttack(true);
                }, 20);
                addEvent(() -> {
                    ModUtils.leapTowards(this, target.getPositionVector(), 0.8f, -0.4f);
                    this.playSound(ModSoundHandler.BOSS_DASH, 0.7f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
                    for (int t = 0; t < 15; t += 5) {
                        addEvent(() -> {
                            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.0, 0)));
                            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                            float damage = 6;
                            ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.3f, 0, false);
                        }, t);
                    }

                }, 60);
                addEvent(() -> {
                    this.setMultiPierceAttack(false);

                }, 100);
            }, 0);


            addEvent(() -> {
                this.meleeSwitch = true;
                this.rangeSwitch = false;
                new ActionAerialTeleport(ModColors.AZURE).performAction(this, target);
                this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.3F));
                addEvent(() -> {
                    this.setMultiPierceAttack(true);
                }, 20);
                addEvent(() -> {
                    ModUtils.leapTowards(this, target.getPositionVector(), 0.8f, -0.4f);
                    this.playSound(ModSoundHandler.BOSS_DASH, 0.7f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
                    for (int t = 0; t < 15; t += 5) {
                        addEvent(() -> {
                            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.0, 0)));
                            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                            float damage = 6;
                            ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.3f, 0, false);
                        }, t);
                    }

                }, 60);
                addEvent(() -> {
                    this.setMultiPierceAttack(false);

                }, 100);
            }, 120);


            addEvent(() -> {
                this.meleeSwitch = true;
                this.rangeSwitch = false;
                new ActionAerialTeleport(ModColors.AZURE).performAction(this, target);
                this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.3F));
                addEvent(() -> {
                    this.setMultiPierceAttack(true);
                }, 20);
                addEvent(() -> {
                    ModUtils.leapTowards(this, target.getPositionVector(), 0.8f, -0.4f);
                    this.playSound(ModSoundHandler.BOSS_DASH, 0.7f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
                    for (int t = 0; t < 15; t += 5) {
                        addEvent(() -> {
                            Vec3d offset = this.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(1.0, 1.0, 0)));
                            DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).build();
                            float damage = 6;
                            ModUtils.handleAreaImpact(1.0f, (e) -> damage, this, offset, source, 0.3f, 0, false);
                        }, t);
                    }
                }, 60);

                addEvent(() -> {

                    this.setMultiPierceAttack(false);
                    targetFloating = false;
                    this.rangeSwitch = true;
                }, 100);
            }, 240);

            addEvent(() -> this.setFightMode(false), 360);
        }
    };

    //Summon Shulkers
    private Consumer<EntityLivingBase> summonShulkers = (target)-> {
        if(!this.isDeathAnim()) {
            this.setFightMode(true);
            this.setShulkerAttack(true);
            addEvent(() -> {
                this.playSound(ModSoundHandler.BOSS_CAST_AMBIENT, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
            }, 25);
            addEvent(() -> {
                for (int i = 0; i < 25; i += 5) {
                    addEvent(() -> {
                        EntityShulkerBullet projectile = new EntityShulkerBullet(this.world);
                        Vec3d targetPos = target.getPositionVector().add(ModUtils.getRelativeOffset(this, new Vec3d(0, 12, 0)));
                        BlockPos initPos = new BlockPos(targetPos.x, targetPos.y, targetPos.z);
                        projectile.setPosition(initPos.getX(), initPos.getY(), initPos.getZ());
                        this.world.spawnEntity(projectile);
                    }, i);
                }
            }, 30);
            addEvent(() -> {
                this.setFightMode(false);
                this.setShulkerAttack(false);
            }, 50);
        }
    };
    //Summon Crystals with Hammer
    private Consumer<EntityLivingBase> hammerProjectile = (target)-> {
        this.setFightMode(true);
        this.setHammerProjectile(true);
            new ActionVollet(crystalBallProjectile, 0.55f).performAction(this, target);

        addEvent(()-> this.setFightMode(false), 80);
        addEvent(()-> this.setHammerProjectile(false), 40);
    };

    @Override
    public void travel(float strafe, float vertical, float forward) {
        ModUtils.aerialTravel(this, strafe, vertical, forward);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    public void setPosition(BlockPos pos) {
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundHandler.BOSS_IDLE;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSoundHandler.BOSS_HURT;
    }
    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundHandler.BOSS_DEATH;
    }

    @Override
    public void onDeath(DamageSource cause) {
        this.setHealth(0.0001f);
        this.setDeadAnim(true);
        if(this.isDeathAnim()) {

            addEvent(()-> this.playSound(ModSoundHandler.BOSS_DEATH, 1.5f, 1.0f), 40);
            addEvent(()-> this.setDeadAnim(false), 90);
            addEvent(()-> this.setDead(), 90);
        }
        super.onDeath(cause);
    }
}
