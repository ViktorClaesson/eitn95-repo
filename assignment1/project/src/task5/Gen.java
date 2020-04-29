package task5;

import java.util.function.Function;

//Denna klass �rver Proc, det g�r att man kan anv�nda time och signalnamn utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation 

class Gen extends Proc {

	// Generatorn har tv� parametrar:
	// There are two parameters:
	public Proc[] sendTo; // Anger till vilken process de genererade kunderna ska skickas //Where to send
							// customers
	public double lambda; // Hur m�nga per sekund som ska generas //How many to generate per second

	public Function<Proc[], Proc> picker;

	public Gen(double lambda, Proc[] sendTo, Function<Proc[], Proc> picker) {
		this.lambda = lambda;
		this.sendTo = sendTo;
		this.picker = picker;
	}

	// H�r nedan anger man vad som ska g�ras n�r en signal kommer //What to do when
	// a signal arrives
	public void TreatSignal(Signal x) {
		switch (x.signalType) {
			case READY:
				SignalList.SendSignal(ARRIVAL, picker.apply(sendTo), time);
				SignalList.SendSignal(READY, this, time + (2.0 / lambda) * random.nextDouble());
				break;
		}
	}
}