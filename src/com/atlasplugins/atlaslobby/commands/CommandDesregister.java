package com.atlasplugins.atlaslobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.Callback;
import com.atlasplugins.atlaslobby.object.AtlasPlayer;

public class CommandDesregister implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(cmd.getName().equalsIgnoreCase("desregistrar")) {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				if(p.hasPermission("atlasplugins.staff.admin")) {
					if(args.length < 1 || args.length > 1) {
						p.sendMessage("§cUtilize: /desregistrar <jogador>.");
						return true;
					}
					if(args.length == 1) {
						if(Main.getStorager().getCache().containsKey(args[0].toLowerCase())) {
							Main.getStorager().getCache().remove(args[0].toLowerCase());
							p.sendMessage("§aJogador desregistrado com sucesso.");
							Main.getStorager().deletePlayer(args[0].toLowerCase(), true);
							return true;
						}else {
							Main.getStorager().userExists(args[0].toLowerCase(), new Callback<Boolean>() {

								@Override
								public void execute(Boolean response, AtlasPlayer lobbyUser) {
									if(response) {
										p.sendMessage("§aJogador desregistrado com sucesso.");
										Main.getStorager().deletePlayer(args[0].toLowerCase(), true);
									}else {
										p.sendMessage("§cEste jogador não existe em nosso banco de dados");
									}
								}
								
							}, true);
						}
					}
				}else {
					p.sendMessage("§cVocê não tem permissão para executar este comando.");
					return true;
				}
			}else {
				ConsoleCommandSender p = Bukkit.getConsoleSender();
				if(args.length < 1 || args.length > 1) {
					p.sendMessage("§cUtilize: /desregistrar <jogador>.");
					return true;
				}
				if(args.length == 1) {
					if(Main.getStorager().getCache().containsKey(args[0].toLowerCase())) {
						Main.getStorager().getCache().remove(args[0].toLowerCase());
						p.sendMessage("§aJogador desregistrado com sucesso.");
						Main.getStorager().deletePlayer(args[0].toLowerCase(), true);
						return true;
					}else {
						Main.getStorager().userExists(args[0].toLowerCase(), new Callback<Boolean>() {

							@Override
							public void execute(Boolean response, AtlasPlayer lobbyUser) {
								if(response) {
									p.sendMessage("§aJogador desregistrado com sucesso.");
									Main.getStorager().deletePlayer(args[0].toLowerCase(), true);
								}else {
									p.sendMessage("§cEste jogador não existe em nosso banco de dados");
								}
							}
							
						}, true);
					}
				}
			}
		}
		return false;
	}

}
