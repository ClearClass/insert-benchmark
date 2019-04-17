package lib.clearclass;

/** тип тестируемой СУБД */
public enum DatabaseType {
	Postgres, H2_file, H2_in_mem;

	@Override
	public String toString() {
		return super.toString().replace('_', '-'); // замена для передачи в gnuplot
	}
}