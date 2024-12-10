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
        frame.setSize(800, 200);
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
                    // Random color for each process
                    Color color = new Color((int) (Math.random() * 0x1000000));
                    g.setColor(color);
                    g.fillRect(x, y, width, height);

                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, width, height);
                    g.drawString("P" + id, x + 15, y + 30);

                    x += width + 10; // Move to the next position
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
        int n = scanner.nextInt();

        System.out.print("Enter the context switching time: ");
        int switchingTime  = scanner.nextInt();

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
            System.out.print("Enter Priority for Process " + id + ": ");
            int priority = scanner.nextInt();
            String name = "P" + (char)(id+'0');
            proarray[i] = new Process( name , id, burst, arrival,priority , 0 , "red");
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
}


/*

 4
 1
 1
 0
 6
 1
 2
 1
 3
 3
 3
 2
 4
 1
 4
 3
 3
 2



 */