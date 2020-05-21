package task2;

import sim.*;
import java.util.stream.IntStream;

public class Task2 {

	public static void run() {
		// Init the simulation
		IntStream s = IntStream.generate(() -> 0);
		s.forEach(x -> System.out.println(x));

		// Send init signals
		Global.SendSignal(Signal.Type.TEST, new Square(0, 0), 0);

		// Run simulation
		Global.advanceUntil(() -> Global.time() > 1000);

		// Analysis
		System.out.println("done");
	}
}