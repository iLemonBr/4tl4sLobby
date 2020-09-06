package com.atlasplugins.atlaslobby.object;

import com.atlasplugins.atlaslobby.Main;
public class AtlasPlayer implements Cloneable{
	
	private String username;
	private String password;
	private String email;
	private Preferences preferences;
	private String registryDate;
	private int kills;
	private int deaths;
	private boolean logged;
	
	public AtlasPlayer(String username, String password, String email, String registryDate) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.preferences = new Preferences();
		this.logged = true;
		this.registryDate = registryDate;
		this.kills = 0;
		this.deaths = 0;
	}

	public String getName() {
		return username;
	}
	
	public void save(boolean asynchronously) {
		AtlasPlayer a = this;
		try {
			a = (AtlasPlayer) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		a.setLogged(false);
		Main.getStorager().savePlayer(this.username, a, asynchronously);
	}
	
	public int getKills() {
		return kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public void addDeath() {
		this.deaths += 1;
	}
	
	public void addKill() {
		this.kills += 1;
	}

	public String getRegistryDate() {
		return registryDate;
	}

	public void setRegistryDate(String registryDate) {
		this.registryDate = registryDate;
	}

	public boolean isLogged() {
		return logged;
	}


	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public Preferences getPreferences() {
		return preferences;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
