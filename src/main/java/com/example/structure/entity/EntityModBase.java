package com.example.structure.entity;

import com.example.structure.blocks.atlar.BlockAltar;
import com.example.structure.entity.ai.MobGroundNavigate;
import com.example.structure.util.ModUtils;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.PriorityQueue;
import com.google.common.base.Predicate;

public abstract class EntityModBase extends EntityCreature {

    private float regenTimer;





    public EntityModBase(World worldIn, float x, float y, float z) {
        super(worldIn);
        this.setPosition(x, y, z);
    }

    protected double healthScaledAttackFactor = 0.0; // Factor that determines how much attack is affected by health

    public float getAttack() {
        return ModUtils.getMobDamage(this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue(), healthScaledAttackFactor, this.getMaxHealth(),
                this.getHealth());
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {

        return super.attackEntityFrom(source, amount);
    }

    private PriorityQueue<TimedEvent> events = new PriorityQueue<TimedEvent>();

    private static float regenStartTimer = 20;

    private Vec3d initialPosition = null;

    protected static final DataParameter<Boolean> IMMOVABLE = EntityDataManager.createKey(EntityModBase.class, DataSerializers.BOOLEAN);

    public EntityModBase(World worldIn) {
        super(worldIn);
        this.experienceValue = 5;

    }
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0);

    }
    protected boolean isImmovable() {
        return this.dataManager == null ? false : this.dataManager.get(IMMOVABLE);
    }

    protected void setImmovable(boolean immovable) {
        this.dataManager.set(IMMOVABLE, immovable);
    }

    public void setImmovablePosition(Vec3d pos) {
        this.initialPosition = pos;
        this.setPosition(0, 0, 0);
    }

    @Override
    protected PathNavigate createNavigator(World worldIn) {
        return new MobGroundNavigate(this, worldIn);
    }

    @SideOnly(Side.CLIENT)
    protected void initAnimation() {
    }

    @Override
    public void move(MoverType type, double x, double y, double z) {
        if(!this.isImmovable()) {
            super.move(type, x, y, z);
        }
    }

    @Override
    public void onLivingUpdate() {

        if (!isDead && this.getHealth() > 0) {
            boolean foundEvent = true;
            while (foundEvent) {
                TimedEvent event = events.peek();
                if (event != null && event.ticks <= this.ticksExisted) {
                    events.remove();
                    event.callback.run();
                } else {
                    foundEvent = false;
                }
            }
        }


        if (!world.isRemote) {
            if (this.getAttackTarget() == null) {
                if (this.regenTimer > this.regenStartTimer) {
                    if (this.ticksExisted % 20 == 0) {
                        this.heal(this.getMaxHealth() * 0.015f);
                    }
                } else {
                    this.regenTimer++;
                }
            } else {
                this.regenTimer = 0;
            }
        }
        super.onLivingUpdate();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(IMMOVABLE, Boolean.valueOf(false));

    }

    public void doRender(RenderManager renderManager, double x, double y, double z, float entityYaw, float partialTicks) {
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));

    }

    /**
     * Adds an event to be executed at a later time. Negative ticks are executed immediately.
     *
     * @param runnable
     * @param ticksFromNow
     */
    public void addEvent(Runnable runnable, int ticksFromNow) {
        events.add(new TimedEvent(runnable, this.ticksExisted + ticksFromNow));
    }

    public static class TimedEvent implements Comparable<TimedEvent> {
        Runnable callback;
        int ticks;

        public TimedEvent(Runnable callback, int ticks) {
            this.callback = callback;
            this.ticks = ticks;
        }

        @Override
        public int compareTo(TimedEvent event) {
            return event.ticks < ticks ? 1 : -1;
        }
    }

}
