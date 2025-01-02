import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SRTF {
    static public List<Integer> executionTimeline = new ArrayList<>(); // Track the execution timeline
    static void calcwaitingtime(Process processarray[], int order[], int n, int switchingTime) {
        int done = 0, curtime = 0, mn = 20000, shortestID = 0;
        int[] bt = new int[n];
        int prevProcessID = -1;


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
                int timeswitching = switchingTime ;
                while (timeswitching > 0){
                    executionTimeline.add(-1);
                    timeswitching -= 1 ;
                } // Represent context switch with -1
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
//        drawGraph(executionTimeline , processarray);
    }

    // Draw graphical representation
    // Draw graphical representation
    public static void drawGraph(List<Integer> timeline, Process[] processes) {
        JFrame frame = new JFrame("SJF Scheduling Timeline");
        frame.setSize(1200, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int x = 50; // Initial x position
                int y = 100; // Fixed y position
                int width = 40; // Fixed width for each process
                int height = 50;

                for (int id : timeline) {
                    if (id == -1) { // Idle time block
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(x, y, width, height);
                        g.setColor(Color.BLACK);
                        g.drawRect(x, y, width, height);
                        g.drawString("IDLE", x + 5, y + 30);
                    } else { // Process block
                        Process process = processes[id-1];
                        Color color = getColorFromString(process.color); // Get the color from string input
                        g.setColor(color);
                        g.fillRect(x, y, width, height);
                        g.setColor(Color.BLACK);
                        g.drawRect(x, y, width, height);
                        g.drawString("P" + process.id, x + 5, y + 30);
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
        int n = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter the context switching time: ");
        int switchingTime = Integer.parseInt(scanner.nextLine().trim());

        // Create an array of processes
        Process[] proarray = new Process[n];

        // Take input for each process
        for (int i = 0; i < n; i++) {
            System.out.print("Enter Process ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Arrival Time for Process " + id + ": ");
            int arrival = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Burst Time for Process " + id + ": ");
            int burst = Integer.parseInt(scanner.nextLine().trim());
            String name = "P" + (char)(id+'0');
            System.out.print("Process Color (Graphical Representation): ");
            String color = scanner.nextLine().trim();
            proarray[i] = new Process( name , id, burst, arrival,0 , 0 , color);
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
        GUI gui = new GUI(executionTimeline , proarray , "SRTF" , totalWaitingTime / proarray.length ,totalTAT / proarray.length );

    }

    private static Color getColorFromString(String colorString) {
        try {
            // If it's a hex string (e.g., "#FF0000"), it will work directly
            return Color.decode(colorString);
        } catch (NumberFormatException e) {
            // Handle named colors manually
            switch (colorString.toLowerCase()) {
                case "red":
                    return Color.RED;
                case "green":
                    return Color.GREEN;
                case "blue":
                    return Color.BLUE;
                case "yellow":
                    return Color.YELLOW;
                case "black":
                    return Color.BLACK;
                case "white":
                    return Color.WHITE;
                case "gray":
                    return Color.GRAY;
                case "lightgray":
                    return Color.LIGHT_GRAY;
                case "darkgray":
                    return Color.DARK_GRAY;
                // Add more cases as necessary
                default:
                    return Color.BLACK; // Default to black if the color is unknown
            }
        }
    }

}


/*
        4
        1
        1
        0
        7
        red
        2
        2
        4
        blue
        3
        4
        1
        yellow
        4
        5
        4
        green

 */


