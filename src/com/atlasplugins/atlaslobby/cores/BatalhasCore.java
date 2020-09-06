package com.atlasplugins.atlaslobby.cores;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.ItemComposer;
import com.atlasplugins.atlaslobby.object.AtlasPlayer;
import com.atlasplugins.atlaslobby.object.BatalhasDefinitions;
import com.atlasplugins.atlaslobby.object.BattlePlayer;

public class BatalhasCore implements Listener {

	private static final double MAX_HEALTH = 20.0;
	private static HashMap<String, BattlePlayer> playersInBattle = new HashMap<String, BattlePlayer>();

	public static void addPlayerBattle(Player p) {
		if (!playersInBattle.containsKey(p.getName())) {
			ArrayList<String> hidder = new ArrayList<String>();
			for (Player online : Bukkit.getOnlinePlayers()) {
				if (!playersInBattle.containsKey(online.getName())) {
					online.hidePlayer(p);
					p.hidePlayer(online);
					hidder.add(online.getName());
				}
			}
			if (playersInBattle.size() > 0) {
				for (BattlePlayer b : playersInBattle.values()) {
					if (Bukkit.getPlayer(b.getPlayer()) != null) {
						p.showPlayer(Bukkit.getPlayer(b.getPlayer()));
					}
				}
			}
			BattlePlayer battlePlayer = new BattlePlayer(p.getName(), hidder);
			playersInBattle.put(p.getName(), battlePlayer);
			p.setGameMode(GameMode.SURVIVAL);
			p.setAllowFlight(false);
			p.setFlying(false);
			p.setMaxHealth(MAX_HEALTH);
			p.setHealth(MAX_HEALTH);
			p.getInventory().clear();
			BatalhasDefinitions.giveItems(p);
		}
	}

	public static void hideOnJoin(Player p) {
		if (playersInBattle.size() > 0) {
			for (BattlePlayer battlePlayer : playersInBattle.values()) {
				if (Bukkit.getPlayer(battlePlayer.getPlayer()) != null) {
					Bukkit.getPlayer(battlePlayer.getPlayer()).hidePlayer(p);
					p.hidePlayer(Bukkit.getPlayer(battlePlayer.getPlayer()));
				}
				battlePlayer.getHiddePlayers().add(p.getName());
			}
		}
	}

	public static void exit(Player p) {
		if (playersInBattle.containsKey(p.getName())) {
			AtlasPlayer atlasPlayer = Main.getStorager().getCache().get(p.getName());
			BattlePlayer battlePlayer = playersInBattle.get(p.getName());
			for (String hidded : battlePlayer.getHiddePlayers()) {
				if (Bukkit.getPlayer(hidded) != null && !playersInBattle.containsKey(hidded)) {
					Bukkit.getPlayer(hidded).showPlayer(p);
					p.showPlayer(Bukkit.getPlayer(hidded));
				}
			}
			for(String s : playersInBattle.keySet()) {
				if(Bukkit.getPlayer(s)!=null) {
					p.hidePlayer(Bukkit.getPlayer(s));
				}
			}
			playersInBattle.remove(p.getName());
			p.getInventory().setHelmet(new ItemComposer(Material.AIR).toItemStack());
			p.getInventory().setChestplate(new ItemComposer(Material.AIR).toItemStack());
			p.getInventory().setLeggings(new ItemComposer(Material.AIR).toItemStack());
			p.getInventory().setBoots(new ItemComposer(Material.AIR).toItemStack());
			p.getInventory().clear();
			p.spigot().respawn();
			p.setMaxHealth(2);
			LobbyCore.giveLobbyItems(p);
			p.setGameMode(GameMode.ADVENTURE);
			if (LobbyCore.getSpawn() != null) {
				p.teleport(LobbyCore.getSpawn());
			}
			if (p.hasPermission("atlasplugins.lobby.fly") && atlasPlayer.getPreferences().isFly()) {
				p.setAllowFlight(true);
				p.setFlying(true);
			}

		}
	}

	public static HashMap<String, BattlePlayer> getPlayersInBattle() {
		return playersInBattle;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (playersInBattle.containsKey(event.getPlayer().getName())) {
			playersInBattle.remove(event.getPlayer().getName());
			p.getInventory().setHelmet(new ItemComposer(Material.AIR).toItemStack());
			p.getInventory().setChestplate(new ItemComposer(Material.AIR).toItemStack());
			p.getInventory().setLeggings(new ItemComposer(Material.AIR).toItemStack());
			p.getInventory().setBoots(new ItemComposer(Material.AIR).toItemStack());
		}
		
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player p = (Player) event.getEntity();
		event.getDrops().clear();
		if (playersInBattle.containsKey(p.getName())) {
			AtlasPlayer atlasPlayer = Main.getStorager().getCache().get(p.getName());
			atlasPlayer.addDeath();
			atlasPlayer.save(true);
			AtlasPlayer killerPlayer = Main.getStorager().getCache().get(p.getKiller().getName());
			killerPlayer.addKill();
			killerPlayer.save(true);
			BattlePlayer battlePlayer = playersInBattle.get(p.getName());
			for (String hidded : battlePlayer.getHiddePlayers()) {
				if (Bukkit.getPlayer(hidded) != null) {
					Bukkit.getPlayer(hidded).showPlayer(p);
					p.showPlayer(Bukkit.getPlayer(hidded));
				}
			}
			for(String s : playersInBattle.keySet()) {
				if(Bukkit.getPlayer(s)!=null) {
					p.hidePlayer(Bukkit.getPlayer(s));
				}
			}
			playersInBattle.remove(p.getName());
			p.getInventory().clear();
			p.getInventory().setHelmet(new ItemComposer(Material.AIR).toItemStack());
			p.getInventory().setChestplate(new ItemComposer(Material.AIR).toItemStack());
			p.getInventory().setLeggings(new ItemComposer(Material.AIR).toItemStack());
			p.getInventory().setBoots(new ItemComposer(Material.AIR).toItemStack());
			p.sendMessage("§cVocê morreu!");
			p.spigot().respawn();
			p.setMaxHealth(2);
			p.setGameMode(GameMode.ADVENTURE);
			if (LobbyCore.getSpawn() != null) {
				p.teleport(LobbyCore.getSpawn());
			}
			if (p.hasPermission("atlasplugins.lobby.fly") && atlasPlayer.getPreferences().isFly()) {
				p.setAllowFlight(true);
				p.setFlying(true);
			}
			new BukkitRunnable() {
				
				@Override
				public void run() {
					LobbyCore.giveLobbyItems(p);
				}
			}.runTaskLater(Main.getPlugin(Main.class), 10l);

		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			Player damager = (Player) event.getDamager();
			Player entity = (Player) event.getEntity();
			if (!playersInBattle.containsKey(damager.getName()) || !playersInBattle.containsKey(entity.getName())) {
				event.setCancelled(true);
				event.setDamage(0);
			}
		} else {
			if (event.getEntity() instanceof Player) {
				event.setCancelled(true);
				event.setDamage(0);
			}
		}
	}

}
