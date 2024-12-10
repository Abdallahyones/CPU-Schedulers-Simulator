import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SRTF {

    static void calcwaitingtime(Process processarray[], int order[], int n, int switchingTime) {
        int done = 0, curtime = 0, mn = 20000, shortestID = 0;
        int[] bt = new int[n];
        int prevProcessID = -1;
        List<Integer> executionTimeline = new ArrayList<>(); // Track the execution timeline

        for (int i = 0; i < n; i++) {
            bt[i] = processarray[i].burst;
        }

        while (done != n) {
            mn = 200000;

            // Get shortest process
            for (int i = 0; i < n; i++) {
                int starvationfact = (curtime - processarray[i].arrivalTime) / 50;
                if (starvationfact < 0) starvationfact = 0;

                if (processarray[i].arrivalTime <= curtime && bt[i] - starvationfact < mn && bt[i] > 0) {
                    mn = bt[i] - starvationfact;
                    shortestID = i;
                }
            }

            if (prevProcessID != -1 && prevProcessID != shortestID) {
                curtime += switchingTime;
                executionTimeline.add(-1); // Represent context switch with -1
            }

            bt[shortestID]--;
            executionTimeline.add(processarray[shortestID].id); // Add the current process ID to the timeline
            prevProcessID = shortestID;

            if (bt[shortestID] == 0) {
                processarray[shortestID].waitingTime = (curtime + 1 + switchingTime) - processarray[shortestID].arrivalTime - processarray[shortestID].burst;
                processarray[shortestID].TAT = (curtime + 1 + switchingTime) - processarray[shortestID].arrivalTime;
                order[done] = processarray[shortestID].id;
                done++;
            }
            curtime++;
        }

        // Draw graphical representation
        drawGraph(executionTimeline);
    }

    // Draw graphical representation
    public static void drawGraph(List<Integer> timeline) {
        JFrame frame = new JFrame("SRTF Scheduling Timeline");
        frame.setSize(1000, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int x = 50; // Initial x position
                int y = 50; // Fixed y position
                int width = 30; // Fixed width for each time unit
                int height = 50;

                for (int id : timeline) {
                    if (id == -1) { // Context switch block
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(x, y, width, height);
                        g.setColor(Color.BLACK);
                        g.drawRect(x, y, width, height);
                        g.drawString("CS", x + 5, y + 30);
                    } else { // Process block
                        Color color = new Color((int) (Math.random() * 0x1000000));
                        g.setColor(color);
                        g.fillRect(x, y, width, height);
                        g.setColor(Color.BLACK);
                        g.drawRect(x, y, width, height);
                        g.drawString("P" + id, x + 5, y + 30);
                    }
                    x += width;
                }
            }
        };

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Take input for the number of processes
        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();

        System.out.print("Enter the context switching time: ");
        int switchingTime = scanner.nextInt();

        // Create an array of processes
        Process[] proarray = new Process[n];

        // Take input for each process
        for (int i = 0; i < n; i++) {
            System.out.print("Enter Process ID: ");
            int id = scanner.nextInt();
            System.out.print("Enter Arrival Time for Process " + id + ": ");
            int arrival = scanner.nextInt();
            System.out.print("Enter Burst Time for Process " + id + ": ");
            int burst = scanner.nextInt();
            String name = "P" + (char)(id+'0');
            proarray[i] = new Process( name , id, burst, arrival,0 , 0 , "red");
//            proarray[i] = new Process(id, burst, arrival, 0);
        }

        // Array to store the execution order
        int[] doneOrder = new int[n];
        calcwaitingtime(proarray, doneOrder, n, switchingTime);

        // Print processes execution order
        System.out.println("Processes Execution Order:");
        for (int i = 0; i < proarray.length; i++) {
            System.out.print("P" + doneOrder[i] + " ");
        }
        System.out.println();

        // Print waiting time and turnaround time for each process
        System.out.println("\nProcess Details:");
        System.out.println("ID\tWaiting Time\tTurnaround Time");
        float totalWaitingTime = 0, totalTAT = 0;
        for (Process p : proarray) {
            System.out.printf("P%d\t%d\t\t%d\n", p.id, p.waitingTime, p.TAT);
            totalWaitingTime += p.waitingTime;
            totalTAT += p.TAT;
        }

        // Print average waiting time and turnaround time
        System.out.printf("\nAverage Waiting Time: %.2f\n", totalWaitingTime / proarray.length);
        System.out.printf("Average Turnaround Time: %.2f\n", totalTAT / proarray.length);
    }
}


/*
        4
        1
        1
        0
        7
        2
        2
        4
        3
        4
        1
        4
        5
        4

 */


