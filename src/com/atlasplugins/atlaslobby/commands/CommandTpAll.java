package com.atlasplugins.atlaslobby.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTpAll implements CommandExecutor{

	public static ArrayList<String> lista = new ArrayList<String>();
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		// TODO Auto-generated method stub
		if(cmd.getName().equalsIgnoreCase("tpall")){
			
			if(sender instanceof Player)
			{
				Player p = (Player)sender;
				if(p.hasPermission("atlasplugins.tpall")){
					if(lista.contains(p.getName())){
					for(Player p3 : Bukkit.getOnlinePlayers()){
						p3.teleport(p.getLocation());
					}
					lista.remove(p.getName());
					p.sendMessage("§aTodos os jogadores conectados no servidor foram teleportados para sua localização.");
					}else{
						p.sendMessage("§eVocê tem certeza que deseja teleportar todos os jogadores conectados no servidor para sua localização?");
						p.sendMessage("§eCaso tenha certeza digite '/tpall' novamente.");
						lista.add(p.getName());
					}
				}else{
					p.sendMessage("§cVocê não tem permissão para executar este comando.");
					return true;
				}
			}
		}
		return false;
	}
}
