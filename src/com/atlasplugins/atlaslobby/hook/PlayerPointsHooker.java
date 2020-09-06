package com.atlasplugins.atlaslobby.hook;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlayerPointsHooker extends CashAPI{

	private PlayerPoints playerPoints;
	
	public PlayerPointsHooker() {
		hookPlayerPoints();
	}
	
	private boolean hookPlayerPoints() {
	    final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints");
	    playerPoints = (PlayerPoints) plugin;
	    return playerPoints != null; 
	}

	public PlayerPoints getPlayerPoints() {
	    return playerPoints;
	}
	
	public double getCash(Player player) {
		return playerPoints.getAPI().look(player.getName());
	}

	public void setCash(Player player, double cash) {
		playerPoints.getAPI().set(player.getCustomName(), (int)cash);
	}

	public double getCash(String player) {
		return playerPoints.getAPI().look(player);
	}

	public void setCash(String player, double cash) {
		playerPoints.getAPI().set(player, (int)cash);
	}
	
}
