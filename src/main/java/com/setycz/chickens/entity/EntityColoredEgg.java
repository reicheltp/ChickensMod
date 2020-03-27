package com.setycz.chickens.entity;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.registry.ChickensRegistry;
import com.setycz.chickens.registry.ChickensRegistryItem;

import init.ModEntityTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by setyc on 13.02.2016.
 */
public class EntityColoredEgg extends ThrowableEntity {
    private static final DataParameter<String> CHICKEN_TYPE = EntityDataManager.createKey(EntityColoredEgg.class, DataSerializers.STRING);
    public static final String TYPE_NBT = "Type";
    
    private static ItemStack itemstack = ItemStack.EMPTY;

    public EntityColoredEgg(World worldIn) {
        super(ModEntityTypes.COLORED_EGG_ENTITY_TYPE,worldIn);
    }

    public EntityColoredEgg(World worldIn, double x, double y, double z) {
        super(ModEntityTypes.COLORED_EGG_ENTITY_TYPE, x, y, z, worldIn);
    }

    public void setChickenType(String type) {
        this.dataManager.set(CHICKEN_TYPE, type);
    }

    private String getChickenType() {
        return this.dataManager.get(CHICKEN_TYPE);
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void onImpact(RayTraceResult result) {
        BlockPos hitPosition = null;
        if (result instanceof EntityRayTraceResult) {
            EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult) result;
            entityRayTraceResult.getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
            hitPosition = entityRayTraceResult.getEntity().getPosition();
        }
        else if (result instanceof BlockRayTraceResult){
            BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) result;
            hitPosition = blockRayTraceResult.getPos();
        }

        if (!this.world.isRemote && hitPosition != null) {
            
            if (this.rand.nextInt(8) == 0)
            {
            	int i = 1;

            	if (this.rand.nextInt(32) == 0) {
            		i = 4;
            	}

            	for (int j = 0; j < i; ++j) {
            		EntityChickensChicken entityChicken = new EntityChickensChicken(this.world);
            		entityChicken.setChickenType(getChickenType());
            		entityChicken.setGrowingAge(-24000);
            		entityChicken.setLocationAndAngles(hitPosition.getX(), hitPosition.getY(), hitPosition.getZ(), this.rotationYaw, 0.0F);

                    entityChicken.getType().spawn(world, null, null, hitPosition, SpawnReason.SPAWN_EGG, true, false);
            	}
            }


            this.world.setEntityState(this, (byte)3);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 3)
        {
            double d0 = 0.08D;

            for (int i = 0; i < 8; ++i)
            {
                this.world.addParticle(ParticleTypes.DAMAGE_INDICATOR, this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getY(), ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D);
            }
        }
    }
    
	public ItemStack getItemStacktoRender() {
			ChickensRegistryItem registry = ChickensRegistry.getByRegistryName(getChickenType());
			itemstack = new ItemStack(ChickensMod.coloredEgg, 1, registry.getDyeMetadata());
		return itemstack;
	}

}
