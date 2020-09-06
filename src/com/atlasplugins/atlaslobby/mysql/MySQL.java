package com.atlasplugins.atlaslobby.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.atlasplugins.atlaslobby.apis.JsonItem;

public class MySQL implements AtlasSQLConnection{

	public static final ConsoleCommandSender console = Bukkit.getConsoleSender();
	
	private String host;
	private String user;
	private String password;
	private String database;
	private ArrayList<String> tablesNames = new ArrayList<String>();
	private int port;

	private Connection connection;
	private JavaPlugin plugin;
	
	public MySQL(JavaPlugin plugin, String host, String user, String password, String database, int port) {
		super();
		this.plugin = plugin;
		this.host = host;
		this.user = user;
		this.password = password;
		this.database = database;
		this.port = port;
		new JsonItem();
	}
	
	@Override
	public Connection getConnection() {
		return connection;
	}

	public void runUpdate(AtlasSQLUpdateRunnable sql, boolean asynchronously) {
		if (asynchronously) {
			new BukkitRunnable() {
				
				@Override
				public void run() {
					sql.run();
				}
			}.runTaskAsynchronously(JavaPlugin.getPlugin(plugin.getClass()));
		}else {
			sql.run();
		}
	}
	
	public void runSQL(AtlasSQLRunnable sql, boolean asynchronously) {
		if (asynchronously) {
            new BukkitRunnable() {
				
				@Override
				public void run() {
					sql.run();
				}
			}.runTaskAsynchronously(JavaPlugin.getPlugin(plugin.getClass()));
		} else {
			sql.run();
		}
	}
	
	public void addTableStatement(String tableQuery) {
		this.tablesNames.add(tableQuery);
	}
	
	public void addTableUser(String tableName) {
		this.tablesNames.add("CREATE TABLE IF NOT EXISTS `"+tableName+"` ( `id` INT NOT NULL AUTO_INCREMENT , `jogador` VARCHAR(50) NOT NULL , `json` TEXT NOT NULL , PRIMARY KEY (`id`));");
	}
	
	@Override
	public void openConnection() {
		if (connection != null) {
			throw new AtlasSQLException("[AtlasMySQL] Não foi possível inicializar o MySQL pois esta conexão atual já está aberta.");
		}
		try {
			this.connection = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database+"?autoReconnect=true", user, password);
			Bukkit.getConsoleSender().sendMessage("§a[AtlasMySQL] A conexão com o MySQL foi efetuada com sucesso, criando tabelas...");
			createTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void closeConnection() {
		if(connection == null) {
			throw new AtlasSQLException("[AtlasMySQL] A conexão remota com o servidor MySQL já foi encerrada.");
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createTable() {
		if(connection!=null) {
			if(this.tablesNames.size() > 0) {
				for(String tableStatement : this.tablesNames) {
					try(Statement stmt = connection.createStatement()){
						stmt.executeUpdate(tableStatement);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}else {
				throw new AtlasSQLException("[AtlasMySQL] Nenhuma tabela foi adicionada para ser carregada.");
			}
		}else {
			throw new AtlasSQLException("[AtlasMySQL] A conexão com o servidor MySQL ainda não foi aberta por este motivo a tabela não foi criada.");
		}
	}

}
