package com.atlasplugins.atlaslobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.command.ConsoleCommandCompleter;
import org.bukkit.entity.Player;

public class CommandTP implements CommandExecutor{

	public boolean isInteger(String a) {
		try {
			Integer.parseInt(a);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(cmd.getName().equalsIgnoreCase("tp")) {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				if(p.hasPermission("atlasplugins.command.tp")) {
					if(args.length < 1) {
						p.sendMessage("§cUtilize: /tp <jogador> [jogador].");
					}
					if(args.length == 1) {
						String player = args[0];
						if(Bukkit.getPlayer(player)!=null) {
							p.teleport(Bukkit.getPlayer(player));
							p.sendMessage("§aVocê foi teleportado até o jogador "+player+".");
							return true;
						}else {
							p.sendMessage("§cEste jogador está offline ou não existe.");
							return true;
						}
					}
					if(args.length == 4) {
						if(isInteger(args[1])) {
							if(isInteger(args[2])) {
								if(isInteger(args[3])) {
									String user = args[0];
									if(Bukkit.getPlayer(user)!=null) {
										int x = Integer.parseInt(args[1]);
										int y = Integer.parseInt(args[2]);
										int z = Integer.parseInt(args[3]);
										Player target = Bukkit.getPlayer(user);
										target.teleport(new Location(target.getWorld(), x, y, z));
										p.sendMessage("§aVocê teletransportou o jogador "+target.getName()+" para "+x+" "+y+" "+z);
									}else {
										p.sendMessage("§cEste jogador está offline.");
									}
								}else {
									p.sendMessage("§cUtilize: /tp [jogador] <x> <y> <z> para se teletransportar até uma cordenada.");
								}
							}else {
								p.sendMessage("§cUtilize: /tp [jogador] <x> <y> <z> para se teletransportar até uma cordenada.");
							}
						}else {
							p.sendMessage("§cUtilize: /tp [jogador] <x> <y> <z> para se teletransportar até uma cordenada.");
						}
					}
					if(args.length == 3) {
						if(isInteger(args[0])) {
							if(isInteger(args[1])) {
								if(isInteger(args[2])) {
									int x = Integer.parseInt(args[0]);
									int y = Integer.parseInt(args[1]);
									int z = Integer.parseInt(args[2]);
									p.teleport(new Location(p.getWorld(), x, y, z));
									p.sendMessage("§aTeletransportado até "+x+" "+y+" "+z);
								}
							}
						}else {
							p.sendMessage("§cUtilize: /tp [jogador] <x> <y> <z> para se teletransportar até uma cordenada.");
						}
					}
					if(args.length == 2) {
						String player = args[0];
						String toPlayer = args[1];
						if(Bukkit.getPlayer(player)!=null) {
							if(Bukkit.getPlayer(toPlayer)!=null) {
								Player a = Bukkit.getPlayer(player);
								Player b = Bukkit.getPlayer(toPlayer);
								b.teleport(a);
								p.sendMessage("§aO jogador "+b.getName()+"§a foi teletransportado até o jogador "+a.getName()+".");
								return true;
							}else {
								p.sendMessage("§cO jogador "+args[1]+"§c está offline.");
								return true;
							}
						}else {
							p.sendMessage("§cO jogador "+args[0]+"§c está offline.");
							return true;
						}
					}
				}else {
					p.sendMessage("§cVocê não tem permissão para executar este comando.");
					return true;
				}
			}else {
				ConsoleCommandSender p = Bukkit.getConsoleSender();
				if(args.length < 2) {
					p.sendMessage("§cUtilize: /tp <jogador> <jogador>.");
				}
				if(args.length == 2) {
					String player = args[0];
					String toPlayer = args[1];
					if(Bukkit.getPlayer(player)!=null) {
						if(Bukkit.getPlayer(toPlayer)!=null) {
							Player a = Bukkit.getPlayer(player);
							Player b = Bukkit.getPlayer(toPlayer);
							b.teleport(a);
							p.sendMessage("§aO jogador "+b.getName()+"§a foi teletransportado até o jogador "+a.getName()+".");
							return true;
						}else {
							p.sendMessage("§cO jogador "+args[1]+"§c está offline.");
							return true;
						}
					}else {
						p.sendMessage("§cO jogador "+args[0]+"§c está offline.");
						return true;
					}
				}
			}
		}
		return false;
	}

}
