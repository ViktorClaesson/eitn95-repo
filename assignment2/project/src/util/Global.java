package util;

import java.util.Random;
import java.util.function.BooleanSupplier;
import java.util.PriorityQueue;

public class Global {
	private static PriorityQueue<Signal> signalList = new PriorityQueue<>();
	private static double time = 0;
	private static Random random = new Random();

	public static double time() {
		return time;
	}

	public static double uniRandom(double lb, double ub) {
		return lb + random.nextDouble() * (ub - lb);
	}

	public static double expRandom(double mean) {
		return -mean * Math.log(1 - random.nextDouble());
	}

	public static void reset() {
		time = 0;
		signalList.clear();
		random = new Random();
	}

	public static void SendSignal(Signal.Type type, Proc dest, double delay) {
		signalList.add(new Signal(type, dest, time + delay));
	}

	public static void advanceWhile(BooleanSupplier bs) {
		while (bs.getAsBoolean()) {
			advance();
		}
	}

	private static void advance() {
		Signal actSignal = signalList.poll();
		time = actSignal.arrivalTime;
		actSignal.destination.TreatSignal(actSignal);
	}
}
