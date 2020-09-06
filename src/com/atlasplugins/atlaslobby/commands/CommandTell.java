package com.atlasplugins.atlaslobby.commands;


import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.UltimateFancy;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class CommandTell implements CommandExecutor {
	
	public static HashMap<String,String> recentes = new HashMap<String,String>();
	public static HashMap<String,Long> timestamp = new HashMap<String,Long>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		// TODO Auto-generated method stub
		if(cmd.getName().equalsIgnoreCase("tell")){
			if(sender instanceof Player){
				Player p = (Player)sender;
				if(args.length < 2){
					p.sendMessage("§cUtilize: /tell <jogador> <mensagem>.");
					return true;
				}
				if(args.length >= 2){
					String message = "";
			        String[] arrayOfString;
					int j = (arrayOfString = args).length;
			        for (int i = 1; i < j; i++)
			        {
			          String part = arrayOfString[i];
			          if (message != "") {
			            message = "§6"+message + " §6";
			          }
			          message = "§6"+message + part;
			        }
					 if(message.contains("\"")){
						 p.sendMessage("§cO caractere '\"' não pode ser utilizado.");
						 return true;
					 }
			        String jogador = args[0];
			        if(Bukkit.getPlayer(jogador) != null){
				        Player target = Bukkit.getPlayer(jogador);
			        	if(jogador.equalsIgnoreCase(p.getName())){
			        		p.sendMessage("§cVocê não pode enviar uma mensagem privada para si mesmo.");
			        	}else{
			        		if(!Main.getStorager().getCache().containsKey(target.getName().toLowerCase())) {
			        			p.sendMessage("§cVocê não pode enviar uma mensagem para este jogador pois ele ainda não se autenticou.");
			        			return true;
			        		}else if(!Main.getStorager().getCache().get(target.getName().toLowerCase()).getPreferences().isTell()) {
			        			p.sendMessage("§cEste jogador desabilitou o recebimento de mensagens privadas no lobby.");
			        			return true;
			        		}
			        		if(Main.getStorager().getCache().containsKey(p.getName().toLowerCase()) && !Main.getStorager().getCache().get(p.getName().toLowerCase()).getPreferences().isTell()) {
			        			p.sendMessage("§cVocê não pode enviar mensagens privadas pois você está com suas preferências de mensagens privadas desabilitadas.");
			        			return true;
			        		}
			        		if(!p.hasPermission("atlasplugins.tell.bypass")){
                               if(timestamp.containsKey(p.getName())){
			        		int secondstowait = 5;
			        		long secondsLeft = ((timestamp.get(p.getName())/1000)+secondstowait) - (System.currentTimeMillis()/1000);
			        		if(secondsLeft>0) {
			        			p.sendMessage("§cVocê precisa esperar "+secondsLeft+"§c segundos para enviar uma nova mensagem.");
			        			return true;
			        		}else{
			        			timestamp.remove(p.getName());
			        		}
                               }else{
                                  	timestamp.put(p.getName(), System.currentTimeMillis());
                               }
			        		}
			        		//ENVIA TELL
			        		if(recentes.containsKey(p.getName())){
			        			recentes.remove(p.getName());
			        			recentes.put(p.getName(), jogador);
			        		}else{
			        			recentes.put(p.getName(), jogador);
			        		}
			        		if(recentes.containsKey(jogador)){
			        			recentes.remove(jogador);
			        			recentes.put(jogador, p.getName());
			        		}else{
			        			recentes.put(jogador, p.getName());
			        		}
			        		//ENVIA TELL
			        		if(PermissionsEx.getUser(target.getName()).getPrefix().contains("[")){
			        			UltimateFancy fancy = new UltimateFancy();
			        			fancy.text("§8Mensagem para §7"+PermissionsEx.getUser(target.getName()).getPrefix().replace("&", "§")+" "+target.getName()+"§8: §6")
			        			.hoverShowText("§6Clique para responder o "+target.getName()).clickSuggestCmd("/tell "+target.getName()+" ").next();
			        			fancy.text("§6"+message).next();
			        			fancy.send(p);
			        		}else{
			        			UltimateFancy fancy = new UltimateFancy();
			        			fancy.text("§8Mensagem para §7"+target.getName()+"§8: §6").clickSuggestCmd("/tell "+target.getName()+" ").hoverShowText("§6Clique para responder o "+target.getName()).next();
			        			fancy.text("§6"+message);
			        			fancy.next();
			        			fancy.send(p);
			        		}
			        		if(PermissionsEx.getUser(p.getName()).getPrefix().contains("[")){
			        			UltimateFancy fancy = new UltimateFancy();
			        			fancy.text("§8Mensagem de §7"+PermissionsEx.getUser(p.getName()).getPrefix().replace("&", "§")+" "+p.getName()+"§8: §6");
			        			fancy.hoverShowText("§6Clique para responder o "+p.getName());
			        			fancy.clickSuggestCmd("/tell "+p.getName()+" ");
			        			fancy.next();
			        			fancy.text("§6"+message);
			        			fancy.send(target);
			        		}else{
			        			UltimateFancy fancy = new UltimateFancy();
			        			fancy.text("§8Mensagem de §7"+p.getName()+"§8: §6");
			        			fancy.hoverShowText("§6Clique para responder o "+p.getName());
			        			fancy.clickSuggestCmd("/tell "+p.getName()+" ");
			        			fancy.next();
			        			fancy.text("§6"+message);
			        			fancy.send(target);
			        		}
			        	}
			        }else{
			        	p.sendMessage("§cEste usuário não está online.");
			        }
				}
			}
		}
		return false;
	}

}
