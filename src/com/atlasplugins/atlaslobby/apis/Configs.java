package com.atlasplugins.atlaslobby.apis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.atlasplugins.atlaslobby.Main;

public enum Configs {

    DATA,MENUS,NPCS,SERVIDORES;

    private RawConfig config;

    Configs() {
	this.config = new RawConfig(Main.getPlugin(Main.class), this.name().toLowerCase() + ".yml");
    }

    /**
     * @return the config
     */

    public static void setup() {
	for (Configs cfg : Configs.values()) {
	    cfg.getRawConfig().saveDefaultConfig();

	}
    }

    public RawConfig getRawConfig() {
	return config;
    }

    public FileConfiguration getConfig() {
	return config.getConfig();
    }

    public void saveConfig() {
	this.config.saveConfig();
    }

    public class RawConfig {

	private JavaPlugin plugin;
	private String configName;
	private File configFile;
	private FileConfiguration config;

	public RawConfig(JavaPlugin plugin, String fileName) {
	    this.plugin = plugin;
	    this.configName = fileName;
	    File dataFolder = plugin.getDataFolder();
	    this.configFile = new File(dataFolder.toString() + File.separatorChar + this.configName);
	}

	public void reloadConfig() {
	    try {
		this.config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(this.configFile), StandardCharsets.UTF_8));
	    } catch (FileNotFoundException e) {
		e.printStackTrace();
	    }
	    InputStream is = this.plugin.getResource(this.configName);
	    if (is != null) {
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(is);
		this.config.setDefaults(defConfig);
	    }
	}

	public FileConfiguration getConfig() {
	    if (this.config == null) {
		reloadConfig();
	    }
	    return this.config;
	}

	public void saveConfig() {
	    if ((this.config == null) || (this.configFile == null)) {
		return;
	    }
	    try {
		getConfig().save(this.configFile);
	    } catch (IOException ex) {
		ex.printStackTrace();
	    }
	}

	public void saveDefaultConfig() {
	    if (!this.configFile.exists()) {
		this.plugin.saveResource(this.configName, false);
	    }
	}
    }

}