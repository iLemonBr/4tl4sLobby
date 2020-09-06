package com.atlasplugins.atlaslobby.object;

public class ModInfo {

	
	private String modKey;
	private String modID;
	private String modName;
	
	
	public ModInfo(String modKey, String modID, String modName) {
		super();
		this.modKey = modKey;
		this.modID = modID;
		this.modName = modName;
	}

	public String getModKey() {
		return modKey;
	}

	public String getModID() {
		return modID;
	}

	public String getModName() {
		return modName;
	}

	public void setModKey(String modKey) {
		this.modKey = modKey;
	}

	public void setModID(String modID) {
		this.modID = modID;
	}

	public void setModName(String modName) {
		this.modName = modName;
	}
	
	
	
}
