package com.example.structure.entity.endking;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.EntityAITimedAttack;
import com.example.structure.entity.util.IPitch;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.*;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class EntityAbstractEndKing extends EntityModBase implements IEntityMultiPart, IPitch {
    protected Vec3d chargeDir;

    protected final BossInfoServer bossInfo = (new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.NOTCHED_12));

    //Boolean to check for any nearby swords
    protected boolean hasSwordsNearby = false;

    //A call for if damage will be done in the selected area by the attack sorter
    protected boolean damageViable = false;
    protected static final DataParameter<Boolean> FIGHT_MODE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    //Used for Full Bones usage of the body
    protected static final DataParameter<Boolean> FULL_BODY_USAGE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    //Used for Upper Body only attacks
    protected static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> PHASE_MODE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final  DataParameter<Boolean> LEAP_SWEEP_ATTACK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> SUMMON_CRYSTALS_ATTACK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> SUMMON_FIREBALLS_ATTACK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);


    //
    protected static final DataParameter<Boolean> TOP_HP = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    protected static final DataParameter<Float> LOOK = EntityDataManager.createKey(EntityModBase.class, DataSerializers.FLOAT);
    public void setTopHp(boolean value) {this.dataManager.set(TOP_HP, Boolean.valueOf(value));}
    public boolean isTopHP() {return this.dataManager.get(TOP_HP);}
    public void setFightMode(boolean value) {this.dataManager.set(FIGHT_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(FIGHT_MODE);}
    public void setSwingingArms(boolean value) {this.dataManager.set(SWINGING_ARMS, Boolean.valueOf(value));}
    public boolean isSwingingArms() {return this.dataManager.get(SWINGING_ARMS);}
    public void setFullBodyUsage(boolean value) {this.dataManager.set(FULL_BODY_USAGE, Boolean.valueOf(value));}
    public boolean isFullBodyUsage() {return this.dataManager.get(FULL_BODY_USAGE);}
    public void setPhaseMode(boolean value) {this.dataManager.set(PHASE_MODE, Boolean.valueOf(value));}
    public boolean isPhaseMode() {return this.dataManager.get(PHASE_MODE);}
    public void setLeapSweepAttack(boolean value) {this.dataManager.set(LEAP_SWEEP_ATTACK, Boolean.valueOf(value));}
    public boolean isLeapSweepAttack() {return this.dataManager.get(LEAP_SWEEP_ATTACK);}
    public void setSummonCrystalsAttack(boolean value) {this.dataManager.set(SUMMON_CRYSTALS_ATTACK, Boolean.valueOf(value));}
    public boolean isSummonCrystalsAttack() {return this.dataManager.get(SUMMON_CRYSTALS_ATTACK);}
    public void setSummonFireballsAttack(boolean value) {this.dataManager.set(SUMMON_FIREBALLS_ATTACK, Boolean.valueOf(value));}
    public boolean isSummonFireBallsAttack() {return this.dataManager.get(SUMMON_FIREBALLS_ATTACK);}
    private final MultiPartEntityPart[] hitboxParts;
    private final MultiPartEntityPart model = new MultiPartEntityPart(this, "model", 0f, 0f);
    private final MultiPartEntityPart legsWhole = new MultiPartEntityPart(this, "legsWhole", 1.0f, 1.1f);
    private final MultiPartEntityPart torso = new MultiPartEntityPart(this, "torso", 1.2f, 1.7f);
    private final MultiPartEntityPart head = new MultiPartEntityPart(this, "head", 0.7f, 0.7f);



    public float variable_distance = 10f;
    public EntityAbstractEndKing(World world) {
        super(world);
        this.hitboxParts = new MultiPartEntityPart[]{model, legsWhole, torso, head};
        this.setSize(2.0f, 3.7f);
        this.isImmuneToFire = true;
        this.isImmuneToExplosions();

    }

    @Override
    public void entityInit() {

        this.dataManager.register(LOOK, 0f);
        this.dataManager.register(FIGHT_MODE, Boolean.valueOf(false));
        this.dataManager.register(FULL_BODY_USAGE, Boolean.valueOf(false));
        this.dataManager.register(SWINGING_ARMS, Boolean.valueOf(false));
        this.dataManager.register(PHASE_MODE, Boolean.valueOf(false));
        this.dataManager.register(LEAP_SWEEP_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_CRYSTALS_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(SUMMON_FIREBALLS_ATTACK, Boolean.valueOf(false));
        this.dataManager.register(TOP_HP, Boolean.valueOf(true));
        super.entityInit();

    }
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
    private void setHitBoxPos(Entity entity, Vec3d offset) {
        Vec3d lookVel = ModUtils.getLookVec(this.getPitch(), this.renderYawOffset);
        Vec3d center = this.getPositionVector().add(ModUtils.yVec(1.2));

        Vec3d position = center.subtract(ModUtils.Y_AXIS.add(ModUtils.getAxisOffset(lookVel, offset)));
        ModUtils.setEntityPosition(entity, position);

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        double HealthChange = this.getHealth() / this.getMaxHealth();
        if(HealthChange > 0.67) {
            variable_distance = 2.0f;
            this.setTopHp(false);
        }

        List<ProjectileSpinSword> nearbySwords = this.world.getEntitiesWithinAABB(ProjectileSpinSword.class, this.getEntityBoundingBox().grow(4D), e -> !e.getIsInvulnerable());
        if(!nearbySwords.isEmpty()) {
            hasSwordsNearby = true;
        } else {
            hasSwordsNearby = false;
        }
    }


    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        Vec3d[] avec3d = new Vec3d[this.hitboxParts.length];
        for (int j = 0; j < this.hitboxParts.length; ++j) {
            avec3d[j] = new Vec3d(this.hitboxParts[j].posX, this.hitboxParts[j].posY, this.hitboxParts[j].posZ);
        }
        //Location of Hitboxes
        this.setHitBoxPos(legsWhole, new Vec3d(0, -0.1, 0));
        this.setHitBoxPos(torso, new Vec3d(0, 1.0, 0));
        this.setHitBoxPos(head, new Vec3d(0, 2.7, 0));

        Vec3d knightPos = this.getPositionVector();
        ModUtils.setEntityPosition(model, knightPos);

        for (int l = 0; l < this.hitboxParts.length; ++l) {
            this.hitboxParts[l].prevPosX = avec3d[l].x;
            this.hitboxParts[l].prevPosY = avec3d[l].y;
            this.hitboxParts[l].prevPosZ = avec3d[l].z;
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.8D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ModConfig.attack_damage);
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityCrystalKnight>(this, EntityCrystalKnight.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
    }



    public void setPosition(BlockPos pos) {
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Entity[] getParts() {
        return this.hitboxParts;
    }

    public boolean damageKing;

    @Override
    public boolean attackEntityFromPart(@Nonnull MultiPartEntityPart part, @Nonnull DamageSource source, float damage) {
        if(!this.isPhaseMode()) {
            if(part == this.head) {

                damageKing = true;
                return this.attackEntityFrom(source, damage);
            }
        }

        if (damage > 0.0F && !source.isUnblockable()) {
            if (!source.isProjectile()) {
                Entity entity = source.getImmediateSource();

                if (entity instanceof EntityLivingBase) {
                    this.blockUsingShield((EntityLivingBase) entity);
                }

            }
            return false;
        }

        return false;
    }

    @Override
    public final boolean attackEntityFrom(DamageSource source, float amount) {
        if(!damageKing && !source.isUnblockable()) {
            return false;

        }

        damageKing = false;
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void setPitch(Vec3d look) {
        float prevLook = this.getPitch();
        float newLook = (float) ModUtils.toPitch(look);
        float deltaLook = 5;
        float clampedLook = MathHelper.clamp(newLook, prevLook - deltaLook, prevLook + deltaLook);
        this.dataManager.set(LOOK, clampedLook);
    }
    //Determines a random Positive or Negative additive to an original Position for random Dashing
    public int randomDashDirGenerator() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if(randomNumberGenerator >= 5) {
            return ModRand.range(6, 8);
        } else {
            return ModRand.range(-8, -6);
        }
    }

    //A random chance that the knight will dash following certain attacks to allow more unpredicatability
    public int randomDashChance() {
        int healthModif = 0;
        double HealthChange = this.getHealth() / this.getMaxHealth();
        if(HealthChange < 0.2) {
        healthModif = 4;
        } else
        if(HealthChange < 0.4) {
            healthModif = 3;
        } else if (HealthChange < 0.6) {
            healthModif = 2;
        } else if (HealthChange < 0.8) {
            healthModif = 1;
        }

        return ModRand.range(0, 10) + healthModif;
    }

    @Override
    public float getPitch() {
        return this.dataManager == null ? 0 : this.dataManager.get(LOOK);
    }
}
