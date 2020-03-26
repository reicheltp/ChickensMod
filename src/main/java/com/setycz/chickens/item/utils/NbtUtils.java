package com.setycz.chickens.item.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class NbtUtils {

    public static final String CHICKEN_TYPE_NBT_KEY = "ChickenType";
    public static final String LIQUID_TYPE_NBT_KEY = "LiquidType";
    public static final String ID_TYPE_NBT_KEY = "id";

    public static void applyChickenRegistryNameToItemStackNbt(ItemStack stack, ResourceLocation entityId)
    {
        CompoundNBT nbtTagCompound = stack.hasTag() ? stack.getTag() : new CompoundNBT();
        CompoundNBT chickenTypeNbtTagCompound = new CompoundNBT();

        chickenTypeNbtTagCompound.putString(ID_TYPE_NBT_KEY, entityId.toString());
        nbtTagCompound.put(CHICKEN_TYPE_NBT_KEY, chickenTypeNbtTagCompound);

        stack.setTag(nbtTagCompound);
    }

    public static void applyLiquidRegistryNameToItemStackNbt(ItemStack stack, ResourceLocation entityId)
    {
        CompoundNBT nbtTagCompound = stack.hasTag() ? stack.getTag() : new CompoundNBT();
        CompoundNBT liquidTypeNbtTagCompound = new CompoundNBT();

        liquidTypeNbtTagCompound.putString(ID_TYPE_NBT_KEY, entityId.toString());
        nbtTagCompound.put(LIQUID_TYPE_NBT_KEY, liquidTypeNbtTagCompound);

        stack.setTag(nbtTagCompound);
    }

    public static String getChickenRegistryNameFromStack(ItemStack stack)
    {
        CompoundNBT nbtTagCompound = stack.getTag();

        if (nbtTagCompound != null && nbtTagCompound.contains(CHICKEN_TYPE_NBT_KEY))
        {
            CompoundNBT chickentag = nbtTagCompound.getCompound(CHICKEN_TYPE_NBT_KEY);

            return chickentag.getString(ID_TYPE_NBT_KEY);
        }

        return null;
    }

    public static String getLiquidRegistryNameFromStack(ItemStack stack)
    {
        CompoundNBT nbtTagCompound = stack.getTag();

        if (nbtTagCompound != null && nbtTagCompound.contains(LIQUID_TYPE_NBT_KEY))
        {
            CompoundNBT liquidtag = nbtTagCompound.getCompound(LIQUID_TYPE_NBT_KEY);

            return liquidtag.getString(ID_TYPE_NBT_KEY);
        }

        return null;
    }
}
