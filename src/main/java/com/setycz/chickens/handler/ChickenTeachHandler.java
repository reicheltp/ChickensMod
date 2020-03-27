package com.setycz.chickens.handler;

import com.setycz.chickens.entity.EntityChickensChicken;
import com.setycz.chickens.registry.ChickensRegistry;
import com.setycz.chickens.registry.ChickensRegistryItem;

import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Created by setyc on 21.03.2016.
 */
public class ChickenTeachHandler {

    @SubscribeEvent
    public void handleInteraction(PlayerInteractEvent.EntityInteract event) {
        PlayerEntity player = event.getPlayer();
        ItemStack item = player.getHeldItem(event.getHand());
        if (item.isEmpty() || item.getItem() != Items.BOOK) {
            return;
        }
        if (!(event.getTarget().getClass() == ChickenEntity.class)) {
            return;
        }

        World worldObj = player.world;
        if (worldObj.isRemote) {
            return;
        }

        ChickensRegistryItem smartChickenDescription = ChickensRegistry.getSmartChicken();
        if (smartChickenDescription == null || !smartChickenDescription.isEnabled()) {
            return;
        }

        ChickenEntity chicken = (ChickenEntity) event.getTarget();
        EntityChickensChicken smartChicken = convertToSmart(chicken, worldObj, smartChickenDescription);

        BlockPos blockPos = chicken.getPosition();

        chicken.remove();
        chicken.onRemovedFromWorld();

        smartChicken.getType().spawn(worldObj, null, player, blockPos, SpawnReason.CONVERSION, true, false);
        smartChicken.spawnExplosionParticle();

        event.setCanceled(true);
    }

    private EntityChickensChicken convertToSmart(ChickenEntity chicken, World worldObj, ChickensRegistryItem smartChickenDescription) {
        EntityChickensChicken smartChicken = new EntityChickensChicken(worldObj);
        BlockPos chickenPos = chicken.getPosition();

        smartChicken.setPositionAndRotation(chickenPos.getX(), chickenPos.getY(), chickenPos.getZ(), chicken.rotationYaw, chicken.rotationPitch);
        smartChicken.onInitialSpawn(worldObj.getDifficultyForLocation(chicken.getPosition()), null);
        smartChicken.setChickenType(ChickensRegistry.SMART_CHICKEN_ID.toString());
        
        if (chicken.hasCustomName()) {
            smartChicken.setCustomName(chicken.getCustomName());
        }

        smartChicken.setGrowingAge(chicken.getGrowingAge());
        return smartChicken;
    }
}
