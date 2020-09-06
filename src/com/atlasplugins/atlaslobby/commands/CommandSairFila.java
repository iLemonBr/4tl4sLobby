package com.atlasplugins.atlaslobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.atlasplugins.atlaslobby.queue.QueueManager;

public class CommandSairFila implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(cmd.getName().equalsIgnoreCase("sairfila")) {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				if(args.length < 1 || args.length > 1) {
					p.sendMessage("§cUtilize: /sairfila <servidor>.");
					return true;
				}
				if(args.length == 1) {
					String servidor = args[0];
					if(QueueManager.getFilas().containsKey(servidor)) {
						if(QueueManager.getFilas().get(servidor).isInQueue(p.getName())) {
							p.sendMessage("§aVocê saiu da fila do servidor "+QueueManager.getFilas().get(servidor).getLobbyServer().getServerName()+".");
							QueueManager.getFilas().get(servidor).removeFila(p.getName());
						}else if(QueueManager.getFilas().get(servidor).isInVIPQueue(p.getName())){
							p.sendMessage("§aVocê saiu da fila do servidor "+QueueManager.getFilas().get(servidor).getLobbyServer().getServerName()+".");
							QueueManager.getFilas().get(servidor).removeVIPFila(p.getName());
						}else {
							p.sendMessage("§cVocê não está na fila deste servidor.");
							return true;
						}
					}else {
						p.sendMessage("§cEste servidor não existe.");
						return true;
					}
				}
			}else {
				Bukkit.getConsoleSender().sendMessage("§cEste comando apenas pode ser executado in-game.");
				return true;
			}
		}
		return false;
	}

}
