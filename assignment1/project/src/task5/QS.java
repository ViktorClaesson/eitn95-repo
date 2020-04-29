package task5;

import java.util.function.DoubleFunction;

// This class defines a simple queuing system with one server. It inherits Proc so that we can use time and the
// signal names without dot notation
class QS extends Proc {
	public int numberInQueue, accumulated, noMeasurements;
	public Proc sendTo;

	DoubleFunction<Double> timeFunc;
	double mean;

	public QS(DoubleFunction<Double> timeFunc, double mean) {
		this.timeFunc = timeFunc;
		this.mean = mean;
	}

	public void TreatSignal(Signal x) {
		switch (x.signalType) {
			case ARRIVAL:
				numberInQueue++;
				if (numberInQueue == 1) {
					SignalList.SendSignal(READY, this, time + timeFunc.apply(mean));
				}
				break;
			case READY:
				numberInQueue--;
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