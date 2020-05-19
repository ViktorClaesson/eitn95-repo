package task1;

import java.io.*;

public class MainSimulation extends Global {

	public static void main(String[] args) throws IOException {

		Signal actSignal;
		new SignalList();

		QS Q1 = new QS();
		Q1.sendTo = null;

		Gen Generator = new Gen();
		Generator.lambda = 9;
		Generator.sendTo = Q1;

		SignalList.SendSignal(READY, Generator, time);
		SignalList.SendSignal(MEASURE, Q1, time);

		while (time < 100000) {
			actSignal = SignalList.FetchSignal();
			time = actSignal.arrivalTime;
			actSignal.destination.TreatSignal(actSignal);
		}

		System.out.println("Mean number of customers in queuing system: " + 1.0 * Q1.accumulated / Q1.noMeasurements);

	}
}