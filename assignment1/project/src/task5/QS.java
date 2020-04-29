package task5;

import java.util.function.DoubleFunction;
import java.util.LinkedList;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class QS extends Proc {
	public int numberInQueue, arrived, serviceTime, accumulated, noMeasurements;
	private LinkedList<Double> arriveTimes = new LinkedList<>();
	public Proc sendTo;

	DoubleFunction<Double> timeFunc;
	double mean;

	public QS(double mean, DoubleFunction<Double> timeFunc) {
		this.timeFunc = timeFunc;
		this.mean = mean;
	}

	public void TreatSignal(Signal x) {
		switch (x.signalType) {
			case ARRIVAL:
				arrived++;
				arriveTimes.add(time);
				numberInQueue++;
				if (numberInQueue == 1) {
					SignalList.SendSignal(READY, this, time + timeFunc.apply(mean));
				}
				break;
			case READY:
				numberInQueue--;
				serviceTime += (time - arriveTimes.pop());
				if (sendTo != null) {
					SignalList.SendSignal(ARRIVAL, sendTo, time);
				}
				if (numberInQueue > 0) {
					SignalList.SendSignal(READY, this, time + timeFunc.apply(mean));
				}
				break;
			case MEASURE:
				noMeasurements++;
				accumulated = accumulated + numberInQueue;
				SignalList.SendSignal(MEASURE, this, time + expRandom(1));
				break;
		}
	}
}