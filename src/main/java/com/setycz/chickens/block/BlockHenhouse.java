package com.setycz.chickens.block;

import java.util.List;

import com.setycz.chickens.ChickensMod;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Created by setyc on 01.03.2016.
 */
public class BlockHenhouse extends ContainerBlock {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", Direction.Plane.HORIZONTAL);

    public BlockHenhouse() {
        super(Material.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, Direction.NORTH));
        this.setHardness(2);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
    	super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.translateToLocal("tile.henhouse.tooltip"));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityHenhouse();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, BlockState state) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityHenhouse) {
            ((TileEntityHenhouse) tileEntity).dropContents();
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, Hand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

        if (stack.hasDisplayName()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityHenhouse) {
                ((TileEntityHenhouse) tileEntity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    @Deprecated
    public BlockState getStateFromMeta(int meta) {
        Direction enumfacing = Direction.byIndex(meta);
        if (enumfacing.getAxis() == Direction.Axis.Y) {
            enumfacing = Direction.NORTH;
        }
        return getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    @Deprecated
    public BlockState withRotation(BlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    @Deprecated
    public BlockState withMirror(BlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, Direction side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof TileEntityHenhouse) {
                playerIn.openGui(ChickensMod.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }
}
