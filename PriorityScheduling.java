import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;


public class PriorityScheduling {
    static void calcwaitingtime(Process processarray [],int order [],int n,int switchingTime){
        int done=0,curtime=0,mnbt=20000,mnpt=200000,shortestID=-1;
        int[] bt = new int[n];
        for (int i = 0; i < n; i++) {
            bt[i]=processarray[i].burst;
        }

        while (done!=n){
            mnbt=200000;
            mnpt=200000;
            shortestID=-1;
            //get shortest process
            for (int i = 0; i < n; i++) {

                if(processarray[i].arrivalTime<=curtime && processarray[i].priority <= mnpt && bt[i] > 0){
                    if( (processarray[i].priority==mnpt && bt[i]<mnbt) || processarray[i].priority< mnpt){
                        mnbt=bt[i];
                        mnpt=processarray[i].priority;
                        shortestID=i;
                    }
                }

            }


            //no arrival yet
            if(shortestID==-1){
                curtime++;
            }
            else{
                curtime+=processarray[shortestID].burst+switchingTime;
                processarray[shortestID].TAT=curtime-processarray[shortestID].arrivalTime;
                processarray[shortestID].waitingTime=processarray[shortestID].TAT-processarray[shortestID].burst;
                bt[shortestID]=0;
                order[done]=processarray[shortestID].id;
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
        int[] doneOrder = new int[n];
        calcwaitingtime(proarray, doneOrder, n,switchingTime);


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

        // Show graphical representation
        createGraph(doneOrder, proarray);
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