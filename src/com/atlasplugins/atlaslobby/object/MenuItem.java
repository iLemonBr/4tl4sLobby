package com.atlasplugins.atlaslobby.object;

import java.util.List;

import org.bukkit.Bukkit;

public class MenuItem {

	
	private String key;
	private int slot;
	private int id;
	private int data;
	private String skull;
	private ConnectionDetails connectionDetails;
	private String name;
	private List<String> lore;
	
	public MenuItem(String key, int slot, int id, int data, String skull, ConnectionDetails connectionDetails,
			String name, List<String> lore) {
		super();
		this.key = key;
		this.slot = slot;
		this.id = id;
		this.data = data;
		this.skull = skull;
		this.connectionDetails = connectionDetails;
		this.name = name;
		this.lore = lore;
	}

	public String getKey() {
		return key;
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

	public String getSkull() {
		return skull;
	}

	public ConnectionDetails getConnectionDetails() {
		return connectionDetails;
	}

	public String getName() {
		return name;
	}

	public List<String> getLore() {
		return lore;
	}

	public void setKey(String key) {
		this.key = key;
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

	public void setSkull(String skull) {
		this.skull = skull;
	}

	public void setConnectionDetails(ConnectionDetails connectionDetails) {
		this.connectionDetails = connectionDetails;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}
	
}
