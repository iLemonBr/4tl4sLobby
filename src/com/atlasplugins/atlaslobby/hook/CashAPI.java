package com.atlasplugins.atlaslobby.hook;

import org.bukkit.entity.Player;

public abstract class CashAPI {

	public abstract double getCash(Player player);
	public abstract double getCash(String player);
	public abstract void setCash(Player player, double cash);
	public abstract void setCash(String player, double cash);
	
}
