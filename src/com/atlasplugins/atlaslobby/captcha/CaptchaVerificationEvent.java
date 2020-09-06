package com.atlasplugins.atlaslobby.captcha;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CaptchaVerificationEvent extends Event{
	
	private static final HandlerList handlers = new HandlerList();
	private Player user;
	
	public CaptchaVerificationEvent(Player user) {
		super();
		this.user = user;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Player getPlayer() {
		return user;
	}
	
}
