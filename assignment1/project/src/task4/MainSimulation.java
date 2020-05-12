package task4;

import java.io.*;

public class MainSimulation extends GlobalSimulation {

	public static void main(String[] args) throws IOException {
		int[] M = {1000, 1000, 1000, 1000, 4000, 4000};
		int[] N = {1000, 1000, 1000, 100, 100, 100};
		int[] x = {100, 10, 200, 10, 10, 10};
		int[] lambda = {8, 80, 4, 4, 4, 4};
		int[] T = {1, 1, 1, 4, 1, 4};
		double average;
		double[] datapoints;
		double standardDeviation;
		int longestCI;
		int currentCI;

		Event actEvent;
		State actState;
		System.out.println(String.format("%20s|%20s|%15s", "Mean", "Standard Deviation", "CI Length"));
		for (int i = 0; i < T.length; i++) {
			longestCI = 0;
			currentCI = 0;
			average = 0.0;
			standardDeviation = 0.0;
			datapoints = new double[M[i]];
				
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
					average += actState.currentNbr;
					sb.append(actState.currentNbr + "\n");
					datapoints[actState.noMeasurements-1] = actState.currentNbr;
				}
			}
			fw.write(sb.toString());
			fw.close();
			average  = average / M[i];
			for (int j = 0; j < datapoints.length; j++) {
				datapoints[j] = (Math.pow((datapoints[j]-(average)), 2));
				standardDeviation += datapoints[j];
			}
			standardDeviation = 1.96 * (standardDeviation/M[i]);
			
			System.out.println(String.format("%20f|%20f|%15s", (average), Math.sqrt(standardDeviation/(average)), average + " +- " + standardDeviation));
		}
	}
}