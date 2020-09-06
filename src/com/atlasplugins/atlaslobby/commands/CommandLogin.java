package com.atlasplugins.atlaslobby.commands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.MD5Hash;
import com.atlasplugins.atlaslobby.apis.Title;
import com.atlasplugins.atlaslobby.cores.LobbyCore;
import com.atlasplugins.atlaslobby.object.AtlasPlayer;


public class CommandLogin implements CommandExecutor {

	public static HashMap<String, Integer> tentativas = new HashMap<String, Integer>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (cmd.getName().equalsIgnoreCase("login")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (Main.getStorager().getCache().containsKey(p.getName().toLowerCase())) {
					AtlasPlayer loginUser = Main.getStorager().getCache().get(p.getName().toLowerCase());
					if (loginUser.isLogged()) {
						p.sendMessage("§cVocê já está autenticado.");
						p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
						return true;
					}
					if (args.length < 1 || args.length > 1) {
						p.sendMessage("§cUtilize: /login <senha>.");
						p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
						return true;
					}
					if (args.length == 1) {
						if(!LobbyCore.getCaptchaSolveds().contains(p.getName())) {
							p.sendMessage("§cVocê precisa verificar-se no captcha primeiro.");
							return true;
						}
						String password = args[0];
						if (MD5Hash.MD5(password).matches(loginUser.getPassword())) {
							    if(!p.hasPermission("atlasplugins.staff.auth")) {
							    	loginUser.setLogged(true);
									p.sendMessage("§aSeja bem vindo(a) de volta!.");
									LobbyCore.giveLobbyItems(p);
									Title t = new Title(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Titulos.Autenticado.Titulo")), ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Titulos.Autenticado.SubTitulo")), 0, 0, 60);
									t.send(p);
									p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
									if(Main.getInstance().getConfig().getBoolean("Dica.Ativar")) {
										new BukkitRunnable() {

											@Override
											public void run() {
												for(String s : Main.getInstance().getConfig().getStringList("Dica.Mensagem")) {
													p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
												}
												p.playSound(p.getLocation(), Sound.valueOf(Main.getInstance().getConfig().getString("Dica.Som")), 2, 2);
											}
										}.runTaskLater(Main.getPlugin(Main.class), 20L * 2);
									}
							    }else {
							    	if(LobbyCore.isLoginStaffSystem()) {
							    		LobbyCore.getAwaitStaffLogin().add(p.getName());
							    		Title t = new Title(
							    				ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Titulos.LoginStaff.Titulo")),
							    				ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Titulos.LoginStaff.SubTitulo")), 5, 80,
												30000);
										t.send(p);
							    	}else {
							    		loginUser.setLogged(true);
										p.sendMessage("§aSeja bem vindo(a) de volta!.");
										LobbyCore.giveLobbyItems(p);
										Title t = new Title(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Titulos.Autenticado.Titulo")), ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Titulos.Autenticado.SubTitulo")), 0, 0, 60);
										t.send(p);
										p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
										if(Main.getInstance().getConfig().getBoolean("Dica.Ativar")) {
											new BukkitRunnable() {

												@Override
												public void run() {
													for(String s : Main.getInstance().getConfig().getStringList("Dica.Mensagem")) {
														p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
													}
													p.playSound(p.getLocation(), Sound.valueOf(Main.getInstance().getConfig().getString("Dica.Som")), 2, 2);
												}
											}.runTaskLater(Main.getPlugin(Main.class), 20L * 2);
										}
							    	}
							    }
						} else {
							if (!tentativas.containsKey(p.getName())) {
								tentativas.put(p.getName(), 2);
								p.sendMessage("§cSenha incorreta!, você tem mais " + tentativas.get(p.getName())
										+ " §ctentativas.");
								p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
								return true;
							} else {
								if (tentativas.get(p.getName()) > 1) {
									tentativas.put(p.getName(), tentativas.get(p.getName()) - 1);
									p.sendMessage("§cSenha incorreta!, você tem mais " + tentativas.get(p.getName())
											+ " §ctentativas.");
									p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
									return true;
								} else {
									p.kickPlayer("§cVocê excedeu o máximo de tentativas, relogue e tente novamente.");
								}
							}
						}
					}
				} else {
					p.sendMessage("§cVocê precisa registrar-se primeiro antes de fazer login.");
					p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
					return true;
				}
			}
		}
		return false;
	}

}
