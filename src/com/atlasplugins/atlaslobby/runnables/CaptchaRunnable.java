package com.atlasplugins.atlaslobby.runnables;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.atlasplugins.atlaslobby.captcha.CaptchaGenerator;
import com.atlasplugins.atlaslobby.cores.LobbyCore;

public class CaptchaRunnable extends BukkitRunnable{

	public void run() {
		Bukkit.getOnlinePlayers().forEach(p -> {
			if (!LobbyCore.getCaptchaSolveds().contains(p.getName())) {
				if(p.getOpenInventory() == null || (p.getOpenInventory() != null && !p.getOpenInventory().getTitle().equalsIgnoreCase("Clique no bloco azul"))) {
					new CaptchaGenerator(p);
				}
			}
		});
	}

}
