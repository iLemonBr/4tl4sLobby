package com.atlasplugins.atlaslobby.queue;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.JSONText;
import com.atlasplugins.atlaslobby.cores.ServersCore;
import com.atlasplugins.atlaslobby.enums.ServerStatus;
import com.atlasplugins.atlaslobby.object.AtlasPlayer;
import com.atlasplugins.atlaslobby.object.LobbyServer;

public class QueueServer {

	private String key;
	private Queue<AtlasPlayer> queueList = new LinkedList<AtlasPlayer>();
	private Queue<AtlasPlayer> queueVIPList = new LinkedList<AtlasPlayer>();

	public QueueServer(String key) {
		this.key = key;
		new BukkitRunnable() {

			@Override
			public void run() {
				andarFila();
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 20L * 2, 20L * 10);
		new BukkitRunnable() {

			@Override
			public void run() {
				andarFilaVIP();
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 20L * 2, 20L * 10);
	}
	
	public void andarFila() {
		LobbyServer lobbyServer = ServersCore.getServers().get(key);
		if (queueList.size() > 0 && queueVIPList.size() == 0) {
			if (lobbyServer.getOnlinePlayers() < lobbyServer.getMaxSlots()) {
				AtlasPlayer next = queueList.peek();
				if (Bukkit.getPlayer(next.getName()) != null) {
					Player p = Bukkit.getPlayer(next.getName());
					if (lobbyServer.getServerStatus().equals(ServerStatus.ONLINE)) {
						enviarPlayer(p, lobbyServer.getServerKey());
						queueList.remove();
						for (AtlasPlayer g : queueList) {
							if (Bukkit.getPlayer(g.getName()) != null) {
								Player a = Bukkit.getPlayer(g.getName());
								a.sendMessage("");
								a.sendMessage("§aVocê está na fila do §e" + lobbyServer.getServerName()
										+ " §ana posição §e#" + getPlayerPosition(a.getName()) + "§a.");
								new JSONText("§aClique ").text("§a§lAQUI ")
										.clickRunCommand("/sairfila " + lobbyServer.getServerKey())
										.hoverText("§aSair da Fila").next().text("§apara sair da fila.").next().send(a);
								a.sendMessage("");
							}
						}
					} else {
						p.sendMessage("§cDesculpe este servidor está offline no momento.");
						queueList.remove();
					}
				} else {
					queueList.remove();
				}
			}
		}
	}

	public void andarFilaVIP() {
		LobbyServer lobbyServer = ServersCore.getServers().get(key);
		if (queueVIPList.size() > 0) {
			if (lobbyServer.getOnlinePlayers() < lobbyServer.getMaxSlots()) {
				AtlasPlayer next = queueVIPList.peek();
				if (Bukkit.getPlayer(next.getName()) != null) {
					Player p = Bukkit.getPlayer(next.getName());
					if (lobbyServer.getServerStatus().equals(ServerStatus.ONLINE)) {
						enviarPlayer(p, lobbyServer.getServerKey());
						queueVIPList.remove();
						for (AtlasPlayer g : queueVIPList) {
							if (Bukkit.getPlayer(g.getName()) != null) {
								Player a = Bukkit.getPlayer(g.getName());
								a.sendMessage("");
								a.sendMessage("§aVocê está na fila do §e" + lobbyServer.getServerName()
										+ " §ana posição §e#" + getPlayerVIPPosition(a.getName()) + "§a.");
								new JSONText("§aClique ").text("§a§lAQUI ")
										.clickRunCommand("/sairfila " + lobbyServer.getServerKey())
										.hoverText("§aSair da Fila").next().text("§apara sair da fila.").next().send(a);
								a.sendMessage("");
							}
						}
					} else {
						p.sendMessage("§cDesculpe este servidor está offline no momento.");
						queueVIPList.remove();
					}
				} else {
					queueVIPList.remove();
				}
			}
		}
	}
	
	public Queue<AtlasPlayer> getQueueList() {
		return queueList;
	}

	public Queue<AtlasPlayer> getQueueVIPList() {
		return queueVIPList;
	}
	
	public LobbyServer getLobbyServer() {
		LobbyServer lobbyServer = ServersCore.getServers().get(key);
		return lobbyServer;
	}

	public void addFila(String name) {
		queueList.add(Main.getStorager().getCache().get(name.toLowerCase()));
	}

	public void addFilaVIP(String name) {
		queueVIPList.add(Main.getStorager().getCache().get(name.toLowerCase()));
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

	public void removeVIPFila(String nickname) {
		Iterator<AtlasPlayer> i = queueList.iterator();
		while (i.hasNext()) {
			AtlasPlayer a = (AtlasPlayer) i.next();
			if (a.getName().equalsIgnoreCase(nickname)) {
				i.remove();
			}
		}
	}

	public int getPlayerVIPPosition(String nickname) {
		int position = 1;
		for (AtlasPlayer a : queueList) {
			if (a.getName().equalsIgnoreCase(nickname)) {
				return position;
			} else {
				position++;
			}
		}
		return position;
	}

	public boolean isInVIPQueue(String nickname) {
		for (AtlasPlayer a : queueList) {
			if (a.getName().equalsIgnoreCase(nickname)) {
				return true;
			}
		}
		return false;
	}

	public void removeFila(String nickname) {
		Iterator<AtlasPlayer> i = queueList.iterator();
		while (i.hasNext()) {
			AtlasPlayer a = (AtlasPlayer) i.next();
			if (a.getName().equalsIgnoreCase(nickname)) {
				i.remove();
			}
		}
	}

	public int getPlayerPosition(String nickname) {
		int position = 1;
		for (AtlasPlayer a : queueList) {
			if (a.getName().equalsIgnoreCase(nickname)) {
				return position;
			} else {
				position++;
			}
		}
		return position;
	}

	public boolean isInQueue(String nickname) {
		for (AtlasPlayer a : queueList) {
			if (a.getName().equalsIgnoreCase(nickname)) {
				return true;
			}
		}
		return false;
	}
}
