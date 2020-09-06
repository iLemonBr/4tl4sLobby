package com.atlasplugins.atlaslobby.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.object.AtlasPlayer;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ChatListener implements Listener{

	@EventHandler
	public void chatting(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		Player p = e.getPlayer();
		if (e.getPlayer().hasPermission("atlasplugins.chat.use")) {
			if(Main.getStorager().getCache().containsKey(p.getName().toLowerCase())) {
				if(Main.getStorager().getCache().get(p.getName().toLowerCase()).isLogged()) {
					AtlasPlayer atlasPlayer = Main.getStorager().getCache().get(p.getName().toLowerCase());
					if(!atlasPlayer.getPreferences().isChat()) {
						p.sendMessage("§cVocê não pode enviar mensagens no chat pois seu chat está desabilitado.");
						return;
					}
					for (Player online : Bukkit.getOnlinePlayers()) {
						if(Main.getStorager().getCache().containsKey(online.getName().toLowerCase())) {
							if(Main.getStorager().getCache().get(online.getName().toLowerCase()).isLogged()) {
								AtlasPlayer onlineAtlas = Main.getStorager().getCache().get(online.getName().toLowerCase());
								if(onlineAtlas.getPreferences().isChat()) {
									online.sendMessage(Main.getInstance().getConfig().getString("ChatFormat").replace("@message", e.getMessage())
											.replace("@player", e.getPlayer().getName()).replace("@pex", (PermissionsEx.getUser(e.getPlayer().getName()).getPrefix().isEmpty() ? "" : ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(e.getPlayer().getName()).getPrefix()))));
								}
							}
						}
					}
				}else {
					p.sendMessage("§cVocê precisa estar autenticado para utilizar o chat.");
					return;
				}
			}else {
				p.sendMessage("§cVocê precisa estar autenticado para utilizar o chat.");
				return;
			}
		}else {
			p.sendMessage("§cVocê não tem permissão para falar no chat do servidor.");
		}
	}
}
