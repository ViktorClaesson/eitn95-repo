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
		Global.SendSignal(Signal.Type.TEST, q, 0);

		// Run simulation
		Global.advanceUntil(() -> Global.time() > 1000);

		// Analysis
		System.out.println("done");
	}
}