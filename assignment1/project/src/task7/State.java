package task7;

import java.util.*;

class State extends GlobalSimulation {

	public boolean isAlive = true, 
	comp1_alive = true, 
	comp2_alive = true, 
	comp3_alive = true, 
	comp4_alive = true, 
	comp5_alive = true;

	// Here follows the state variables and other variables that might be needed
	// e.g. for measurements
	Random slump = new Random(); // This is just a random number generator
	// The following method is called by the main program each time a new event has
	// been fetched
	// from the event list in the main loop.
	public void treatEvent(Event x) {
		switch (x.eventType) {
			case COMP_1:
				comp_1();
				break;
			case COMP_2:
				comp_2();
				break;
			case COMP_3:
				comp_3();
				break;
			case COMP_4:
				comp_4();
				break;
			case COMP_5:
				comp_5();
				break;
		}
		// printInfo(x);
	}

	// The following methods defines what should be done when an event takes place.
	// This could
	// have been placed in the case in treatEvent, but often it is simpler to write
	// a method if
	// things are getting more complicated than this.

	private void comp_1() {
		comp1_alive = false;
		comp2_alive = false;
		comp5_alive = false;
		alive();
	}

	private void comp_2() {
		comp2_alive = false;
		alive();
	}
	
	private void comp_3() {
		comp3_alive = false;
		comp4_alive = false;
		alive();
	}

	private void comp_4() {
		comp4_alive = false;
		alive();
	}

	private void comp_5() {
		comp5_alive = false;
		alive();
	}

	private void alive() {
		if(!comp1_alive && !comp2_alive && !comp3_alive && !comp4_alive && !comp5_alive)
			isAlive = false;
	}
}