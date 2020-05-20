import java.io.IOException;
import task1.Task1;
import task2.Task2;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            String task = args[0];
            if (task.equals("task1")) {
                String propertiesFilePath = args.length > 1 ? args[1] : null;
                Task1.run(propertiesFilePath);
            } else if (task.equals("task2")) {
                Task2.run();
            } else {
                System.out.printf("No such task: %s\n", task);
            }
        } else {
            System.out.println("Please specify task by using the first argument.");
        }
    }
}