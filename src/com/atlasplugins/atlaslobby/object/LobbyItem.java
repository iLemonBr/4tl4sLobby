package com.atlasplugins.atlaslobby.object;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.atlasplugins.atlaslobby.apis.HiddenStringUtils;
import com.atlasplugins.atlaslobby.apis.ItemComposer;

public class LobbyItem {

	
	private ItemStack builded;
	private String lKey;
	private int slot;
	private int id;
	private int data;
	private String name;
	private List<String> lore;
	private boolean openMenu;
	private String menu;
	
	public LobbyItem(String lKey, int slot, int id, int data, String name, List<String> lore, boolean openMenu, String menu) {
		super();
		this.slot = slot;
		this.id = id;
		this.data = data;
		this.name = name;
		this.lore = lore;
		this.openMenu = openMenu;
		this.menu = menu;
		builded = new ItemComposer(Material.getMaterial(this.id)).setDurability((short)this.data).setName(this.name+HiddenStringUtils.encodeString(lKey)).setLore(this.lore).toItemStack();
	}
	
	public ItemStack getItemStack() {
		return builded;
	}
	
	public int getSlot() {
		return slot;
	}
	public int getId() {
		return id;
	}
	public int getData() {
		return data;
	}
	public String getName() {
		return name;
	}
	public List<String> getLore() {
		return lore;
	}
	public boolean isOpenMenu() {
		return openMenu;
	}
	public String getMenu() {
		return menu;
	}
	public void setSlot(int slot) {
		this.slot = slot;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setData(int data) {
		this.data = data;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLore(List<String> lore) {
		this.lore = lore;
	}
	public void setOpenMenu(boolean openMenu) {
		this.openMenu = openMenu;
	}
	public void setMenu(String menu) {
		this.menu = menu;
	}
	
	
}
