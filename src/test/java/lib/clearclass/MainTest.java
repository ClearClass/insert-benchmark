package lib.clearclass;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import lib.clearclass.utils.ConnectionFactory;
import lib.clearclass.utils.Plotter;

public class MainTest {
	@Test
	public void insertTest(){
		DatabaseType db = DatabaseType.H2_in_mem;
		try {
			Connection conn = ConnectionFactory.getConnection(db);
			JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(conn, true));
			try(Statement stm = conn.createStatement()){	
				stm.execute("CREATE TABLE IF NOT EXISTS words(id serial PRIMARY KEY, word VARCHAR(32));");
				
				Insert.m0(conn);
				assertEquals(1000, getRows(jdbcTemplate));
				
				Insert.m1(conn);
				assertEquals(2000, getRows(jdbcTemplate));
				
				Insert.m2(conn);
				assertEquals(3000, getRows(jdbcTemplate));
				
				Insert.m3(conn);
				assertEquals(4000, getRows(jdbcTemplate));
				
				Insert.m4(conn);
				assertEquals(5000, getRows(jdbcTemplate));
				
				Insert.m5(conn);
				assertEquals(6000, getRows(jdbcTemplate));
				
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
	
	int getRows(JdbcTemplate jdbcTemplate){
		SqlRowSet rset = jdbcTemplate.queryForRowSet("SELECT COUNT(*) FROM words"); 
		rset.next();
		return rset.getInt(1);
	}
	
	@Test
	public void plotterTest(){
		String[] x = {"<Stm>..<Stm>", "<Stm>", "<PrStm>", "<Batch>", "<Hibernate>", "<JdbcTemplate>"};
		double[] y = {1, 1, 1, 1, 1, 1};
		Plotter.gplot(x, y, "test data");
		Plotter.gclose(1000); // показать окно в течение 1000 мс
	}
}