package com.atlasplugins.atlaslobby.runnables;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.cores.LobbyCore;
import com.atlasplugins.atlaslobby.mysql.AtlasStorage;
import com.atlasplugins.atlaslobby.object.AtlasPlayer;

public class KickerTask extends BukkitRunnable {

	public AtlasStorage storager = Main.getStorager();
	public static HashMap<String, Long> kickMillis = new HashMap<String, Long>();

	@Override
	public void run() {
		Bukkit.getOnlinePlayers().forEach(p -> {
			if (!kickMillis.containsKey(p.getName())) {
				if (!isLogged(p) || !LobbyCore.getCaptchaSolveds().contains(p.getName())) {
					kickMillis.put(p.getName(), System.currentTimeMillis());
				}
			} else {
				long current = System.currentTimeMillis();
				long past = kickMillis.get(p.getName());
				if(current >= (past + 1*60*1000)) {
					if (LobbyCore.getCaptchaSolveds().contains(p.getName())) {
						if (storager.getCache().containsKey(p.getName().toLowerCase())) {
							AtlasPlayer atlasPlayer = storager.getCache().get(p.getName().toLowerCase());
							if (!atlasPlayer.isLogged()) {
								p.kickPlayer("§cVocê demorou muito para se autenticar.");
								kickMillis.remove(p.getName());
							}
						}else{
							p.kickPlayer("§cVocê demorou muito para se autenticar.");
							kickMillis.remove(p.getName());
						}
					}else {
						p.kickPlayer("§cVocê demorou muito para completar o captcha.");
						kickMillis.remove(p.getName());
					}
				}
			}
		});
	}
	
	public boolean isLogged(Player p) {
		if(storager.getCache().containsKey(p.getName().toLowerCase())) {
			AtlasPlayer atlasPlayer = storager.getCache().get(p.getName().toLowerCase());
			return atlasPlayer.isLogged();
		}else {
			return false;
		}
	}

}
