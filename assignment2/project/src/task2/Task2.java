package task2;

import sim.*;
import sim.Signal.Type;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Task2 {

	public static Square randomSquare(List<Square> squares) {
		return squares.get((int) Global.uniRandom(0, squares.size()));
	}

	public static void initNeighbours(List<Square> squares, Square square, int idx) {
		Function<Integer, Square> getSquare = i -> (i >= 0 && i < squares.size()) ? squares.get(i) : null;
		Square N = getSquare.apply(idx - 20);
		Square NE = getSquare.apply(idx - 20 + 1);
		Square E = getSquare.apply(idx + 1);
		Square SE = getSquare.apply(idx + 20 + 1);
		Square S = getSquare.apply(idx + 20);
		Square SW = getSquare.apply(idx + 20 - 1);
		Square W = getSquare.apply(idx - 1);
		Square NW = getSquare.apply(idx - 20 - 1);
		Square[] neighbours = { N, NE, E, SE, S, SW, W, NW };
		square.setNeighbours(neighbours);
	}

	public static void run() {
		// Reset
		Global.reset();
		Data.reset();

		// Init the simulation
		List<Square> squares = IntStream.range(0, 200).mapToObj(i -> new Square()).collect(Collectors.toList());
		int[] idx = { 0 };
		squares.forEach(sq -> initNeighbours(squares, sq, idx[0]++));

		// Send init signals
		List<Student> students = IntStream.range(0, 20).mapToObj(i -> new Student(randomSquare(squares), 2 * 60))
				.collect(Collectors.toList());

		students.forEach(student -> {
			if (!student.isTalking())
				Global.SendSignal(Type.MOVE_TO_EDGE, student, 0);
		});

		// Run simulation
		Global.advanceUntil(() -> Global.time() > 1000);

		// Analysis
		System.out.println(Data.meetings);
	}
}