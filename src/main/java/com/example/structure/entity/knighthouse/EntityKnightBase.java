package com.example.structure.entity.knighthouse;

import com.example.structure.entity.EntityBuffker;
import com.example.structure.entity.EntityCrystalKnight;
import com.example.structure.entity.EntityModBase;
import com.example.structure.entity.ai.EntityAIAvoidCrowding;
import com.example.structure.entity.ai.EntityAIWanderWithGroup;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public abstract class EntityKnightBase extends EntityModBase {
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

    @Override
    protected boolean canDespawn() {

            // Edit this to restricting them not despawning in Dungeons
            return this.ticksExisted > 20 * 60 * 20;

    }
}
