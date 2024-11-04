import java.io.*;
import java.util.*;

public class FCFS {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java FCFS <schedule_file>");
            return;
        }

        String fileName = args[0];

        try {
            List<Task> tasks = readTasksFromFile(fileName);
            runFCFS(tasks);
        } catch (IOException e) {
            System.out.println("Error reading the file: " + fileName);
            e.printStackTrace();
        }
    }

    public static List<Task> readTasksFromFile(String fileName) throws IOException {
        List<Task> tasks = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim().replaceAll(",\\s*$", "");
            String[] parts = line.split(",");
            if (parts.length == 3) {
                String taskName = parts[0].trim();
                int priority = Integer.parseInt(parts[1].trim().replaceAll("[^\\d]", ""));
                int burstTime = Integer.parseInt(parts[2].trim().replaceAll("[^\\d]", ""));
                tasks.add(new Task(taskName, priority, burstTime));
            } else {
                System.out.println("Invalid format in line: " + line);
            }
        }
        reader.close();
        return tasks;
    }

    public static void runFCFS(List<Task> tasks) {
        int totalWaitTime = 0, totalTurnaroundTime = 0;
        int currentTime = 0;

        for (Task task : tasks) {
            int waitTime = currentTime;
            int turnaroundTime = waitTime + task.burstTime;
            currentTime += task.burstTime;

            System.out.println("Task: " + task.name + ", Wait Time: " + waitTime + ", Turnaround Time: " + turnaroundTime);

            totalWaitTime += waitTime;
            totalTurnaroundTime += turnaroundTime;
        }

        System.out.println("Average Wait Time: " + (totalWaitTime / (double) tasks.size()));
        System.out.println("Average Turnaround Time: " + (totalTurnaroundTime / (double) tasks.size()));
    }
}
