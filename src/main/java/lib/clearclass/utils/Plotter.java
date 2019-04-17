package lib.clearclass.utils;

import java.io.PrintStream;

public class Plotter {
	private static Process gnuplot;
	private static PrintStream std; // стандартный поток gnuplot
	private static int n = 0; // счетчик графиков gnuplot
	
	static {
		try {
			String[] cmd = {"gnuplot.exe", "-persist"};
			gnuplot = Runtime.getRuntime().exec(cmd);
			std = new PrintStream(gnuplot.getOutputStream());
		} catch (Exception e) {
			System.err.println("Failed to start gnuplot process");
		} 
	}
	
	private enum FontSize {
		Little(14, 12), Big(22, 18);
		final int t; // title
		final int l; // xlabel, ylabel
		private FontSize(int t, int l) {
			this.t = t;
			this.l = l;
		}
	}
	
	public static void gplot(String[] x, double[] y, String title) {
		String color = "#804040"; // узнать все цвета: show colornames
		std.println("set terminal wxt " + n++);
		std.println("set grid");
		std.println("set boxwidth 0.5");
		std.println("set style fill solid");
		FontSize fz = FontSize.Little;
		std.println("set title \"" + title + "\" font \"Times New Roman, " + fz.t + "\";");
		std.println("set xlabel \"Способ вставки\" font \"Times New Roman, " + fz.l + " \";");
		std.println("set ylabel \"Время выполнения, мс\" font \"Times New Roman, " + fz.l + "\";");
		std.println("set key off");
		std.print("plot '-' using 1:3:xtic(2) with boxes ");
		std.println("linecolor rgb \"" + color + "\" ");
		for(int k = 0; k < x.length; k++)
			std.println(k + " " + x[k] + " " + y[k]);
		std.println("e");
		std.flush();
	}
	
	// закрыть ресурсы gnuplot
	public static void gclose(){
		std.close();
	}
	
	public static void gclose(int pause){
		std.close();
		try {
			Thread.sleep(pause);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gnuplot.destroy();
	}
}