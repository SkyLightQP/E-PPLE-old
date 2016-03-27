package me.efe.efeserver.mysql;

import java.sql.Connection;
import java.sql.SQLException;

public abstract interface DataInterface {
	public abstract String driver();

	public abstract void connect() throws ClassNotFoundException, SQLException,
			SQLException;

	public abstract void disconnect() throws SQLException;

	public abstract Connection getConnection();
}