package sim;

import java.util.Random;
import java.util.PriorityQueue;

public class Global {
	private static PriorityQueue<Signal> signalList = new PriorityQueue<>();
	public static double time = 0;
	public static Random random = new Random();

	public static double uniRandom(double lb, double ub) {
		return lb + random.nextDouble() * (ub - lb);
	}

	public static double expRandom(double mean) {
		return -mean * Math.log(1 - random.nextDouble());
	}

	public static void SendSignal(Signal.Type type, SignalTreater dest, double delay) {
		signalList.add(new Signal(type, dest, time + delay));
	}

	public static void advance() {
		Signal actSignal = signalList.poll();
		time = actSignal.arrivalTime;
		actSignal.destination.TreatSignal(actSignal);
	}

	public static void reset() {
		time = 0;
		signalList.clear();
		random = new Random();
	}
}
