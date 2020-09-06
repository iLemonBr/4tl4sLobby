package com.atlasplugins.atlaslobby.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandFly implements CommandExecutor{

	public static ArrayList<String> lista = new ArrayList<String>();
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		// TODO Auto-generated method stub
		if(cmd.getName().equalsIgnoreCase("fly")){
			if(sender instanceof Player){
				Player p = (Player)sender;
				if(p.hasPermission("atlasplugins.staff.admin")){
					
					if(args.length == 1){
						String name = args[0];
						Player target = Bukkit.getPlayer(name);
						if(target != null){
							if(!lista.contains(p.getName())){
							target.setFlying(true);
							target.sendMessage("§aSeu modo de vôo foi alterador por "+p.getName());
							p.sendMessage("§aO modo de vôo do jogador "+target.getName()+"§a foi alterado.");
							lista.add(target.getName());
							}else{
								target.setFlying(false);
								target.sendMessage("§aSeu modo de vôo foi alterador por "+p.getName());
								p.sendMessage("§aO modo de vôo do jogador "+target.getName()+"§a foi alterado.");
								lista.remove(target.getName());
							}
						}
					}else{
						if(!lista.contains(p.getName())){
							p.setAllowFlight(true);
							p.setFlying(true);
						p.sendMessage("§aSeu modo de vôo foi alterado com sucesso.");
						lista.add(p.getName());
						}else{
							p.setAllowFlight(false);
							p.setFlying(false);
							p.sendMessage("§aSeu modo de vôo foi alterado com sucesso.");
							lista.remove(p.getName());
						}
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
