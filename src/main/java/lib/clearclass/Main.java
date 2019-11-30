package lib.clearclass;

import java.sql.*;
import lib.clearclass.utils.*;

public class Main {
	// условное обозначение способа вставки (всего 7 способов)
	static String[] x = {"<Stm>..<Stm>", "<Stm>", "<PrStm>", "<Batch>", "<Hib-ORM>", "<Hib-SQL>", "<JdbcTempl>"};
	// результаты измерений (в мс) для каждого способа
	static double[] y = new double[7];

	public static void main(String[] args) {
		System.out.println("Вставка 1000 строк:");
		for (DatabaseType db : DatabaseType.values()) {
			executeTest(db); // заполняет массив y
			Plotter.gplot(x, y, "Вставка 1000 строк, " + db);
		}
		Plotter.gclose();
	}
	
	static void executeTest(DatabaseType db){
		try {
			Connection conn = ConnectionFactory.getConnection(db);
			try(Statement stm = conn.createStatement()){	
				stm.execute("CREATE TABLE IF NOT EXISTS words(id serial PRIMARY KEY, word VARCHAR(32));");
				System.out.println("-------------------------------------------------- " + db);
				
				y[0] = Insert.m0(conn);
				System.out.println(String.format("%-38s %1.1f мс", "В режиме автофиксации команд: ", y[0]));
				
				y[1] = Insert.m1(conn);
				System.out.println(String.format("%-38s %1.1f мс", "Одной транзакцией: ", y[1]));
				
				y[2] = Insert.m2(conn);
				System.out.println(String.format("%-38s %1.1f мс", "Внутри транзакции, PreSt :", y[2]));
				
				y[3] = Insert.m3(conn);
				System.out.println(String.format("%-37s %1.1f мс", "Внутри транзакции, PreSt, batch-режим:", y[3]));
				
				y[4] = Insert.m4(conn);
				System.out.println(String.format("%-38s %1.1f мс", "C использованием Hibernate-ORM: ", y[4]));
				
				y[5] = Insert.m5(conn);
				System.out.println(String.format("%-38s %1.1f мс", "C использованием Hibernate-NativeQuery: ", y[5]));
				
				y[6] = Insert.m6(conn);
				System.out.println(String.format("%-38s %1.1f мс", "C использованием JdbcTemplate: ", y[6]));
				
				stm.execute("DROP TABLE words;");
				conn.commit();
			} catch (SQLException e) {
				if (!conn.getAutoCommit()) conn.rollback();
				throw new RuntimeException(e);
			} finally {
				conn.close();
			}
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
}