package sim;

public abstract class Proc {
	public abstract void TreatSignal(Signal x);

	public void TypeNotImplemented() throws RuntimeException {
		throw new RuntimeException("Signal.Type that is not implemented for the SignalTreater recieved.");
	}
}
