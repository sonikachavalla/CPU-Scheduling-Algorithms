import java.io.*;
import java.util.*;

public class RoundRobin {
    private static final int TIME_QUANTUM = 10;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Error: the command format should be- java AlgorithmName schedule.txt");
            return;
        }

        String ScheduleFName = args[0];

        try {
            List<Task> tsk = readTasksFromFile(ScheduleFName);
            execRoundRobin(tsk);
        } catch (IOException e) {
            System.out.println("Error reading the file: " + ScheduleFName);
            e.printStackTrace();
        }
    }

    public static List<Task> readTasksFromFile(String ScheduleFName) throws IOException {
        List<Task> tsk = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(ScheduleFName));
        String tline;

        while ((tline = reader.readLine()) != null) {
            tline = tline.trim().replaceAll(",\\s*$", "");
            String[] parts = tline.split(",");
            if (parts.length == 3) {
                String taskName = parts[0].trim();
                int priority = Integer.parseInt(parts[1].trim().replaceAll("[^\\d]", ""));
                int burstTime = Integer.parseInt(parts[2].trim().replaceAll("[^\\d]", ""));
                tsk.add(new Task(taskName, priority, burstTime));
            } else {
                System.out.println("Invalid format in line: " + tline);
            }
        }
        reader.close();
        return tsk;
    }

    public static void execRoundRobin(List<Task> tsk) {
        int currentTime = 0;
        int ttlWaitTime = 0, ttlTurnaroundTime = 0;

        while (!tsk.isEmpty()) {
            Iterator<Task> iterator = tsk.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                int timeSlice = Math.min(task.burstTime, TIME_QUANTUM);
                task.burstTime -= timeSlice;
                currentTime += timeSlice;

                if (task.burstTime == 0) {
                    int turnaroundTime = currentTime;
                    int waitTime = turnaroundTime - timeSlice;
                    System.out.println("Round Robin Scheduling Algorithm-");
                    System.out.println("Task: " + task.name + ", Wait Time: " + waitTime + ", Turnaround Time: " + turnaroundTime);
                    ttlWaitTime += waitTime;
                    ttlTurnaroundTime += turnaroundTime;
                    iterator.remove();
                }
            }
        }

        System.out.println("RR Average Wait Time: " + (ttlWaitTime / (double) tsk.size()));
        System.out.println("RR Average Turnaround Time: " + (ttlTurnaroundTime / (double) tsk.size()));
    }
}