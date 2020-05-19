package task1;

import java.util.Random;

public class Global {
	public static final int BEGIN_TRANSMISSION = 1, END_TRANSMISSION = 2, CHECK_NETWORK = 3;
	public static double time = 0;

	public static final Random random = new Random();

	public static final State state = new State();

	public static double uniRandom(double lb, double ub) {
		return lb + random.nextDouble() * (ub - lb);
	}

	public static double expRandom(double mean) {
		return -mean * Math.log(1 - random.nextDouble());
	}
}
