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
		for (int ts : tsL) {
			for (int tp : tpL) {
				for (int radius : radiusL) {
					for (int nbrTowers : nbrTowersL) {
						for (double lb : lbL) {
							for (double ub : ubL) {
								Signal actSignal;
								new SignalList();

								Gateway gateway = new Gateway();
								LinkedList<Node> nodeList = new LinkedList<>();
								for (int i = 0; i < nbrTowers; i++) {
									double x = 10 * random.nextDouble();
									double y = 10 * random.nextDouble();
									double dist = Math.hypot(x - 5, y - 5);
									SmartSensorTower st = new SmartSensorTower(dist < radius ? gateway : null, ts, tp,
											lb, ub);
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

								// RESET GLOBAL VARIABLES
								time = 0;
								state.reset();
								while (state.transmissions < 25000) {
									actSignal = SignalList.FetchSignal();
									time = actSignal.arrivalTime;
									actSignal.destination.TreatSignal(actSignal);
								}

								double lossRate = 1.0 * (state.transmissions - state.successful_transmissions)
										/ state.transmissions;
								double load = state.transmissions / time;

								String output = String.format("%.4f\t%.4f\t%f\n", lossRate, load, time);
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