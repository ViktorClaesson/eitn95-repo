package task2;

import util.*;
import util.Signal.Type;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

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

	public static int pairs(int n) {
		return n * (n - 1) / 2;
	}

	public static int[] toFrequencyArray(Collection<Integer> data, int max) {
		int[] out = new int[max];
		data.stream().forEach(f -> out[f - 1]++);
		return out;
	}

	public static void run(String propertiesFilePath) throws IOException {
		// READ CONFIG
		Properties prop = new Properties();
		if (propertiesFilePath != null) {
			FileInputStream fis = new FileInputStream(propertiesFilePath);
			prop.load(fis);
			fis.close();
		}
		int minSpeed = Integer.parseInt(prop.getProperty("minSpeed", "2"));
		int maxSpeed = Integer.parseInt(prop.getProperty("maxSpeed", "2"));
		int RUNS = Integer.parseInt(prop.getProperty("runs", "1000"));
		int SIZE = Integer.parseInt(prop.getProperty("size", "20"));
		int N_STUDENTS = Integer.parseInt(prop.getProperty("n", "20"));
		System.out.println("Running Simulation with ...");
		System.out.println(String.format("minSpeed = %d", minSpeed));
		System.out.println(String.format("maxSpeed = %d", maxSpeed));
		System.out.println(String.format("RUNS = %d", RUNS));
		System.out.println(String.format("SIZE = %d", SIZE));
		System.out.println(String.format("N_STUDENTS = %d", N_STUDENTS));

		// OUTPUT FILES
		new File("src/task2/results").mkdirs();
		FileWriter fw_freq = new FileWriter("src/task2/results/freq.txt");
		FileWriter fw_time = new FileWriter("src/task2/results/time.txt");

		int max_tmp = 0;
		double[] time_runs = new double[RUNS];
		List<Collection<Integer>> meetings_runs = new ArrayList<>(RUNS);
		final int RUNS_DIGITS = 1 + (int) Math.log10(RUNS);

		for (int r = 0; r < RUNS; r++) {
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
					.mapToObj(i -> new Student(i, randomSquare(squares), Global.uniRandom(minSpeed, maxSpeed) * 60))
					.collect(Collectors.toList());

			students.forEach(student -> {
				if (!student.isTalking())
					Global.SendSignal(Type.AT_MIDDLE, student, 0);
			});

			// Run simulation
			int pairs = pairs(N_STUDENTS);
			Global.advanceWhile(() -> Data.meetings.keySet().size() < pairs);

			// Saving run
			max_tmp = Math.max(max_tmp, Data.meetings.values().stream().max(Integer::compare).get());

			meetings_runs.add(Data.meetings.values());
			time_runs[r] = Global.time();
		}

		// Analysis and Logging output
		final int MAX = max_tmp;

		int[] sum_array = toFrequencyArray(
				meetings_runs.stream().map(o -> o.stream()).reduce(Stream::concat).get().collect(Collectors.toList()),
				MAX);

		final int SUM_MAX_DIGITS = 1 + (int) Math.log10(Arrays.stream(sum_array).max().getAsInt());

		Statistic time_stats = new Statistic(time_runs);
		fw_time.write(String.format("%8s\t%8s\t%16s\n", "time µ", "time σ", "CI 95%"));
		fw_time.write(String.format("%8.2f\t%8.2f\t%16s\n", time_stats.avg, time_stats.stdev, time_stats.interval()));

		String header_format_meets = "Meets:%" + (RUNS_DIGITS - 1) + "s\t";
		String header_format_run = "Run %" + RUNS_DIGITS + "d:\t";
		String header_format_sum = "Sum:%" + (RUNS_DIGITS + 1) + "s\t";
		String header_format_freq = "Freq:%" + RUNS_DIGITS + "s\t";
		String header_format_freq_proc = "Freq %%:%" + (RUNS_DIGITS - 2) + "s\t";

		String result_format = "%" + Math.max(SUM_MAX_DIGITS, 6) + "d\t";
		String result_freq_format = "%" + Math.max(SUM_MAX_DIGITS, 6) + ".4f\t";
		String result_freq_proc_format = "%" + Math.max(SUM_MAX_DIGITS, 5) + ".2f%%\t";

		// HEADER;
		fw_freq.write(String.format(header_format_meets, ""));
		IntStream.range(0, max_tmp).forEach(i -> {
			try {
				fw_freq.write(String.format(result_format, i + 1));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		fw_freq.write("\n");

		// RUNS;
		IntStream.range(0, meetings_runs.size()).forEach(i -> {
			try {
				fw_freq.write(String.format(header_format_run, i + 1));
				int[] freqs = toFrequencyArray(meetings_runs.get(i), MAX);
				for (int f : freqs) {
					fw_freq.write(String.format(result_format, f));
				}
				fw_freq.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		// SUM;
		fw_freq.write(String.format(header_format_sum, ""));
		for (int f : sum_array) {
			fw_freq.write(String.format(result_format, f));
		}
		fw_freq.write("\n");

		// FREQ;
		fw_freq.write(String.format(header_format_freq, ""));
		int sumOfSumArray = Arrays.stream(sum_array).sum();
		for (int f : sum_array) {
			fw_freq.write(String.format(result_freq_format, 1.0 * f / sumOfSumArray));
		}
		fw_freq.write("\n");

		// FREQ PROCENT;
		fw_freq.write(String.format(header_format_freq_proc, ""));
		for (int f : sum_array) {
			fw_freq.write(String.format(result_freq_proc_format, 100.0 * f / sumOfSumArray));
		}
		fw_freq.write("\n");

		// CLOSE FILES;

		fw_freq.close();
		fw_time.close();
	}
}