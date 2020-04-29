package task5;

import java.util.function.Function;
import java.util.function.DoubleFunction;

//Denna klass �rver Proc, det g�r att man kan anv�nda time och signalnamn utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation 

class Gen extends Proc {

	// Generatorn har tv� parametrar:
	// There are two parameters:
	public Proc[] sendTo; // Anger till vilken process de genererade kunderna ska skickas //Where to send
							// customers
	public double mean; // Hur m�nga per sekund som ska generas //How many to generate per second

	public Function<Proc[], Proc> procPicker;

	public DoubleFunction<Double> timePicker;

	public Gen(double mean, Proc[] sendTo, Function<Proc[], Proc> procPicker) {
		this.mean = mean;
		this.sendTo = sendTo;
		this.procPicker = procPicker;
	}

	// H�r nedan anger man vad som ska g�ras n�r en signal kommer //What to do when
	// a signal arrives
	public void TreatSignal(Signal x) {
		switch (x.signalType) {
			case READY:
				SignalList.SendSignal(ARRIVAL, procPicker.apply(sendTo), time);
				SignalList.SendSignal(READY, this, time + 2 * random.nextDouble() * mean);
				break;
		}
	}
}