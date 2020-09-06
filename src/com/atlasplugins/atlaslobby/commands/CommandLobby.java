package com.atlasplugins.atlaslobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.atlasplugins.atlaslobby.cores.LobbyCore;

public class CommandLobby implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String paramString, String[] args) {

		if (cmd.getName().equalsIgnoreCase("lobby")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (p.hasPermission("atlasplugins.staff.admin")) {
					if (args.length < 1 || args.length > 1) {
						p.sendMessage("");
						p.sendMessage("§e/lobby definir §8- §7Define a localização de spawn de jogadores.");
						p.sendMessage("§e/lobby ir §8- §7Voltar a localização de spawn de jogadores (apenas staffs)");
						p.sendMessage("");
						return true;
					}
					if(args.length == 1) {
						if(args[0].equalsIgnoreCase("ir")) {
							if(LobbyCore.getSpawn() == null) {
								p.sendMessage("§cNenhum spawn ainda foi definido, por favor utilize '/lobby definir' para definir um spawn de jogadores.");
								return true;
							}else {
								p.teleport(LobbyCore.getSpawn());
								p.sendMessage("§aTeletransportado com sucesso.");
								return true;
							}
						}else if(args[0].equalsIgnoreCase("definir")) {
							if(LobbyCore.getSpawn() == null) {
								p.sendMessage("§aLocalização de spawn de jogadores no lobby definida com sucesso.");
							}else {
								p.sendMessage("§aNova localização de spawn de jogadores definida X: §f"+p.getLocation().getX()+" §aY: §f"+p.getLocation().getY()+" §aZ: §f"+p.getLocation().getZ());
							}
							LobbyCore.setSpawn(p.getLocation());
							return true;
						}else {
							p.sendMessage("");
							p.sendMessage("§e/lobby definir §8- §7Define a localização de spawn de jogadores.");
							p.sendMessage("§e/lobby ir §8- §7Voltar a localização de spawn de jogadores (apenas staffs)");
							p.sendMessage("");
							return true;
						}
					}
				} else {
					if(LobbyCore.getSpawn()!=null) {
						p.teleport(LobbyCore.getSpawn());
						p.sendMessage("§aTeletransportado com sucesso.");
						return true;
					}else {
						p.sendMessage("§cNenhuma localização de spawn do lobby ainda foi definida, por isso você não foi teletransportado.");
						return true;
					}
				}
			}
		}
		return false;
	}

}
