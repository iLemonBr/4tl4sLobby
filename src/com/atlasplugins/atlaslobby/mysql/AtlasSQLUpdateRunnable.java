package com.atlasplugins.atlaslobby.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AtlasSQLUpdateRunnable implements Runnable{

	private Connection connection;
	private AtlasSQL sql;

	public AtlasSQLUpdateRunnable(Connection connection, AtlasSQL sql) {
		this.connection = connection;
		this.sql = sql;
	}

	public Connection getConnection() {
		return connection;
	}

	public void run() {
		try (PreparedStatement statement = connection.prepareStatement(sql.getSQL())) {
			sql.applyObjects(statement);
			
			statement.executeUpdate();
		} catch (SQLException ex) {
			throw new AtlasSQLException("[AtlasSQL] Erro ao executar a query: "+sql.getSQL());
		}
	}

}
