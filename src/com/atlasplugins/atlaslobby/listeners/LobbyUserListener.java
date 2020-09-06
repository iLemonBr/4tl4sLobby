package com.atlasplugins.atlaslobby.listeners;

import java.util.HashMap;

import com.atlasplugins.atlaslobby.captcha.CaptchaGenerator;
import com.atlasplugins.atlaslobby.captcha.CaptchaListener;
import com.atlasplugins.atlaslobby.runnables.CaptchaRunnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.Callback;
import com.atlasplugins.atlaslobby.apis.HiddenStringUtils;
import com.atlasplugins.atlaslobby.apis.ItemComposer;
import com.atlasplugins.atlaslobby.cores.BatalhasCore;
import com.atlasplugins.atlaslobby.cores.LobbyCore;
import com.atlasplugins.atlaslobby.cores.MenuCore;
import com.atlasplugins.atlaslobby.inventorys.Lobbies;
import com.atlasplugins.atlaslobby.inventorys.Profile;
import com.atlasplugins.atlaslobby.object.AtlasPlayer;
import com.atlasplugins.atlaslobby.object.LobbyItem;
import com.atlasplugins.atlaslobby.runnables.KickerTask;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyUserListener implements Listener {
	
	public static HashMap<String,Long> cooldownTime = new HashMap<String,Long>();
	
	@EventHandler
	public void onInteractLobbies(PlayerInteractEvent event) {
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(event.getItem()!=null) {
				Player p = event.getPlayer();
				if(!event.getItem().hasItemMeta() || !event.getItem().getItemMeta().hasDisplayName()) {
					return;
				}
				if(event.getItem().getType().equals(LobbyCore.getDefinedLobbyItem().getItem().getType())
						&& event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(LobbyCore.getDefinedLobbyItem().getItem().getItemMeta().getDisplayName())) {
					BatalhasCore.addPlayerBattle(p);
					for(String s : Main.getInstance().getConfig().getStringList("Mensagens.EntrouPVP")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
					}
				}
				if(event.getItem().getType().equals(Material.SKULL_ITEM)) {
					Profile.open(p);
				}
				if(event.getItem().getType().equals(Material.NETHER_STAR)) {
					Lobbies.open(p);
				}
				if(event.getItem().getType().equals(Material.INK_SACK)) {
					if(Main.getStorager().getCache().containsKey(p.getName())) {
						AtlasPlayer atlasPlayer = Main.getStorager().getCache().get(p.getName());
						if(cooldownTime.containsKey(p.getName())) {
							long current = System.currentTimeMillis();
							long past = cooldownTime.get(p.getName());
							if(current >= (past + 15*1000)) {
								if(atlasPlayer.getPreferences().isVisibility()) {
									atlasPlayer.getPreferences().setVisibility(false);
									for(Player online : Bukkit.getOnlinePlayers()) {
										p.hidePlayer(online);
									}
									LobbyCore.updateJogadoresState(p);
									atlasPlayer.save(true);
									p.sendMessage("§cVisibilidade de jogadores desativada.");
								}else {
									atlasPlayer.getPreferences().setVisibility(true);
									for(Player online : Bukkit.getOnlinePlayers()) {
										if(!BatalhasCore.getPlayersInBattle().containsKey(online.getName())) {
										p.showPlayer(online);
										}
									}
									LobbyCore.updateJogadoresState(p);
									atlasPlayer.save(true);
									p.sendMessage("§aVisibilidade de jogadores ativada.");
								}
								cooldownTime.put(p.getName(), System.currentTimeMillis());
							}else {
								p.sendMessage("§cAguarde alguns segundos para desativar ou ativar esta opção.");
								return;
							}
						}else {
							if(atlasPlayer.getPreferences().isVisibility()) {
								atlasPlayer.getPreferences().setVisibility(false);
								for(Player online : Bukkit.getOnlinePlayers()) {
									p.hidePlayer(online);
								}
								LobbyCore.updateJogadoresState(p);
								atlasPlayer.save(true);
								p.sendMessage("§cVisibilidade de jogadores desativada.");
							}else {
								atlasPlayer.getPreferences().setVisibility(true);
								for(Player online : Bukkit.getOnlinePlayers()) {
									if(!BatalhasCore.getPlayersInBattle().containsKey(online.getName())) {
									p.showPlayer(online);
									}
								}
								LobbyCore.updateJogadoresState(p);
								atlasPlayer.save(true);
								p.sendMessage("§aVisibilidade de jogadores ativada.");
							}
							cooldownTime.put(p.getName(), System.currentTimeMillis());
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onInteractWithItem(PlayerInteractEvent event) {
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(event.getItem()!=null) {
				if(LobbyCore.isLobbyItem(event.getItem())) {
					LobbyItem lobbyItem = LobbyCore.getLobbyItems().get(HiddenStringUtils.extractHiddenString(event.getItem().getItemMeta().getDisplayName()));
					if(lobbyItem.isOpenMenu()) {
						if(MenuCore.getMenus().containsKey(lobbyItem.getMenu())) {
							MenuCore.getMenus().get(lobbyItem.getMenu()).open(event.getPlayer());
						}else {
							Bukkit.getConsoleSender().sendMessage("§c[AtlasLobby] O jogador "+event.getPlayer().getName()+" esta tentando interagir com um item que abre um menu que não existe.");
						}
					}
				}
			}
		}
	}


	@EventHandler
	public void aindaNaoExecutarComandos(PlayerCommandPreprocessEvent event) {
		Player p = event.getPlayer();
		if(event.getMessage().contains("/register") || event.getMessage().contains("/login") || event.getMessage().contains("/registrar")
				|| event.getMessage().contains("/logar") || event.getMessage().contains("/loginstaff")) {
			return;
		}
		if(!Main.getStorager().getCache().containsKey(p.getName().toLowerCase())) {
			event.setCancelled(true);
			p.sendMessage("§cVocê precisa se autenticar primeiro antes de utilizar comandos.");
		}else {
			AtlasPlayer atlasPlayer = Main.getStorager().getCache().get(p.getName().toLowerCase());
			if(!atlasPlayer.isLogged()) {
				event.setCancelled(true);
				p.sendMessage("§cVocê precisa se autenticar primeiro antes de utilizar comandos.");
			}
			
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if(LobbyCore.getSpawn()!=null) {
			p.teleport(LobbyCore.getSpawn());
		}
		p.setGameMode(GameMode.ADVENTURE);
		p.getInventory().clear();
		p.resetTitle();
		p.setFoodLevel(20);
		p.setExp(0);
		p.setMaxHealth(2);
		p.setHealth(2.0);
		p.getInventory().setHelmet(new ItemComposer(Material.AIR).toItemStack());
		p.getInventory().setChestplate(new ItemComposer(Material.AIR).toItemStack());
		p.getInventory().setLeggings(new ItemComposer(Material.AIR).toItemStack());
		p.getInventory().setBoots(new ItemComposer(Material.AIR).toItemStack());
		BatalhasCore.hideOnJoin(event.getPlayer());
		Main.getStorager().loadUser(event.getPlayer().getName().toLowerCase());
		new BukkitRunnable() {
			@Override
			public void run() {
				new CaptchaGenerator(event.getPlayer());
			}
		}.runTaskLater(Main.getPlugin(Main.class),20L * 4);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onQuit(PlayerQuitEvent event) {
		Main.getStorager().getCache().get(event.getPlayer().getName().toLowerCase()).setLogged(false);;
		Main.getStorager().unLoadUser(event.getPlayer().getName().toLowerCase());
		if(event.getPlayer().hasPermission("atlasplugins.staff.auth")) {
			if(LobbyCore.getAwaitStaffLogin().contains(event.getPlayer().getName())) {
				LobbyCore.getAwaitStaffLogin().remove(event.getPlayer().getName());
			}
		}
		if(KickerTask.kickMillis.containsKey(event.getPlayer().getName())) {
			KickerTask.kickMillis.remove(event.getPlayer().getName());
		}
	}
	
	@EventHandler
	public void preventDoubleNick(PlayerLoginEvent event) {
		Main.getStorager().userExists(event.getPlayer().getName(), new Callback<Boolean>() {

			@Override
			public void execute(Boolean response, AtlasPlayer lobbyUser) {
				if(response) {
					if(!lobbyUser.getName().equals(event.getPlayer().getName())) {
						event.disallow(Result.KICK_OTHER, "§c§l"+ChatColor.stripColor(Main.getInstance().getConfig().getString("Geral.ServerName"))+"§r\n§r\n§r§cO nick '"+event.getPlayer().getName()+"' não pode ser utilizado por já existir um cadastro com o nick '"+lobbyUser.getName()+"'.\n§r§c\n§cSerá necessário utilizar a mesma configuração de caracteres maiúsculos e minúsculos para acessar o servidor.");
					}
				}
			}
		
		} , false);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if(!event.getPlayer().hasPermission("atlasplugins.staff.admin")) {
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onBreakBlock(BlockBreakEvent event) {
		if(!event.getPlayer().hasPermission("atlasplugins.staff.admin")) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e){
		e.setJoinMessage(null);
	}
	@EventHandler
	public void left(PlayerQuitEvent e){
		e.setQuitMessage(null);
	}
	@EventHandler
	public void death(PlayerDeathEvent e){
		e.setDeathMessage(null);
	}
	@EventHandler
	public void drop(PlayerDropItemEvent e){
		e.setCancelled(true);
	}
	@EventHandler
	public void cancel(PlayerPickupItemEvent e) {
		if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void cancelEntity(PlayerArmorStandManipulateEvent e){
		e.setCancelled(true);
	}
	@EventHandler
	public void inver(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player) {
		Player p = (Player)e.getWhoClicked();
		if(p.getGameMode().equals(GameMode.ADVENTURE)) {
		e.setCancelled(true);
		}
		}
	}
	@EventHandler
	public void weather(WeatherChangeEvent event) {
		event.setCancelled(true);
	}
	
}
