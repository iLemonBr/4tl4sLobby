package com.atlasplugins.atlaslobby.hook;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class DefaultCash extends CashAPI{

	public DefaultCash() {
		Bukkit.getConsoleSender().sendMessage("§c[AtlasFactions] Erro, nenhum plugin de cash encontrado.");
	}
	
	@Override
	public double getCash(Player player) {
		return player.getLevel();
	}

	@Override
	public void setCash(Player player, double cash) {
		player.setLevel((int) cash);
	}

	public double getCash(String player) {
		return 0;
	}

	@Override
	public void setCash(String player, double cash) {
		Bukkit.getPlayer(player).setLevel((int) cash);
	}

}
