package com.atlasplugins.atlaslobby.incoming;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.cores.LobbyCore;
import com.atlasplugins.atlaslobby.cores.ServersCore;
import com.atlasplugins.atlaslobby.enums.ServerStatus;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class PingReciver implements PluginMessageListener{

	public Main core;
	
	public PingReciver(Main core) {
		this.core = core;
		Bukkit.getMessenger().registerOutgoingPluginChannel(core, "BungeeCord");
		Bukkit.getMessenger().registerIncomingPluginChannel(core, "BungeeCord", this);
	}

	@Override
	public void onPluginMessageReceived(String channel, Player p, byte[] message) {
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("PlayerCount")) {
			String server = in.readUTF();
			int playerCount = in.readInt();
			if(server.equalsIgnoreCase("ALL")) {
				LobbyCore.setOnlinePlayersInNetwork(playerCount);
			}else {
				try {
					if (ServersCore.getServers().containsKey(server)) {
						if(ServersCore.getServers().get(server).getServerStatus().equals(ServerStatus.NOT_PINGED) || ServersCore.getServers().get(server).getServerStatus().equals(ServerStatus.OFFLINE)) {
							ServersCore.getServers().get(server).setServerStatus(ServerStatus.ONLINE);
						}
						ServersCore.getServers().get(server).setOnlinePlayers(playerCount);
					}
					if(LobbyCore.containsLobbyByServerName(server)) {
						LobbyCore.getLobbiesRegistreds().get(LobbyCore.getLobbyInfoByServerName(server)).setOnlinePlayers(playerCount);
					}
				} catch (Exception e) {
					Bukkit.getConsoleSender().sendMessage("§c[AtlasLobby] O servidor "+server+" não encontra-se acessível no momento.");
				}
			}
		}
	}

}