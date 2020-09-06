package com.atlasplugins.atlaslobby.object;

public class AtlasServer {

	private LobbyServer lobbyServer;
	private LobbyInfo lobbyInfo;
	
	
	public AtlasServer(LobbyServer lobbyServer, LobbyInfo lobbyInfo) {
		super();
		this.lobbyServer = lobbyServer;
		this.lobbyInfo = lobbyInfo;
	}

	public boolean isLobbyServer() {
		return (lobbyServer != null);
	}
	
	public boolean isLobbyInfo() {
		return (lobbyInfo!=null);
	}

	public LobbyServer getLobbyServer() {
		return lobbyServer;
	}

	public LobbyInfo getLobbyInfo() {
		return lobbyInfo;
	}

	public void setLobbyServer(LobbyServer lobbyServer) {
		this.lobbyServer = lobbyServer;
	}

	public void setLobbyInfo(LobbyInfo lobbyInfo) {
		this.lobbyInfo = lobbyInfo;
	}
}
