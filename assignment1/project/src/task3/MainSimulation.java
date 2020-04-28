package task3;

import java.io.*;
import java.util.ArrayList;

public class MainSimulation extends GlobalSimulation {

	public static void main(String[] args) throws IOException {
		System.out.println(String.format("%16s|%16s|%16s|%16s", "Theory N", "Simulated N", "Theory T", "Simulated T"));
		
		ArrayList<Double> meantime = new ArrayList<>();
		meantime.add(1.1);
		meantime.add(1.5);
		meantime.add(2.0);
		for (int i = 0; i < 3; i++) {
			double arrTime = (Double)meantime.get(i);

			Event actEvent;
			State actState = new State(arrTime); // The state that shoud be used
			// Some events must be put in the event list at the beginning
			eventList = new EventListClass();
			insertEvent(ARRIVALQ1, 0);
			insertEvent(MEASURE, actState.expRandom(5));

			// The main simulation loop
			while (actState.noMeasurements < 1000) {
				actEvent = eventList.fetchEvent();
				time = actEvent.eventTime;
				actState.treatEvent(actEvent);
			}

			// Printing the result of the simulation, in this case a mean value
			System.out.println(String.format("%16.2f|%16.2f|%16.2f|%16.2f", (2)/(arrTime-1),
			1.0 * actState.accumulated / actState.noMeasurements, (2*arrTime)/(arrTime-1),
			1.0 * actState.totalTime / actState.arrived));
		}
	}
}