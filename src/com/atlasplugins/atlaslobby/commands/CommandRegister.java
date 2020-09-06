package com.atlasplugins.atlaslobby.commands;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.atlasplugins.atlaslobby.Main;
import com.atlasplugins.atlaslobby.apis.DateAPI;
import com.atlasplugins.atlaslobby.apis.MD5Hash;
import com.atlasplugins.atlaslobby.apis.Title;
import com.atlasplugins.atlaslobby.cores.LobbyCore;
import com.atlasplugins.atlaslobby.events.AtlasPlayerRegisterEvent;
import com.atlasplugins.atlaslobby.mysql.AtlasSQL;
import com.atlasplugins.atlaslobby.mysql.AtlasSQLUpdateRunnable;
import com.atlasplugins.atlaslobby.mysql.AtlasStorage;
import com.atlasplugins.atlaslobby.mysql.MySQL;
import com.atlasplugins.atlaslobby.object.AtlasPlayer;

public class CommandRegister implements CommandExecutor {

	public AtlasStorage storager = Main.getStorager();
	public MySQL mySQLConnection = Main.getMySQLConnection();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (cmd.getName().equalsIgnoreCase("register")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (Main.getStorager().getCache().containsKey(p.getName().toLowerCase())) {
					p.sendMessage("§cVocê já está registrado no servidor.");
					p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
					return true;
				}
				if (args.length < 2 || args.length > 2) {
					p.sendMessage("§cUtilize: /register <senha> <repetir a senha>.");
					p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
					return true;
				}
				if (args.length == 2) {
					if (args[0].matches(args[1])) {
						if(!LobbyCore.getCaptchaSolveds().contains(p.getName())) {
							p.sendMessage("§cVocê precisa verificar-se no captcha primeiro.");
							return true;
						}
						String password = args[0];
						AtlasPlayer atlasPlayer = new AtlasPlayer(p.getName(), MD5Hash.MD5(password), "Nenhum", DateAPI.getCurrentDate());
						mySQLConnection.runUpdate(new AtlasSQLUpdateRunnable(mySQLConnection.getConnection(), new AtlasSQL("INSERT INTO `lobbyusers` (`jogador`, `json`) VALUES (?,?)", p.getName().toLowerCase(),
								storager.encode(atlasPlayer))), true);
						storager.getCache().put(p.getName().toLowerCase(), atlasPlayer);
						AtlasPlayer a = Main.getStorager().getCache().get(p.getName().toLowerCase());
						a.setLogged(true);
						AtlasPlayerRegisterEvent cEvent = new AtlasPlayerRegisterEvent(p, atlasPlayer);
					    Bukkit.getPluginManager().callEvent(cEvent);
					    LobbyCore.giveLobbyItems(p);
					    Title t = new Title(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Titulos.Autenticado.Titulo")), ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Titulos.Autenticado.SubTitulo")), 0, 0, 60);
						t.send(p);
						p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
						if(Main.getInstance().getConfig().getBoolean("Dica.Ativar")) {
							new BukkitRunnable() {

								@Override
								public void run() {
									for(String s : Main.getInstance().getConfig().getStringList("Dica.Mensagem")) {
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
									}
									p.playSound(p.getLocation(), Sound.valueOf(Main.getInstance().getConfig().getString("Dica.Som")), 2, 2);
								}
							}.runTaskLater(Main.getPlugin(Main.class), 20L * 2);
						}
					} else {
						p.sendMessage("§cAs senhas não se coincidem.");
						p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
						return true;
					}
				}

			} else if (sender instanceof ConsoleCommandSender) {
				ConsoleCommandSender p = Bukkit.getConsoleSender();
				if (args.length < 3 || args.length > 3) {
					p.sendMessage("§cUtilize: /register <jogador> <senha> <repetir a senha>.");
					return true;
				}
				if (args.length == 3) {
					String jogador = args[0];
					if (!Main.getStorager().containsUser(jogador)) {
						if (args[1].matches(args[2])) {
							String password = args[1];
							AtlasPlayer atlasPlayer = new AtlasPlayer(jogador, password, "Nenhum", DateAPI.getCurrentDate());
							Main.getStorager().getCache().put(jogador.toLowerCase(), atlasPlayer);
							mySQLConnection.runUpdate(new AtlasSQLUpdateRunnable(mySQLConnection.getConnection(), 
									 new AtlasSQL("INSERT INTO `lobbyusers` (`jogador`, `json`) VALUES (?,?)", jogador.toLowerCase(),storager.encode(atlasPlayer))), true);
							AtlasPlayer a = Main.getStorager().getCache().get(p.getName().toLowerCase());
							a.setLogged(true);
							p.sendMessage("§aJogador cadastrado com sucesso.");
						} else {
							p.sendMessage("§cAs senhas não se coincidem.");
							return true;
						}
					} else {
						p.sendMessage("§cDesculpe, este jogador já está cadastrado.");
						return true;
					}
				}
			}
		}
		return false;
	}

}
