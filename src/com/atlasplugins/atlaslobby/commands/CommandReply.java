package com.atlasplugins.atlaslobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandReply implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		// TODO Auto-generated method stub
		if(cmd.getName().equalsIgnoreCase("r")){
			if(sender instanceof Player){
				Player p = (Player)sender;
				if(args.length < 1){
					p.sendMessage("§cUtilize: /r <mensagem>.");
					return true;
				}
				if(args.length >=1){
					if(CommandTell.recentes.containsKey(p.getName())){
						String quem = CommandTell.recentes.get(p.getName());
					    String message = "";
				        String[] arrayOfString;
						int j = (arrayOfString = args).length;
				        for (int i = 0; i < j; i++)
				        {
				          String part = arrayOfString[i];
				          if (message != "") {
				            message = message + " ";
				          }
				          message = message + part;
				        }
						 if(message.contains("\"")){
							 p.sendMessage("§cO caractere '\"' não pode ser utilizado.");
							 return true;
						 }
				        p.chat("/tell "+quem+" "+message);
					}else{
						p.sendMessage("§cVocê não tem ninguém para responder.");
						return true;
					}
				}
			}
		}
		return false;
	}

}
