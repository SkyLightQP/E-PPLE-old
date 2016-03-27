package me.efe.efeserver.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL implements DataInterface {
	Connection connection;
	private String host;
	private String port;
	private String username;
	private String password;
	private String database;

	public MySQL(String host, String port, String username, String password,
			String database) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.database = database;
	}

	public String driver() {
		return "com.mysql.jdbc.Driver";
	}

	public void connect() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		String dbPath = String.format(
				"jdbc:mysql://%s:%s/%s?user=%s&password=%s&characterEncoding=utf-8&"
						+ "useUnicode=true", this.host, this.port,
				this.database, this.username, this.password);
		this.connection = DriverManager.getConnection(dbPath);
	}

	public void disconnect() throws SQLException {
		this.connection.close();
	}

	public Connection getConnection() {
		return this.connection;
	}
}