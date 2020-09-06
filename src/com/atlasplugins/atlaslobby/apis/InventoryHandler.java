package com.atlasplugins.atlaslobby.apis;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.atlasplugins.atlaslobby.Main;

/**
 * 
 * @author don't
 *
 */
public class InventoryHandler {

	static {
		JavaPlugin main = Main.getPlugin(Main.class);
		Bukkit.getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onClick(InventoryClickEvent e) {
				if (e.getSlotType() == SlotType.OUTSIDE || e.getCurrentItem() == null) return;
				if (e.getInventory().getHolder() instanceof HandlerHolder) {
					e.setCancelled(true);
					((HandlerHolder)e.getInventory().getHolder()).getInventoryHandler().handler.accept(e);
				}
			}
		}, main);
	}
	
	private Inventory inventory;
	private Consumer<InventoryClickEvent> handler;
	
	public InventoryHandler(String name, int size) {
		this.inventory = Bukkit.createInventory(new HandlerHolder(this), size, name);
	}
	
	public InventoryHandler handler(Consumer<InventoryClickEvent> handler) {
		this.handler = handler;
		return this;
	}
	
	public InventoryHandler item(int slot, ItemStack item) {
		inventory.setItem(slot, item);
		return this;
	}
	
	public InventoryHandler fill(ItemStack item, boolean replace){
		for (int i = 0; i < inventory.getSize(); i++){
			if (inventory.getItem(i) == null || replace)
			inventory.setItem(i,item);
		}
		return this;
	}

	public InventoryHandler fill(ItemStack item){
		return fill(item,true);
	}

	public InventoryHandler item(ItemStack item, int... slot) {
		for (int i : slot) inventory.setItem(i, item);
		return this;
	}
	
	public InventoryHandler items(Consumer<Inventory> consumer){
		consumer.accept(this.inventory);
		return this;
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
	public void open(Player player) {
		player.openInventory(this.inventory);
	}
	
	private class HandlerHolder implements InventoryHolder {

		private InventoryHandler handler;
		
		public HandlerHolder(InventoryHandler handler) {
			super();
			this.handler = handler;
		}

		@Override
		public Inventory getInventory() {
			return handler.inventory;
		}

		public InventoryHandler getInventoryHandler() {
			return handler;
		}
		
	}
	
}