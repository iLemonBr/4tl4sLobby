package com.atlasplugins.atlaslobby.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQLResult {

	public void process(ResultSet result) throws SQLException;

}
