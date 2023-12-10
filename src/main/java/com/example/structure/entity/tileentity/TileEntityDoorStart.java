package com.example.structure.entity.tileentity;

import com.example.structure.blocks.BlockDungeonDoor;
import com.example.structure.init.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class TileEntityDoorStart extends TileEntity implements ITickable {



    @Override
    public void update() {

        EnumFacing facing = BlockDungeonDoor.getFacing(this.getBlockMetadata());
        boolean triggered = (this.getBlockMetadata() & 8) > 0;
        boolean nearbyActivatedBlocks =false;
        BlockPos posOriginal = this.getPos();
        for(int i = 0; i <= 10; i++) {
            BlockPos pos1 = posOriginal.add(new BlockPos(i,0, 0));
            BlockPos pos2 = posOriginal.add(new BlockPos(-i, 0, 0));
            BlockPos pos3 = posOriginal.add(new BlockPos(0, i, 0));
            BlockPos pos4 = posOriginal.add(new BlockPos(0, -i, 0));
            BlockPos pos5 = posOriginal.add(new BlockPos(0, 0, i));
            BlockPos pos6 = posOriginal.add(new BlockPos(0, 0, -i));
            if(world.getBlockState(pos1) == ModBlocks.END_ASH_DOOR_ACTIVATE.getDefaultState()) {
                nearbyActivatedBlocks = true;
            }
            if(world.getBlockState(pos2) == ModBlocks.END_ASH_DOOR_ACTIVATE.getDefaultState()) {
                nearbyActivatedBlocks = true;
            }
            if(world.getBlockState(pos3) == ModBlocks.END_ASH_DOOR_ACTIVATE.getDefaultState()) {
                nearbyActivatedBlocks = true;
            }
            if(world.getBlockState(pos4) == ModBlocks.END_ASH_DOOR_ACTIVATE.getDefaultState()) {
                nearbyActivatedBlocks = true;
            }
            if(world.getBlockState(pos5) == ModBlocks.END_ASH_DOOR_ACTIVATE.getDefaultState()) {
                nearbyActivatedBlocks = true;
            }
            if(world.getBlockState(pos6) == ModBlocks.END_ASH_DOOR_ACTIVATE.getDefaultState()) {
                nearbyActivatedBlocks = true;
            }
        }

        if(facing.equals(EnumFacing.UP)) {
            if(triggered && !nearbyActivatedBlocks) {

                BlockPos pos = this.getPos().add(new BlockPos(0, 1,0));
                for(int i = 0; i <= 7; i++) {
                    BlockPos modifiedPos = pos.add(new BlockPos(0, i, 0));
                    if(!world.getBlockState(modifiedPos).isFullBlock() && !world.getBlockState(modifiedPos).isBlockNormalCube() && !world.getBlockState(modifiedPos).isFullCube() && !world.getBlockState(modifiedPos).isSideSolid(world, modifiedPos, facing.getOpposite())) {

                        world.setBlockState(modifiedPos, ModBlocks.END_BARRIER.getDefaultState());
                    }
                }


            }
            if(!triggered || nearbyActivatedBlocks) {

                for(int i = 0; i <= 7; i++) {
                    BlockPos pos = this.getPos().add(new BlockPos(0, 1,0));
                    BlockPos modifiedPos = pos.add(new BlockPos(0, i, 0));
                    if(world.getBlockState(modifiedPos) == ModBlocks.END_BARRIER.getDefaultState()) {

                        world.setBlockToAir(modifiedPos);
                    }
                }


            }
        }
    if(facing.equals(EnumFacing.DOWN)) {
        if(triggered && !nearbyActivatedBlocks) {

            BlockPos pos = this.getPos().add(new BlockPos(0, -1,0));
            for(int i = 0; i <= 7; i++) {
                BlockPos modifiedPos = pos.add(new BlockPos(0, -i, 0));
                if(!world.getBlockState(modifiedPos).isFullBlock() && !world.getBlockState(modifiedPos).isBlockNormalCube() && !world.getBlockState(modifiedPos).isFullCube() && !world.getBlockState(modifiedPos).isSideSolid(world, modifiedPos, facing.getOpposite())) {

                    world.setBlockState(modifiedPos, ModBlocks.END_BARRIER.getDefaultState());
                }
            }


        }
        if(!triggered || nearbyActivatedBlocks) {

            for(int i = 0; i <= 7; i++) {
                BlockPos pos = this.getPos().add(new BlockPos(0, -1,0));
                BlockPos modifiedPos = pos.add(new BlockPos(0, -i, 0));
                if(world.getBlockState(modifiedPos) == ModBlocks.END_BARRIER.getDefaultState()) {

                    world.setBlockToAir(modifiedPos);
                }
            }


        }
    }

        if(facing.equals(EnumFacing.WEST)) {
            if(triggered && !nearbyActivatedBlocks) {

                BlockPos pos = this.getPos().add(new BlockPos(-1, 0,0));
                for(int i = 0; i <= 7; i++) {
                    BlockPos modifiedPos = pos.add(new BlockPos(-i, 0, 0));
                    if(!world.getBlockState(modifiedPos).isFullBlock() && !world.getBlockState(modifiedPos).isBlockNormalCube() && !world.getBlockState(modifiedPos).isFullCube() && !world.getBlockState(modifiedPos).isSideSolid(world, modifiedPos, facing.getOpposite())) {

                        world.setBlockState(modifiedPos, ModBlocks.END_BARRIER.getDefaultState());
                    }
                }


            }
            if(!triggered || nearbyActivatedBlocks) {

                for(int i = 0; i <= 7; i++) {
                    BlockPos pos = this.getPos().add(new BlockPos(-1, 0,0));
                    BlockPos modifiedPos = pos.add(new BlockPos(-i, 0, 0));
                    if(world.getBlockState(modifiedPos) == ModBlocks.END_BARRIER.getDefaultState()) {

                        world.setBlockToAir(modifiedPos);
                    }
                }


            }
        }

        if(facing.equals(EnumFacing.EAST)) {
            if(triggered && !nearbyActivatedBlocks) {

                BlockPos pos = this.getPos().add(new BlockPos(1, 0,0));
                for(int i = 0; i <= 7; i++) {
                    BlockPos modifiedPos = pos.add(new BlockPos(i, 0, 0));
                    if(!world.getBlockState(modifiedPos).isFullBlock() && !world.getBlockState(modifiedPos).isBlockNormalCube() && !world.getBlockState(modifiedPos).isFullCube() && !world.getBlockState(modifiedPos).isSideSolid(world, modifiedPos, facing.getOpposite())) {

                        world.setBlockState(modifiedPos, ModBlocks.END_BARRIER.getDefaultState());
                    }
                }


            }
            if(!triggered || nearbyActivatedBlocks) {

                for(int i = 0; i <= 7; i++) {
                    BlockPos pos = this.getPos().add(new BlockPos(1, 0,0));
                    BlockPos modifiedPos = pos.add(new BlockPos(i, 0, 0));
                    if(world.getBlockState(modifiedPos) == ModBlocks.END_BARRIER.getDefaultState()) {
                        world.setBlockToAir(modifiedPos);
                    }
                }


            }
        }

        if(facing.equals(EnumFacing.NORTH)) {
            if(triggered && !nearbyActivatedBlocks) {

                BlockPos pos = this.getPos().add(new BlockPos(0, 0,-1));
                for(int i = 0; i <= 7; i++) {
                    BlockPos modifiedPos = pos.add(new BlockPos(0, 0, -i));
                    if(!world.getBlockState(modifiedPos).isFullBlock() && !world.getBlockState(modifiedPos).isBlockNormalCube() && !world.getBlockState(modifiedPos).isFullCube() && !world.getBlockState(modifiedPos).isSideSolid(world, modifiedPos, facing.getOpposite())) {

                        world.setBlockState(modifiedPos, ModBlocks.END_BARRIER.getDefaultState());
                    }
                }


            }
            if(!triggered || nearbyActivatedBlocks) {

                for(int i = 0; i <= 7; i++) {
                    BlockPos pos = this.getPos().add(new BlockPos(0, 0,-1));
                    BlockPos modifiedPos = pos.add(new BlockPos(0, 0, -i));
                    if(world.getBlockState(modifiedPos) == ModBlocks.END_BARRIER.getDefaultState()) {

                        world.setBlockToAir(modifiedPos);
                    }
                }


            }
        }


        if(facing.equals(EnumFacing.SOUTH)) {
            if(triggered && !nearbyActivatedBlocks) {

                BlockPos pos = this.getPos().add(new BlockPos(0, 0,1));
                for(int i = 0; i <= 7; i++) {
                    BlockPos modifiedPos = pos.add(new BlockPos(0, 0, i));
                    if(!world.getBlockState(modifiedPos).isFullBlock() && !world.getBlockState(modifiedPos).isBlockNormalCube() && !world.getBlockState(modifiedPos).isFullCube() && !world.getBlockState(modifiedPos).isSideSolid(world, modifiedPos, facing.getOpposite())) {

                        world.setBlockState(modifiedPos, ModBlocks.END_BARRIER.getDefaultState());
                    } else {
                        return;
                    }
                }


            }
            if(!triggered || nearbyActivatedBlocks) {

                for(int i = 0; i <= 7; i++) {
                    BlockPos pos = this.getPos().add(new BlockPos(0, 0,1));
                    BlockPos modifiedPos = pos.add(new BlockPos(0, 0, i));
                    if(world.getBlockState(modifiedPos) == ModBlocks.END_BARRIER.getDefaultState()) {

                        world.setBlockToAir(modifiedPos);
                    }
                }


            }
        }


    }
}
