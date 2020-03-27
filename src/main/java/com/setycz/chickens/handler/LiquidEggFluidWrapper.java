package com.setycz.chickens.handler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.setycz.chickens.registry.LiquidEggRegistry;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import static com.setycz.chickens.item.utils.NbtUtils.getLiquidRegistryNameFromStack;
import static net.minecraftforge.fluids.FluidAttributes.BUCKET_VOLUME;

/**
 * Created by setyc on 13.12.2016.
 */
public class LiquidEggFluidWrapper implements IFluidHandler, IFluidHandlerItem, ICapabilityProvider {

    private final ItemStack container;

    public LiquidEggFluidWrapper(ItemStack container) {
        this.container = container;
    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(this);
        }
        return null;
    }

    //TODO What is this used for???
    @Override
    public int getTanks() {
        return 0;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int i) {
        return getFluid();
    }

    @Override
    public int getTankCapacity(int i) {
        return BUCKET_VOLUME;
    }

    @Override
    public boolean isFluidValid(int i, @Nonnull FluidStack fluidStack) {
        return fluidStack.getFluid() == Fluids.WATER || fluidStack.getFluid() == Fluids.LAVA;
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        return 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction fluidAction) {
        FluidStack fluidStack = getFluid();
        if (!resource.isFluidEqual(fluidStack)) {
            return null;
        }

        return drain(resource.getAmount(), fluidAction);
    }

    private FluidStack getFluid() {
        Fluid fluid = LiquidEggRegistry.getByRegistryName(getLiquidRegistryNameFromStack(container)).getFluid();
        return new FluidStack(fluid, BUCKET_VOLUME);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction fluidAction) {
        if (container.getCount() < 1 || maxDrain < BUCKET_VOLUME) {
            return null;
        }

        FluidStack fluidStack = getFluid();
        if (fluidAction == FluidAction.EXECUTE) {
            container.shrink(1);
        }

        return fluidStack;
    }

    /**
     * @return empty stack - item is consumable
     */
    @Override
    public ItemStack getContainer() {
        return ItemStack.EMPTY;
    }
}
