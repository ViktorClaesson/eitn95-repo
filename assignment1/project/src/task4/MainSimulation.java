package task4;

import java.io.*;

public class MainSimulation extends GlobalSimulation {

	public static void main(String[] args) throws IOException {
		int[] M = {1000, 1000, 1000, 1000, 4000, 4000};
		int[] N = {1000, 1000, 1000, 100, 100, 100};
		int[] x = {100, 10, 200, 10, 10, 10};
		int[] lambda = {8, 80, 4, 4, 4, 4};
		int[] T = {1, 1, 1, 4, 1, 4};

		Event actEvent;
		State actState;
		for (int i = 0; i < T.length; i++) {
				
			actState = new State(N[i], x[i], lambda[i], T[i]); // The state that shoud be used
			// Some events must be put in the event list at the beginning
			eventList = new EventListClass();
			insertEvent(ARRIVAL, 0);
			insertEvent(MEASURE, 1);

			File file = new File(String.format("results/task4/%d.txt", i));
			FileWriter fw = new FileWriter(file);
			StringBuffer sb = new StringBuffer();
			// The main simulation loop
			while (actState.noMeasurements < M[i]) {
				actEvent = eventList.fetchEvent();
				time = actEvent.eventTime;
				actState.treatEvent(actEvent);
				if (actEvent.eventType == MEASURE) {
					sb.append(actState.currentNbr + "\n");
				}
			}
			fw.write(sb.toString());
			fw.close();
		}
	}
}