package task2;

import sim.*;
import sim.Signal.Type;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Task2 {

	public static Square randomSquare(List<Square> squares) {
		return squares.get((int) Global.uniRandom(0, squares.size()));
	}

	public static void initNeighbours(int SIZE, List<Square> squares, Square square, int x, int y) {
		BiFunction<Integer, Integer, Square> getSquare = (xp,
				yp) -> (xp >= 0 && xp < SIZE && yp >= 0 && yp < SIZE) ? squares.get(xp + SIZE * yp) : null;
		Square N = getSquare.apply(x, y - 1);
		Square NE = getSquare.apply(x + 1, y - 1);
		Square E = getSquare.apply(x + 1, y);
		Square SE = getSquare.apply(x + 1, y + 1);
		Square S = getSquare.apply(x, y + 1);
		Square SW = getSquare.apply(x - 1, y + 1);
		Square W = getSquare.apply(x - 1, y);
		Square NW = getSquare.apply(x - 1, y - 1);
		Square[] neighbours = { N, NE, E, SE, S, SW, W, NW };
		square.setNeighbours(neighbours);
	}

	public static void run(int SIZE, int N_STUDENTS, double SPEED) {
		// Reset
		Global.reset();
		Data.reset();

		// Init the simulation
		List<Square> squares = IntStream.range(0, SIZE * SIZE).mapToObj(i -> new Square(i % 20, i / 20))
				.collect(Collectors.toList());
		int[] idx = { 0 };
		squares.forEach(sq -> initNeighbours(SIZE, squares, sq, idx[0] % SIZE, idx[0]++ / SIZE));

		// Send init signals
		List<Student> students = IntStream.range(0, N_STUDENTS)
				.mapToObj(i -> new Student(randomSquare(squares), SPEED * 60)).collect(Collectors.toList());

		students.forEach(student -> {
			if (!student.isTalking())
				Global.SendSignal(Type.AT_MIDDLE, student, 0);
		});

		// Run simulation
		Global.advanceWhile(() -> Global.time() < 100);

		// Analysis
		System.out.printf("Total meetings: %d", Data.meetings);
	}
}