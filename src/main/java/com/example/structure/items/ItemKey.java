package com.example.structure.items;

import com.example.structure.init.ModBlocks;
import com.example.structure.util.ModUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemKey extends ItemBase{
    private String info_loc;

    public ItemKey(String name, String info_loc, CreativeTabs tab) {
        super(name, tab);
        this.info_loc = info_loc;
        this.setMaxDamage(1);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.LIGHT_PURPLE + ModUtils.translateDesc(info_loc));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        Vec3d playerLook = player.getLookVec();
        BlockPos look = new BlockPos(playerLook.x, playerLook.y, playerLook.z);
        if(worldIn.getBlockState(look) == ModBlocks.END_KEY_BLOCK) {
            stack.damageItem(1, player);
        }



        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }
}
