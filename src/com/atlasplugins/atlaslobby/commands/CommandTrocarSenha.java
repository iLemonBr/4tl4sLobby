package com.atlasplugins.atlaslobby.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.MD5Hash;

public class CommandTrocarSenha implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(cmd.getName().equalsIgnoreCase("trocarsenha")) {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				if(args.length < 2 || args.length > 2) {
					p.sendMessage("§cUtilize: /trocarsenha <senha antiga> <nova senha>.");
					return true;
				}
				if(args.length == 2) {
					String oldPassword = args[0];
					String newPassword = args[1];
					if(Main.getStorager().getCache().containsKey(p.getName().toLowerCase())) {
						if(Main.getStorager().getCache().get(p.getName().toLowerCase()).isLogged()) {
							if(Main.getStorager().getCache().get(p.getName().toLowerCase()).getPassword().matches(MD5Hash.MD5(oldPassword))) {
								Main.getStorager().getCache().get(p.getName().toLowerCase()).setPassword(MD5Hash.MD5(newPassword));
								Main.getStorager().getCache().get(p.getName().toLowerCase()).save(true);
								p.playSound(p.getLocation(), Sound.VILLAGER_YES, 1, 1);
								p.sendMessage("§aSua senha foi alterada com sucesso!");
							}else {
								p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
								p.sendMessage("§cA sua senha antiga é inválida, tente novamente.");
								return true;
							}
						}else {
							p.sendMessage("§cVocê precisa se autenticar primeiro.");
							return true;
						}
					}else {
						p.sendMessage("§cOcorreu um erro, tente relogar.");
						return true;
					}
				}
			}
		}
		return false;
	}

}
