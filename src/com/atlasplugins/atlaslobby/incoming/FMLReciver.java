package com.atlasplugins.atlaslobby.incoming;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.object.ModData;
import com.atlasplugins.atlaslobby.object.ModInfo;

public class FMLReciver implements PluginMessageListener, Listener{

	public HashMap<Player,ModData> modManager = new HashMap<Player,ModData>();
	public HashMap<String,ModInfo> blackListMods = new HashMap<String,ModInfo>();
	public String kickMessage;
	public Main core;
	
	public FMLReciver(Main core) {
		this.core = core;
		this.kickMessage = "";

		for(String part : core.getInstance().getConfig().getStringList("Mensagens.KickadoMod")) {
			if(this.kickMessage.isEmpty()) {
				this.kickMessage = ChatColor.translateAlternateColorCodes('&', part);
			}else {
				this.kickMessage = this.kickMessage+"\n"+ChatColor.translateAlternateColorCodes('&', part);
			}
		}

		for(String modKey : Main.getInstance().getConfig().getConfigurationSection("AntiMod.ModBlackList").getKeys(false)) {
			String modID = Main.getInstance().getConfig().getString("AntiMod.ModBlackList."+modKey+".ModID");
			String modName = Main.getInstance().getConfig().getString("AntiMod.ModBlackList."+modKey+".ModName");
			ModInfo mod = new ModInfo(modKey, modID, modName);
			blackListMods.put(modID, mod);
		}

		Bukkit.getPluginManager().registerEvents(this, core);
		Bukkit.getMessenger().registerIncomingPluginChannel(core, "FML|HS", this);
		Bukkit.getMessenger().registerOutgoingPluginChannel(core, "FML|HS");

	}
	
	public void onPluginMessageReceived(String channel, Player player, byte[] data) {
		  if (data[0] == 2) {
	            final ModData modData = this.getModData(data);
	            for(String mod : modData.getMods()) {
	            	if(blackListMods.containsKey(mod)) {
	            		player.kickPlayer(this.kickMessage.replace("@mod", ""+blackListMods.get(mod).getModName()));
	            	}
	            }
	       }
	}
	
	@EventHandler
	public void sendPacket(PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		new BukkitRunnable() {
			@Override
			public void run() {
				FMLReciver.this.sendFmlPacket(player, (byte)-2, (byte)0);
				FMLReciver.this.sendFmlPacket(player, (byte)0, (byte)2, (byte)0, (byte)0, (byte)0, (byte)0);
				FMLReciver.this.sendFmlPacket(player, (byte)2, (byte)0, (byte)0, (byte)0, (byte)0);
			}
		}.runTaskLater(Main.getPlugin(Main.class), 20L);
	}
	
    private void sendFmlPacket(final Player player,  byte... data) {
        player.sendPluginMessage(core, "FML|HS", data);
    }

    private ModData getModData(final byte[] data) {
        final Map<String, String> mods = new HashMap<String, String>();
        boolean store = false;
        String tempName = null;
        int end;
        for (int i = 2; i < data.length; i = end, store = !store) {
            end = i + data[i] + 1;
            final byte[] range = Arrays.copyOfRange(data, i + 1, end);
            final String string = new String(range);
            if (store) {
                mods.put(tempName, string);
            }
            else {
                tempName = string;
            }
        }
        return new ModData(mods);
    }
}
