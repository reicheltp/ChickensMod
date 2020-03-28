package com.setycz.chickens.client.gui.container;

import javax.annotation.Nonnull;

import com.setycz.chickens.block.TileEntityHenhouse;
import com.setycz.chickens.capabilities.InventoryStroageModifiable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Created by setyc on 06.03.2016.
 */
public class ContainerHenhouse extends Container {

    private final TileEntityHenhouse tileEntityHenhouse;
    private InventoryStroageModifiable invTileEntityHenhouse;
    private int energy;
    
     
    public ContainerHenhouse(PlayerInventory playerInventory, TileEntityHenhouse tileEntityHenhouse) {
        this.tileEntityHenhouse = tileEntityHenhouse;
         
        this.invTileEntityHenhouse = (InventoryStroageModifiable) tileEntityHenhouse.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN);
             
        //input 
        // Overriding canTake and decrStack to use the internal extract and by pass the locks in place for other things taking items. 
        
        this.addSlot(new SlotItemHandler(invTileEntityHenhouse, TileEntityHenhouse.hayBaleSlotIndex, 25, 19)
        		{
        			@Override
        			public boolean canTakeStack(PlayerEntity playerIn)
        			{
        				return !invTileEntityHenhouse.extractItemInternal(TileEntityHenhouse.hayBaleSlotIndex, 1, true).isEmpty();
        			}

        			@Override
        			@Nonnull
        			public ItemStack decrStackSize(int amount)
        			{
        				return invTileEntityHenhouse.extractItemInternal(TileEntityHenhouse.hayBaleSlotIndex, amount, false);
        			}
        		});
        
        //output
        this.addSlot(new SlotItemHandler(invTileEntityHenhouse, TileEntityHenhouse.dirtSlotIndex, 25, 55));

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                this.addSlot(new SlotItemHandler(
                		invTileEntityHenhouse,
                        TileEntityHenhouse.firstItemSlotIndex + (row * 3) + column,
                        98 + column * 18,
                        17 + row * 18));
            }
        }

        for (int l = 0; l < 3; ++l) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, l * 18 + 84));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            assert !itemStack1.isEmpty();
            itemstack = itemStack1.copy();

            if (index < this.invTileEntityHenhouse.getSlots()) {
                if (!this.mergeItemStack(itemStack1, this.invTileEntityHenhouse.getSlots(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemStack1, 0, this.invTileEntityHenhouse.getSlots(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        //tileEntityHenhouse.closeInventory(playerIn);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

//        for (IContainerListener listener : listeners) {
//            if (energy != tileEntityHenhouse.getField(0)) {
//                listener.sendWindowProperty(this, 0, tileEntityHenhouse.getField(0));
//            }
//        }

        energy = tileEntityHenhouse.getField(0);
    }

    @Override
    public void updateProgressBar(int id, int data) {
        tileEntityHenhouse.setField(id, data);
    }
}
