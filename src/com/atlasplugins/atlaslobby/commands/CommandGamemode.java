package com.atlasplugins.atlaslobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.atlasplugins.atlaslobby.cores.LobbyCore;

public class CommandGamemode implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		// TODO Auto-generated method stub
		if(cmd.getName().equalsIgnoreCase("gamemode")){
			if(sender instanceof Player){
				Player p = (Player)sender;
				if(p.hasPermission("atlasplugins.gamemode")){
					if(args.length < 1){
						p.sendMessage("§cUtilize: /gamemode [0/1/2/3]");
						return true;
					}
					if(args.length == 2){
						String name = args[1];
						Player target = Bukkit.getPlayer(name);
						if(target != null){
						if(args[0].equalsIgnoreCase("0")){
							if(p.hasPermission("atlasplugins.gamemode.0")) {
							target.setGameMode(GameMode.SURVIVAL);
							p.getInventory().clear();
							LobbyCore.giveLobbyItems(target);
							p.sendMessage("§aO Modo de jogo do jogador "+target.getName()+"§a foi alterado para Survival.");
							target.sendMessage("§aSeu modo de jogo foi alterado por "+p.getName());
							}else {
								p.sendMessage("§cVocê não tem permissão para setar este modo de jogo para este jogador.");
								return true;
							}
						}else if(args[0].equalsIgnoreCase("1")){
							if(p.hasPermission("atlasplugins.gamemode.1")) {
							target.setGameMode(GameMode.CREATIVE);
							p.getInventory().clear();
							p.sendMessage("§aO Modo de jogo do jogador "+target.getName()+"§a foi alterado para Criativo.");
							target.sendMessage("§aSeu modo de jogo foi alterado por "+p.getName());
							}else {
								p.sendMessage("§cVocê não tem permissão para setar este modo de jogo para este jogador.");
								return true;
							}
						}else if(args[0].equalsIgnoreCase("3")){
							if(p.hasPermission("atlasplugins.gamemode.3")) {
							target.setGameMode(GameMode.SPECTATOR);
							p.getInventory().clear();
							LobbyCore.giveLobbyItems(target);
							p.sendMessage("§aO Modo de jogo do jogador "+target.getName()+"§a foi alterado para Espectador.");
							target.sendMessage("§aSeu modo de jogo foi alterado por "+p.getName());
							}else {
								p.sendMessage("§cVocê não tem permissão para setar este modo de jogo para este jogador.");
								return true;
							}
						}else if(args[0].equalsIgnoreCase("2")){
							if(p.hasPermission("atlasplugins.gamemode.2")) {
							target.setGameMode(GameMode.ADVENTURE);
							p.getInventory().clear();
							LobbyCore.giveLobbyItems(target);
							p.sendMessage("§aO Modo de jogo do jogador "+target.getName()+"§a foi alterado para Modo Aventura.");
							target.sendMessage("§aSeu modo de jogo foi alterado por "+p.getName());
							}else {
								p.sendMessage("§cVocê não tem permissão para setar este modo de jogo para este jogador.");
								return true;
							}
						}
						}else{
							p.sendMessage("§cEste jogador esta offline.");
						}
					}
					if(args.length == 1){
						if(args[0].equalsIgnoreCase("0")){
							if(p.hasPermission("atlasplugins.gamemode.0")) {
							p.setGameMode(GameMode.SURVIVAL);
							p.getInventory().clear();
							LobbyCore.giveLobbyItems(p);
							p.sendMessage("§aSeu modo de jogo foi alterado para Survival.");
					        return true;
							}else {
								p.sendMessage("§cVocê não tem permissão para entrar neste modo de jogo.");
							}
						}else
						if(args[0].equalsIgnoreCase("1")){
							if(p.hasPermission("atlasplugins.gamemode.1")) {
							p.setGameMode(GameMode.CREATIVE);
							p.getInventory().clear();
							p.sendMessage("§aSeu modo de jogo foi alterado para Criativo.");
							return true;
							}else {
								p.sendMessage("§cVocê não tem permissão para entrar neste modo de jogo.");
							}
						}else if(args[0].equalsIgnoreCase("3")){
							if(p.hasPermission("atlasplugins.gamemode.3")) {
							p.setGameMode(GameMode.SPECTATOR);
							p.getInventory().clear();
							LobbyCore.giveLobbyItems(p);
							p.sendMessage("§aSeu modo de jogo foi alterado para Espectador.");
							return true;
							}else {
								p.sendMessage("§cVocê não tem permissão para entrar neste modo de jogo.");
							}
							
						}else if(args[0].equalsIgnoreCase("2")){
							if(p.hasPermission("atlasplugins.gamemode.3")) {
							p.setGameMode(GameMode.ADVENTURE);
							p.getInventory().clear();
							LobbyCore.giveLobbyItems(p);
							p.sendMessage("§aSeu modo de jogo foi alterado para Modo Aventura.");
							return true;
							}else {
								p.sendMessage("§cVocê não tem permissão para entrar neste modo de jogo.");
							}
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
