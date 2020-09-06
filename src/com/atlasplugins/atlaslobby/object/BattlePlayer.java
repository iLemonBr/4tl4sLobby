package com.atlasplugins.atlaslobby.object;

import java.util.List;

public class BattlePlayer {

	
	private String player;
	private List<String> hiddePlayers;
	
	public BattlePlayer(String player, List<String> hiddePlayers) {
		super();
		this.player = player;
		this.hiddePlayers = hiddePlayers;
	}

	public String getPlayer() {
		return player;
	}

	public List<String> getHiddePlayers() {
		return hiddePlayers;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public void setHiddePlayers(List<String> hiddePlayers) {
		this.hiddePlayers = hiddePlayers;
	}
	
	
}
