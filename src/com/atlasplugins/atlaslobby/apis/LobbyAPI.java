package com.atlasplugins.atlaslobby.apis;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.cores.LobbyCore;
import com.atlasplugins.atlaslobby.cores.ServersCore;
import com.atlasplugins.atlaslobby.enums.ServerStatus;
import com.atlasplugins.atlaslobby.object.LobbyServer;
import com.atlasplugins.atlaslobby.queue.QueueManager;
import com.atlasplugins.atlaslobby.queue.QueueServer;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class LobbyAPI {

	public static String getPlayerGroupPrefix(Player p) {
		return (ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(p.getName()).getPrefix().replace("[", "").replace("]", "")).isEmpty() ? "§7Nenhum" : ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(p.getName()).getPrefix().replace("[", "").replace("]", "")));
	}
	
	public static void connectPlayerToTheServer(Player p, String server) {
		LobbyServer lobbyServer = ServersCore.getServers().get(server);
		if(LobbyCore.isFila()) {
			if(!p.hasPermission("atlasplugins.fila.bypass")) {
				if(ServersCore.getServers().containsKey(server)) {
					if(lobbyServer.isBetaVIP() && !p.hasPermission(lobbyServer.getBetaVipPermission())) {
						for(String s : Main.getInstance().getConfig().getStringList("Mensagens.BetaVIPMessage")) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
						}
						return;
					}
					if(lobbyServer.isMaintence() && !p.hasPermission("atlasplugins.maintence.bypass."+server)) {
						for(String s : Main.getInstance().getConfig().getStringList("Mensagens.MaintenceMode")) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
						}
						return;
					}
					if(lobbyServer.getServerStatus().equals(ServerStatus.OFFLINE)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Mensagens.ServidorOffline")));
						return;
					}
					if(!QueueManager.getFilas().containsKey(server)) {
						p.sendMessage("§cOcorreu um erro durante a conexão com este servidor, um staff foi notificado.");
						Bukkit.getConsoleSender().sendMessage("§e[AtlasLobby] Erro: Servidor "+server+" não encontrado.");
						return;
					}
					QueueServer queueServer = QueueManager.getFilas().get(server);
					if(!p.hasPermission("atlasplugins.fila.preferencia")) {
						if(!queueServer.isInQueue(p.getName())) {
							queueServer.addFila(p.getName());
								  p.sendMessage("");
						      	  p.sendMessage("§aVocê está na fila do §e"+lobbyServer.getServerName()+" §ana posição §e#"+queueServer.getPlayerPosition(p.getName())+"§a.");
						      	  new JSONText("§aClique ").text("§a§lAQUI ").clickRunCommand("/sairfila "+lobbyServer.getServerKey()).hoverText("§aSair da Fila").next().text("§apara sair da fila.").next().send(p);
						      	  p.sendMessage("");
						}else {
							p.sendMessage("§cVocê já está na fila do servidor "+lobbyServer.getServerName()+".");
						}
					}else {
						//add to queueVIPList
						if(!queueServer.isInVIPQueue(p.getName())) {
							queueServer.addFilaVIP(p.getName());
								  p.sendMessage("");
						      	  p.sendMessage("§aVocê está na fila do §e"+lobbyServer.getServerName()+" §ana posição §e#"+queueServer.getPlayerVIPPosition(p.getName())+"§a.");
						      	  new JSONText("§aClique ").text("§a§lAQUI ").clickRunCommand("/sairfila "+lobbyServer.getServerKey()).hoverText("§aSair da Fila").next().text("§apara sair da fila.").send(p);
						      	  p.sendMessage("");
						}else {
							p.sendMessage("§cVocê já está na fila do servidor "+lobbyServer.getServerName()+".");
						}
					}
				}
			}else {
				if(lobbyServer.getServerStatus().equals(ServerStatus.OFFLINE)) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Mensagens.ServidorOffline")));
					return;
				}
				p.sendMessage("§aConectando-se diretamente com o servidor (sem fila)...");
				enviarPlayer(p, server);
			}
		}else {
			enviarPlayer(p, server);
		}
	}
	
	public static String getPlayerCounted(String serverName) {
		if(ServersCore.getServers().containsKey(serverName)) {
			LobbyServer lobbyServer = ServersCore.getServers().get(serverName);
			if(lobbyServer.getServerStatus().equals(ServerStatus.ONLINE) || lobbyServer.getServerStatus().equals(ServerStatus.NOT_PINGED)) {
			  return ServersCore.getServers().get(serverName).getOnlinePlayers()+"/"+ServersCore.getServers().get(serverName).getMaxSlots()
					  +" "+(lobbyServer.getOnlinePlayers() > 1 || lobbyServer.getOnlinePlayers() == 0 ? "jogadores" : "jogador");
			}else {
				return "§cOffline";
			}
		}
		return "0/0 jogadores";
	}
	public static String getPlayerCount(String serverName) {
		if(ServersCore.getServers().containsKey(serverName)) {
			LobbyServer lobbyServer = ServersCore.getServers().get(serverName);
			if(lobbyServer.getServerStatus().equals(ServerStatus.ONLINE) || lobbyServer.getServerStatus().equals(ServerStatus.NOT_PINGED)) {
			  return ServersCore.getServers().get(serverName).getOnlinePlayers()+"/"+ServersCore.getServers().get(serverName).getMaxSlots();
			}else {
				return "§cOffline";
			}
		}
		if(LobbyCore.containsLobbyByServerName(serverName)){
			return LobbyCore.getLobbiesRegistreds().get(LobbyCore.getLobbyInfoByServerName(serverName)).getOnlinePlayers()+"/"+LobbyCore.getLobbiesRegistreds().get(LobbyCore.getLobbyInfoByServerName(serverName)).getMaxPlayers();
		}
		return "0/0";
	}
	
	public static void enviarPlayer(Player p, String server) {
		Main.getStorager().getCache().get(p.getName().toLowerCase()).setLogged(false);
		p.sendMessage("§aConectando...");
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("Connect");
			out.writeUTF(server);
		} catch (Exception e) {

		}
		p.sendPluginMessage(Main.getPlugin(Main.class), "BungeeCord", b.toByteArray());
	}
}
