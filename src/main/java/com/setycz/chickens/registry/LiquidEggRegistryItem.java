package com.setycz.chickens.registry;

import com.setycz.chickens.ChickensMod;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;

/**
 * Created by setyc on 14.02.2016.
 */
public class LiquidEggRegistryItem {
    private final String NAME;
    private final ResourceLocation RESOURCELOCATION;
    private final Block LIQUID;
    private final int EGGCOLOR;
    private final Fluid FLUID;

    public LiquidEggRegistryItem(String name, Block liquid, int eggColor, Fluid fluid) {
        this.NAME = name;
        this.LIQUID = liquid;
        this.EGGCOLOR = eggColor;
        this.FLUID = fluid;
        this.RESOURCELOCATION = new ResourceLocation(ChickensMod.MODID, name);
    }

    public String getRegistryName(){
        return RESOURCELOCATION.toString();
    }

    public String getNAME(){
        return NAME;
    }

    public ResourceLocation getResourceLocation(){
        return RESOURCELOCATION;
    }

    public Block getLiquid() {
        return LIQUID;
    }

    public int getEggColor() {
        return EGGCOLOR;
    }

    public Fluid getFluid() {
        return FLUID;
    }
}
