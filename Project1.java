import java.io.*;
import java.util.*;
class Task {
    String name;
    int priority;
    int cpuBurst;

    public Task(String name, int priority, int cpuBurst) {
        this.name = name;
        this.priority = priority;
        this.cpuBurst = cpuBurst;
    }

    @Override
    public String toString() {
        return name + " (Priority: " + priority + ", Burst: " + cpuBurst + "ms)";
    }
}

class FCFS {
    public void schedule(List<Task> tasks) {
        int time = 0;
        for (Task task : tasks) {
            System.out.println("Time " + time + ": Executing " + task);
            time += task.cpuBurst;
        }
        System.out.println("FCFS scheduling complete.\n");
    }
}

class SJF {
    public void schedule(List<Task> tasks) {
        tasks.sort(Comparator.comparingInt(task -> task.cpuBurst)); // Sort by CPU burst time
        int time = 0;
        for (Task task : tasks) {
            System.out.println("Time " + time + ": Executing " + task);
            time += task.cpuBurst;
        }
        System.out.println("SJF scheduling complete.\n");
    }
}

class PriorityScheduling {
    public void schedule(List<Task> tasks) {
        tasks.sort((task1, task2) -> Integer.compare(task2.priority, task1.priority)); // Sort by priority
        int time = 0;
        for (Task task : tasks) {
            System.out.println("Time " + time + ": Executing " + task);
            time += task.cpuBurst;
        }
        System.out.println("Priority scheduling complete.\n");
    }
}

class RoundRobin {
    private static final int TIME_QUANTUM = 10;

    public void schedule(List<Task> tasks) {
        Queue<Task> queue = new LinkedList<>(tasks);
        int time = 0;
        
        while (!queue.isEmpty()) {
            Task task = queue.poll();
            if (task.cpuBurst > TIME_QUANTUM) {
                System.out.println("Time " + time + ": Executing " + task + " for " + TIME_QUANTUM + "ms");
                time += TIME_QUANTUM;
                task.cpuBurst -= TIME_QUANTUM;
                queue.add(task);
            } else {
                System.out.println("Time " + time + ": Executing " + task + " for " + task.cpuBurst + "ms");
                time += task.cpuBurst;
            }
        }
        System.out.println("Round Robin scheduling complete.\n");
    }
}

class PriorityWithRR {
    private static final int TIME_QUANTUM = 10;

    public void schedule(List<Task> tasks) {
        tasks.sort((task1, task2) -> Integer.compare(task2.priority, task1.priority));
        Queue<Task> queue = new LinkedList<>(tasks);
        int time = 0;

        while (!queue.isEmpty()) {
            Task task = queue.poll();
            if (task.cpuBurst > TIME_QUANTUM) {
                System.out.println("Time " + time + ": Executing " + task + " for " + TIME_QUANTUM + "ms");
                time += TIME_QUANTUM;
                task.cpuBurst -= TIME_QUANTUM;
                queue.add(task);
            } else {
                System.out.println("Time " + time + ": Executing " + task + " for " + task.cpuBurst + "ms");
                time += task.cpuBurst;
            }
        }
        System.out.println("Priority with Round Robin scheduling complete.\n");
    }
}

class Scheduler {
    public List<Task> readTasksFromFile(String filename) throws IOException {
        List<Task> tasks = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",\\s*");
            String name = parts[0].trim();
            int priority = Integer.parseInt(parts[1].trim());
            int cpuBurst = Integer.parseInt(parts[2].trim());
            tasks.add(new Task(name, priority, cpuBurst));
        }
        reader.close();
        return tasks;
    }

    public void executeScheduling(String algorithm, String filename) throws IOException {
        List<Task> tasks = readTasksFromFile(filename);

        switch (algorithm) {
            case "FCFS":
                new FCFS().schedule(tasks);
                break;
            case "SJF":
                new SJF().schedule(tasks);
                break;
            case "Priority":
                new PriorityScheduling().schedule(tasks);
                break;
            case "RR":
                new RoundRobin().schedule(tasks);
                break;
            case "PriorityRR":
                new PriorityWithRR().schedule(tasks);
                break;
            default:
                System.out.println("Unknown algorithm.");
        }
    }
}

public class Project1 {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("no. of arguments passed is incorrect ");
            return;
        }

        String algorithm = args[0];
        String filename = args[1];

        Scheduler scheduler = new Scheduler();
        try {
            scheduler.executeScheduling(algorithm, filename);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
