package com.atlasplugins.atlaslobby.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.cores.BatalhasCore;

public class CommandSair implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(cmd.getName().equalsIgnoreCase("sair")) {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				if(BatalhasCore.getPlayersInBattle().containsKey(p.getName())) {
					BatalhasCore.exit(p);
					for(String s : Main.getInstance().getConfig().getStringList("Mensagens.SaiuPVP")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
					}
				}else {
					p.sendMessage("§cVocê não está em modo PVP.");
					return true;
				}
			}
		}
		return false;
	}

}
