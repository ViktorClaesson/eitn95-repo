package task7;

import java.io.*;

public class MainSimulation extends GlobalSimulation {

	public static void main(String[] args) throws IOException {
		double mean = 0;
		for (int i = 0; i <= 1000; i++) {
			Event actEvent;
			State actState = new State(); // The state that shoud be used
			// Some events must be put in the event list at the beginning
			eventList = new EventListClass();
			insertEvent(COMP_1, (actState.slump.nextDouble()*4) + 1);
			insertEvent(COMP_2, (actState.slump.nextDouble()*4) + 1);
			insertEvent(COMP_3, (actState.slump.nextDouble()*4) + 1);
			insertEvent(COMP_4, (actState.slump.nextDouble()*4) + 1);
			insertEvent(COMP_5, (actState.slump.nextDouble()*4) + 1);

			// The main simulation loop
			while (actState.isAlive) {
				actEvent = eventList.fetchEvent();
				time = actEvent.eventTime;
				actState.treatEvent(actEvent);
			}
			mean += time;
		}
		System.out.println(mean/1000);
	}
}