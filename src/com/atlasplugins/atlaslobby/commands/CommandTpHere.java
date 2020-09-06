package com.atlasplugins.atlaslobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CommandTpHere implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		// TODO Auto-generated method stub
		if(cmd.getName().equalsIgnoreCase("tphere")) {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				if(p.hasPermission("atlasplugins.tphere")) {
					if(args.length == 1) {
						String user = args[0];
						if(Bukkit.getPlayer(user)!=null) {
							Player target = Bukkit.getPlayer(user);
							target.teleport(p.getLocation());
							target.sendMessage("§aO jogador "+(PermissionsEx.getUser(target).getPrefix().equalsIgnoreCase("&f") ? "§7"+p.getName() : PermissionsEx.getUser(target).getPrefix().replace("&", "§")+" "+p.getName())+"§a teletransportou você até esta localização.");
							p.sendMessage("§aVocê teleportou o jogador "+(PermissionsEx.getUser(target).getPrefix().equalsIgnoreCase("&f") ? "§7"+target.getName() : PermissionsEx.getUser(target).getPrefix().replace("&", "§")+" "+target.getName())+" §aaté sua localização atual.");
						}else {
							p.sendMessage("§cEste jogador está offline.");
							return true;
						}
					}else {
						p.sendMessage("§cUtilize: /tphere <jogador> para teleporta-lo até este local.");
						return true;
					}
				}else {
					p.sendMessage("§cVocê não tem permissão para executar este comando.");
					return true;
				}
			}else {
				sender.sendMessage("§cEste comando pode apenas ser utilizado in-game.");
				return true;
			}
		}
		return false;
	}

}
