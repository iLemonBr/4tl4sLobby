package com.atlasplugins.atlaslobby.inventorys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.HiddenStringUtils;
import com.atlasplugins.atlaslobby.apis.ItemComposer;
import com.atlasplugins.atlaslobby.apis.LobbyAPI;
import com.atlasplugins.atlaslobby.apis.Scroller;
import com.atlasplugins.atlaslobby.cores.LobbyCore;
import com.atlasplugins.atlaslobby.enums.ServerStatus;
import com.atlasplugins.atlaslobby.object.LobbyInfo;

public class Lobbies {

	public Main core;
	private static Scroller scroller;
	
	public Lobbies(Main core) {
		build();
		new BukkitRunnable() {
			
			@Override
			public void run() {
				build();
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 20L*2, 20L*2);
	}
	
	private void build() {
		List<ItemStack> items = new ArrayList<ItemStack>();
		int i = 1;
		for(LobbyInfo lobby : LobbyCore.getLobbiesRegistreds().values()) {
			if(LobbyCore.getCurrentLobbyName().equalsIgnoreCase(lobby.getlKey())){
			  items.add(new ItemComposer(Material.NETHER_STAR).setName("§aLobby #"+i+HiddenStringUtils.encodeString(lobby.getServer()))
					  .setLore("§fJogadores: §7"+Bukkit.getOnlinePlayers().size()+"/"+Bukkit.getMaxPlayers(),"§e","§eVocê está aqui.")
					  .addEnchantment(Enchantment.DURABILITY, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).toItemStack());
			} else {
				  items.add(new ItemComposer(Material.NETHER_STAR).setName("§aLobby #"+i+HiddenStringUtils.encodeString(lobby.getServer()))
						  .setLore("§fJogadores: §7"+LobbyAPI.getPlayerCount(lobby.getServer()),"§e","§aClique para se conectar.").toItemStack());
			}
			i++;
		}
		scroller = Scroller.builder().withName("Lobbies").withAllowedSlots(Arrays.asList(10,11,12,13,14,15,16)).withSize(27)
		.withPreviousPageSlot(9).withNextPageSlot(17).withItems(items).withOnChooseItem((p,item)->{
			if(item!=null && item.getType().equals(Material.NETHER_STAR)) {
				String serverConnector = HiddenStringUtils.extractHiddenString(item.getItemMeta().getDisplayName());
				if(LobbyCore.containsLobbyByServerName(serverConnector)) {
					LobbyInfo lobbyInfo = LobbyCore.getLobbiesRegistreds().get(LobbyCore.getLobbyInfoByServerName(serverConnector));
					if(lobbyInfo.isManutencao()) {
						if(p.hasPermission("atlasplugins.staff.admin")) {
							LobbyAPI.enviarPlayer(p, serverConnector);
							p.closeInventory();
						}else {
							p.sendMessage("§cEste lobby está em manutenção.");
							p.closeInventory();
						}
					}else {
						LobbyAPI.enviarPlayer(p, serverConnector);
					}
				}
			}
		}).build();
	}
	
	public static void open(Player p) {
		scroller.open(p);
	}
}
