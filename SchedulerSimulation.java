import java.util.*;

class Process {
    String name;
    int arrivalTime;
    int burstTime;
    int priority;
    int remainingTime;
    int waitingTime = 0;
    int turnaroundTime = 0;
    double fcaiFactor;
    int quantum;
    public Process(String name,int quantum, int arrivalTime, int burstTime, int priority) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
        this.quantum = quantum;
    }
    void calculateFCAIFactor(double V1, double V2) {
        this.fcaiFactor = (10 - priority) + (arrivalTime / V1) + (remainingTime / V2);
    }
}

public class SchedulerSimulation {

    // 1. Non-Preemptive Priority Scheduling
    public static void nonPreemptivePriority(List<Process> processes) {
        processes.sort((p1, p2) -> p1.priority != p2.priority
                ? Integer.compare(p1.priority, p2.priority)
                : Integer.compare(p1.arrivalTime, p2.arrivalTime));

        int currentTime = 0;
        for (Process p : processes) {
            currentTime = Math.max(currentTime, p.arrivalTime);
            p.waitingTime = currentTime - p.arrivalTime;
            currentTime += p.burstTime;
            p.turnaroundTime = currentTime - p.arrivalTime;
            System.out.println(p.name + " executed. WT: " + p.waitingTime + ", TAT: " + p.turnaroundTime);
        }
    }

    // 2. Non-Preemptive Shortest Job First (SJF) Scheduling
    public static void nonPreemptiveSJF(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0;
        List<Process> readyQueue = new ArrayList<>();

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                readyQueue.add(processes.remove(0));
            }
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }
            readyQueue.sort(Comparator.comparingInt(p -> p.burstTime));
            Process nextProcess = readyQueue.remove(0);
            nextProcess.waitingTime = currentTime - nextProcess.arrivalTime;
            currentTime += nextProcess.burstTime;
            nextProcess.turnaroundTime = currentTime - nextProcess.arrivalTime;
            System.out.println(nextProcess.name + " executed. WT: " + nextProcess.waitingTime + ", TAT: " + nextProcess.turnaroundTime);
        }
    }

    // 3. Preemptive Shortest Remaining Time First (SRTF) Scheduling
    public static void preemptiveSRTF(List<Process> processes) {
        int currentTime = 0;
        while (!processes.isEmpty()) {
            final int currentTimeCopy = currentTime;  // Make a final copy of currentTime

            Process nextProcess = processes.stream()
                    .filter(p -> p.arrivalTime <= currentTimeCopy)
                    .min(Comparator.comparingInt(p -> p.remainingTime))
                    .orElse(null);

            if (nextProcess == null) {
                currentTime++;
                continue;
            }

            nextProcess.remainingTime--;
            currentTime++;

            if (nextProcess.remainingTime == 0) {
                nextProcess.waitingTime = currentTime - nextProcess.arrivalTime - nextProcess.burstTime;
                nextProcess.turnaroundTime = currentTime - nextProcess.arrivalTime;
                System.out.println(nextProcess.name + " completed. WT: " + nextProcess.waitingTime + ", TAT: " + nextProcess.turnaroundTime);
                processes.remove(nextProcess);
            }
        }
    }

     public static void fcaiScheduling(List<Process> processes) {

         Queue<Process> ReadyQueue = new LinkedList<>();
         int currentTime = 0;
         Process process = null;

         System.out.println("\nExecution Timeline:");

         while (!processes.isEmpty() || !ReadyQueue.isEmpty()) {
             // Add processes to the Ready Queue if they've arrived
             Iterator<Process> it = processes.iterator();
             while (it.hasNext()) {
                 Process p = it.next();
                 if (p.arrivalTime <= currentTime) {
                     ReadyQueue.add(p);
                     it.remove();
                 }
             }

             if (ReadyQueue.isEmpty()) {
                 currentTime++;
                 continue;
             }

             // Fetch the next process
             if (process == null) {
                 process = ReadyQueue.poll();
             }

             int curr = 0;
             int Quantum40Percent = (int) Math.ceil(process.quantum * 0.4); // First 40% is non-preemptive
             boolean preempted = false;
             boolean done = false;
             String msg = "";

             // Execute process while meeting quantum and remaining burst conditions
             while (curr < process.quantum && !preempted && !done) {
                 curr++;
                 currentTime++;
                 process.remainingTime--;

                 // If the process completes during execution
                 if (process.remainingTime == 0) {
                     done = true;
                     msg = "Completed";
                     break;
                 }

                 // Check if preemption conditions are met at 40% of the quantum
                 if (curr >= Quantum40Percent) {
                     preempted = checkPreemption(ReadyQueue, process);
                 }
             }

             if (!done) {
                 msg = preempted ? "Preempted" : "Finished Quantum";
             }

             // Print process execution details
             System.out.println("Time " + (currentTime - curr) + "-" + currentTime + ": " + process.name +
                     " executed for " + curr + " units (" + msg + ")");

             // Update process details after execution
             updateProcess(process, preempted,processes);

             // Handle process states after execution
             if (done) {
                 // If process completed, calculate and print stats
                 process.turnaroundTime = currentTime - process.arrivalTime;
                 process.waitingTime = process.turnaroundTime - process.burstTime;

                 System.out.println(process.name + " completed. TAT: " + process.turnaroundTime +
                         ", WT: " + process.waitingTime);

                 process = null;
             } else {
                 // If preempted, requeue the process
                 if (preempted) {
                     ReadyQueue.add(process);
                     process = null;
                 }
             }
         }
     }

     // Method to update process parameters
     public static void updateProcess(Process process, boolean preempted, List<Process> processes) {
         double V1 = Collections.max(processes, Comparator.comparingInt(p -> p.arrivalTime)).arrivalTime / 10.0;
         double V2 = Collections.max(processes, Comparator.comparingInt(p -> p.burstTime)).burstTime / 10.0;

         if (!preempted) {
             process.quantum += 2; // Increase quantum time if not preempted
         } else {
             process.quantum += (process.quantum - (process.burstTime - process.remainingTime));
         }

         System.out.println("Quantum = " + process.quantum);
         process.calculateFCAIFactor(V1, V2); // Update FCAI factor
         System.out.println("FCAI = " + process.fcaiFactor);
     }

     // Method to check for preemption
     public static boolean checkPreemption(Queue<Process> ReadyQueue, Process currentProcess) {
         for (Process queuedProcess : ReadyQueue) {
             if (queuedProcess.fcaiFactor < currentProcess.fcaiFactor) {
                 return true; // Preempt if a process with lower FCAI is found
             }
         }
         return false;
     }




     public static void main(String[] args) {
         Scanner scanner = new Scanner(System.in);

         // Get the number of processes
         System.out.print("Enter the number of processes: ");
         int numProcesses = Integer.parseInt(scanner.nextLine().trim()); // Read full line and parse

         // Get the Round Robin Time Quantum
         System.out.print("Enter the Round Robin Time Quantum: ");
         int roundRobinQuantum = Integer.parseInt(scanner.nextLine().trim());

         // Get the Context Switching Time
         System.out.print("Enter the Context Switching Time: ");
         int contextSwitchingTime = Integer.parseInt(scanner.nextLine().trim());

         // Create a list of processes
         List<Process> processes = new ArrayList<>();

         for (int i = 0; i < numProcesses; i++) {
             System.out.println("\nEnter details for Process " + (i + 1) + ":");

             System.out.print("Process Name: ");
             String name = scanner.nextLine().trim();

             System.out.print("Process Color (Graphical Representation): ");
             String color = scanner.nextLine().trim();

             System.out.print("Arrival Time: ");
             int arrivalTime = Integer.parseInt(scanner.nextLine().trim()); // Clear buffer after this line

             System.out.print("Burst Time: ");
             int burstTime = Integer.parseInt(scanner.nextLine().trim()); // Clear buffer after this line

             System.out.print("Priority (1-10, lower number = higher priority): ");
             int priority = Integer.parseInt(scanner.nextLine().trim()); // Clear buffer after this line

             processes.add(new Process(name, roundRobinQuantum, arrivalTime, burstTime, priority));
         }

         // Execute FCAI Scheduling
         System.out.println("\nExecuting FCAI Scheduling...");
         SchedulerSimulation.fcaiScheduling(processes);

         scanner.close();
     }

 }

/*
4
4
1
P1
red
0
17
4
P2
blue
3
6
9
P3
green
4
10
3
P4
yellow
29
4
10


 */


