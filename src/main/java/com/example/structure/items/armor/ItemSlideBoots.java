package com.example.structure.items.armor;

import com.example.structure.util.ModUtils;
import net.minecraft.block.BlockIce;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemSlideBoots extends ModArmorBase{

    private static Set<EnumFacing> walls = new HashSet<EnumFacing>();
    protected boolean isSkating = false;
    protected int dmgOnSecUsage = 40;

    public ItemSlideBoots(String name, ArmorMaterial materialIn, int renderIdx, EntityEquipmentSlot slotIn, String textureName) {
        super(name, materialIn, renderIdx, slotIn, textureName);

    }


    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {


        super.onArmorTick(world, player, itemStack);
        if (itemStack != null && itemStack.getItem() == this) {
            Vec3d playerVel = player.getLookVec().scale(1.4f);
           if(player.isSneaking() && player.canBePushed()) {
                player.motionX = playerVel.x;
                player.motionZ = playerVel.z;
                player.velocityChanged = true;
                isSkating = true;
           } else {
            isSkating =false;
           }


           if(this.isSkating) {
               dmgOnSecUsage--;
           }
           if(dmgOnSecUsage < 0) {
               itemStack.damageItem(1, player);
               this.dmgOnSecUsage = 40;
           }


        }
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);


      //  tooltip.add(ModUtils.translateDesc(TextFormatting.LIGHT_PURPLE + "skate_boots"));
    }
}
