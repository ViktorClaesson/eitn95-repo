package task6;

import java.io.*;
import java.util.LinkedList;

public class MainSimulation extends GlobalSimulation {

	public static void main(String[] args) throws IOException {
		int minutesInDay = (17 * 60) - (9 * 60);
		int overTime = 0;
		System.out.println(String.format("%16s|%16s", "Average overtime", "Average Serv-time"));
		Event actEvent;
		State actState;
		LinkedList<Double> startTime;
		double averageServTime = 0;
		double totalAverageServTime = 0;

		for (int i = 0; i < 1000; i++) {
			handling = 0;
			time = 0;
			averageServTime = 0;
			startTime = new LinkedList<>();
			actState = new State(minutesInDay); // The state that shoud be used
			// Some events must be put in the event list at the beginning
			eventList = new EventListClass();
			insertEvent(ARRIVAL, actState.expRandom(15));

			// The main simulation loop
			while (actState.waiting > 0 || actState.currentTime < minutesInDay) {
				actEvent = eventList.fetchEvent();
				time = actEvent.eventTime;
				actState.treatEvent(actEvent);
				if (actEvent.eventType == ARRIVAL) {
					startTime.add(actState.currentTime);
				} else if (actEvent.eventType == DEPART){
					averageServTime += actState.currentTime - startTime.poll();
				}
			}
			totalAverageServTime += averageServTime/actState.arrivals;
			overTime += (int)(((actState.time/60)*60) + actState.time%60) - minutesInDay;
		}
		System.out.println(String.format("%16d|%16d", overTime/1000, (int)totalAverageServTime/1000));
	}
}