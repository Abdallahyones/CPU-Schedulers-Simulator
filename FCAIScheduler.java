//import java.awt.*;
//import javax.swing.*;
//import java.util.*;
//
////class Process {
////    int pid; // Process ID
////    int burstTime; // Burst Time
////    int waitingTime; // Waiting Time
////    int turnaroundTime; // Turnaround Time
////    String name; // Process Name
////
////    public Process(int pid, int burstTime, String name) {
////        this.pid = pid;
////        this.burstTime = burstTime;
////        this.name = name;
////        this.waitingTime = 0;
////        this.turnaroundTime = 0;
////    }
////}
//
//public class FCAIScheduler {
//    private static ArrayList<Integer> quantumHistory = new ArrayList<>();
//    private static ArrayList<String> executionOrder = new ArrayList<>();
//
//    public static void fcaiScheduling(Process[] processes, int quantum) {
//        int time = 0; // Current time
//        int n = processes.length;
//        int[] remainingBurstTime = new int[n];
//        quantumHistory.add(quantum); // Log initial quantum
//
//        // Initialize remaining burst times
//        for (int i = 0; i < n; i++) {
//            remainingBurstTime[i] = processes[i].burstTime;
//        }
//
//        while (true) {
//            boolean done = true;
//
//            for (int i = 0; i < n; i++) {
//                if (remainingBurstTime[i] > 0) {
//                    done = false;
//
//                    // Process execution
//                    executionOrder.add(processes[i].name);
//                    if (remainingBurstTime[i] > quantum) {
//                        time += quantum;
//                        remainingBurstTime[i] -= quantum;
//
//                        // Quantum update logic (optional)
//                        quantum += 2; // Example: increasing quantum
//                        quantumHistory.add(quantum);
//                    } else {
//                        time += remainingBurstTime[i];
//                        processes[i].waitingTime = time - processes[i].burstTime;
//                        remainingBurstTime[i] = 0;
//                    }
//                }
//            }
//
//            if (done) break;
//        }
//
//        // Calculate turnaround times
//        for (Process process : processes) {
//            process.turnaroundTime = process.waitingTime + process.burstTime;
//        }
//    }
//
//    public static void printStatistics(Process[] processes) {
//        System.out.println("Processes Execution Order: " + executionOrder);
//        System.out.println("\nQuantum Time Updates: " + quantumHistory);
//
//        int totalWT = 0, totalTAT = 0;
//        System.out.printf("\n%-10s %-10s %-10s %-10s\n", "PID", "Burst", "Waiting", "Turnaround");
//        for (Process process : processes) {
//            System.out.printf("%-10d %-10d %-10d %-10d\n", process.pid, process.burstTime, process.waitingTime, process.turnaroundTime);
//            totalWT += process.waitingTime;
//            totalTAT += process.turnaroundTime;
//        }
//
//        System.out.println("\nAverage Waiting Time: " + (double) totalWT / processes.length);
//        System.out.println("Average Turnaround Time: " + (double) totalTAT / processes.length);
//    }
//
//    // Graphical Representation
//    public static void createGraph(Process[] processes) {
//        JFrame frame = new JFrame("CPU Scheduling Graph");
//        frame.setSize(800, 400);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        JPanel panel = new JPanel() {
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                int x = 50; // Start position
//                int y = 50;
//                int width = 50;
//                int height = 50;
//
//                for (String process : executionOrder) {
//                    g.setColor(new Color((int) (Math.random() * 0x1000000))); // Random color
//                    g.fillRect(x, y, width, height);
//                    g.setColor(Color.BLACK);
//                    g.drawString(process, x + 10, y + 25);
//                    x += width;
//                }
//            }
//        };
//
//        frame.add(panel);
//        frame.setVisible(true);
//    }
//
//    public static void main(String[] args) {
//        // Sample Processes
//        Process[] processes = {
//                new Process(1, 10, "P1"),
//                new Process(2, 5, "P2"),
//                new Process(3, 8, "P3")
//        };
//
//        int quantum = 2; // Initial quantum
//
//        fcaiScheduling(processes, quantum);
//        printStatistics(processes);
//        createGraph(processes);
//    }
//}
