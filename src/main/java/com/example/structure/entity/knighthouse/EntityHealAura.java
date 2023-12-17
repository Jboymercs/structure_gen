package com.example.structure.entity.knighthouse;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityEnderKnight;
import com.example.structure.entity.EntityModBase;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class EntityHealAura extends EntityModBase implements IAnimatable {

    private String ANIM_SCALE = "scale";
    private AnimationFactory factory = new AnimationFactory(this);
    public EntityHealAura(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    public EntityHealAura(World worldIn) {
        super(worldIn);
        this.setImmovable(true);
        this.setSize(0.1f, 0.1f);
        this.setNoAI(true);
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100D);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.rotationYaw = 0;
        this.rotationPitch = 0;
        this.rotationYawHead = 0;
        this.renderYawOffset = 0;
        this.motionX = 0;
        this.motionZ = 0;
        List<EntityEnderKnight> nearbyEnderKnights = this.world.getEntitiesWithinAABB(EntityEnderKnight.class, this.getEntityBoundingBox().grow(2D), e -> !e.getIsInvulnerable());
        List<EntityEnderKnight> nearbyEnderShield = this.world.getEntitiesWithinAABB(EntityEnderKnight.class, this.getEntityBoundingBox().grow(2D), e -> !e.getIsInvulnerable());
        if(ticksExisted == 15 || ticksExisted == 30 || ticksExisted == 45) {
            if (!nearbyEnderKnights.isEmpty()) {
                for (EntityEnderKnight knight : nearbyEnderKnights) {
                    float maxHealth = knight.getHealth() / knight.getMaxHealth();
                    if (maxHealth < 1) {
                        knight.heal(5F);
                    }

                }
            }
        }

        if(this.ticksExisted > 15 && this.ticksExisted <= 50) {
           world.setEntityState(this, ModUtils.PARTICLE_BYTE);
        }
        if(ticksExisted == 60) {
            this.setDead();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == ModUtils.PARTICLE_BYTE) {
            ModUtils.circleCallback(2, 30, (pos)-> {
                pos = new Vec3d(pos.x, 0, pos.y);
                ParticleManager.spawnColoredSmoke(world, this.getPositionVector(), ModColors.RED, pos.normalize().add(ModUtils.yVec(0.1f)));
            });
        }
        super.handleStatusUpdate(id);
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SCALE, false));
        return PlayState.CONTINUE;
    }


    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    }

    @Override
    protected boolean canDropLoot() {
        return false;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
