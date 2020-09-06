package com.atlasplugins.atlaslobby.cores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.Configs;
import com.atlasplugins.atlaslobby.apis.HiddenStringUtils;
import com.atlasplugins.atlaslobby.apis.ItemComposer;
import com.atlasplugins.atlaslobby.apis.LocationSerializer;
import com.atlasplugins.atlaslobby.object.AtlasPlayer;
import com.atlasplugins.atlaslobby.object.BatalhasDefinitions;
import com.atlasplugins.atlaslobby.object.DefinedLobbyItem;
import com.atlasplugins.atlaslobby.object.LobbyInfo;
import com.atlasplugins.atlaslobby.object.LobbyItem;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class LobbyCore {

	private static ArrayList<String> awaitStaffLogin = new ArrayList<String>();
	private static ArrayList<String> captchasSolved = new ArrayList<String>();
	private static HashMap<String,LobbyInfo> lobbiesRegistreds = new HashMap<String,LobbyInfo>();
	private static String serverListKick;
	private static String staffPassword;
	private static String currentLobby;
	private static int onlinePlayersInNetwork;
	private static boolean scoreboard;
	private static boolean loginStaffSystem;
	private static boolean captcha;
	private static boolean jogadores;
	private static boolean fila;
	private static boolean batalhas;
	private static int jogadoresSlot;
	private static Location spawn;
	private static HashMap<String,LobbyItem> lobbyItems = new HashMap<String,LobbyItem>();
	private static DefinedLobbyItem perfil = null;
	private static DefinedLobbyItem lobbies = null;
	private static DefinedLobbyItem batalhasItem = null;
	private static BatalhasDefinitions batalhasInfo;
	
	public LobbyCore() {
		FileConfiguration config = Main.getInstance().getConfig();

		spawn = null;
		currentLobby = config.getString("Geral.Lobby");
		batalhas = config.getBoolean("Batalhas.Ativar");
		fila = config.getBoolean("Geral.Fila");
		scoreboard = config.getBoolean("Geral.ScoreBoard");
		loginStaffSystem = config.getBoolean("Geral.LoginStaffSystem");
		captcha = config.getBoolean("Geral.Captcha");
		jogadores = config.getBoolean("Jogadores.Ativar");
		jogadoresSlot = config.getInt("Jogadores.Slot");
		staffPassword = config.getString("LoginStaff");
		
		for(String lobby : config.getConfigurationSection("LobbyList").getKeys(false)) {
			boolean manutencao = config.getBoolean("LobbyList."+lobby+".Manutencao");
			String server = config.getString("LobbyList."+lobby+".Server");
			LobbyInfo lInfo = new LobbyInfo(lobby, server, config.getInt("LobbyList."+lobby+".MaxPlayers"), manutencao);
			lobbiesRegistreds.put(lobby, lInfo);
		}
		
		for(String lKey : config.getConfigurationSection("GiveItems").getKeys(false)) {
			int slot = config.getInt("GiveItems."+lKey+".Slot");
			int id = config.getInt("GiveItems."+lKey+".Item.ID");
			int data = config.getInt("GiveItems."+lKey+".Item.Data");
			String name = config.getString("GiveItems."+lKey+".Item.Nome");
			List<String> lore = config.getStringList("GiveItems."+lKey+".Item.Lore");
			boolean openMenu = config.getBoolean("GiveItems."+lKey+".Menu.AbrirMenu");
			String menu = config.getString("GiveItems."+lKey+".Menu.MenuName");
			LobbyItem item = new LobbyItem(lKey, slot, id, data, name, lore, openMenu, menu);
			lobbyItems.put(lKey, item);
		}
		
		if(config.getBoolean("ForceAddServerList.Ativar")) {
			serverListKick = "";
			for(String part : config.getStringList("ForceAddServerList.Mensagem")) {
				if(serverListKick.isEmpty()) {
					serverListKick = ChatColor.translateAlternateColorCodes('&', part);
				}else {
					serverListKick = serverListKick+"\n"+ChatColor.translateAlternateColorCodes('&', part);
				}
			}
		}
		
		if(config.getBoolean("Perfil.Ativar")) {
			perfil = new DefinedLobbyItem(config.getInt("Perfil.Slot"), new ItemComposer(Material.SKULL_ITEM).setDurability((short)3).setName(config.getString("Perfil.Nome")).setLore(config.getStringList("Perfil.Lore")).toItemStack());
		}
		if(config.getBoolean("Lobbies.Ativar")) {
			lobbies = new DefinedLobbyItem(config.getInt("Lobbies.Slot"), 
					new ItemComposer(Material.NETHER_STAR)
					.setName(config.getString("Lobbies.Nome"))
					.setLore(config.getStringList("Lobbies.Lore")).toItemStack());
		}
		
		if(batalhas) {
			batalhasInfo = new BatalhasDefinitions(Main.getPlugin(Main.class));
			batalhasItem = new DefinedLobbyItem(config.getInt("Batalhas.Slot"), 
					new ItemComposer(Material.valueOf(config.getString("Batalhas.Item")))
					.setName(config.getString("Batalhas.Nome"))
					.setLore(config.getStringList("Batalhas.Lore")).addItemFlag(ItemFlag.HIDE_ATTRIBUTES).addItemFlag(ItemFlag.HIDE_PLACED_ON).addItemFlag(ItemFlag.HIDE_UNBREAKABLE).toItemStack());
		}
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for(World w : Bukkit.getWorlds()) {
					w.setDifficulty(Difficulty.PEACEFUL);
				}
				if(Configs.DATA.getConfig().getString("Spawn").contains(";")) {
					spawn = LocationSerializer.getDeserializedLocation(Configs.DATA.getConfig().getString("Spawn"));
					Bukkit.getConsoleSender().sendMessage("§a[AtlasLobby] Todas as configurações foram carregadas com sucesso.");
				}else {
					Bukkit.getConsoleSender().sendMessage("§c[AtlasLobby] Nenhum local de spawn de jogadores foi definido, por favor utilize /lobby definir.");
				}
			}
		}.runTask(Main.getPlugin(Main.class));
       new BukkitRunnable() {
			
			@Override
			public void run() {
				for(LobbyInfo server : lobbiesRegistreds.values()) {
						try {
							ByteArrayDataOutput out1 = ByteStreams.newDataOutput();
					        out1.writeUTF("PlayerCount");
					        out1.writeUTF(server.getServer());
					        Bukkit.getServer().sendPluginMessage(Main.getPlugin(Main.class), "BungeeCord", out1.toByteArray());
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 20L*2, 20L*10);
	}
	
	public static boolean isScoreboard() {
		return scoreboard;
	}

	public static DefinedLobbyItem getDefinedLobbyItem() {
		return batalhasItem;
	}
	
	public static Location getSpawn() {
		return spawn;
	}
	
	public static void setSpawn(Location loc) {
		spawn = loc;
		Configs.DATA.getConfig().set("Spawn", LocationSerializer.getSerializedLocation(loc));
		Configs.DATA.saveConfig();
	}
	
	public static boolean isFila() {
		return fila;
	}
	
	public static int getOnlinePlayersInNetwork() {
		return onlinePlayersInNetwork;
	}

	public static void setOnlinePlayersInNetwork(int onlinePlayersInNetwork) {
		LobbyCore.onlinePlayersInNetwork = onlinePlayersInNetwork;
	}
	
	public static String getLobbyInfoByServerName(String serverName) {
		for(LobbyInfo lobby : lobbiesRegistreds.values()) {
			if(lobby.getServer().equalsIgnoreCase(serverName)) {
				return lobby.getlKey();
			}
		}
		return null;
	}
	
	public static boolean containsLobbyByServerName(String serverName) {
		for(LobbyInfo lobby : lobbiesRegistreds.values()) {
			if(lobby.getServer().equalsIgnoreCase(serverName)) {
				return true;
			}
		}
		return false;
	}

	public static HashMap<String, LobbyInfo> getLobbiesRegistreds() {
		return lobbiesRegistreds;
	}

	public static String getCurrentLobbyName() {
		return currentLobby;
	}
	
	public static String getServerListKickMessage() {
		return serverListKick;
	}
	
	public static HashMap<String,LobbyItem> getLobbyItems(){
		return lobbyItems;
	}
	
	public static ArrayList<String> getAwaitStaffLogin() {
		return awaitStaffLogin;
	}

	public static ArrayList<String> getCaptchaSolveds(){
		return captchasSolved;
	}
	
	public static String getStaffPassword() {
		return staffPassword;
	}
	
	public static boolean isCaptcha() {
		return captcha;
	}
	
	public static boolean isLoginStaffSystem() {
		return loginStaffSystem;
	}
	
	public static void setCaptcha(boolean a) {
		captcha = a;
	}
	
	public static boolean isLobbyItem(ItemStack i) {
		if(i!=null && i.hasItemMeta() && i.getItemMeta().hasDisplayName() && HiddenStringUtils.hasHiddenString(i.getItemMeta().getDisplayName())) {
			return true;
		}
		return false;
	}
	
	public static void updateJogadoresState(Player p) {
		AtlasPlayer atlasPlayer = Main.getStorager().getCache().get(p.getName().toLowerCase());
		if(atlasPlayer.getPreferences().isVisibility()) {
			p.getInventory().setItem(jogadoresSlot, new ItemComposer(Material.INK_SACK).setDurability((short)10).setName("§fJogadores §aON").toItemStack());
		}else {
			p.getInventory().setItem(jogadoresSlot, new ItemComposer(Material.INK_SACK).setDurability((short)8).setName("§fJogadores §cOFF").toItemStack());
		}
	}
	
	public static void giveLobbyItems(Player p) {
		if(perfil!=null) {
			p.getInventory().setItem(perfil.getSlot(), new ItemComposer(perfil.getItem().clone()).setSkullOwner(p.getName()).toItemStack());
		}
		if(lobbies!=null) {
			p.getInventory().setItem(lobbies.getSlot(), lobbies.getItem().clone());
		}
		if(batalhas) {
			p.getInventory().setItem(batalhasItem.getSlot(), batalhasItem.getItem().clone());
		}
		if(jogadores && Main.getStorager().getCache().containsKey(p.getName().toLowerCase())) {
			AtlasPlayer atlasPlayer = Main.getStorager().getCache().get(p.getName().toLowerCase());
			if(atlasPlayer.getPreferences().isVisibility()) {
				p.getInventory().setItem(jogadoresSlot, new ItemComposer(Material.INK_SACK).setDurability((short)10).setName("§fJogadores §aON").toItemStack());
			}else {
				p.getInventory().setItem(jogadoresSlot, new ItemComposer(Material.INK_SACK).setDurability((short)8).setName("§fJogadores §cOFF").toItemStack());
			}
		}
		for(LobbyItem item : lobbyItems.values()) {
			p.getInventory().setItem(item.getSlot(), item.getItemStack());
		}
	}
}
