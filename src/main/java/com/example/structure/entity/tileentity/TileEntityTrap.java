package com.example.structure.entity.tileentity;

import com.example.structure.entity.endking.EntityRedCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class TileEntityTrap extends TileEntity implements ITickable {

    public int cooldown = 0;

    @Override
    public void update() {

        AxisAlignedBB box = new AxisAlignedBB(pos, pos.add(1,2,1));
        List<EntityPlayer> players = this.world.getEntitiesWithinAABB(EntityPlayer.class, box);
        if(!players.isEmpty() && cooldown >= 60) {
            if(!world.isRemote) {
                EntityRedCrystal spike = new EntityRedCrystal(this.world);
                Vec3d modifiedPos = new Vec3d(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                spike.setPosition(modifiedPos.x, modifiedPos.y, modifiedPos.z);
                world.spawnEntity(spike);
                spike.playSound(SoundEvents.EVOCATION_FANGS_ATTACK, 1.0f, 1.0f);
                cooldown = 0;
            }
        } else {
            cooldown++;
        }
    }
}
