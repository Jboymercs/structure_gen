package com.example.structure.entity.knighthouse;

import com.example.structure.entity.EntityBuffker;
import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.EntityAIAvoidCrowding;
import com.example.structure.entity.ai.EntityAIWanderWithGroup;
import com.example.structure.util.ModRand;
import com.example.structure.util.handlers.ModSoundHandler;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.List;

public abstract class EntityKnightBase extends EntityModBase {

    private static final DataParameter<Integer> SKIN_TYPE = EntityDataManager.<Integer>createKey(EntityKnightBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> INTERACT = EntityDataManager.createKey(EntityKnightBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FIGHT_MODE = EntityDataManager.createKey(EntityEnderKnight.class, DataSerializers.BOOLEAN);

    public void setInteract(boolean value) {this.dataManager.set(INTERACT, Boolean.valueOf(value));}
    public boolean isInteract() {return this.dataManager.get(INTERACT);}
    public void setFightMode(boolean value) {this.dataManager.set(FIGHT_MODE, Boolean.valueOf(value));}
    public boolean isFightMode() {return this.dataManager.get(FIGHT_MODE);}
    /**
     * Allows me to insert specific AI and make them seem like they work more like a group
     * @param worldIn
     * @param x
     * @param y
     * @param z
     */

    public EntityKnightBase(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }


    public EntityKnightBase(World worldIn) {
        super(worldIn);
    }

    @Override
    protected boolean canDropLoot() {
        return true;
    }

    public int interactTimer = 200 + ModRand.range(10, 100);
    public int animationTimer = 40;
    public boolean currentlyOnInteract = false;
    @Override
    public void onUpdate() {
        super.onUpdate();
        EntityLivingBase target = this.getAttackTarget();
        List<EntityKnightBase> nearbyKnights = this.world.getEntitiesWithinAABB(EntityKnightBase.class, this.getEntityBoundingBox().grow(4D), e-> !e.getIsInvulnerable() && !EntityKnightBase.CAN_TARGET.apply(e));
        if(target == null && interactTimer < 0 && !currentlyOnInteract && !this.isFightMode()) {
           InteractIdeal();
        }
        if(this.isInteract()) {
            animationTimer--;
        }
        if(animationTimer == 0) {
            stopInteract();
        }

        if(!this.currentlyOnInteract) {
            interactTimer--;
        }



    }

    public void InteractIdeal() {
        this.setInteract(true);
        this.currentlyOnInteract = true;
        interactTimer = 200  + ModRand.range(10, 100);
    }

    public void stopInteract() {
        this.setInteract(false);
        this.currentlyOnInteract = false;
        animationTimer = 40;
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(6, new EntityAIWanderWithGroup(this, 1.0D));
        this.tasks.addTask(7, new EntityAIAvoidCrowding(this, 1.0D));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 1, true, false, null));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityCrystalKnight>(this, EntityCrystalKnight.class, 1, true, false, null));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityBuffker>(this, EntityBuffker.class, 1, true, false, null));
        this.targetTasks.addTask(4, new EntityAIHurtByTarget(this, false));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(SKIN_TYPE, Integer.valueOf(this.rand.nextInt(3)));
        this.dataManager.register(INTERACT, Boolean.valueOf(false));
        this.dataManager.register(FIGHT_MODE, Boolean.valueOf(false));
    }


    public static boolean isFriendlyKnight(Entity entity) {
        return !CAN_TARGET.apply(entity);
    }
    public static final Predicate<Entity> CAN_TARGET = entity -> {
        return !(entity instanceof EntityKnightBase);
    };
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!CAN_TARGET.apply(source.getTrueSource())) {
            return false;
        }
        return super.attackEntityFrom(source, amount);
    }

    public int getSkin()
    {
        return this.dataManager.get(SKIN_TYPE).intValue();
    }

    public void setSkin(int skinType)
    {
        this.dataManager.set(SKIN_TYPE, Integer.valueOf(skinType));
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("Variant", getSkin());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        setSkin(nbt.getInteger("Variant"));
    }

    @Override
    protected boolean canDespawn() {

            // Edit this to restricting them not despawning in Dungeons
            return this.ticksExisted > 20 * 60 * 20;

    }



    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_PLAYER_HURT;
    }


    @Override
    protected void playStepSound(BlockPos pos, Block blockIn)
    {
        this.playSound(ModSoundHandler.KNIGHT_STEP, 0.7F, 1.0f / (rand.nextFloat() * 0.4F + 0.4f));
    }
}
