package com.atlasplugins.atlaslobby.incoming;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.atlasplugins.atlaslobby.Main;

public class WDLReciver implements PluginMessageListener{
	
	public Main core;
	public String kickMessage;
	public static final String MODIFICATION = "World Downloader";
	
	public WDLReciver(Main core) {
		this.core = core;
		this.kickMessage = "";
		for(String part : core.getInstance().getConfig().getStringList("Mensagens.KickadoMod")) {
			if(this.kickMessage.isEmpty()) {
				this.kickMessage = ChatColor.translateAlternateColorCodes('&', part.replace("@mod", MODIFICATION));
			}else {
				this.kickMessage = this.kickMessage+"\n"+ChatColor.translateAlternateColorCodes('&', part.replace("@mod", MODIFICATION));
			}
		}
		Bukkit.getMessenger().registerIncomingPluginChannel(core, "WDL|INIT", this);
		Bukkit.getMessenger().registerOutgoingPluginChannel(core, "WDL|CONTROL");
	}
	
	public void unRegister() {
		Bukkit.getMessenger().unregisterIncomingPluginChannel(core, "WDL|INIT");
		Bukkit.getMessenger().unregisterOutgoingPluginChannel(core, "WDL|CONTROL");
	}

	@Override
    public void onPluginMessageReceived(final String channel, final Player player, final byte[] data) {
		 if (channel.equals("WDL|INIT") ) {
			 player.kickPlayer(kickMessage);
		 }
	}

}
