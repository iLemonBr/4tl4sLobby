package com.atlasplugins.atlaslobby.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.atlasplugins.atlaslobby.object.AtlasPlayer;

public class AtlasPlayerRegisterEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private AtlasPlayer atlasPlayer;
	
	public AtlasPlayerRegisterEvent(Player player, AtlasPlayer atlasPlayer) {
		this.player = player;
		this.atlasPlayer = atlasPlayer;
	}
	
	public Player getPlayer() {
		return player;
	}

	public AtlasPlayer getAtlasPlayer() {
		return atlasPlayer;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setAtlasPlayer(AtlasPlayer atlasPlayer) {
		this.atlasPlayer = atlasPlayer;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
