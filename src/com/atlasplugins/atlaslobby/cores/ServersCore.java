package com.atlasplugins.atlaslobby.cores;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.Configs;
import com.atlasplugins.atlaslobby.enums.ServerStatus;
import com.atlasplugins.atlaslobby.object.LobbyServer;
import com.atlasplugins.atlaslobby.queue.QueueManager;
import com.atlasplugins.atlaslobby.queue.QueueServer;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class ServersCore {

	private static BukkitTask playerCountUpdater;
	private static HashMap<String,LobbyServer> servers = new HashMap<String,LobbyServer>();
	
	
	public ServersCore() {
		FileConfiguration config = Configs.SERVIDORES.getConfig();
		
		for(String server : config.getConfigurationSection("Servidores").getKeys(false)) {
			String serverName = config.getString("Servidores."+server+".ServerName");
			String ipAdress = config.getString("Servidores."+server+".IP");
			int port = config.getInt("Servidores."+server+".Port");
			int maxSlots = config.getInt("Servidores."+server+".MaxSlots");
			boolean betaVIPActive = config.getBoolean("Servidores."+server+".BetaVIP.Ativar");
			String betaVIPPermission = config.getString("Servidores."+server+".BetaVIP.Permissao");
			boolean maintence = config.getBoolean("Servidores."+server+".Manutencao");
			servers.put(server, new LobbyServer(server, serverName, ipAdress, port, maxSlots, betaVIPActive, betaVIPPermission, maintence));
			QueueServer queueServer = new QueueServer(server);
			QueueManager.getFilas().put(server, queueServer);
		}
		playerCountUpdater = new BukkitRunnable() {
			
			@Override
			public void run() {
				try {
					ByteArrayDataOutput out1 = ByteStreams.newDataOutput();
			        out1.writeUTF("PlayerCount");
			        out1.writeUTF("ALL");
			        Bukkit.getServer().sendPluginMessage(Main.getPlugin(Main.class), "BungeeCord", out1.toByteArray());
				} catch (Exception e) {
					e.printStackTrace();
				}
				for(LobbyServer server : servers.values()) {
					if(ping(server)) {
						if(server.getServerStatus().equals(ServerStatus.NOT_PINGED) || server.getServerStatus().equals(ServerStatus.OFFLINE)) {
							server.setServerStatus(ServerStatus.ONLINE);
						}
						try {
							ByteArrayDataOutput out1 = ByteStreams.newDataOutput();
					        out1.writeUTF("PlayerCount");
					        out1.writeUTF(server.getServerKey());
					        Bukkit.getServer().sendPluginMessage(Main.getPlugin(Main.class), "BungeeCord", out1.toByteArray());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else {
						server.setServerStatus(ServerStatus.OFFLINE);
					}
				}
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(Main.class), 20L*2, 20L*8);
		Bukkit.getConsoleSender().sendMessage("§a[Servidores] Todos os servidores foram carregados com sucesso.");
	}
	
	public static HashMap<String,LobbyServer> getServers(){
		return servers;
	}
	
	private static boolean ping(LobbyServer lobbyServer) {
		try {
			Socket socket = new Socket();
            socket.connect(new InetSocketAddress(lobbyServer.getServerIP(), lobbyServer.getServerPort()), 1 * 1000);
           
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
           
            out.write(0xFE);
           
            StringBuilder str = new StringBuilder();
           
            int b;
            while ((b = in.read()) != -1) {
                    if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24) {
                            str.append((char) b);
                    }
            }
           return true;
    } catch (Exception e) {
    	return false;
    }
	}
	
}
