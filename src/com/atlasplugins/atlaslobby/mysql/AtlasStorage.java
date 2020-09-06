package com.atlasplugins.atlaslobby.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.Callback;
import com.atlasplugins.atlaslobby.apis.Title;
import com.atlasplugins.atlaslobby.cores.LobbyCore;
import com.atlasplugins.atlaslobby.object.AtlasPlayer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AtlasStorage {

	private String tableName;
	private Gson gson;
	private MySQL mySQLConnection = Main.getMySQLConnection();
	private HashMap<String, AtlasPlayer> cache = new HashMap<String, AtlasPlayer>();

	public AtlasStorage(String table) {
		tableName = table;
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.enableComplexMapKeySerialization();
		gson = gsonBuilder.create();
	}

	public HashMap<String, AtlasPlayer> getCache() {
		return cache;
	}
	
	public void userExists(String user, Callback<Boolean> callback , boolean async) {
		mySQLConnection.runSQL(new AtlasSQLRunnable(mySQLConnection.getConnection(), 
				new AtlasSQL("SELECT * FROM `"+tableName+"` WHERE `jogador` = ?", user.toLowerCase()), (result)->{
			 boolean a = result.next();
			callback.execute(a, a ? decode(result.getString("json"), AtlasPlayer.class) : null);
		}), async);
	}

	public boolean containsUser(String user) {
		try (PreparedStatement statement = mySQLConnection.getConnection()
				.prepareStatement("SELECT * FROM `" + tableName + "` WHERE `jogador` = ?")) {
			statement.setObject(1, user.toLowerCase());
			try (ResultSet result = statement.executeQuery()) {
				return result.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void loadUser(String user) {
		Player p = Bukkit.getPlayer(user);
		if (!cache.containsKey(user)) {
			mySQLConnection.runSQL(new AtlasSQLRunnable(mySQLConnection.getConnection(),
					new AtlasSQL("SELECT * FROM `" + tableName + "` WHERE `jogador` = ?", user), (result) -> {
						if (LobbyCore.isCaptcha()) {
							if (result.next()) {
								AtlasPlayer a = decode(result.getString("json"), AtlasPlayer.class);
								a.setLogged(false);
								if(p.hasPermission("atlasplugins.lobby.fly") && a.getPreferences().isFly()) {
									p.setAllowFlight(true);
									p.setFlying(true);
								}
								cache.put(user, a);
							}
						}else {
							if(result.next()) {
								AtlasPlayer a = (AtlasPlayer) decode(result.getString("json"), AtlasPlayer.class);
								a.setLogged(false);
								if(p.hasPermission("atlasplugins.lobby.fly") && a.getPreferences().isFly()) {
									p.setAllowFlight(true);
									p.setFlying(true);
								}
								cache.put(user, a);
								Title t = new Title(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Titulos.Login.Titulo")), ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Titulos.Login.SubTitulo")), 5, 80,
										30000);
								t.send(Bukkit.getPlayer(user));
							}else {
								Title t = new Title(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Titulos.Registro.Titulo")), ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Titulos.Registro.SubTitulo")), 5, 80,
										30000);
								t.send(Bukkit.getPlayer(user));
							}
						}
					}), true);
		}

	}

	public void savePlayer(String user, Object o, boolean asynchronously) {
		mySQLConnection.runUpdate(
				new AtlasSQLUpdateRunnable(mySQLConnection.getConnection(), new AtlasSQL(
						"UPDATE `" + tableName + "` SET `json` = ? WHERE `jogador` = ?;", encode(o), user)),
				asynchronously);
	}

	public void unLoadUser(String user) {
		if (cache.containsKey(user)) {
			mySQLConnection.runUpdate(new AtlasSQLUpdateRunnable(mySQLConnection.getConnection(), new AtlasSQL(
					"UPDATE `" + tableName + "` SET `json` = ? WHERE `jogador` = ?;", encode(cache.get(user)), user)),
					true);
			cache.remove(user);
		}
	}
	
	public void deletePlayer(String user,boolean async) {
		mySQLConnection.runUpdate(new AtlasSQLUpdateRunnable(mySQLConnection.getConnection(), new AtlasSQL("DELETE FROM `"+tableName+"` WHERE `jogador` = ?;", user)), async);
	}

	public String encode(Object classz) {
		String objectencoded = "";
		try {
			objectencoded = gson.toJson(classz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectencoded;
	}

	public <T> T decode(String encoded, Class<T> classz) {
		T f = null;
		try {
			f = gson.fromJson(encoded, classz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

}
