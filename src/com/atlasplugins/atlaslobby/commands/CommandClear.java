package com.atlasplugins.atlaslobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;


public class CommandClear implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(cmd.getName().equalsIgnoreCase("clear")){
			if(sender instanceof Player){
				Player p = (Player)sender;
				if(p.hasPermission("atlasplugins.staff.admin")){
					if(args.length <1){
						p.getInventory().clear();
						p.sendMessage("§aTodos os itens de seu inventário foram removidos com sucesso.");
					}
					if(args.length == 1){
						String name = args[0];
						if(p.getName().equalsIgnoreCase(name)){
							p.sendMessage("§cUtilize apenas /clear para limpar seu próprio inventário.");
						}else{
						Player target = Bukkit.getPlayer(name);
						if(target != null){
							target.getInventory().clear();
							target.sendMessage("§aSeu inventário foi limpo por um membro da equipe.");
							p.sendMessage("§aTodos os itens do inventário do jogador "+target.getName()+"§a foram removidos com sucesso.");
							
						}else{
							p.sendMessage("§cO jogador "+args[0]+"§c esta offline.");
						}
						}
					}
				}else{
					p.sendMessage("§cVocê não tem permissão para executar este comando.");
					return true;
				}
			}else if(sender instanceof ConsoleCommandSender) {
				ConsoleCommandSender p = Bukkit.getConsoleSender();
				if(args.length < 1){
					p.sendMessage("Utilize: /clear <jogador>.");
					return true;
				}
				if(args.length == 1){
					String name = args[0];
					Player target = Bukkit.getPlayer(name);
					if(target != null){
						target.getInventory().clear();
						target.sendMessage("§aSeu inventário foi limpo por um membro da equipe.");
						p.sendMessage("§aTodos os itens do inventário do jogador "+target.getName()+"§a foram removidos com sucesso.");
						
					}else{
						p.sendMessage("§cO jogador "+args[0]+"§c esta offline.");
					}
				}
			}
		}
		// TODO Auto-generated method stub
		return false;
	}
	

}
