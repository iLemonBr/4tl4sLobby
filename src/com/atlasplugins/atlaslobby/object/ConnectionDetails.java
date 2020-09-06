package com.atlasplugins.atlaslobby.object;

public class ConnectionDetails {

	
	private boolean active;
	private String serverName;
	
	public ConnectionDetails(boolean active, String serverName) {
		super();
		this.active = active;
		this.serverName = serverName;
	}

	public boolean isActive() {
		return active;
	}

	public String getServerName() {
		return serverName;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	
}
