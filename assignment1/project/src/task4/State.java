package task4;

import java.util.*;

class State extends GlobalSimulation {
	int servers, serviceTime, lambda, measureTime;

	public State(int servers, int serviceTime, int lambda, int measureTime) {
		this.servers = servers;
		this.serviceTime = serviceTime;
		this.lambda = lambda;
		this.measureTime = measureTime;
	}

	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	public int currentNbr = 0, noMeasurements = 0;

	Random slump = new Random(); // This is just a random number generator

	public double expRandom(double mean) {
		return -mean * Math.log(1 - slump.nextDouble());
	}

	public String eventTypeToString(Event x) {
		switch (x.eventType) {
			case ARRIVAL:
				return "ARRIVALQ1";
			case DEPART:
				return "DEPARTQ1";
			case MEASURE:
				return "MEASURE";
			default:
				return "Uknown";
		}
	}

	// The following method is called by the main program each time a new event has
	// been fetched
	// from the event list in the main loop.
	public void treatEvent(Event x) {
		switch (x.eventType) {
			case ARRIVAL:
				arrival();
				break;
			case DEPART:
				depart();
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

	private void arrival() {
		if(currentNbr <= servers){
			currentNbr++;
			insertEvent(DEPART, time + serviceTime);
		}
		insertEvent(ARRIVAL, time + expRandom(1.0/lambda));
	}

	private void depart() {
		currentNbr--;
	}

	private void measure() {
		noMeasurements++;
		insertEvent(MEASURE, time + measureTime);
	}
}