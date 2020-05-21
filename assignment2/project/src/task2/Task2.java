package task2;

import sim.*;
import java.util.stream.IntStream;

public class Task2 {

	public static void run() {
		// Init the simulation
		IntStream.range(0, 200).forEach(i -> System.out.println(i));

		// Send init signals
		System.exit(0);

		// Run simulation
		Global.advanceUntil(() -> Global.time() > 1000);

		// Analysis
		System.out.println("done");
	}
}