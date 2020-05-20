package task2;

import sim.*;

class Queue extends SignalTreater {
	public void TreatSignal(Signal x) {
		switch (x.signalType) {
			case TEST: {
				Global.SendSignal(Signal.Type.TEST, this, 1);
			}
				break;
			default:
				TypeNotImplemented();
				break;
		}
	}
}