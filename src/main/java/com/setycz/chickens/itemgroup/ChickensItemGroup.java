package com.setycz.chickens.itemgroup;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;

/**
 * Created by setyc on 12.02.2016.
 */
public class ChickensItemGroup extends ItemGroup {

    public ChickensItemGroup() {
        super("chickens");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Items.CHICKEN);
    }

}
