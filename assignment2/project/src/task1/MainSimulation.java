package task1;

import java.io.*;
import java.util.*;

public class MainSimulation extends Global {
	static class Node {
		double x, y;
		public SmartSensorTower tower;

		public Node(double x, double y, SmartSensorTower tower) {
			this.x = x;
			this.y = y;
			this.tower = tower;
		}
	}

	public static void taskAB(int ts, int tp, int radius, int nbrTowers) {
		Gateway gateway = new Gateway();
		for (int i = 0; i < nbrTowers; i++) {
			double x = 10 * random.nextDouble();
			double y = 10 * random.nextDouble();
			double dist = Math.hypot(x - 5, y - 5);
			SimpleSensorTower st = new SimpleSensorTower(dist < radius ? gateway : null, ts, tp);
			SignalList.SendSignal(BEGIN_TRANSMISSION, st, expRandom(tp));
		}
	}

	public static void taskCD(int ts, int tp, int radius, int nbrTowers, double lb, double ub) {
		Gateway gateway = new Gateway();
		LinkedList<Node> nodeList = new LinkedList<>();
		for (int i = 0; i < nbrTowers; i++) {
			double x = 10 * random.nextDouble();
			double y = 10 * random.nextDouble();
			double dist = Math.hypot(x - 5, y - 5);
			SmartSensorTower st = new SmartSensorTower(dist < radius ? gateway : null, ts, tp, lb, ub);
			nodeList.add(new Node(x, y, st));
			SignalList.SendSignal(BEGIN_TRANSMISSION, st, expRandom(tp));
		}
		for (Node node : nodeList) {
			List<SmartSensorTower> withinRadius = new ArrayList<>();
			for (Node other : nodeList) {
				if (node == other)
					continue;

				if (Math.hypot(other.y - node.y, other.x - node.x) < radius)
					withinRadius.add(other.tower);
			}
			node.tower.addTowersWithinRadius(withinRadius);
		}
	}

	public static void main(String[] args) throws IOException {
		// READ CONFIG
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(args[0]);
		prop.load(fis);
		fis.close();

		int[] tsL = Arrays.asList(prop.getProperty("ts", "4000").split(", ")).stream().mapToInt(Integer::parseInt)
				.toArray();
		int[] tpL = Arrays.asList(prop.getProperty("tp", "1").split(", ")).stream().mapToInt(Integer::parseInt)
				.toArray();
		int[] radiusL = Arrays.asList(prop.getProperty("r", "7").split(", ")).stream().mapToInt(Integer::parseInt)
				.toArray();
		int[] nbrTowersL = Arrays.asList(prop.getProperty("n", "1000").split(", ")).stream().mapToInt(Integer::parseInt)
				.toArray();
		double[] lbL = Arrays.asList(prop.getProperty("lb", "1").split(", ")).stream().mapToDouble(Double::parseDouble)
				.toArray();
		double[] ubL = Arrays.asList(prop.getProperty("ub", "1.5").split(", ")).stream()
				.mapToDouble(Double::parseDouble).toArray();

		FileWriter fw = new FileWriter("src/task1/output.txt");
		String header = String.format("%8s\t%8s\t%8s\t%8s\t%4s\t%4s\t%4s\t%4s\t%4s\t%4s\n", "succRate", "lossRate",
				"load", "time", "ts", "tp", "r", "n", "lb", "ub");
		fw.write(header);
		System.out.print(header);
		for (int ts : tsL) {
			for (int tp : tpL) {
				for (int radius : radiusL) {
					for (int nbrTowers : nbrTowersL) {
						for (double lb : lbL) {
							for (double ub : ubL) {
								Signal actSignal;
								new SignalList();

								taskCD(ts, tp, radius, nbrTowers, lb, ub);

								// RESET GLOBAL VARIABLES
								time = 0;
								state.reset();
								while (state.transmissions < 100000) {
									actSignal = SignalList.FetchSignal();
									time = actSignal.arrivalTime;
									actSignal.destination.TreatSignal(actSignal);
								}

								double succRate = 1.0 * state.successful_transmissions / state.transmissions;
								double lossRate = 1.0 - succRate;
								double load = state.transmissions / time;

								String output = String.format(
										"%8.4f\t%8.4f\t%8.4f\t%8.2f\t%4d\t%4d\t%4d\t%4d\t%4.2f\t%4.2f\n", succRate,
										lossRate, load, time, ts, tp, radius, nbrTowers, lb, ub);
								fw.write(output);
								System.out.print(output);
							}
						}
					}
				}
			}
		}
		fw.close();
	}
}