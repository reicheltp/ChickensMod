package com.setycz.chickens.block;

import java.util.List;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * Created by setyc on 01.03.2016.
 */
public class BlockHenhouse extends ContainerBlock {
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

    public BlockHenhouse() {
        super(Properties.create(Material.WOOD).hardnessAndResistance(2));
        this.getDefaultState().with(FACING, Direction.NORTH);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader blockReader, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        super.addInformation(stack, blockReader, tooltip, advanced);
        tooltip.add(TextComponentUtils.toTextComponent(() -> I18n.format("tile.henhouse.tooltip")));
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader iBlockReader) {
        return new TileEntityHenhouse();
    }

    @Override
    public void onReplaced(BlockState oldBlockState, World world, BlockPos blockPos, BlockState newBlockState, boolean p_196243_5_) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TileEntityHenhouse) {
            ((TileEntityHenhouse) tileEntity).dropContents();
        }

        super.onReplaced(oldBlockState, world, blockPos, newBlockState, p_196243_5_);
    }

    @Override
    public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, Hand hand) {
        return this.getDefaultState().with(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.with(FACING, placer.getHorizontalFacing().getOpposite()), 2);

        if (stack.hasDisplayName()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityHenhouse) {
                ((TileEntityHenhouse) tileEntity).setCustomName(stack.getDisplayName().getFormattedText());
            }
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    @Deprecated
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    @Deprecated
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    public ActionResultType func_225533_a_(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityHenhouse) {
                NetworkHooks.openGui((ServerPlayerEntity) playerIn, getContainer(state, worldIn, pos), pos);
            }
        }
        return ActionResultType.PASS;
    }
}
