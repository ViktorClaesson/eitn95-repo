package util;

public class Statistic {
    public final double avg, stdev, ci_high, ci_low;

    public Statistic(double[] values) {
        double avg_tmp = 0;
        for (double v : values) {
            avg_tmp += v;
        }
        avg_tmp /= values.length;

        double stdev_tmp = 0;
        if (values.length > 1) {
            for (double v : values) {
                stdev_tmp += Math.pow(v - avg_tmp, 2);
            }
            stdev_tmp /= (values.length - 1);
            stdev_tmp = Math.sqrt(stdev_tmp);
        }

        avg = avg_tmp;
        stdev = stdev_tmp;
        ci_low = avg_tmp - 1.96 * stdev_tmp / Math.sqrt(values.length);
        ci_high = avg_tmp + 1.96 * stdev_tmp / Math.sqrt(values.length);
    }

    public String interval() {
        return String.format(" %6.2f - %6.2f", ci_low, ci_high);
    }

    @Override
    public String toString() {
        return String.format("%8.4f\t%8.4f", avg, stdev);
    }
}