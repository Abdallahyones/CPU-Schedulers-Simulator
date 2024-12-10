import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class SJF {

    static void calcwaitingtime(Process processarray[], int order[], int n, List<Integer> timeline) {
        int done = 0, curtime = 0, mn = 2000, shortestID = -1;

        while (done != n) {
            mn = 2000; // Reset the minimum burst time for each iteration
            shortestID = -1;

            // Get the shortest process
            for (int i = 0; i < n; i++) {
                int starvationfact = (curtime - processarray[i].arrivalTime) / 50;
                if (starvationfact < 0) starvationfact = 0;

                if (processarray[i].arrivalTime <= curtime && processarray[i].burst - starvationfact < mn && processarray[i].burst != -1) {
                    mn = processarray[i].burst - starvationfact;
                    shortestID = i;
                }
            }

            // No process available yet
            if (shortestID == -1) {
                timeline.add(-1); // Represent idle time with -1
                curtime++;
            } else {
                // Execute the shortest process
                processarray[shortestID].waitingTime = curtime - processarray[shortestID].arrivalTime;
                for (int j = 0; j < processarray[shortestID].burst; j++) {
                    timeline.add(processarray[shortestID].id); // Add the process ID to the timeline
                }
                curtime += processarray[shortestID].burst;
                processarray[shortestID].burst = -1; // Mark as completed
                processarray[shortestID].TAT = curtime - processarray[shortestID].arrivalTime;
                order[done] = processarray[shortestID].id;
                done++;
            }
        }
    }

    // Draw graphical representation
    public static void drawGraph(List<Integer> timeline) {
        JFrame frame = new JFrame("SJF Scheduling Timeline");
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
                    if (id == -1) { // Idle time block
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(x, y, width, height);
                        g.setColor(Color.BLACK);
                        g.drawRect(x, y, width, height);
                        g.drawString("IDLE", x + 5, y + 30);
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

        // Create an array of processes
        Process[] proarray = new Process[n];

        // Take input for each Process
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
        List<Integer> timeline = new ArrayList<>();
        calcwaitingtime(proarray, doneOrder, n, timeline);

        // Print Processes execution order
        System.out.println("Processes Execution Order:");
        for (int i = 0; i < proarray.length; i++) {
            System.out.print("P" + doneOrder[i] + " ");
        }
        System.out.println();

        // Print waiting time and turnaround time for each Process
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

        // Draw graphical representation
        drawGraph(timeline);
    }
}

/*
        4
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


