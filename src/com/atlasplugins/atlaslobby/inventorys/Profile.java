package com.atlasplugins.atlaslobby.inventorys;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.InventoryHandler;
import com.atlasplugins.atlaslobby.apis.ItemComposer;
import com.atlasplugins.atlaslobby.apis.LobbyAPI;
import com.atlasplugins.atlaslobby.object.AtlasPlayer;

public class Profile {

	
	public static void open(Player p) {
		AtlasPlayer aPlayer = Main.getStorager().getCache().get(p.getName().toLowerCase());
		new InventoryHandler("Perfil", 27).item(11, 
				new ItemComposer(Material.SKULL_ITEM).setSkullOwner(p.getName()).setDurability((short)3)
				.setName("§aSuas Informações")
				.setLore("§fGrupo: "+LobbyAPI.getPlayerGroupPrefix(p)
				,"§fCash: §7"+Main.getCashAPI().getCash(p.getName()),
				"§f",
				"§fCadastrado em: §7"+aPlayer.getRegistryDate(),
				"§fPrimeiro login: §7"+aPlayer.getRegistryDate(),
				"§f",
				"§fBatalhas:",
				" §fKills: §7"+aPlayer.getKills(),
				" §fMortes: §7"+aPlayer.getDeaths())
				.toItemStack()).item(15, new ItemComposer(Material.REDSTONE_COMPARATOR).setName("§aPreferências").setLore("§7Clique para abrir as preferências.").toItemStack()).handler(event->{
					if(event.getSlot() == 15) {
						//open Preferences
						PreferencesInventory.open(p);
					}
				}).open(p);
	}
}
