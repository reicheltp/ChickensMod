package com.setycz.chickens.item;

import java.util.List;

import javax.annotation.Nullable;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.handler.IColorSource;
import com.setycz.chickens.handler.LiquidEggFluidWrapper;
import com.setycz.chickens.registry.LiquidEggRegistry;
import com.setycz.chickens.registry.LiquidEggRegistryItem;

import init.ModItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EggItem;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by setyc on 14.02.2016.
 */
public class ItemLiquidEgg extends EggItem implements IColorSource {
    public ItemLiquidEgg() {
        super(new Properties().group(ModItemGroups.CHICKENS_TAB));
        setRegistryName(new ResourceLocation(ChickensMod.MODID, "liquid_egg"));
    }

    @Override
    public String getTranslationKey() {
        return "liquid_egg";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack,  World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(I18n.translateToLocal("item.liquid_egg.tooltip"));
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab))
        {
        	for (LiquidEggRegistryItem liquid : LiquidEggRegistry.getAll()) {
        		subItems.add(new ItemStack(this, 1, liquid.getId()));
        	}
        }
        
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return LiquidEggRegistry.findById(stack.getMetadata()).getEggColor();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Block liquid = LiquidEggRegistry.findById(stack.getMetadata()).getLiquid();
        return I18n.translateToLocal(getTranslationKey() + "." + liquid.getTranslationKey().substring(5) + ".name");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, false);

        //noinspection ConstantConditions
        if (raytraceresult == null) {
            return new ActionResult<ItemStack>(ActionResultType.PASS, itemStackIn);
        } else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
            return new ActionResult<ItemStack>(ActionResultType.PASS, itemStackIn);
        } else {
            BlockPos blockpos = raytraceresult.getBlockPos();
            if (!worldIn.isBlockModifiable(playerIn, blockpos)) {
                return new ActionResult<ItemStack>(ActionResultType.FAIL, itemStackIn);
            } else {
                boolean flag1 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
                BlockPos blockPos1 = flag1 && raytraceresult.sideHit == Direction.UP ? blockpos : blockpos.offset(raytraceresult.sideHit);

                Block liquid = LiquidEggRegistry.findById(itemStackIn.getMetadata()).getLiquid();
                if (!playerIn.canPlayerEdit(blockPos1, raytraceresult.sideHit, itemStackIn)) {
                    return new ActionResult<ItemStack>(ActionResultType.FAIL, itemStackIn);
                } else if (this.tryPlaceContainedLiquid(playerIn, worldIn, blockPos1, liquid)) {
                    //noinspection ConstantConditions
                    playerIn.addStat(Stats.getObjectUseStats(this));
                    return !playerIn.capabilities.isCreativeMode ? new ActionResult<ItemStack>(ActionResultType.SUCCESS, new ItemStack(itemStackIn.getItem(), itemStackIn.getCount() - 1, itemStackIn.getMetadata())) : new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemStackIn);
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
            if (worldIn.provider.doesWaterVaporize() && liquid == Blocks.FLOWING_WATER) {
                int i = pos.getX();
                int j = pos.getY();
                int k = pos.getZ();
                worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

                for (int l = 0; l < 8; ++l) {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
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
