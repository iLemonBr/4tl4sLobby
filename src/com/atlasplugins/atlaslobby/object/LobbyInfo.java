package com.atlasplugins.atlaslobby.object;

import com.atlasplugins.atlaslobby.enums.ServerStatus;

public class LobbyInfo {

	
	private String lKey;
	private ServerStatus serverStatus;
	private String server;
	private int maxPlayers;
	private int onlinePlayers;
	private boolean manutencao;
	
	public LobbyInfo(String lKey, String server, int maxPlayers, boolean manutencao) {
		super();
		this.lKey = lKey;
		this.server = server;
		this.maxPlayers = maxPlayers;
		this.manutencao = manutencao;
		this.onlinePlayers = 0;
		this.serverStatus = ServerStatus.NOT_PINGED;
	}
	
	public ServerStatus getServerStatus() {
		return serverStatus;
	}

	public void setServerStatus(ServerStatus serverStatus) {
		this.serverStatus = serverStatus;
	}

	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	public void setOnlinePlayers(int onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
	}

	public String getlKey() {
		return lKey;
	}
	public String getServer() {
		return server;
	}
	public int getMaxPlayers() {
		return maxPlayers;
	}
	public boolean isManutencao() {
		return manutencao;
	}
	public void setlKey(String lKey) {
		this.lKey = lKey;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	public void setManutencao(boolean manutencao) {
		this.manutencao = manutencao;
	}
	
	
}
