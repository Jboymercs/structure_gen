package com.example.structure.entity;

import com.example.structure.init.ModItems;
import com.example.structure.items.Items;
import com.example.structure.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityExplosion extends Projectile{

    public EntityExplosion(World worldIn, EntityLivingBase throwerIn, float damage) {
        super(worldIn, throwerIn, 0);
    }

    @Override
    public Item getItemToRender() {
        return ModItems.INVISIBLE;
    }

    public EntityExplosion(World worldIn) {
        super(worldIn);
    }

    public EntityExplosion(World worldIn, double x, double y, double z, ItemStack stack) {
        super(worldIn, x, y, z);
        this.setNoGravity(true);
    }



    @Override
    public void onUpdate() {
        super.onUpdate();
        if(ticksExisted == 20) {
         new EntityCrystalKnight(world).onSummon(this.getPosition(), this);
        }

        if(ticksExisted > 40) {
            this.setDead();

        }
    }
}
