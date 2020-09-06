package com.atlasplugins.atlaslobby.commands;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.Title;
import com.atlasplugins.atlaslobby.cores.LobbyCore;
import com.atlasplugins.atlaslobby.object.AtlasPlayer;

public class CommandLoginStaff implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(cmd.getName().equalsIgnoreCase("loginstaff")) {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				if(p.hasPermission("atlasplugins.staff.auth")) {
					if(args.length < 1 || args.length > 1) {
						p.sendMessage("§cUtilize: /loginstaff <senha>.");
						return true;
					}
					if(args.length == 1) {
						if(args[0].equals(LobbyCore.getStaffPassword())) {
							if(LobbyCore.isLoginStaffSystem()) {
								if(Main.getStorager().getCache().containsKey(p.getName().toLowerCase())) {
									AtlasPlayer loginUser = Main.getStorager().getCache().get(p.getName().toLowerCase());
									if(LobbyCore.getAwaitStaffLogin().contains(p.getName())) {
										loginUser.setLogged(true);
										p.sendMessage("§aSeja bem vindo(a) de volta!.");
										LobbyCore.giveLobbyItems(p);
										Title t = new Title(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Titulos.Autenticado.Titulo")), ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Titulos.Autenticado.SubTitulo")), 0, 0, 60);
										t.send(p);
										p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
										new BukkitRunnable() {

											@Override
											public void run() {
												// TODO Auto-gene
												p.sendMessage("");
												p.sendMessage("§e §lDICA: §eNunca compartilhe sua senha com ninguém.");
												p.sendMessage(" §eassim você nunca irá ser roubado ou invadido por alguém.");
												p.sendMessage("");
												p.playSound(p.getLocation(), Sound.ORB_PICKUP, 2, 2);
											}
										}.runTaskLater(Main.getPlugin(Main.class), 20L * 2);
										LobbyCore.getAwaitStaffLogin().remove(p.getName());
									}else {
										p.sendMessage("§cVocê precisa se autenticar primeiro antes de executar este comando.");
										return true;
									}
								}else {
									p.sendMessage("§cVocê precisa se registrar antes de executar este comando.");
									return true;
								}
							}else {
								p.sendMessage("§cEste comando foi desabilitado.");
								return true;
							}
						}else {
							p.kickPlayer("§cSenha Incorreta");
						}
					}
				}else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Mensagens.SemPermissao")));
					return true;
				}
			}
		}
		return false;
	}

}
