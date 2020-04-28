package task4;

import java.io.*;

public class MainSimulation extends GlobalSimulation {

	public static void main(String[] args) throws IOException {
		System.out.println(String.format("%16s|%16s|%16s", "Q1 arrival time", "Mean Q2 size", "Rejected chance"));

		int measurements = 1000;

		Event actEvent;
		State actState = new State(1); // The state that shoud be used
		// Some events must be put in the event list at the beginning
		eventList = new EventListClass();
		insertEvent(ARRIVALQ1, 0);
		insertEvent(MEASURE, actState.expRandom(5));

		// The main simulation loop
		while (actState.noMeasurements < measurements) {
			actEvent = eventList.fetchEvent();
			time = actEvent.eventTime;
			actState.treatEvent(actEvent);
		}

		// Printing the result of the simulation, in this case a mean value
		System.out.println(String.format("%16.2f|%16.2f|%15.2f%%"));
	

	}
}