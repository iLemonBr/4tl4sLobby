package com.atlasplugins.atlaslobby.object;

import org.bukkit.inventory.ItemStack;

public class DefinedLobbyItem {

	
	private int slot;
	private ItemStack item;
	public DefinedLobbyItem(int slot, ItemStack item) {
		super();
		this.slot = slot;
		this.item = item;
	}
	
	public int getSlot() {
		return slot;
	}
	public ItemStack getItem() {
		return item;
	}
	public void setSlot(int slot) {
		this.slot = slot;
	}
	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	
}
