package com.atlasplugins.atlaslobby.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.HiddenStringUtils;
import com.atlasplugins.atlaslobby.apis.InventoryHandler;
import com.atlasplugins.atlaslobby.apis.ItemComposer;
import com.atlasplugins.atlaslobby.apis.LobbyAPI;
import com.atlasplugins.atlaslobby.queue.QueueManager;

public class Menu {

	
	private InventoryHandler builded;
	private String key;
	private int size;
	private String title;
	private HashMap<String, MenuItem> items;
	
	
	public Menu(String key, int size, String title, HashMap<String, MenuItem> items) {
		super();
		this.key = key;
		this.size = size;
		this.title = title;
		this.items = items;
		new BukkitRunnable() {
			
			@Override
			public void run() {
				build();
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 20L, 20L);
	}

	public void build() {
		InventoryHandler handler = new InventoryHandler(this.title, this.size);
		for(Entry<String, MenuItem> z : this.items.entrySet()) {
			String key = z.getKey();
			MenuItem item = z.getValue();
			List<String> lore = new ArrayList<String>();
			for(String s : item.getLore()) {
				lore.add(s.replace("{jogadores}", LobbyAPI.getPlayerCounted(item.getConnectionDetails().getServerName())));
			}
			if(!item.getSkull().contains("textures.minecraft.net")) {
				handler.item(item.getSlot(), new ItemComposer(Material.getMaterial(item.getId())).setDurability((short)item.getData())
						.setName(item.getName()+HiddenStringUtils.encodeString(key)).setLore(lore).toItemStack());
			}else {
				handler.item(item.getSlot(), 
						new ItemComposer(item.getSkull()).setDurability((short)3).setName(item.getName()+HiddenStringUtils.encodeString(key)).setLore(lore).toItemStack());
			}
		}
		handler.handler(event->{
			if(event.getCurrentItem()!=null && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
				if(HiddenStringUtils.hasHiddenString(event.getCurrentItem().getItemMeta().getDisplayName())) {
					String key = HiddenStringUtils.extractHiddenString(event.getCurrentItem().getItemMeta().getDisplayName());
					if(items.containsKey(key)) {
						MenuItem menuItem = items.get(key);
						if(menuItem.getConnectionDetails().isActive()) {
							Player p = (Player)event.getWhoClicked();
							String serverKey = menuItem.getConnectionDetails().getServerName();
							LobbyAPI.connectPlayerToTheServer(p, serverKey);
							p.closeInventory();
						}
					}
				}
			}
		});
		this.builded = handler;
	}
	
	public void open(Player p) {
		builded.open(p);
	}

	public Inventory getBuilded() {
		return builded.getInventory();
	}

	public String getKey() {
		return key;
	}

	public int getSize() {
		return size;
	}

	public String getTitle() {
		return title;
	}

	public HashMap<String, MenuItem> getItems() {
		return items;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setItems(HashMap<String, MenuItem> items) {
		this.items = items;
	}
	
	
}
