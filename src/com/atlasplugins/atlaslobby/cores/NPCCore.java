package com.atlasplugins.atlaslobby.cores;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.Configs;
import com.atlasplugins.atlaslobby.apis.Hidder;
import com.atlasplugins.atlaslobby.apis.ItemComposer;
import com.atlasplugins.atlaslobby.apis.LobbyAPI;
import com.atlasplugins.atlaslobby.apis.LocationSerializer;
import com.atlasplugins.atlaslobby.object.ConnectionDetails;
import com.atlasplugins.atlaslobby.object.LobbyNPC;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class NPCCore {

	
	private static HashMap<String,LobbyNPC> npcs = new HashMap<String,LobbyNPC>();
	
	public NPCCore() {
		FileConfiguration config = Configs.NPCS.getConfig();
		for(String npcKey : config.getConfigurationSection("NPCS").getKeys(false)) {
			double hologramDistance = config.getDouble("NPCS."+npcKey+".Hologram.Distance");
			boolean activeHologram = config.getBoolean("NPCS."+npcKey+".Hologram.Ativar");
			List<String> hologramLines = config.getStringList("NPCS."+npcKey+".Hologram.Lines");
			String npcSkin = config.getString("NPCS."+npcKey+".Skin");
			ConnectionDetails connectionDetails = new ConnectionDetails(config.getBoolean("NPCS."+npcKey+".Conexao.Ativar"), config.getString("NPCS."+npcKey+".Conexao.Servidor"));
			ItemStack itemInHand = null;
			if(config.getBoolean("NPCS."+npcKey+".ItemInHand.Ativar")) {
				if(config.getString("NPCS."+npcKey+".ItemInHand.Item.Skull").contains("https://") || config.getString("NPCS."+npcKey+".ItemInHand.Item.Skull").contains("http://")) {
					itemInHand = new ItemComposer(config.getString("NPCS."+npcKey+".ItemInHand.Item.Skull")).toItemStack();
				}else {
					itemInHand = new ItemComposer(Material.getMaterial(config.getInt("NPCS."+npcKey+".ItemInHand.Item.ID"))).setDurability((short)config.getInt("NPCS."+npcKey+".ItemInHand.Item.Data")).toItemStack();
				}
				if(config.getBoolean("NPCS."+npcKey+".ItemInHand.Item.Enchanted")) {
					itemInHand.addEnchantment(Enchantment.DURABILITY, 1);
				}
			}
			Location location = null;
			if(!config.getString("NPCS."+npcKey+".Localizacao").isEmpty() && config.getString("NPCS."+npcKey+".Localizacao").contains(";")) {
				location = LocationSerializer.getDeserializedLocation(config.getString("NPCS."+npcKey+".Localizacao"));
			}
			LobbyNPC npc = new LobbyNPC(npcKey, npcSkin, connectionDetails, itemInHand, location, activeHologram, hologramDistance, hologramLines);
			npc.spawn();
			npcs.put(npcKey, npc);
		}
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for(LobbyNPC npc : npcs.values()) {
					if(npc.isSpawned()) {
						TextLine t = (TextLine)npc.getHologram().getLine(0);
						t.setText((npc.getConnectionDetails().isActive() ? "§7"+LobbyAPI.getPlayerCount(npc.getConnectionDetails().getServerName()) : "§7"));
					}
				}
			}
		}.runTaskTimer(Main.getPlugin(Main.class), 20L*10, 20L*10);
	}
	
	public static HashMap<String,LobbyNPC> getNPCS() {
		return npcs;
	}
	
	public static boolean containsNPC(String npcKey) {
		return npcs.containsKey(npcKey);
	}
}
