package com.setycz.chickens.proxy;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.block.BlockHenhouse;
import com.setycz.chickens.block.TileEntityHenhouse;
import com.setycz.chickens.entity.EntityColoredEgg;
import com.setycz.chickens.item.ItemAnalyzer;
import com.setycz.chickens.item.ItemColoredEgg;
import com.setycz.chickens.item.ItemLiquidEgg;
import com.setycz.chickens.item.ItemSpawnEgg;
import com.setycz.chickens.registry.LiquidEggRegistry;
import com.setycz.chickens.registry.LiquidEggRegistryItem;
import init.ModItemGroups;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonProxy {

    public void preInit() {

    }

    public void init() {

    }

    public static final Item spawnEgg = new ItemSpawnEgg();
    public static final Item coloredEgg = new ItemColoredEgg();
    public static final Item liquidEgg = new ItemLiquidEgg();
    public static final Item analyzer = new ItemAnalyzer();

    public static final Block henhouse = new BlockHenhouse("henhouse");
    public static final Block henhouse_acacia = new BlockHenhouse("henhouse_acacia");
    public static final Block henhouse_birch = new BlockHenhouse("henhouse_birch");
    public static final Block henhouse_dark_oak = new BlockHenhouse("henhouse_dark_oak");
    public static final Block henhouse_jungle = new BlockHenhouse("henhouse_jungle");
    public static final Block henhouse_spruce = new BlockHenhouse("henhouse_spruce");

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                henhouse,
                henhouse_acacia,
                henhouse_birch,
                henhouse_dark_oak,
                henhouse_jungle,
                henhouse_spruce
        );
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                coloredEgg,
                spawnEgg,
                liquidEgg,
                analyzer,
                getItemBlockForRegistry(henhouse, henhouse.getRegistryName()),
                getItemBlockForRegistry(henhouse_acacia, henhouse_acacia.getRegistryName()),
                getItemBlockForRegistry(henhouse_birch, henhouse_birch.getRegistryName()),
                getItemBlockForRegistry(henhouse_dark_oak, henhouse_dark_oak.getRegistryName()),
                getItemBlockForRegistry(henhouse_jungle, henhouse_jungle.getRegistryName()),
                getItemBlockForRegistry(henhouse_spruce, henhouse_spruce.getRegistryName())
        );
    }

    @SubscribeEvent
    public static void onTileEntityRegistry(RegistryEvent.Register<TileEntityType<?>> event){
        event.getRegistry().registerAll(
                TileEntityType.Builder.create(
                        () -> new TileEntityHenhouse(), henhouse).build(null).setRegistryName(new ResourceLocation(MODID, "ritual_bowl")
                )
        );
    }

    private static BlockItem getItemBlockForRegistry(Block block, ResourceLocation resourceLocation) {

        BlockItem blockItem = new BlockItem(block, new Item.Properties().group(ModItemGroups.CHICKENS_TAB)){
            @Override
            public String getTranslationKey() {
                return "name";
            }
        };

        blockItem.setRegistryName(resourceLocation);

        return blockItem;
    }



    //TODO Check this from here on!
    public void registerLiquidEgg(LiquidEggRegistryItem liquidEgg) {
        DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(ChickensMod.liquidEgg, new com.setycz.chickens.common.CommonProxy.DispenseLiquidEgg());
        DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(ChickensMod.coloredEgg, new com.setycz.chickens.common.CommonProxy.DispenseColorEgg());
    }

    class DispenseColorEgg extends ProjectileDispenseBehavior {
        @Override
        protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
            EntityColoredEgg entityColoredEgg = new EntityColoredEgg(worldIn, position.getX(), position.getY(),
                    position.getZ());
            entityColoredEgg.setChickenType(((ItemColoredEgg) stackIn.getItem()).getChickenType(stackIn));
            return entityColoredEgg;
        }
    }

    class DispenseLiquidEgg extends DefaultDispenseItemBehavior {
        @Override
        protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
            ItemLiquidEgg itemLiquidEgg = (ItemLiquidEgg) stack.getItem();
            BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().getValue(DispenserBlock.FACING));
            Block liquid = LiquidEggRegistry.findById(stack.getMetadata()).getLiquid();
            if (!itemLiquidEgg.tryPlaceContainedLiquid(null, source.getWorld(), blockpos, liquid)) {
                return super.dispenseStack(source, stack);
            }
            stack.shrink(1);
            return stack;
        }
    }
}
