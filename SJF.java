import java.io.*;
import java.util.*;

public class SJF {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Error: the command format should be- java AlgorithmName schedule.txt");
            return;
        }

        String ScheduleFName = args[0];

        try {
            List<Task> tsk = readTasksFromFile(ScheduleFName);
            runSJF(tsk);
        } catch (IOException e) {
            System.out.println("Error reading the file: " + ScheduleFName);
            e.printStackTrace();
        }
    }

    public static List<Task> readTasksFromFile(String ScheduleFName) throws IOException {
        List<Task> tsk = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(ScheduleFName));
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim().replaceAll(",\\s*$", "");
            String[] parts = line.split(",");
            if (parts.length == 3) {
                String taskName = parts[0].trim();
                int priority = Integer.parseInt(parts[1].trim().replaceAll("[^\\d]", ""));
                int burstTime = Integer.parseInt(parts[2].trim().replaceAll("[^\\d]", ""));
                tsk.add(new Task(taskName, priority, burstTime));
            } else {
                System.out.println("Invalid format in line: " + line);
            }
        }
        reader.close();
        return tsk;
    }

    public static void runSJF(List<Task> tsk) {
        tsk.sort(Comparator.comparingInt(task -> task.burstTime));
        int currentTime = 0;
        int ttlWaitTime = 0, ttlTurnaroundTime = 0;

        for (Task task : tsk) {
            int waitTime = currentTime;
            int turnaroundTime = waitTime + task.burstTime;
            currentTime += task.burstTime;
            System.out.println("Shortest Job First Scheduling Algorithm-");
            System.out.println("Task: " + task.name + ", Wait Time: " + waitTime + ", Turnaround Time: " + turnaroundTime);

            ttlWaitTime += waitTime;
            ttlTurnaroundTime += turnaroundTime;
        }

        System.out.println("SJF Average Wait Time: " + ( ttlWaitTime/ (double) tsk.size()));
        System.out.println("SJF Average Turnaround Time: " + (ttlTurnaroundTime / (double) tsk.size()));
    }
}