package com.setycz.chickens.client.gui;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Created by setyc on 06.03.2016.
 */
public interface IInventoryGui {
    Container createContainer(PlayerInventory inventoryplayer);

    @OnlyIn(Dist.CLIENT)
    ContainerScreen createGui(PlayerInventory inventoryplayer);
}
