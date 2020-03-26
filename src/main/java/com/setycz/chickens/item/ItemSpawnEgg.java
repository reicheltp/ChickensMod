package com.setycz.chickens.item;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.entity.EntityChickensChicken;
import com.setycz.chickens.handler.IColorSource;
import com.setycz.chickens.registry.ChickensRegistry;
import com.setycz.chickens.registry.ChickensRegistryItem;

import init.ModItemGroups;
import init.ModItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.World;

import static com.setycz.chickens.item.utils.NbtUtils.applyChickenRegistryNameToItemStackNbt;
import static com.setycz.chickens.item.utils.NbtUtils.getChickenRegistryNameFromStack;

/**
 * Created by setyc on 12.02.2016.
 */
@SuppressWarnings("deprecation")
public class ItemSpawnEgg extends Item implements IColorSource {

    public ItemSpawnEgg() {
        super(new Properties().group(ModItemGroups.CHICKENS_TAB));
        setRegistryName(new ResourceLocation(ChickensMod.MODID, "spawn_egg"));
    }

    public static ItemStack from(ChickensRegistryItem chicken){
        ItemStack chickenItemStack = new ItemStack(ModItems.ITEMSPAWNEGG);
        applyChickenRegistryNameToItemStackNbt(chickenItemStack, chicken.getRegistryName());
        return chickenItemStack;
    }

    @Override
    public String getTranslationKey() {
        return "spawn_egg";
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
        ChickensRegistryItem chickenDescription = ChickensRegistry.getByRegistryName(getChickenRegistryNameFromStack(stack));
        if(chickenDescription == null)
            return null;

        return TextComponentUtils.toTextComponent(() -> I18n.format("entity." + chickenDescription.getEntityName() + ".name"));
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        ChickensRegistryItem chickenDescription = ChickensRegistry.getByRegistryName(getChickenRegistryNameFromStack(stack));
        if(chickenDescription == null) return 0000000;
        return renderPass == 0 ? chickenDescription.getBgColor() : chickenDescription.getFgColor();
    }



    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        PlayerEntity playerIn = context.getPlayer();
        World worldIn = context.getWorld();

        if (!worldIn.isRemote) {
            ItemStack stack = playerIn.getHeldItem(context.getHand());
            BlockPos correlatedPos = correctPosition(context.getPos(), context.getFace());
            activate(stack, worldIn, correlatedPos);
            if (!playerIn.isCreative()) {
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

    private void activate(ItemStack stack, World worldIn, BlockPos pos) {
        EntityChickensChicken entity = new EntityChickensChicken(worldIn);
        if (entity == null) {
            return;
        }

        entity.onInitialSpawn(worldIn.getDifficultyForLocation(pos), null);
        entity.setChickenType(getChickenRegistryNameFromStack(stack));

        CompoundNBT stackNBT = stack.serializeNBT();

        if (stackNBT != null) {
            entity.writeEntityToNBT(new CompoundNBT());
            CompoundNBT entityNBT = entity.serializeNBT();
            entityNBT.merge(stackNBT);
            entity.readEntityFromNBT(entityNBT);
        }

        BlockPos spawnPos = new BlockPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        entity.getType().spawn(worldIn, null, null, spawnPos, SpawnReason.MOB_SUMMONED, true, false);
    }
}
