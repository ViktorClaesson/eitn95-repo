package task5;

import java.util.Random;

public class Global {
    public static final int ARRIVAL = 1, READY = 2, MEASURE = 3;
    public static double time = 0;

    public static Random random = new Random();

    public static void resetGlobal() {
        time = 0;
        random = new Random();
    }

    public static double expRandom(double mean) {
        return -mean * Math.log(1 - random.nextDouble());
    }
}
