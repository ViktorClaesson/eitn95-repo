package task2;

import sim.*;

public class Task2 {

	public static void run() {
		// Init the simulation
		Proc q = new Square(0, 0);

		// Send init signals
		Global.SendSignal(Signal.Type.TEST, q, 0);

		// Run simulation
		Global.advanceUntil(() -> Global.time() > 1000);

		// Analysis
		System.out.println("done");
	}
}