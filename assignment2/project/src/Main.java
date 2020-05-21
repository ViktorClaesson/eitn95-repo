import java.io.IOException;
import task1.Task1;
import task2.Task2;

public class Main {
    public static void main(String[] args) throws IOException {
        long timestamp_start = System.currentTimeMillis();
        if (args.length > 0) {
            String task = args[0];
            if (task.equals("task1")) {
                String propertiesFilePath = args.length > 1 ? args[1] : null;
                Task1.run("src/task1/config/" + propertiesFilePath);
            } else if (task.equals("task2")) {
                Task2.run(20, 20, 2, 1000);
            } else {
                System.out.printf("No such task: %s\n", task);
            }
        } else {
            System.out.println("Please specify task by using the first argument.");
        }
        long timestamp_end = System.currentTimeMillis();
        System.out.printf("Main runtime: %.2f seconds\n", (timestamp_end - timestamp_start) * 1e-3);
    }
}