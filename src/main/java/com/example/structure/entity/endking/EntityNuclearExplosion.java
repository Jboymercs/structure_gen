package com.example.structure.entity.endking;

import com.example.structure.config.ModConfig;
import com.example.structure.entity.EntityModBase;
import com.example.structure.event_handler.ClientEvents;
import com.example.structure.event_handler.ClientRender;
import com.example.structure.util.ModColors;
import com.example.structure.util.ModDamageSource;
import com.example.structure.util.ModRand;
import com.example.structure.util.ModUtils;
import com.example.structure.util.handlers.ModSoundHandler;
import com.example.structure.util.handlers.ParticleManager;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
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

import java.util.List;

public class EntityNuclearExplosion extends EntityModBase implements IAnimatable {
    private final String ANIM_SUMMON = "explode";
    private AnimationFactory factory = new AnimationFactory(this);
    public EntityNuclearExplosion(World worldIn, float x, float y, float z) {
        super(worldIn, x, y, z);
    }

    public EntityNuclearExplosion(World worldIn) {
        super(worldIn);
        this.setImmovable(true);
        this.setSize(1.0f, 2.0f);

    }

    public int randomFireDir() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if(randomNumberGenerator >= 5) {
            return ModRand.range(2, 8);
        } else {
            return ModRand.range(-8, -2);
        }
    }

    public int randomFireDirTwo() {
        int randomNumberGenerator = ModRand.range(0, 10);
        if(randomNumberGenerator >= 5) {
            return ModRand.range(2, 8);
        } else {
            return ModRand.range(-8, -2);
        }
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if(ModUtils.PARTICLE_BYTE == id) {
            ModUtils.circleCallback(2, 50, (pos) -> {
                pos = new Vec3d(pos.x, 0, pos.y);
                if(rand.nextInt(3) == 0) {
                    ParticleManager.spawnColoredSmoke(world, pos.add(this.getPositionVector().add(ModUtils.yVec(0.2))), ModColors.GREY, pos.normalize().scale(1).add(ModUtils.yVec(0.1)));
                }
            });
        }
        super.handleStatusUpdate(id);
    }



    public boolean setBlockToFire(BlockPos pos) {
        if(!world.getBlockState(pos.down()).isFullBlock()) {
            return world.setBlockState(pos.down(), Blocks.FIRE.getDefaultState());
        } else if (!world.getBlockState(pos).isFullBlock()) {
            return world.setBlockState(pos, Blocks.FIRE.getDefaultState());
        }
        else if (!world.getBlockState(pos.up()).isFullBlock()) {
            return world.setBlockState(pos.up(), Blocks.FIRE.getDefaultState());
        }
        return world.setBlockState(pos, Blocks.FIRE.getDefaultState());
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
        if(ticksExisted == 1) {
            this.playSound(ModSoundHandler.BOMB_EXPLODE, 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.4f));
        }

        List<EntityPlayer> nearbyPlayers = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().grow(48D),e -> !e.getIsInvulnerable());
        if(!nearbyPlayers.isEmpty() && ticksExisted > 15 && ticksExisted < 85) {
            world.setEntityState(this, ModUtils.PARTICLE_BYTE);
            for(EntityPlayer player : nearbyPlayers) {
                ClientRender.SCREEN_SHAKE = 2f;
            }
        }



        //Do Explosion Effects
        if(ticksExisted > 15 && ticksExisted < 85) {
            List<EntityLivingBase> nearbyEntities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(7D),e -> (!(e instanceof EntityEndKing)));
            int hitCooldown = 5;
            for(EntityLivingBase entityLivingBase : nearbyEntities) {
                  Vec3d targetPos = entityLivingBase.getPositionVector();
                  float damage = 7.0f;
                  DamageSource source = ModDamageSource.builder().type(ModDamageSource.MOB).directEntity(this).disablesShields().build();
                  ModUtils.handleAreaImpact(1.0f, (e)-> damage, this, targetPos, source, 0.4f, 10, false);


            }
            //Fly them away
            List<EntityLivingBase> nearbyEntitiestoFly = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(10D),e -> (!(e instanceof EntityEndKing)));
            for(EntityLivingBase entityLivingBase :nearbyEntitiestoFly) {
                Vec3d targetPos = entityLivingBase.getPositionVector();
                Vec3d currentPos = this.getPositionVector();
                double d0 = (targetPos.x - currentPos.x) * 0.035;
                double d1 = (targetPos.y - currentPos.y) * 0.02;
                double d2 = (targetPos.z - currentPos.z) * 0.035;
                Vec3d vel = new Vec3d(d0, d1, d2);
                ModUtils.addEntityVelocity(entityLivingBase, vel);
            }

            if(rand.nextInt(5) == 0) {
                Vec3d modifiedPos = this.getPositionVector().add(new Vec3d(randomFireDir(), 0, randomFireDirTwo()));
                BlockPos pos = new BlockPos(modifiedPos.x, modifiedPos.y, modifiedPos.z);
                this.setBlockToFire(pos);
            }


        }

        if(ticksExisted == 100) {
            this.setDead();
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "idle_controller", 0, this::predicateIdle));
    }

    private<E extends IAnimatable> PlayState predicateIdle(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_SUMMON, false));
        return PlayState.CONTINUE;
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
