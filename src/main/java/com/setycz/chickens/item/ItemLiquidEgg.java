package com.setycz.chickens.item;

import java.util.List;

import javax.annotation.Nullable;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.handler.LiquidEggFluidWrapper;
import com.setycz.chickens.registry.LiquidEggRegistry;
import com.setycz.chickens.registry.LiquidEggRegistryItem;

import init.ModItemGroups;
import init.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EggItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import static com.setycz.chickens.item.utils.NbtUtils.applyLiquidRegistryNameToItemStackNbt;
import static com.setycz.chickens.item.utils.NbtUtils.getLiquidRegistryNameFromStack;

/**
 * Created by setyc on 14.02.2016.
 */
public class ItemLiquidEgg extends EggItem {
    public ItemLiquidEgg() {
        super(new Properties().group(ModItemGroups.CHICKENS_TAB));
        setRegistryName(new ResourceLocation(ChickensMod.MODID, "liquid_egg"));
    }

    public static ItemStack from(LiquidEggRegistryItem liquid) {
        ItemStack itemStack = new ItemStack(ModItems.LIQUIDEGG);
        applyLiquidRegistryNameToItemStackNbt(itemStack, liquid.getResourceLocation());
        return itemStack;
    }

    @Override
    public String getTranslationKey() {
        return "liquid_egg";
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(TextComponentUtils.toTextComponent(() -> I18n.format("item.liquid_egg.tooltip")));
    }

    @Override
    public void fillItemGroup(ItemGroup itemGroup, NonNullList<ItemStack> itemStacks) {
        if (!isInGroup(group)){
            return;
        }

        for (LiquidEggRegistryItem liquid : LiquidEggRegistry.getAll()) {
            itemStacks.add(from(liquid));
        }
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        String registryName = getLiquidRegistryNameFromStack(stack);
        Block liquid = LiquidEggRegistry.getByRegistryName(registryName).getLiquid();

        return TextComponentUtils.toTextComponent(() ->I18n.format(getTranslationKey() + "." + liquid.getTranslationKey().substring(5) + ".name"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.NONE);

        if (raytraceresult == null) {
            return new ActionResult<ItemStack>(ActionResultType.PASS, itemStackIn);
        } else if (!(raytraceresult instanceof BlockRayTraceResult)) {
            return new ActionResult<ItemStack>(ActionResultType.PASS, itemStackIn);
        } else {
            BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) raytraceresult;
            BlockPos blockpos = blockRayTraceResult.getPos();
            if (!worldIn.isBlockModifiable(playerIn, blockpos)) {
                return new ActionResult<ItemStack>(ActionResultType.FAIL, itemStackIn);
            } else {
                boolean isBlockHitEditable = !playerIn.canPlayerEdit(blockpos, blockRayTraceResult.getFace(), itemStackIn);
                BlockPos blockPos1 = isBlockHitEditable && blockRayTraceResult.getFace() == Direction.UP ? blockpos : blockpos.offset(blockRayTraceResult.getFace());

                Block liquid = LiquidEggRegistry.getByRegistryName(getLiquidRegistryNameFromStack(itemStackIn)).getLiquid();

                if (!playerIn.canPlayerEdit(blockPos1, blockRayTraceResult.getFace(), itemStackIn)) {
                    return new ActionResult<ItemStack>(ActionResultType.FAIL, itemStackIn);
                } else if (this.tryPlaceContainedLiquid(playerIn, worldIn, blockPos1, liquid)) {
                    return !playerIn.isCreative() ? new ActionResult<ItemStack>(ActionResultType.SUCCESS, new ItemStack(itemStackIn.getItem(), itemStackIn.getCount() - 1)) : new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemStackIn);
                } else {
                    return new ActionResult<ItemStack>(ActionResultType.FAIL, itemStackIn);
                }
            }
        }
    }

    public boolean tryPlaceContainedLiquid(@Nullable PlayerEntity playerIn, World worldIn, BlockPos pos, Block liquid) {
        Material material = worldIn.getBlockState(pos).getMaterial();
        boolean flag = !material.isSolid();

        if (!worldIn.isAirBlock(pos) && !flag) {
            return false;
        } else {
            if (worldIn.dimension.doesWaterVaporize() && liquid == Blocks.WATER) {
                int i = pos.getX();
                int j = pos.getY();
                int k = pos.getZ();
                worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
                for (int l = 0; l < 8; ++l) {
                    worldIn.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
                }
            } else {
                if (!worldIn.isRemote && flag && !material.isLiquid()) {
                    worldIn.destroyBlock(pos, true);
                }

                worldIn.setBlockState(pos, liquid.getDefaultState(), 3);
            }

            return true;
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return new LiquidEggFluidWrapper(stack);
    }
}
