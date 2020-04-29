package task5;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

//Denna klass �rver Global s� att man kan anv�nda time och signalnamnen utan punktnotation
//It inherits Proc so that we can use time and the signal names without dot notation

public class MainSimulation extends Global {

    public static Proc pickLeast(QS[] input) {
        List<QS> candidates = Arrays.asList(input);
        int min = candidates.stream().mapToInt(qs -> qs.numberInQueue).min().getAsInt();
        List<QS> minList = candidates.stream().filter(a -> a.numberInQueue <= min).collect(Collectors.toList());
        Proc temp = minList.get(random.nextInt(minList.size()));
        return temp;
    }

    public static void resetQueues(QS[] queueList) {
        for (int i = 0; i < queueList.length; i++) {
            queueList[i] = new QS(0.5, mean -> expRandom(mean));
        }
    }

    public static void main(String[] args) throws IOException {
        // SETUP
        Signal actSignal;

        // MAKE THE QUEUES
        QS[] queueList = new QS[5];

        List<Double> arrivalTimes = Arrays.asList(new Double[] { 0.11, 0.12, 0.15, 2.0 });

        System.out.println(String.format("%12s|%12s|%7s|%7s|%7s|%12s", "Arrival Mean", "Algorithm", "Mean L", "Mean W",
                "Lambda", "Little's Law"));

        for (double meanA : arrivalTimes) {
            // MAKE THE GENERATORS
            List<Gen> generators = new ArrayList<Gen>();

            Gen RandomGenerator = new Gen("Random", meanA, queueList, ql -> ql[random.nextInt(ql.length)]);
            Gen RoundRobinGenerator = new Gen("Round Robin", meanA, queueList, new Function<Proc[], Proc>() {
                private int counter = 0;

                @Override
                public Proc apply(Proc[] ql) {
                    Proc temp = ql[counter];
                    counter = (counter + 1) % ql.length;
                    return temp;
                }
            });
            Gen LeastGenerator = new Gen("Least in Q", meanA, queueList, ql -> pickLeast((QS[]) ql));

            generators.add(RandomGenerator);
            generators.add(RoundRobinGenerator);
            generators.add(LeastGenerator);

            for (Gen gen : generators) {
                resetGlobal();

                // RESET THE QUEUES
                resetQueues(queueList);

                // MAKE THE SIGNAL LIST
                new SignalList();

                // START THE SIGNALS
                SignalList.SendSignal(READY, gen, time);
                for (QS q : queueList) {
                    SignalList.SendSignal(MEASURE, q, time);
                }

                // SIMULATE
                while (time < 100000) {
                    actSignal = SignalList.FetchSignal();
                    time = actSignal.arrivalTime;
                    actSignal.destination.TreatSignal(actSignal);
                }

                // PRINT RESULTS
                double meanL = Arrays.asList(queueList).stream()
                        .mapToDouble(q -> 1.0 * q.accumulated / q.noMeasurements).sum();
                double meanW = Arrays.asList(queueList).stream().mapToDouble(q -> 1.0 * q.serviceTime / q.arrived).sum()
                        / queueList.length;
                double lambda = Arrays.asList(queueList).stream().mapToDouble(q -> q.arrived).sum() / time;

                System.out.println(String.format("%12.2f|%12s|%7.2f|%7.2f|%7.2f|%12.2f", meanA, gen.name, meanL, meanW,
                        lambda, lambda * meanW / meanL));
            }
        }
    }
}