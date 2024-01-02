package com.example.structure.items;

import com.example.structure.Main;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemModBook extends ItemBase{
    public ItemModBook(String name, CreativeTabs tab) {
        super(name, tab);
        this.setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if(worldIn.isRemote){
            Main.proxy.openGuiBook(stack);
        }
        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }
}
