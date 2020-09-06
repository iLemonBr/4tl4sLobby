package com.atlasplugins.atlaslobby.cores;

import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.atlasplugins.atlaslobby.apis.Configs;
import com.atlasplugins.atlaslobby.object.ConnectionDetails;
import com.atlasplugins.atlaslobby.object.Menu;
import com.atlasplugins.atlaslobby.object.MenuItem;

public class MenuCore {

	
	private static HashMap<String,Menu> menus = new HashMap<String,Menu>();
	
	public MenuCore() {
		FileConfiguration config = Configs.MENUS.getConfig();
		for(String menuKey : config.getConfigurationSection("Menus").getKeys(false)) {
			String title = config.getString("Menus."+menuKey+".Titulo");
			int size = config.getInt("Menus."+menuKey+".Slots");
			HashMap<String,MenuItem> items = new HashMap<String,MenuItem>();
			for(String itemKey : config.getConfigurationSection("Menus."+menuKey+".Items").getKeys(false)) {
				int slot = config.getInt("Menus."+menuKey+".Items."+itemKey+".Slot");
				int itemID = config.getInt("Menus."+menuKey+".Items."+itemKey+".ID");
				int itemData = config.getInt("Menus."+menuKey+".Items."+itemKey+".Data");
				String skull = config.getString("Menus."+menuKey+".Items."+itemKey+".Skull");
				ConnectionDetails connectionDetails = new ConnectionDetails(config.getBoolean("Menus."+menuKey+".Items."+itemKey+".Conexao.Ativar"), config.getString("Menus."+menuKey+".Items."+itemKey+".Conexao.Servidor"));
				String name = config.getString("Menus."+menuKey+".Items."+itemKey+".Nome");
				List<String> lore = config.getStringList("Menus."+menuKey+".Items."+itemKey+".Lore");
				MenuItem menuItem = new MenuItem(itemKey, slot, itemID, itemData, skull, connectionDetails, name, lore);
				items.put(itemKey, menuItem);
			}
			Menu menu = new Menu(menuKey, size, title, items);
			menu.build();
			menus.put(menuKey, menu);
		}
	}
	
	public static HashMap<String,Menu> getMenus() {
		return menus;
	}
	
	public static boolean containsMenu(String menuName) {
		return menus.containsKey(menuName);
	}
	
	public static void openMenu(Player p, String menuName) {
		
	}
}
