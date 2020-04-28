package task2;

import java.util.*;

class State extends GlobalSimulation {

	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int accumulated, noMeasurements;
	public int aInBuffer, bInBuffer;

	private final double lambda = 150, x_a = 0.002, x_b = 0.004, d = 1, measure_time = 0.1;

	Random slump = new Random(); // This is just a random number generator

	public double expRandom(double mean) {
		return -mean * Math.log(1 - slump.nextDouble());
	}

	public String eventTypeToString(Event x) {
		switch (x.eventType) {
			case ARRIVAL_A:
				return "ARRIVAL_A";
			case ARRIVAL_B:
				return "ARRIVAL_B";
			case DEPART_A:
				return "DEPART_A";
			case DEPART_B:
				return "DEPART_B";
			case MEASURE:
				return "MEASURE";
			default:
				return "Uknown";
		}
	}

	public void printInfo(Event x) {
		System.out.println(String.format("%10s | %6.4f | %2d | %2d | %4d | %4d", eventTypeToString(x), x.eventTime,
				aInBuffer, bInBuffer, accumulated, noMeasurements));
	}

	// The following method is called by the main program each time a new event has
	// been fetched
	// from the event list in the main loop.
	public void treatEvent(Event x) {
		switch (x.eventType) {
			case ARRIVAL_A:
				arrivalA();
				break;
			case ARRIVAL_B:
				arrivalB();
				break;
			case DEPART_A:
				departA();
				break;
			case DEPART_B:
				departB();
				break;
			case MEASURE:
				measure();
				break;
		}
		// printInfo(x);
	}

	// The following methods defines what should be done when an event takes place.
	// This could
	// have been placed in the case in treatEvent, but often it is simpler to write
	// a method if
	// things are getting more complicated than this.

	private void arrivalA() {
		aInBuffer++;
		if (aInBuffer == 1 && bInBuffer == 0)
			insertEvent(DEPART_A, time + x_a);
		insertEvent(ARRIVAL_A, time + expRandom(1 / lambda));
	}

	private void arrivalB() {
		bInBuffer++;
		if (aInBuffer == 0 && bInBuffer == 1)
			insertEvent(DEPART_B, time + x_b);
	}

	private void departNext() {
		if (aInBuffer > 0)
			insertEvent(DEPART_A, time + x_a);
		else if (bInBuffer > 0)
			insertEvent(DEPART_B, time + x_b);
	}

	private void departA() {
		aInBuffer--;
		insertEvent(ARRIVAL_B, time + expRandom(d));
		departNext();
	}

	private void departB() {
		bInBuffer--;
		departNext();
	}

	private void measure() {
		// measure
		accumulated += aInBuffer + bInBuffer;
		noMeasurements += 1;
		insertEvent(MEASURE, time + measure_time);
	}
}