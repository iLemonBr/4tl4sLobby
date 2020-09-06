package com.atlasplugins.atlaslobby.object;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.Configs;
import com.atlasplugins.atlaslobby.apis.Hidder;
import com.atlasplugins.atlaslobby.apis.LobbyAPI;
import com.atlasplugins.atlaslobby.apis.LocationSerializer;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class LobbyNPC {
	
	private String npcKey;
	private String npcSkin;
	private ConnectionDetails connectionDetails;
	private ItemStack itemInHand;
	private Location location;
	private boolean useHologram;
	private double hologramDistance;
	private List<String> hologramLines;
	private int npcID;
	private Hologram hologram;
	private boolean spawned;
	
	
	public LobbyNPC(String npcKey, String npcSkin, ConnectionDetails connectionDetails, ItemStack itemInHand,
			Location location, boolean useHologram, double hologramDistance, List<String> hologramLines) {
		super();
		this.spawned = false;
		this.npcKey = npcKey;
		this.npcSkin = npcSkin;
		this.connectionDetails = connectionDetails;
		this.itemInHand = itemInHand;
		this.location = location;
		this.useHologram = useHologram;
		this.hologramDistance = hologramDistance;
		this.hologramLines = hologramLines;
		this.npcID = getNumericReferenceNumber(npcKey);
	}
	
	public void saveLocation() {
		Configs.NPCS.getConfig().set("NPCS."+npcKey+".Localizacao", LocationSerializer.getSerializedLocation(location));
		Configs.NPCS.saveConfig();
	}
	
	public int getNPCID() {
		return npcID;
	}
	
	public Hologram getHologram() {
		return hologram;
	}
	
	public void despawn() {
		if(hologram != null) {
			hologram.delete();
		}
		if(location!=null) {
			for(Entity e : location.getWorld().getEntities()) {
				if(e.getType().equals(EntityType.ARMOR_STAND) && e.hasMetadata("hidder")) {
					e.remove();
				}
			}
		}
	}
	
	public void spawn() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(location != null) {
					//spawn
					if(hologram != null) {
						hologram.clearLines();
						hologram.delete();
					}
					
					if(CitizensAPI.getNPCRegistry().getById(npcID)!=null) {
						CitizensAPI.getNPCRegistry().getById(npcID).despawn();
					}
					
		        	NPCRegistry registry = CitizensAPI.getNPCRegistry();
					NPC npc = registry.createNPC(EntityType.PLAYER, UUID.randomUUID(), npcID, "§8[NPC] "+npcKey);
					npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, npcSkin);
					npc.spawn(location);
					if(useHologram) {
						Location loc2 = location.clone().add(0, hologramDistance, 0);
						Hologram holo = HologramsAPI.createHologram(Main.getPlugin(Main.class), loc2);
						int i = 0;
						for(String s : hologramLines) {
							holo.insertTextLine(i, ChatColor.translateAlternateColorCodes('&', s));
							i++;
						}
					}
					hologram = HologramsAPI.createHologram(Main.getPlugin(Main.class), location.clone().add(0,1.34,0));
					hologram.insertTextLine(0, (connectionDetails.isActive() ? "§7"+LobbyAPI.getPlayerCount(connectionDetails.getServerName()) : "§7"));
					new BukkitRunnable() {
						
						@Override
						public void run() {
							for(Player online : Bukkit.getOnlinePlayers()) {
								Hidder.hideNPCNameTag(online, npc.getEntity());
							}
							Hidder.addMetaDados(npc.getEntity(), (connectionDetails.isActive() ? connectionDetails.getServerName() : "Nenhum"));
							Bukkit.getConsoleSender().sendMessage("§a[NPC] NPC "+npcKey+" carregado com sucesso.");
							LobbyNPC.this.spawned = true;
							
						}
					}.runTaskLater(Main.getPlugin(Main.class), 20L*3);
				}else {
					Bukkit.getConsoleSender().sendMessage("§c[AtlasLobby] O NPC "+LobbyNPC.this.npcKey+" não foi spawnado pois ainda não foi definido uma localização para ele.");
				}
			}
		}.runTaskLater(Main.getPlugin(Main.class),20L*1);
	}
	
	
	
    public boolean isSpawned() {
		return spawned;
	}

	private static int getNumericReferenceNumber(String str) {
    	int id = 0;
    	for(char c : str.toCharArray()) {
    		id+= Character.getNumericValue(c);
    	}
    	return id;
    }

	public String getNpcKey() {
		return npcKey;
	}

	public String getNpcSkin() {
		return npcSkin;
	}

	public ConnectionDetails getConnectionDetails() {
		return connectionDetails;
	}

	public ItemStack getItemInHand() {
		return itemInHand;
	}

	public Location getLocation() {
		return location;
	}

	public boolean isUseHologram() {
		return useHologram;
	}

	public double getHologramDistance() {
		return hologramDistance;
	}

	public List<String> getHologramLines() {
		return hologramLines;
	}

	public void setNpcKey(String npcKey) {
		this.npcKey = npcKey;
	}

	public void setNpcSkin(String npcSkin) {
		this.npcSkin = npcSkin;
	}

	public void setConnectionDetails(ConnectionDetails connectionDetails) {
		this.connectionDetails = connectionDetails;
	}

	public void setItemInHand(ItemStack itemInHand) {
		this.itemInHand = itemInHand;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setUseHologram(boolean useHologram) {
		this.useHologram = useHologram;
	}

	public void setHologramDistance(double hologramDistance) {
		this.hologramDistance = hologramDistance;
	}

	public void setHologramLines(List<String> hologramLines) {
		this.hologramLines = hologramLines;
	}

	
}
