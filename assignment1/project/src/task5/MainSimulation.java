package task5;

import java.io.*;
import java.util.Queue;
import java.util.List;
import java.util.Arrays;

//Denna klass �rver Global s� att man kan anv�nda time och signalnamnen utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation

public class MainSimulation extends Global {

    public static Proc pickLeast(QS[] input) {
        List<QS> candidates = Arrays.asList(input);

        candidates.sort((a, b) -> b.noMeasurements - a.noMeasurements);
        candidates.removeIf(a -> a.noMeasurements > candidates.get(0).noMeasurements);
        return candidates.get(random.nextInt(candidates.size()));
    }

    public static void main(String[] args) throws IOException {

        // Signallistan startas och actSignal deklareras. actSignal �r den senast
        // utplockade signalen i huvudloopen nedan.
        // The signal list is started and actSignal is declaree. actSignal is the latest
        // signal that has been fetched from the
        // signal list in the main loop below.

        Signal actSignal;
        new SignalList();

        // H�r nedan skapas de processinstanser som beh�vs och parametrar i dem ges
        // v�rden.
        // Here process instances are created (two queues and one generator) and their
        // parameters are given values.

        QS[] queueList = new QS[] { new QS(mean -> expRandom(mean), 0.5), new QS(mean -> expRandom(mean), 0.5),
                new QS(mean -> expRandom(mean), 0.5), new QS(mean -> expRandom(mean), 0.5),
                new QS(mean -> expRandom(mean), 0.5) };

        int counter = 0;
        Gen RandomGenerator = new Gen(9, queueList, ql -> ql[random.nextInt(ql.length)]);
        Gen RoundRobinGenerator = new Gen(9, queueList, ql -> ql[counter++ % ql.length]);
        Gen LeastGenerator = new Gen(9, queueList, ql -> pickLeast((QS[]) ql));
        // Generator ska generera nio kunder per sekund //Generator shall generate 9
        // customers per second
        // De genererade kunderna ska skickas till k�systemet QS // The generated
        // customers shall be sent to Q1

        // H�r nedan skickas de f�rsta signalerna f�r att simuleringen ska komma ig�ng.
        // To start the simulation the first signals are put in the signal list

        SignalList.SendSignal(READY, RandomGenerator, time);
        for (QS q : queueList) {
            SignalList.SendSignal(MEASURE, q, time);
        }

        // Detta �r simuleringsloopen:
        // This is the main loop

        while (time < 100000) {
            actSignal = SignalList.FetchSignal();
            time = actSignal.arrivalTime;
            actSignal.destination.TreatSignal(actSignal);
        }

        // Slutligen skrivs resultatet av simuleringen ut nedan:
        // Finally the result of the simulation is printed below:

        System.out.println("Mean number of customers in queuing system: " + 1.0 * Q1.accumulated / Q1.noMeasurements);

    }
}