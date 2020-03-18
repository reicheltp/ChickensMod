package com.setycz.chickens.handler;

import com.setycz.chickens.entity.EntityChickensChicken;
import com.setycz.chickens.registry.ChickensRegistry;
import com.setycz.chickens.registry.ChickensRegistryItem;

import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by setyc on 21.03.2016.
 */
public class ChickenTeachHandler {

    @SubscribeEvent
    public void handleInteraction(PlayerInteractEvent.EntityInteract event) {
        ItemStack item = event.getEntityPlayer().getHeldItem(event.getHand());
        if (item.isEmpty() || item.getItem() != Items.BOOK) {
            return;
        }
        if (!(event.getTarget().getClass() == ChickenEntity.class)) {
            return;
        }

        World worldObj = event.getEntityPlayer().world;
        if (worldObj.isRemote) {
            return;
        }

        ChickensRegistryItem smartChickenDescription = ChickensRegistry.getSmartChicken();
        if (smartChickenDescription == null || !smartChickenDescription.isEnabled()) {
            return;
        }

        ChickenEntity chicken = (ChickenEntity) event.getTarget();
        EntityChickensChicken smartChicken = convertToSmart(chicken, worldObj, smartChickenDescription);

        worldObj.removeEntity(chicken);
        worldObj.spawnEntity(smartChicken);
        smartChicken.spawnExplosionParticle();

        event.setCanceled(true);
    }

    private EntityChickensChicken convertToSmart(ChickenEntity chicken, World worldObj, ChickensRegistryItem smartChickenDescription) {
        EntityChickensChicken smartChicken = new EntityChickensChicken(worldObj);
        smartChicken.setPositionAndRotation(chicken.posX, chicken.posY, chicken.posZ, chicken.rotationYaw, chicken.rotationPitch);
        smartChicken.onInitialSpawn(worldObj.getDifficultyForLocation(chicken.getPosition()), null);
        //smartChicken.setChickenType(smartChickenDescription.getId());
        smartChicken.setChickenType(ChickensRegistry.SMART_CHICKEN_ID.toString());
        
        if (chicken.hasCustomName()) {
            smartChicken.setCustomNameTag(chicken.getCustomNameTag());
        }
        smartChicken.setGrowingAge(chicken.getGrowingAge());
        return smartChicken;
    }
}
