package lib.clearclass.utils;

import java.sql.*;

import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import lib.clearclass.DatabaseType;

class PgConnectionFactory {
	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Драйвер не найден", e);
		}
	}
	private static String url = "jdbc:postgresql://localhost:5432/db1";
	private static String login = "postgres";
	private static String password = "post";
	public static Connection getConnection() throws SQLException{
		return DriverManager.getConnection(url, login, password);
	}
}

class H2ConnectionFactory {
	static String url1 = "jdbc:h2:./dbfile";
	static String url2 = "jdbc:h2:mem:";
	static String login = "user";
	static String password = "pass";
	static Connection getConnection(DatabaseType db) throws SQLException {
		if(db==DatabaseType.H2_file) 
			return DriverManager.getConnection(url1, login, password);
		if(db==DatabaseType.H2_in_mem) 
			return DriverManager.getConnection(url2, login, password);
		throw new RuntimeException("Unsupported DatabaseType argument: " + db 
				+ "; only 'DatabaseType.H2_file' or 'DatabaseType.H2_in_mem' is allowed");
	}
}

class H2SingleConnectionFactory extends H2ConnectionFactory {
	static SingleConnectionDataSource ds1 = new SingleConnectionDataSource();
	static SingleConnectionDataSource ds2 = new SingleConnectionDataSource();
	static {
		ds1.setUrl(url1);
		ds1.setUsername(login);
		ds1.setPassword(password);
		ds1.setSuppressClose(true);
		ds2.setUrl(url2);
		ds2.setUsername(login);
		ds2.setPassword(password);
		ds2.setSuppressClose(true);
	}
	static Connection getConnection(DatabaseType db) throws SQLException {
		if(db==DatabaseType.H2_file) 
			return ds1.getConnection();
		if(db==DatabaseType.H2_in_mem) 
			return ds2.getConnection();
		throw new RuntimeException("Unsupported DatabaseType argument: " + db + "; "
				+ "only 'DatabaseType.H2_file' or 'DatabaseType.H2_in_mem' is allowed");
	}
}

public class ConnectionFactory {
	public static Connection getConnection(DatabaseType db) throws SQLException {
		if(db==DatabaseType.Postgres) 
			return PgConnectionFactory.getConnection();
		if (db==DatabaseType.H2_file || db==DatabaseType.H2_in_mem)
			return H2ConnectionFactory.getConnection(db);
		throw new RuntimeException("Unsupported DatabaseType argument: " + db);
	}
}