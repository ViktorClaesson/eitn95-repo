package task2;

import sim.*;

class Square extends Proc {

	private int x, y;

	public Square(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
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