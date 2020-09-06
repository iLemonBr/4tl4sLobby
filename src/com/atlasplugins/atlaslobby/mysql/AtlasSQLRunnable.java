package com.atlasplugins.atlaslobby.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AtlasSQLRunnable implements Runnable{

	private Connection connection;
	private AtlasSQL sql;
	private SQLResult action;

	public AtlasSQLRunnable(Connection connection, AtlasSQL sql, SQLResult action) {
		this.connection = connection;
		this.sql = sql;
		this.action = action;
	}

	public Connection getConnection() {
		return connection;
	}

	public AtlasSQL getSQL() {
		return sql;
	}

	public void run() {
		try (PreparedStatement statement = connection.prepareStatement(sql.getSQL())) {
			sql.applyObjects(statement);

			try (ResultSet result = statement.executeQuery()) {
				action.process(result);
			}
		} catch (SQLException ex) {
			throw new AtlasSQLException("Erro ao executar o SQL (" + sql.getSQL() + ")", ex);
		}
	}

}
