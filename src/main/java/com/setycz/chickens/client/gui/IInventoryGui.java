package com.setycz.chickens.client.gui;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by setyc on 06.03.2016.
 */
public interface IInventoryGui {
    Container createContainer(PlayerInventory inventoryplayer);

    @SideOnly(Side.CLIENT)
    ContainerScreen createGui(PlayerInventory inventoryplayer);
}
