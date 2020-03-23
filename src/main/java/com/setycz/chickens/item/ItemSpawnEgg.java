package com.setycz.chickens.item;

import javax.annotation.Nullable;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.entity.EntityChickensChicken;
import com.setycz.chickens.handler.IColorSource;
import com.setycz.chickens.registry.ChickensRegistry;
import com.setycz.chickens.registry.ChickensRegistryItem;

import init.ModItemGroups;
import init.ModItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by setyc on 12.02.2016.
 */
@SuppressWarnings("deprecation")
public class ItemSpawnEgg extends Item implements IColorSource {

    public ItemSpawnEgg() {
        super(new Properties().group(ModItemGroups.Default));
        setRegistryName(new ResourceLocation(ChickensMod.MODID, "spawn_egg"));
    }

    public static ItemStack from(ChickensRegistryItem chicken){
        ItemStack chickenItemStack = new ItemStack(ModItems.ITEMSPAWNEGG);
        applyEntityIdToItemStack(chickenItemStack, chicken.getRegistryName());
        return chickenItemStack;
    }

    @Override
    public void fillItemGroup(ItemGroup itemGroup, NonNullList<ItemStack> items) {
        if (!isInGroup(group)){
            return;
        }

        for (ChickensRegistryItem chicken : ChickensRegistry.getItems() ){
            items.add(from(chicken));
        }
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        ChickensRegistryItem chickenDescription = ChickensRegistry.getByRegistryName(getTypeFromStack(stack));
        if(chickenDescription == null)
            return null;

        return TextComponentUtils.toTextComponent(() -> I18n.format("entity." + chickenDescription.getEntityName() + ".name"));
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        ChickensRegistryItem chickenDescription = ChickensRegistry.getByRegistryName(getTypeFromStack(stack));
        if(chickenDescription == null) return 0000000;
        return renderPass == 0 ? chickenDescription.getBgColor() : chickenDescription.getFgColor();
    }

    @Override
    public ActionResultType onItemUse(PlayerEntity playerIn, World worldIn, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            ItemStack stack = playerIn.getHeldItem(hand);
            BlockPos correlatedPos = correctPosition(pos, facing);
            activate(stack, worldIn, correlatedPos, stack.getMetadata());
            if (!playerIn.capabilities.isCreativeMode) {
                stack.shrink(1);
            }
        }
        return ActionResultType.SUCCESS;
    }

    private BlockPos correctPosition(BlockPos pos, Direction side) {
        final int[] offsetsXForSide = new int[]{0, 0, 0, 0, -1, 1};
        final int[] offsetsYForSide = new int[]{-1, 1, 0, 0, 0, 0};
        final int[] offsetsZForSide = new int[]{0, 0, -1, 1, 0, 0};

        int posX = pos.getX() + offsetsXForSide[side.ordinal()];
        int posY = pos.getY() + offsetsYForSide[side.ordinal()];
        int posZ = pos.getZ() + offsetsZForSide[side.ordinal()];

        return new BlockPos(posX, posY, posZ);
    }

    private void activate(ItemStack stack, World worldIn, BlockPos pos, int metadata) {
        ResourceLocation entityName = new ResourceLocation(ChickensMod.MODID, ChickensMod.CHICKEN);
        EntityChickensChicken entity = (EntityChickensChicken) EntityList.createEntityByIDFromName(entityName, worldIn);
        if (entity == null) {
            return;
        }
        entity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        entity.onInitialSpawn(worldIn.getDifficultyForLocation(pos), null);
        entity.setChickenType(getTypeFromStack(stack));

        CompoundNBT stackNBT = stack.getTagCompound();
        if (stackNBT != null) {
            CompoundNBT entityNBT = entity.writeToNBT(new CompoundNBT());
            entityNBT.merge(stackNBT);
            entity.readEntityFromNBT(entityNBT);
        }

        worldIn.spawnEntity(entity);
    }
    
    
    public static void applyEntityIdToItemStack(ItemStack stack, ResourceLocation entityId)
    {
        CompoundNBT nbttagcompound = stack.hasTagCompound() ? stack.getTagCompound() : new CompoundNBT();
        CompoundNBT nbttagcompound1 = new CompoundNBT();
        nbttagcompound1.setString("id", entityId.toString());
        nbttagcompound.setTag("ChickenType", nbttagcompound1);
        stack.setTagCompound(nbttagcompound);
    }
    
    
    /**
     * Applies the data in the EntityTag tag of the given ItemStack to the given Entity.
     * @return 
     */
    @Nullable
    public static String getTypeFromStack(ItemStack stack)
    {
    	CompoundNBT nbttagcompound = stack.getTagCompound();

    	if (nbttagcompound != null && nbttagcompound.hasKey("ChickenType", 10))
    	{
    		new CompoundNBT();
    		CompoundNBT chickentag = nbttagcompound.getCompoundTag("ChickenType");
    		
    		return chickentag.getString("id");
    	}
    	
    	return null;
    }
    
   
}
