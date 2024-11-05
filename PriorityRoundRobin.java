import java.io.*;
import java.util.*;

public class PriorityRoundRobin {
    private static final int TIME_QUANTUM = 10;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java PriorityRoundRobin <schedule_file>");
            return;
        }

        String ScheduleFName = args[0];

        try {
            List<Task> tsk = readTasksFromFile(ScheduleFName);
            execPriorityRoundRobin(tsk);
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
            String[] details = line.split(",");
            if (details.length == 3) {
                String taskName = details[0].trim();
                int priority = Integer.parseInt(details[1].trim().replaceAll("[^\\d]", ""));
                int burstTime = Integer.parseInt(details[2].trim().replaceAll("[^\\d]", ""));
                tsk.add(new Task(taskName, priority, burstTime));
            } else {
                System.out.println("Invalid format in line: " + line);
            }
        }
        reader.close();
        return tsk;
    }

    public static void execPriorityRoundRobin(List<Task> tsk) {
        Map<Integer, List<Task>> priorityGroups = new TreeMap<>();
        for (Task task : tsk) {
            priorityGroups.computeIfAbsent(task.priority, k -> new ArrayList<>()).add(task);
        }

        int currentTime = 0;
        int ttlWaitingTime = 0, ttlTurnaroundTime = 0;

        for (List<Task> grp : priorityGroups.values()) {
            while (!grp.isEmpty()) {
                Iterator<Task> iterator = grp.iterator();
                while (iterator.hasNext()) {
                    Task task = iterator.next();
                    int timeSlice = Math.min(task.burstTime, TIME_QUANTUM);
                    task.burstTime -= timeSlice;
                    currentTime += timeSlice;

                    if (task.burstTime == 0) {
                        int turnaroundTime = currentTime;
                        int waitTime = turnaroundTime - task.burstTime;

                        System.out.println("Task: " + task.name + ", Wait Time: " + waitTime + ", Turnaround Time: " + turnaroundTime);
                        ttlWaitingTime += waitTime;
                        ttlTurnaroundTime += turnaroundTime;
                        iterator.remove();
                    }
                }
            }
        }

        System.out.println("Average Wait Time: " + (ttlWaitingTime / (double) tsk.size()));
        System.out.println("Average Turnaround Time: " + (ttlTurnaroundTime / (double) tsk.size()));
    }
}