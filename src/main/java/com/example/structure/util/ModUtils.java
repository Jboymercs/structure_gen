package com.example.structure.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ModUtils {
    //VERY IMPORTANT

    public static int getAverageGroundHeight(World world, int x, int z, int sizeX, int sizeZ, int maxVariation) {
        sizeX = x + sizeX;
        sizeZ = z + sizeZ;
        int corner1 = calculateGenerationHeight(world, x, z);
        int corner2 = calculateGenerationHeight(world, sizeX, z);
        int corner3 = calculateGenerationHeight(world, x, sizeZ);
        int corner4 = calculateGenerationHeight(world, sizeX, sizeZ);

        int max = Math.max(Math.max(corner3, corner4), Math.max(corner1, corner2));
        int min = Math.min(Math.min(corner3, corner4), Math.min(corner1, corner2));
        if (max - min > maxVariation) {
            return -1;
        }
        return min;
    }

    public static int calculateGenerationHeight(World world, int x, int z) {
        return world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();
    }

    public static AxisAlignedBB makeBox(Vec3d pos1, Vec3d pos2) {
        return new AxisAlignedBB(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
    }


    public static Vec3d getRelativeOffset(EntityLivingBase actor, Vec3d offset) {
        Vec3d look = ModUtils.getVectorForRotation(0, actor.renderYawOffset);
        Vec3d side = look.rotateYaw((float) Math.PI * 0.5f);
        return look.scale(offset.x).add(yVec((float) offset.y)).add(side.scale(offset.z));
    }

    public static Vec3d yVec(double heightAboveGround) {
        return new Vec3d(0, heightAboveGround, 0);
    }

    public static Vec3d getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d(f1 * f2, f3, f * f2);
    }

    public static void handleAreaImpact(float radius, Function<Entity, Float> maxDamage, Entity source, Vec3d pos, DamageSource damageSource,
                                        float knockbackFactor, int fireFactor, boolean damageDecay) {
        if (source == null) {
            return;
        }

        List<Entity> list = source.world.getEntitiesWithinAABBExcludingEntity(source, new AxisAlignedBB(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z).grow(radius));

        Predicate<Entity> isInstance = i -> i instanceof EntityLivingBase || i instanceof MultiPartEntityPart || i.canBeCollidedWith();
        double radiusSq = Math.pow(radius, 2);

        list.stream().filter(isInstance).forEach((entity) -> {

            // Get the hitbox size of the entity because otherwise explosions are less
            // effective against larger mobs
            double avgEntitySize = entity.getEntityBoundingBox().getAverageEdgeLength() * 0.75;

            // Choose the closest distance from the center or the head to encourage
            // headshots
            double distance = Math.min(Math.min(getCenter(entity.getEntityBoundingBox()).distanceTo(pos),
                            entity.getPositionVector().add(ModUtils.yVec(entity.getEyeHeight())).distanceTo(pos)),
                    entity.getPositionVector().distanceTo(pos));

            // Subtracting the average size makes it so that the full damage can be dealt
            // with a direct hit
            double adjustedDistance = Math.max(distance - avgEntitySize, 0);
            double adjustedDistanceSq = Math.pow(adjustedDistance, 2);
            double damageFactor = damageDecay ? Math.max(0, Math.min(1, (radiusSq - adjustedDistanceSq) / radiusSq)) : 1;

            // Damage decays by the square to make missed impacts less powerful
            double damageFactorSq = Math.pow(damageFactor, 2);
            double damage = maxDamage.apply(entity) * damageFactorSq;
            if (damage > 0 && adjustedDistanceSq < radiusSq) {
                entity.setFire((int) (fireFactor * damageFactorSq));
                if(entity.attackEntityFrom(damageSource, (float) damage)) {
                    double entitySizeFactor = avgEntitySize == 0 ? 1 : Math.max(0.5, Math.min(1, 1 / avgEntitySize));
                    double entitySizeFactorSq = Math.pow(entitySizeFactor, 2);

                    // Velocity depends on the entity's size and the damage dealt squared
                    Vec3d velocity = getCenter(entity.getEntityBoundingBox()).subtract(pos).normalize().scale(damageFactorSq).scale(knockbackFactor).scale(entitySizeFactorSq);
                    entity.addVelocity(velocity.x, velocity.y, velocity.z);
                }
            }
        });
    }

    public static void handleAreaImpact(float radius, Function<Entity, Float> maxDamage, Entity source, Vec3d pos, DamageSource damageSource) {
        handleAreaImpact(radius, maxDamage, source, pos, damageSource, 1, 0);
    }

    public static void handleAreaImpact(float radius, Function<Entity, Float> maxDamage, Entity source, Vec3d pos, DamageSource damageSource,
                                        float knockbackFactor, int fireFactor) {
        handleAreaImpact(radius, maxDamage, source, pos, damageSource, knockbackFactor, fireFactor, true);
    }

    private static Vec3d getCenter(AxisAlignedBB box) {
        return new Vec3d(box.minX + (box.maxX - box.minX) * 0.5D, box.minY + (box.maxY - box.minY) * 0.5D, box.minZ + (box.maxZ - box.minZ) * 0.5D);
    }

    public static void leapTowards(EntityLivingBase entity, Vec3d target, float horzVel, float yVel) {
        Vec3d dir = target.subtract(entity.getPositionVector()).normalize();
        Vec3d leap = new Vec3d(dir.x, 0, dir.z).normalize().scale(horzVel).add(ModUtils.yVec(yVel));
        entity.motionX += leap.x;
        if (entity.motionY < 0.1) {
            entity.motionY += leap.y;
        }
        entity.motionZ += leap.z;

        // Normalize to make sure the velocity doesn't go beyond what we expect
        double horzMag = Math.sqrt(Math.pow(entity.motionX, 2) + Math.pow(entity.motionZ, 2));
        double scale = horzVel / horzMag;
        if (scale < 1) {
            entity.motionX *= scale;
            entity.motionZ *= scale;
        }
    }
    // Act as a quick dash of sorts from it's location to the Player's location, in a fluid line. Used mostly for the Crystal boss
    public static void leapDash (EntityLivingBase entity, Vec3d target, float horzVel, float targetYValue) {
        Vec3d dir = target.subtract(entity.getPositionVector().normalize());
        Vec3d leap = new Vec3d(dir.x, 0, dir.z).normalize();
        if(entity.posY > targetYValue) {
            entity.motionY -= leap.y;
        }
        if(entity.motionY < targetYValue) {
            entity.motionY += leap.y;
        }

        entity.motionZ += leap.x;
        entity.motionZ += leap.z;

        double horzMag = Math.sqrt(Math.pow(entity.motionX, 2) + Math.pow(entity.motionZ, 2));
        double scale = horzVel / horzMag;
        if(scale < 1) {
            entity.motionX *= scale;
            entity.motionZ *= scale;
        }

    }


}
