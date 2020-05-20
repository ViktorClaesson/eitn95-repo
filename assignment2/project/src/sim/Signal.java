package sim;

public class Signal implements Comparable<Signal> {
	public enum Type {
		// TASK 1
		BEGIN_TRANSMISSION, END_TRANSMISSION, CHECK_NETWORK,

		// TASK 2
		TEST
	}

	public Type signalType;
	public SignalTreater destination;
	public double arrivalTime;

	public Signal(Type signalType, SignalTreater destination, double arrivalTime) {
		this.signalType = signalType;
		this.destination = destination;
		this.arrivalTime = arrivalTime;
	}

	@Override
	public int compareTo(Signal other) {
		return Double.compare(arrivalTime, other.arrivalTime);
	}
}
