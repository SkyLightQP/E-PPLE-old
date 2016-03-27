package me.efe.efeserver.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlLite implements DataInterface {
	Connection connection;
	private String filePath;

	public SqlLite(String filePath) {
		this.filePath = filePath;
	}

	public String driver() {
		return "org.sqlite.JDBC";
	}

	public void connect() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		this.connection = DriverManager.getConnection("jdbc:sqlite:"
				+ this.filePath);
	}

	public void disconnect() throws SQLException {
		this.connection.close();
	}

	public Connection getConnection() {
		return this.connection;
	}
}