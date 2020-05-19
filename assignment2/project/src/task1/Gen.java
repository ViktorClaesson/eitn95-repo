package task1;

import java.util.*;

class Gen extends Proc {

	Random slump = new Random();

	public Proc sendTo;
	public double lambda;

	public void TreatSignal(Signal x) {
		switch (x.signalType) {
			case READY: {
				SignalList.SendSignal(ARRIVAL, sendTo, time);
				SignalList.SendSignal(READY, this, time + (2.0 / lambda) * slump.nextDouble());
			}
				break;
		}
	}
}