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

	public static double taskAB(int ts, int tp, int radius, int nbrTowers) {
		int withinGateway = 0;
		Gateway gateway = new Gateway();
		for (int i = 0; i < nbrTowers; i++) {
			double x = 10 * random.nextDouble();
			double y = 10 * random.nextDouble();
			double dist = Math.hypot(x - 5, y - 5);
			SimpleSensorTower st = new SimpleSensorTower(dist < radius ? gateway : null, ts, tp);
			SignalList.SendSignal(BEGIN_TRANSMISSION, st, expRandom(tp));
			withinGateway = dist < radius ? withinGateway + 1 : withinGateway;
		}
		return 1.0 * withinGateway / nbrTowers;
	}

	public static double taskCD(int ts, int tp, int radius, int nbrTowers, double lb, double ub) {
		int withinGateway = 0;
		Gateway gateway = new Gateway();
		LinkedList<Node> nodeList = new LinkedList<>();
		for (int i = 0; i < nbrTowers; i++) {
			double x = 10 * random.nextDouble();
			double y = 10 * random.nextDouble();
			double dist = Math.hypot(x - 5, y - 5);
			SmartSensorTower st = new SmartSensorTower(dist < radius ? gateway : null, ts, tp, lb, ub);
			nodeList.add(new Node(x, y, st));
			SignalList.SendSignal(BEGIN_TRANSMISSION, st, expRandom(tp));
			withinGateway = dist < radius ? withinGateway + 1 : withinGateway;
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
		return 1.0 * withinGateway / nbrTowers;
	}

	public static void main(String[] args) throws IOException {
		// READ CONFIG
		Properties prop = new Properties();
		if (args.length > 0) {
			FileInputStream fis = new FileInputStream(args[0]);
			prop.load(fis);
			fis.close();
		}

		int[] tsL = Arrays.asList(prop.getProperty("ts", "4000").split(", ")).stream().mapToInt(Integer::parseInt)
				.toArray();
		int[] tpL = Arrays.asList(prop.getProperty("tp", "1").split(", ")).stream().mapToInt(Integer::parseInt)
				.toArray();
		int[] radiusL = Arrays.asList(prop.getProperty("r", "15").split(", ")).stream().mapToInt(Integer::parseInt)
				.toArray();
		int[] nbrTowersL = Arrays.asList(prop.getProperty("n", "2660").split(", ")).stream().mapToInt(Integer::parseInt)
				.toArray();
		double[] lbL = Arrays.asList(prop.getProperty("lb", "1").split(", ")).stream().mapToDouble(Double::parseDouble)
				.toArray();
		double[] ubL = Arrays.asList(prop.getProperty("ub", "1").split(", ")).stream().mapToDouble(Double::parseDouble)
				.toArray();
		int runs = Integer.parseInt(prop.getProperty("runs", "1"));

		FileWriter fw = new FileWriter("src/task1/results/output.txt");
		String header = String.format("%8s\t%8s\t%8s\t%10s\t%6s\t%4s\t%4s\t%4s\t%4s\t%4s\t%4s\t%14s\t%8s\n", "succRate",
				"lossRate", "load", "time", "runs", "ts", "tp", "r", "n", "lb", "ub", "reach gateway", "n*succ");
		fw.write(header);
		System.out.print(header);
		for (int ts : tsL) {
			for (int tp : tpL) {
				for (int radius : radiusL) {
					for (int nbrTowers : nbrTowersL) {
						for (double lb : lbL) {
							for (double ub : ubL) {
								double withinGateway = 0, succRate = 0, lossRate = 0, load = 0;
								for (int i = 0; i < runs; i++) {
									Signal actSignal;
									new SignalList();

									ub = Math.max(lb, ub);
									withinGateway += taskCD(ts, tp, radius, nbrTowers, lb, ub);

									// RESET GLOBAL VARIABLES
									time = 0;
									state.reset();
									while (state.transmissions < 100000) {
										actSignal = SignalList.FetchSignal();
										time = actSignal.arrivalTime;
										actSignal.destination.TreatSignal(actSignal);
									}

									succRate += 1.0 * state.successful_transmissions / state.transmissions;
									lossRate += 1.0 - succRate;
									load += state.transmissions / time;
								}
								withinGateway /= runs;
								succRate /= runs;
								lossRate /= runs;
								load /= runs;
								String output = String.format(
										"%8.4f\t%8.4f\t%8.4f\t%10.2f\t%6d\t%4d\t%4d\t%4d\t%4d\t%4.2f\t%4.2f\t%14.2f\t%8.2f\n",
										succRate, lossRate, load, time, runs, ts, tp, radius, nbrTowers, lb, ub,
										withinGateway, nbrTowers * succRate);
								fw.write(output);
								fw.flush();
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