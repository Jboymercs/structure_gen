package com.example.structure.entity;

import com.example.structure.init.ModItems;
import com.example.structure.items.Items;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EntityCrystalSpikeSmall extends Projectile{
    private static final int PARTICLE_AMOUNT = 1;

    private final String ANIM_IDLE = "idle";


    public EntityCrystalSpikeSmall(World worldIn, EntityLivingBase throwerIn, float damage, ItemStack stack) {
        //FIX
        super(worldIn, throwerIn, damage);
        this.setNoGravity(true);


    }

    public EntityCrystalSpikeSmall(World worldIn) {
        super(worldIn);
    }

    public EntityCrystalSpikeSmall(World worldIn, double x, double y, double z, ItemStack stack) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
    }


    @Override
    public Item getItemToRender() {
        return Items.CRYSTAL_BALL_ITEM;
    }




    private <E extends IAnimatable>PlayState predicateProjectile(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIM_IDLE, true));
        return PlayState.CONTINUE;
    }

@Override
    protected void spawnParticles() {
        for (int i = 0; i < this.PARTICLE_AMOUNT; i++) {

        }
    }


}
