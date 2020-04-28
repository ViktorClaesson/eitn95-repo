package task1;

import java.util.*;

class State extends GlobalSimulation {

	public final double arrTime;

	public State(double arrTime) {
		this.arrTime = arrTime;
	}

	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int numberInQueue1 = 0, numberInQueue2, accumulated = 0, arrived = 0, rejected = 0, noMeasurements = 0;

	Random slump = new Random(); // This is just a random number generator

	public double expRandom(double mean) {
		return -mean * Math.log(1 - slump.nextDouble());
	}

	public String eventTypeToString(Event x) {
		switch (x.eventType) {
			case ARRIVALQ1:
				return "ARRIVALQ1";
			case DEPARTQ1:
				return "DEPARTQ1";
			case DEPARTQ2:
				return "DEPARTQ2";
			case MEASURE:
				return "MEASURE";
			default:
				return "Uknown";
		}
	}

	public void printInfo(Event x) {
		System.out.println(String.format("%10s %5.2f %4d %4d %4d %4d", eventTypeToString(x), x.eventTime,
				numberInQueue1, numberInQueue2, arrived, rejected));
	}

	// The following method is called by the main program each time a new event has
	// been fetched
	// from the event list in the main loop.
	public void treatEvent(Event x) {
		switch (x.eventType) {
			case ARRIVALQ1:
				arrivalq1();
				break;
			case DEPARTQ1:
				departq1();
				break;
			case DEPARTQ2:
				departq2();
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

	private void arrivalq1() {
		arrived++;
		if (numberInQueue1 < 10) { // ACCEPT
			numberInQueue1++;
			if (numberInQueue1 == 1)
				insertEvent(DEPARTQ1, time + expRandom(2.1));
		} else { // REJECT
			rejected++;
		}
		insertEvent(ARRIVALQ1, time + arrTime);
	}

	private void departq1() {
		numberInQueue1--;
		if (numberInQueue1 > 0)
			insertEvent(DEPARTQ1, time + expRandom(2.1));

		numberInQueue2++;
		if (numberInQueue2 == 1)
			insertEvent(DEPARTQ2, time + 2);
	}

	private void departq2() {
		numberInQueue2--;
		if (numberInQueue2 > 0)
			insertEvent(DEPARTQ2, time + 2);
	}

	private void measure() {
		accumulated = accumulated + numberInQueue2;
		noMeasurements++;
		insertEvent(MEASURE, time + expRandom(5));
	}
}