package lib.clearclass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class Insert {
	private static int N = 1000; // количество строк для вставки

	/** Вставка строк в режиме автофиксации команд (с использованием Statement) */
	public static double m0(Connection conn) throws SQLException {
		conn.setAutoCommit(true);
		long t1 = System.nanoTime();
		try(Statement stm = conn.createStatement()) {
			for (int i = 0; i < N; i++)
				stm.executeUpdate("INSERT INTO words(word) VALUES ('погадка" + i + "');");
			long t2 = System.nanoTime();
			double dt=(t2-t1); dt/=1e6;
			return dt;
		}
	}
	
	/** Вставка строк внутри одной транзакции (с использованием Statement) */
	public static double m1(Connection conn) throws SQLException {
		conn.setAutoCommit(false);
		long t1 = System.nanoTime();
		try(Statement stm = conn.createStatement()) {
			for (int i = 0; i < N; i++)
				stm.executeUpdate("INSERT INTO words(word) VALUES ('погадка" + i + "');");
			conn.commit();
			long t2 = System.nanoTime();
			double dt = (t2-t1); dt/=1e6;
			return dt;
		}
	}
	
	/** Вставка строк внутри одной транзакции (с использованием PreparedStatement) */
	public static double m2(Connection conn) throws SQLException {
		conn.setAutoCommit(false);
		long t1 = System.nanoTime();
		try(PreparedStatement pstm = conn.prepareStatement("INSERT INTO words(word) VALUES(?);")){
			for (int i = 0; i < N; i++) {
				pstm.setString(1, "погадка"+i);
				pstm.executeUpdate();
			}
			conn.commit();
			long t2 = System.nanoTime();
			double dt = (t2-t1); dt/=1e6;
			return dt;
		}
	}
	
	/** Вставка строк внутри одной транзакции (с использованием PreparedStatement и batch-режима) */
	public static double m3(Connection conn) throws SQLException {
		conn.setAutoCommit(false);
		long t1 = System.nanoTime();
		try(PreparedStatement pstm = conn.prepareStatement("INSERT INTO words(word) VALUES(?);")){
			for (int i = 0; i < N; i++) {
				pstm.setString(1, "погадка"+i);
				pstm.addBatch();
			}
			pstm.executeBatch();
			conn.commit();
			long t2 = System.nanoTime();
			double dt = (t2-t1); dt/=1e6;
			return dt;
		}
	}
	
	/** Вставка строк c использованием Hibernate */
	public static double m4(Connection conn) {
		String connName = conn.getClass().getName();
		String dialect;
		if(connName.equals("org.postgresql.jdbc.PgConnection")) 
			dialect = "PostgreSQL93Dialect";
		else if(connName.equals("org.h2.jdbc.JdbcConnection")) 
			dialect = "H2Dialect";
		else 
			throw new RuntimeException("Unknown dialect for connection : " + connName);
		
		SessionFactory sf = new Configuration()
			.addAnnotatedClass(lib.clearclass.Word.class)
			.setProperty("hibernate.dialect", "org.hibernate.dialect." + dialect)
			.buildSessionFactory();
				
		StatelessSession session = sf.openStatelessSession(conn);
		
		Word[] words = new Word[N];
		for (int i = 0; i < N; i++)
			words[i] = new Word("погадка" + i);
		
		long t1 = System.nanoTime();
		session.beginTransaction();
		for (int i = 0; i < N; i++)
			session.insert(words[i]);
		session.getTransaction().commit();
		long t2 = System.nanoTime();
		session.close();
		sf.close();
		double dt = (t2-t1); dt/=1e6;
		return dt;
	}
	
	/** Вставка строк c использованием JdbcTemplate */
	public static double m5(Connection conn) throws SQLException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(conn, true));
		String query = "INSERT INTO words(word) VALUES(?);";
		List<Object[]> queryData = new ArrayList<>();
		for (int i = 0; i < N; i++)
			queryData.add(new String[]{"погадка" + i});
		long t1 = System.nanoTime();
		jdbcTemplate.batchUpdate(query, queryData);
		long t2 = System.nanoTime();
		double dt = (t2-t1); dt/=1e6;
		return dt;
	}
}