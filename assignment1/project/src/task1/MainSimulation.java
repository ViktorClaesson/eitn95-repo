package task1;

import java.io.*;

public class MainSimulation extends GlobalSimulation {

	public static void main(String[] args) throws IOException {
		System.out.println(String.format("%16s|%16s|%16s", "Q1 arrival time", "Mean Q2 size", "Rejected chance"));

		double start = 0.5;
		double end = 5.0;
		double step = 0.5;

		double amp = end - start;
		int itr = (int) Math.ceil(amp / step);
		for (int i = 0; i <= itr; i++) {
			double arrTime = start + amp * i / itr;

			Event actEvent;
			State actState = new State(arrTime); // The state that shoud be used
			// Some events must be put in the event list at the beginning
			eventList = new EventListClass();
			insertEvent(ARRIVALQ1, 0);
			insertEvent(MEASURE, actState.expRandom(5));

			// The main simulation loop
			while (actState.noMeasurements < 10000) {
				actEvent = eventList.fetchEvent();
				time = actEvent.eventTime;
				actState.treatEvent(actEvent);
			}

			// Printing the result of the simulation, in this case a mean value
			System.out.println(String.format("%16.2f|%16.2f|%15.2f%%", arrTime,
					1.0 * actState.accumulated / actState.noMeasurements,
					100.0 * actState.rejected / actState.arrived));
		}

	}
}