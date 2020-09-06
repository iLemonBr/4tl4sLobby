package com.atlasplugins.atlaslobby.captcha;

import com.atlasplugins.atlaslobby.object.AtlasPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.Title;
import com.atlasplugins.atlaslobby.cores.LobbyCore;

public class CaptchaListener implements Listener {

	@EventHandler
	public void onCaptcha(CaptchaVerificationEvent event) {
		if (Main.getInstance().getConfig().getBoolean("Geral.Titulos")) {
			Player p = event.getPlayer();
			if (Main.getStorager().getCache().containsKey(event.getPlayer().getName().toLowerCase())) {
				Title t = new Title(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Titulos.Login.Titulo")), ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Titulos.Login.SubTitulo")), 5, 80,
						30000);
				t.send(p);
			}else {
				Title t = new Title(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Titulos.Registro.Titulo")), ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Titulos.Registro.SubTitulo")), 5, 80,
						30000);
				t.send(p);
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e){
		Player p = (Player) e.getPlayer();
		if (p.getOpenInventory().getTitle().equalsIgnoreCase("Clique no bloco azul") && !LobbyCore.getCaptchaSolveds().contains(e.getPlayer().getName())){
            Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable() {
                @Override
                public void run() {
                    p.openInventory(e.getInventory());
                }
            }, 1L);
		}
	}
	@EventHandler
	public void onK(PlayerKickEvent event) {
		if (CaptchaGenerator.correctSlot.containsKey(event.getPlayer().getName())) {
			CaptchaGenerator.correctSlot.remove(event.getPlayer().getName());
		}
		if (LobbyCore.getCaptchaSolveds().contains(event.getPlayer().getName())) {
			LobbyCore.getCaptchaSolveds().remove(event.getPlayer().getName());
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (CaptchaGenerator.correctSlot.containsKey(event.getPlayer().getName())) {
			CaptchaGenerator.correctSlot.remove(event.getPlayer().getName());
		}
		if (LobbyCore.getCaptchaSolveds().contains(event.getPlayer().getName())) {
			LobbyCore.getCaptchaSolveds().remove(event.getPlayer().getName());
		}
	}

	@EventHandler
	public void onInv(InventoryClickEvent event) {
		if (event.getInventory().getName().equalsIgnoreCase("Clique no bloco azul")) {
			event.setCancelled(true);
			Player p = (Player) event.getWhoClicked();
			if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
				return;
			}
			if (!CaptchaGenerator.correctSlot.containsKey(p.getName())) {
				Bukkit.getConsoleSender().sendMessage("§cVocê não está no captcha ;/");
				return;
			}
			int correctSlot = CaptchaGenerator.correctSlot.get(p.getName());
			if (event.getSlot() == correctSlot) {
				CaptchaGenerator.correctSlot.remove(p.getName());
				LobbyCore.getCaptchaSolveds().add(p.getName());
				p.closeInventory();
				p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, 1);
				p.sendMessage("§aCaptcha efetuado com sucesso.");
				CaptchaVerificationEvent Cevent = new CaptchaVerificationEvent(p);
				Bukkit.getPluginManager().callEvent(Cevent);
			} else {
				p.kickPlayer("§cVocê precisa clicar no bloco azul para se autenticar.");
			}
		}
	}
}
