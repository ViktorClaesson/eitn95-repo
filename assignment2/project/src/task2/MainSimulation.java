package task2;

import java.io.*;
import sim.Global;
import sim.SignalTreater;
import sim.Signal;

public class MainSimulation {

	public static void main(String[] args) throws IOException {
		// Init the simulation
		SignalTreater q = new Queue();

		// Send init signals
		Global.SendSignal(Signal.Type.TEST, q, Global.time);

		// Run simulation
		while (Global.time < 100000) {
			try {
				Global.advance();
			} catch (Exception e) {
				System.err.println(e);
			}
		}

		// Analysis
		System.out.println("done");
	}
}