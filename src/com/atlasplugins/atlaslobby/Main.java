package com.atlasplugins.atlaslobby;

import com.atlasplugins.atlaslobby.apis.Configs;
import com.atlasplugins.atlaslobby.apis.PasswordFilter;
import com.atlasplugins.atlaslobby.captcha.CaptchaListener;
import com.atlasplugins.atlaslobby.chat.ChatListener;
import com.atlasplugins.atlaslobby.commands.*;
import com.atlasplugins.atlaslobby.cores.*;
import com.atlasplugins.atlaslobby.hook.CashAPI;
import com.atlasplugins.atlaslobby.hook.DefaultCash;
import com.atlasplugins.atlaslobby.hook.PlayerPointsHooker;
import com.atlasplugins.atlaslobby.incoming.FMLReciver;
import com.atlasplugins.atlaslobby.incoming.PingReciver;
import com.atlasplugins.atlaslobby.incoming.WDLReciver;
import com.atlasplugins.atlaslobby.inventorys.Lobbies;
import com.atlasplugins.atlaslobby.listeners.GeneralLobbyListener;
import com.atlasplugins.atlaslobby.listeners.JumperListener;
import com.atlasplugins.atlaslobby.listeners.LobbyUserListener;
import com.atlasplugins.atlaslobby.mysql.AtlasStorage;
import com.atlasplugins.atlaslobby.mysql.MySQL;
import com.atlasplugins.atlaslobby.object.LobbyNPC;
import com.atlasplugins.atlaslobby.runnables.CaptchaRunnable;
import com.atlasplugins.atlaslobby.runnables.KickerTask;
import com.atlasplugins.atlaslobby.scoreboard.ScoreBoard;
import com.atlasplugins.atlaslobby.scoreboard.ScoreUpdater;
import com.google.common.io.Resources;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.charset.Charset;

public class Main extends JavaPlugin{

	private static MySQL mySQLConnection;
	private static Main instance;
	private static AtlasStorage storager;
	private static CashAPI cashAPI;
	
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage("§e[AtlasLobby] Inicializando...");
		saveDefaultConfig();
		try {
			File file = new File(getDataFolder() + File.separator, "config.yml");
			String allText = Resources.toString(file.toURI().toURL(), Charset.forName("UTF-8"));
			getConfig().loadFromString(allText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		(Main.instance = this).saveDefaultConfig();
		new PasswordFilter().registerFilter();
		Configs.setup();
		new MenuCore();
		new NPCCore();
		new LobbyCore();
		new Lobbies(this);
		new ServersCore();
		new PingReciver(this);
		mySQLConnection = new MySQL(this, this.getConfig().getString("Mysql.Host"), this.getConfig().getString("Mysql.Usuario"), this.getConfig().getString("Mysql.Senha"), this.getConfig().getString("Mysql.Database"), 3306);
		mySQLConnection.addTableUser("lobbyusers");
		mySQLConnection.openConnection();
		storager = new AtlasStorage("lobbyusers");
		loadEvents();
		hookCashPlugin();
		registerCommands();
		registerRunnables();
		registerIncoming();
		Bukkit.getConsoleSender().sendMessage("§a[AtlasLobby] Inicializado com sucesso.");
	}
	
	
	public void onDisable() {
		for(LobbyNPC npc : NPCCore.getNPCS().values()) {
			npc.despawn();
		}
		
		mySQLConnection.closeConnection();
	}
	
	public static CashAPI getCashAPI() {
		return cashAPI;
	}
	
	public void hookCashPlugin() {
	   if(Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")){
	    	cashAPI = new PlayerPointsHooker();
	        Bukkit.getConsoleSender().sendMessage("§a[AtlasLeilao] PlayerPointsHooker sincronizado com sucesso.");
	    }else {
	    	cashAPI = new DefaultCash();
	    }
	}
	
	
	public static AtlasStorage getStorager() {
		return storager;
	}
	
	public static MySQL getMySQLConnection() {
		return mySQLConnection;
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	private void registerIncoming() {
		if(Main.getInstance().getConfig().getBoolean("Geral.AntiWDL")) {
			new WDLReciver(this);
		}
		if(Main.getInstance().getConfig().getBoolean("AntiMod.Ativar")) {
			new FMLReciver(this);
		}
	}
	
	private void registerRunnables() {
		if(LobbyCore.isCaptcha()) {
		Bukkit.getScheduler().runTaskTimer(this, new CaptchaRunnable(), 15L, 15L);
		}
		Bukkit.getScheduler().runTaskTimer(this, new KickerTask(), 15L, 15L);
		if(LobbyCore.isScoreboard()) {
			Bukkit.getOnlinePlayers().forEach(p -> {
				ScoreBoard.createScoreBoard(p);
			});
			new ScoreUpdater().runTaskTimerAsynchronously(this, 40L, 100L);
		}
	}
	
	private void registerCommands() {
		getCommand("register").setExecutor(new CommandRegister());
		getCommand("login").setExecutor(new CommandLogin());
		getCommand("loginstaff").setExecutor(new CommandLoginStaff());
		getCommand("definirnpc").setExecutor(new CommandDefinirNPC());
		getCommand("lobby").setExecutor(new CommandLobby());
		getCommand("sair").setExecutor(new CommandSair());
		getCommand("sairfila").setExecutor(new CommandSairFila());
		getCommand("tell").setExecutor(new CommandTell());
		getCommand("r").setExecutor(new CommandReply());
		getCommand("clear").setExecutor(new CommandClear());
		getCommand("gamemode").setExecutor(new CommandGamemode());
		getCommand("desregistrar").setExecutor(new CommandDesregister());
		getCommand("fly").setExecutor(new CommandFly());
	}
	
	private void loadEvents() {
		Bukkit.getPluginManager().registerEvents(new GeneralLobbyListener(), this);
		Bukkit.getPluginManager().registerEvents(new BatalhasCore(), this);
		Bukkit.getPluginManager().registerEvents(new LobbyUserListener(), this);
		Bukkit.getPluginManager().registerEvents(new JumperListener(), this);
		Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
		Bukkit.getPluginManager().registerEvents(new CaptchaListener(), this);
		Bukkit.getPluginManager().registerEvents(new ScoreBoard(), this);
	}
	
}
