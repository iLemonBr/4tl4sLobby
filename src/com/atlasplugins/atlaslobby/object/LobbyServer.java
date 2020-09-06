package com.atlasplugins.atlaslobby.object;

import org.bukkit.Bukkit;

import com.atlasplugins.atlaslobby.enums.ServerStatus;

public class LobbyServer {

	private String serverKey;
	private ServerStatus serverStatus;
	private String serverName;
	private String serverIP;
	private int serverPort;
	private int maxSlots;
	private int onlinePlayers;
	private boolean betaVIP;
	private String betaVipPermission;
	private boolean maintence;
	
	public LobbyServer(String serverKey, String serverName, String serverIP, int serverPort, int maxSlots,
			boolean betaVIP, String betaVipPermission, boolean maintence) {
		super();
		this.serverKey = serverKey;
		this.serverName = serverName;
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		this.maxSlots = maxSlots;
		this.betaVIP = betaVIP;
		this.betaVipPermission = betaVipPermission;
		this.maintence = maintence;
		this.onlinePlayers = 0;
		this.serverStatus = ServerStatus.NOT_PINGED;
		Bukkit.getConsoleSender().sendMessage("§a[Servidor] "+this.serverName+" carregado.");
	}
	
	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	public void setOnlinePlayers(int onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
	}

	public ServerStatus getServerStatus() {
		return serverStatus;
	}

	public void setServerStatus(ServerStatus serverStatus) {
		this.serverStatus = serverStatus;
	}

	public String getServerKey() {
		return serverKey;
	}
	public String getServerName() {
		return serverName;
	}
	public String getServerIP() {
		return serverIP;
	}
	public int getServerPort() {
		return serverPort;
	}
	public int getMaxSlots() {
		return maxSlots;
	}
	public boolean isBetaVIP() {
		return betaVIP;
	}
	public String getBetaVipPermission() {
		return betaVipPermission;
	}
	public boolean isMaintence() {
		return maintence;
	}
	public void setServerKey(String serverKey) {
		this.serverKey = serverKey;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public void setMaxSlots(int maxSlots) {
		this.maxSlots = maxSlots;
	}
	public void setBetaVIP(boolean betaVIP) {
		this.betaVIP = betaVIP;
	}
	public void setBetaVipPermission(String betaVipPermission) {
		this.betaVipPermission = betaVipPermission;
	}
	public void setMaintence(boolean maintence) {
		this.maintence = maintence;
	}
	
}
