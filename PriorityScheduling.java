import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;


public class PriorityScheduling {

    static void calcwaitingtime(Process[] processArray, List<Integer> order, int n, int switchingTime) {
        int done = 0, curTime = 0, mnbt = 20000, mnpt = 200000, shortestID = -1;
        int[] bt = new int[n];
        for (int i = 0; i < n; i++) {
            bt[i] = processArray[i].burst;
        }

        while (done != n) {
            mnbt = 200000;
            mnpt = 200000;
            shortestID = -1;

            // Get the shortest process
            for (int i = 0; i < n; i++) {
                if (processArray[i].arrivalTime <= curTime && processArray[i].priority <= mnpt && bt[i] > 0) {
                    if ((processArray[i].priority == mnpt && bt[i] < mnbt) || processArray[i].priority < mnpt) {
                        mnbt = bt[i];
                        mnpt = processArray[i].priority;
                        shortestID = i;
                    }
                }
            }

            // No arrival yet
            if (shortestID == -1) {
                curTime++;
            } else {
                curTime += processArray[shortestID].burst + switchingTime;
                processArray[shortestID].TAT = curTime - processArray[shortestID].arrivalTime;
                processArray[shortestID].waitingTime = processArray[shortestID].TAT - processArray[shortestID].burst;
                bt[shortestID] = 0;
                order.add(processArray[shortestID].id); // Add to list instead of array
                done++;
            }
        }
    }


    // Draw graphical representation
    public static void createGraph(int[] executionOrder, Process[] processes) {
        JFrame frame = new JFrame("Processes Execution Order");
        frame.setSize(1200, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int x = 50; // Initial x position
                int y = 50; // Fixed y position
                int width = 50; // Fixed width for each process block
                int height = 50;

                for (int id : executionOrder) {
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
                    x += width; // Move to the next position
                }
            }
        };

        frame.add(panel);
        frame.setVisible(true);
    }


    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        // Take input for the number of processes
        System.out.print("Enter the number of processes: ");
        int n = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter the context switching time: ");
        int switchingTime  = Integer.parseInt(scanner.nextLine().trim());

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
            System.out.print("Enter Priority for Process " + id + ": ");
            int priority = Integer.parseInt(scanner.nextLine().trim());
            String name = "P" + (char)(id+'0');
            System.out.print("Process Color (Graphical Representation): ");
            String color = scanner.nextLine().trim();
            proarray[i] = new Process( name , id, burst, arrival,priority , 0 , color);
        }

        // Array to store the execution order
//        int[] doneOrder = new int[n];
        List<Integer> doneOrder = new ArrayList<>();
        calcwaitingtime(proarray, doneOrder, n,switchingTime);


        // Print processes execution order
        System.out.println("Processes Execution Order:");
        for (int i = 0; i < proarray.length; i++) {
            System.out.print("P" + doneOrder.get(i) + " ");
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

        // Show graphical representation
//        createGraph(doneOrder, proarray);
        GUI gui = new GUI(doneOrder , proarray , "PriorityScheduling" , totalWaitingTime / proarray.length ,totalTAT / proarray.length );
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
 6
 1
 red
 2
 1
 3
 3
 blue
 3
 2
 4
 1
 green
 4
 3
 3
 2
 yellow



 */