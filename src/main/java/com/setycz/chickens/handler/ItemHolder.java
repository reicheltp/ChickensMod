package com.setycz.chickens.handler;

import java.util.HashMap;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.setycz.chickens.ChickensMod;

import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;

public class ItemHolder
{
	private String source = null;
	
	private int itemID;
	private CompoundNBT nbtData;
	private JsonObject nbtRawJson;
	
	private boolean isComplete = false;
	
	private ItemStack stack =  ItemStack.EMPTY;
	
	private int stackSize = 1;
	
	public static HashMap<Integer, Integer> ErroredItems = new HashMap<Integer, Integer>();
	
	Gson gson = new Gson();
	
	public ItemHolder(){
		itemID = Item.getIdFromItem(Items.AIR);
		nbtData = null;
		stack = ItemStack.EMPTY;
	}
	
	public ItemHolder(Item itemIn) {
		itemID = Item.getIdFromItem(itemIn);
		nbtData = null;
		stack = ItemStack.EMPTY;
	}
	
	public ItemHolder(ItemStack stackIn, boolean isFinal){
		itemID = Item.getIdFromItem(stackIn.getItem());
		stack = stackIn;
		nbtData = stackIn.hasTag() ?  stackIn.getTag() : null;
		stackSize = stackIn.getCount();
		isComplete = isFinal;
	}
	
	public ItemHolder(int itemID, int metaID, int qty) {
		this.itemID = itemID;
		this.nbtData = null;
	}
	
	public boolean hasSource() {
		return this.source != null;
	}
	
	public String getSource() {
		return this.source;
	}
	
	public ItemHolder setSource(String sourceIn) {
		this.source = sourceIn;
		return this;
	}

	@Nullable
	public Item getItem() {
		return Item.getItemById(this.itemID);
	}
	
	public int getStackSize() {
		return !this.stack.isEmpty() ? this.stack.getCount() : this.stackSize;
	}
	
	public int getAmount() {
		return stackSize;
	}
	
	/**
	 * Get or Create itemstack for this Loot Item
	 * @return
	 */
	public ItemStack getStack() {
		if(!isComplete) {
			
			Item item = getItem();
			if(item != null) 
			{
				stack = new ItemStack(getItem(), this.getAmount());
				if(this.nbtData != null && !this.nbtData.isEmpty())
	                	stack.setTag(this.nbtData);
				
				isComplete = true;
			}else {
				handleItemNotFound();
			}
		}

		return stack.copy();
	}
	
	private void handleItemNotFound() {
		
		if(!ErroredItems.containsKey(this.itemID)) 
			ErroredItems.put(this.itemID, 1);
		else
			ErroredItems.replace(this.itemID, ErroredItems.get(this.itemID) + 1);

		if(ErroredItems.get(this.itemID) <= 3) {
			ChickensMod.log.error("Could not find specfied Item: ["+ this.itemID +"]"+(this.hasSource() ? " | Source: ["+ this.getSource()+"]" : "")+ " | Dropping Default Item: ["+ this.stack.getDisplayName()+"]");
			if(ErroredItems.get(this.itemID) == 3)
				ChickensMod.log.error("Will silent error this itemID: ["+ this.itemID +"]");
		}
	}
	
	public ItemHolder readJsonObject(JsonObject data) throws NumberFormatException {
		itemID =  data.has("itemID") ? data.get("itemID").getAsInt() : Item.getIdFromItem(Items.AIR);
		stackSize = data.has("qty") ? data.get("qty").getAsInt() : 1;
		
		nbtRawJson =  data.has("nbt")  ? data.get("nbt").getAsJsonObject() : null; 
			
		try {
			nbtData = data.has("nbt")  ? JsonToNBT.getTagFromJson(data.get("nbt").getAsJsonObject().toString()) : null;
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
			nbtData = null;
		}
		
		return this;
	}
	
	public JsonObject writeJsonObject(JsonObject data) throws NumberFormatException {
		data.addProperty("itemID", itemID);

		if(stackSize > 1)
			data.addProperty("qty", getStackSize());
		
		if(nbtData != null && !nbtData.isEmpty()) {
			JsonElement element = gson.fromJson(nbtData.toString(), JsonElement.class);
			data.add("nbt", element.getAsJsonObject());
		}
		
		return data;
	}
	
	@Override
	public String toString() {
		return this.itemID + ":" + this.stackSize + (this.nbtData != null ? ":" + this.nbtData.toString() : "") ;
	}
}
