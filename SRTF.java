import java.util.Scanner;

//class process
//{
//    int id,burst,arrival,waiting,TAT,priority;
//    public  process(int id,int burst,int arrival,int priority){
//        this.id=id;
//        this.burst=burst;
//        this.arrival=arrival;
//        this.priority=priority;
//        this.waiting=0;
//        this.TAT=0;
//    }
//}

public class SRTF {

    static void calcwaitingtime(process processarray [],int order [],int n){
        int done=0,curtime=0,mn=20000,shortestID=0;
        int[] bt = new int[n];
        for (int i = 0; i < n; i++) {
            bt[i]=processarray[i].burst;
        }

        while (done!=n){
            mn=200000;
            //get shortest process
            for (int i = 0; i < n; i++) {

                if(processarray[i].arrival<=curtime && bt[i]<mn && bt[i]>0){
                    mn=bt[i];
                    shortestID=i;
                }
            }

            bt[shortestID]--;
            if(bt[shortestID]==0){
                processarray[shortestID].waiting=(curtime+1)-processarray[shortestID].arrival-processarray[shortestID].burst;
                processarray[shortestID].TAT=(curtime+1)-processarray[shortestID].arrival;
                order[done]=processarray[shortestID].id;
                done++;
            }
            curtime++;
        }
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        // Take input for the number of processes
        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();

        // Create an array of processes
        process[] proarray = new process[n];

        // Take input for each process
        for (int i = 0; i < n; i++) {
            System.out.print("Enter Process ID: ");
            int id = scanner.nextInt();
            System.out.print("Enter Arrival Time for Process " + id + ": ");
            int arrival = scanner.nextInt();
            System.out.print("Enter Burst Time for Process " + id + ": ");
            int burst = scanner.nextInt();
            proarray[i] = new process(id, burst, arrival,0);
        }

        // Array to store the execution order
        int[] doneOrder = new int[n];
        calcwaitingtime(proarray, doneOrder, n);


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
        for (process p : proarray) {
            System.out.printf("P%d\t%d\t\t%d\n", p.id, p.waiting, p.TAT);
            totalWaitingTime += p.waiting;
            totalTAT += p.TAT;
        }

        // Print average waiting time and turnaround time
        System.out.printf("\nAverage Waiting Time: %.2f\n", totalWaitingTime / proarray.length);
        System.out.printf("Average Turnaround Time: %.2f\n", totalTAT / proarray.length);
    }
}
