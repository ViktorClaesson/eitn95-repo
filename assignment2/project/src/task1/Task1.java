package task1;

import util.*;
import java.io.*;
import java.util.*;

public class Task1 {
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
			double x = Global.uniRandom(0, 10);
			double y = Global.uniRandom(0, 10);
			double dist = Math.hypot(x - 5, y - 5);
			SimpleSensorTower st = new SimpleSensorTower(dist < radius ? gateway : null, ts, tp);
			Global.SendSignal(Signal.Type.BEGIN_TRANSMISSION, st, Global.expRandom(ts));
			withinGateway = dist < radius ? withinGateway + 1 : withinGateway;
		}
		return 1.0 * withinGateway / nbrTowers;
	}

	public static double taskCD(int ts, int tp, int radius, int nbrTowers, double lb, double ub) {
		int withinGateway = 0;
		Gateway gateway = new Gateway();
		LinkedList<Node> nodeList = new LinkedList<>();
		for (int i = 0; i < nbrTowers; i++) {
			double x = Global.uniRandom(0, 10);
			double y = Global.uniRandom(0, 10);
			double dist = Math.hypot(x - 5, y - 5);
			SmartSensorTower st = new SmartSensorTower(dist < radius ? gateway : null, ts, tp, lb, ub);
			nodeList.add(new Node(x, y, st));
			Global.SendSignal(Signal.Type.BEGIN_TRANSMISSION, st, Global.expRandom(ts));
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

	public static int sims(double[] lbs, double[] ubs) {
		int count = 0;
		for (double lb : lbs) {
			for (double ub : ubs) {
				if (ub < lb)
					continue;
				count++;
			}
		}
		return count;
	}

	public static void run(String propertiesFilePath) throws IOException {
		// READ CONFIG
		Properties prop = new Properties();
		if (propertiesFilePath != null) {
			FileInputStream fis = new FileInputStream(propertiesFilePath);
			prop.load(fis);
			fis.close();
		}

		int[] tsL = Arrays.asList(prop.getProperty("ts", "4000").split(", ")).stream().mapToInt(Integer::parseInt)
				.toArray();
		int[] tpL = Arrays.asList(prop.getProperty("tp", "1").split(", ")).stream().mapToInt(Integer::parseInt)
				.toArray();
		int[] radiusL = Arrays.asList(prop.getProperty("r", "7").split(", ")).stream().mapToInt(Integer::parseInt)
				.toArray();
		int[] nbrTowersL = Arrays.asList(prop.getProperty("n", "2000").split(", ")).stream().mapToInt(Integer::parseInt)
				.toArray();
		double[] lbL = Arrays.asList(prop.getProperty("lb", "1").split(", ")).stream().mapToDouble(Double::parseDouble)
				.toArray();
		double[] ubL = Arrays.asList(prop.getProperty("ub", "1").split(", ")).stream().mapToDouble(Double::parseDouble)
				.toArray();
		int runs = Integer.parseInt(prop.getProperty("runs", "1"));
		boolean smart = Boolean.parseBoolean(prop.getProperty("smart", "false"));

		System.out.printf("Running %d %s simulations...\n",
				runs * tsL.length * tpL.length * radiusL.length * nbrTowersL.length * sims(lbL, ubL),
				smart ? "smart" : "simple");

		new File("src/task1/results/").mkdirs();
		FileWriter fw_avg = new FileWriter("src/task1/results/avg.txt");
		FileWriter fw_all = new FileWriter("src/task1/results/all.txt");

		String header_all = "%8s\t%8s\t%8s\t%8s\t%10s\t%4s\t%6s\t%4s\t%4s\t%6s\t%4s\t%4s\t%8s\t%8s\n";
		String result_all = "%8.5f\t%8.5f\t%8.5f\t%8.5f\t%10.2f\t%4d\t%6d\t%4d\t%4d\t%6d\t%4.2f\t%4.2f\t%8.5f\t%8.2f\n";
		Object[] header_values_all = { "S_rate", "L_rate", "λ", "T_put", "time", "runs", "ts", "tp", "r", "n", "lb",
				"ub", "%!dead", "n*succ" };

		String header_avg = "%8s\t%8s\t%8s\t%8s\t%8s\t%8s\t%8s\t%8s\t%10s\t%10s\t%4s\t%6s\t%4s\t%4s\t%6s\t%4s\t%4s\t%8s\t%8s\n";
		String result_avg = "%16s\t%16s\t%16s\t%16s\t%10.2f\t%10.4f\t%4d\t%6d\t%4d\t%4d\t%6d\t%4.2f\t%4.2f\t%8.5f\t%8.2f\n";
		Object[] header_values_avg = { "S_rate_µ", "S_rate_σ", "L_rate_µ", "L_rate_σ", "λ_µ", "λ_σ", "T_put_µ",
				"T_put_σ", "time_µ", "time_σ", "runs", "ts", "tp", "r", "n", "lb", "ub", "%!dead_µ", "n*succ" };

		String header_sys = header_all;
		String result_sys = result_all;
		Object[] header_values_sys = { "S_rate_µ", "L_rate_µ", "λ_µ", "T_put_µ", "time_µ", "runs", "ts", "tp", "r", "n",
				"lb", "ub", "%!dead_µ", "n*succ" };

		fw_all.write(String.format(header_all, header_values_all));
		fw_avg.write(String.format(header_avg, header_values_avg));
		System.out.print(String.format(header_sys, header_values_sys));
		long timestamp_start = System.currentTimeMillis();
		for (int ts : tsL) {
			for (int tp : tpL) {
				for (int radius : radiusL) {
					for (int nbrTowers : nbrTowersL) {
						for (double lb : lbL) {
							for (double ub : ubL) {
								if (ub < lb)
									continue;

								double withinGateway = 0;
								double[] time_run = new double[runs];
								double[] succRate_run = new double[runs];
								double[] lossRate_run = new double[runs];
								double[] load_run = new double[runs];
								double[] T_put_run = new double[runs];
								for (int i = 0; i < runs; i++) {
									// Reset Global
									Global.reset();
									Data.reset();

									// Init simulation
									double withinGateway_run;
									if (!smart) {
										withinGateway_run = taskAB(ts, tp, radius, nbrTowers);
									} else {
										withinGateway_run = taskCD(ts, tp, radius, nbrTowers, lb, ub);
									}
									withinGateway += withinGateway_run;

									// Run simulation
									Global.advanceWhile(() -> (Data.transmissions < 5000 || Global.time() < ts));

									// Record data
									time_run[i] = Global.time();
									succRate_run[i] = 1.0 * Data.successful_transmissions / Data.transmissions;
									lossRate_run[i] = 1.0 - succRate_run[i];
									load_run[i] = Data.transmissions / Global.time();
									T_put_run[i] = Data.successful_transmissions / Global.time();

									// simulation analysis
									Object[] result_values_all = { succRate_run[i], lossRate_run[i], load_run[i],
											T_put_run[i], time_run[i], runs, ts, tp, radius, nbrTowers, lb, ub,
											withinGateway_run, nbrTowers * succRate_run[i] };
									String output = String.format(result_all, result_values_all);
									fw_all.write(output);
								}

								// combined runs analysis
								withinGateway /= runs;
								Statistic time_stat = new Statistic(time_run);
								Statistic succRate = new Statistic(succRate_run);
								Statistic lossRate = new Statistic(lossRate_run);
								Statistic load = new Statistic(load_run);
								Statistic T_put = new Statistic(T_put_run);

								Object[] result_values_avg = { succRate, lossRate, load, T_put, time_stat.avg,
										time_stat.stdev, runs, ts, tp, radius, nbrTowers, lb, ub, withinGateway,
										nbrTowers * succRate.avg };

								Object[] result_values_sys = { succRate.avg, lossRate.avg, load.avg, T_put.avg,
										time_stat.avg, runs, ts, tp, radius, nbrTowers, lb, ub, withinGateway,
										nbrTowers * succRate.avg };

								fw_avg.write(String.format(result_avg, result_values_avg));
								System.out.printf(result_sys, result_values_sys);
							}
						}
					}
				}
			}
		}
		long timestamp_end = System.currentTimeMillis();
		System.out.printf("Ran %d %s simulations @ %.2f seconds\n",
				runs * tsL.length * tpL.length * radiusL.length * nbrTowersL.length * sims(lbL, ubL),
				smart ? "smart" : "simple", (timestamp_end - timestamp_start) * 1e-3);

		fw_all.close();
		fw_avg.close();
	}
}