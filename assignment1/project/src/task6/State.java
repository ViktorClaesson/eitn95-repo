package task6;

import java.util.*;

class State extends GlobalSimulation {
	public int minutesInDay;
	public double currentTime = 0;
	public int waiting = 0;
	public int arrivals = 0;

	public State(int minutesInDay) {
		this.minutesInDay = minutesInDay;
	}

	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements

	Random slump = new Random(); // This is just a random number generator

	public double expRandom(double mean) {
		return -mean * Math.log(1 - slump.nextDouble());
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
		currentTime = time;
		arrivals++;
		double temp1 = time + expRandom(15);
		if(temp1 <= minutesInDay){
			waiting ++;
			insertEvent(ARRIVAL, temp1);
		}
		if(handling == 0 && waiting > 0){
			insertEvent(DEPART, time + ((slump.nextDouble() * 10) + 10));
			handling ++;
			waiting--;
		}
	}

	private void depart() {
		handling--;
		currentTime = time;
		if (handling == 0 && waiting > 0){
			insertEvent(DEPART, time + ((slump.nextDouble() * 10) + 10));
			handling++;
			waiting--;
		} else {
			insertEvent(MEASURE, time);
		}
	}

	private void measure() {
		currentTime = time;
		insertEvent(MEASURE, time + 1);
	}
}