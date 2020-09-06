package com.atlasplugins.atlaslobby.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.cores.NPCCore;
import com.atlasplugins.atlaslobby.object.LobbyNPC;

public class CommandDefinirNPC implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String paramString,String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("definirnpc")) {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				if(p.hasPermission("atlasplugins.definirnpc")) {
					if(args.length < 1 || args.length > 1) {
						p.sendMessage("§cUtilize: /definirnpc <npc>.");
						return true;
					}
					if(args.length == 1) {
						if(NPCCore.getNPCS().containsKey(args[0])) {
							LobbyNPC lobbyNPC = NPCCore.getNPCS().get(args[0]);
							lobbyNPC.setLocation(p.getLocation());
							lobbyNPC.saveLocation();
							lobbyNPC.spawn();
							p.sendMessage("§aNPC §e"+lobbyNPC.getNpcKey()+"§a spawnado com sucesso.");
							return true;
						}else {
							p.sendMessage("§cEste NPC não existe.");
							return true;
						}
					}
				}else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Mensagens.SemPermissao")));
				}
			}
		}
		return false;
	}

}
