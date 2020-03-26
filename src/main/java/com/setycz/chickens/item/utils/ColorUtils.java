package com.setycz.chickens.item.utils;

import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class ColorUtils {
    public Item getDyeItemFromDye(DyeColor color){
        switch (color){
            case RED:
                return Items.RED_DYE;
            case BLUE:
                return Items.BLUE_DYE;
            case CYAN:
                return Items.CYAN_DYE;
            case GRAY:
                return Items.GRAY_DYE;
            case LIME:
                return Items.LIME_DYE;
            case PINK:
                return Items.PINK_DYE;
            case BLACK:
                return Items.BLACK_DYE;
            case BROWN:
                return Items.BROWN_DYE;
            case GREEN:
                return Items.GREEN_DYE;
            case WHITE:
                return Items.WHITE_DYE;
            case ORANGE:
                return Items.ORANGE_DYE;
            case LIGHT_BLUE:
                return Items.LIGHT_BLUE_DYE;
            case PURPLE:
                return Items.PURPLE_DYE;
            case YELLOW:
                return Items.YELLOW_DYE;
            case MAGENTA:
                return Items.MAGENTA_DYE;
            case LIGHT_GRAY:
                return Items.LIGHT_GRAY_DYE;
            default:
                return null;
        }
    }
}
