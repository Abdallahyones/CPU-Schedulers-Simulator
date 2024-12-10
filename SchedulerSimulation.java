import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

//class Process {
//    String name;
//    int arrivalTime;
//    int LeaveTime;
//    int burstTime;
//    int priority;
//    int remainingTime;
//    int waitingTime = 0;
//    int turnaroundTime = 0;
//    double fcaiFactor;
//    int quantum;
//    public Process(String name,int quantum, int arrivalTime, int burstTime, int priority) {
//        this.name = name;
//        this.arrivalTime = arrivalTime;
//        this.burstTime = burstTime;
//        this.remainingTime = burstTime;
//        this.priority = priority;
//        this.quantum = quantum;
//    }
//    void calculateFCAIFactor(double V1, double V2) {
//        //this.fcaiFactor = (10 - priority) + (arrivalTime / V1) + (remainingTime / V2);
//        this.fcaiFactor= (int)Math.ceil((10 - priority) + (arrivalTime / V1) + (remainingTime / V2));
//    }
//}

class SchedulerSimulation {
    Queue<Process> processes = new PriorityQueue<Process>(Comparator.comparingInt(c -> c.arrivalTime));
    double V1, V2;
    List<String> timeline = new ArrayList<>(); // To track timeline for graphical representation
    List<String> colors = new ArrayList<>(); // List to track colors for graphical representation


    public SchedulerSimulation(Queue<Process> processes) {
        this.processes = processes;
        int mxArr = 0, mxBurst = 0;
        for (Process p : processes) {
            mxBurst = Math.max(p.remainingTime, mxBurst);
            mxArr = Math.max(p.arrivalTime, mxArr);
        }
        this.V1 = mxArr / 10.0;
        this.V2 = mxBurst / 10.0;
        for (Process p : processes) {
            p.calculateFCAIFactor(V1, V2);
        }
    }

    public void run() {
        Queue<Process> ReadyQueue = new LinkedList<>();
        int currentTime = 0;
        Process process = null;

        while (!processes.isEmpty() || !ReadyQueue.isEmpty()) {
            // Get Process at certain time
            ReadyQueue.addAll(GetProcesses(processes, currentTime));
            if (ReadyQueue.isEmpty()) {
                timeline.add("IDLE");
                colors.add("LIGHT_GRAY"); // Add "IDLE" color to timeline
                currentTime++;
                continue;
            }

            if (process == null) {
                process = ReadyQueue.poll();
            }
            int curr = 0;
            int Quantum40Percent = (int) Math.ceil(process.quantum * 0.4);
            boolean preempted = false;
            boolean done = false;
            String Msg = "";

            while (curr < process.quantum && !preempted && !done) {
                timeline.add(process.name); // Add process name to the timeline
                colors.add(process.color); // Add the color of the process to the colors list
                System.out.println("Curr : " + (curr + currentTime) + " Name " + process.name +
                        " Remaining Burst Time : " + process.remainingTime);
                if (process.remainingTime == 0) {
                    done = true;
                    process.LeaveTime = currentTime + curr;
                    System.out.println("Process " + process.name + " is Done.");
                    process = null;
                    break;
                }

                if (curr < Quantum40Percent) {
                    curr++;
                    process.remainingTime--;
                    continue;
                }

                // System.out.println("Current : " + currentTime + curr);
                ReadyQueue.addAll(GetProcesses(processes, currentTime + curr));
                Process pre = null;
                double mn = Integer.MAX_VALUE;
                for (Process p : ReadyQueue) {
                    if (process.fcaiFactor > p.fcaiFactor) {
                        if (mn > p.fcaiFactor) {
                            mn = p.fcaiFactor;
                            pre = p;
                        }
                    }
                }

                if (pre == null) {
                    curr++;
                    process.remainingTime--;
                    continue;
                }

                preempted = true;
                ReadyQueue.remove(pre);
                updateProcess(process, true, curr, processes);
                System.out.println("Preempted " + pre.name + " this " + process.name + " New FCAI");
                ReadyQueue.add(process);
                process = pre;
                break;
            }

            currentTime += curr;
            if (!preempted && !done) {
                ReadyQueue.add(process);
                System.out.println();
                updateProcess(process, preempted, curr, processes);
                System.out.println("Quantum finished of process " + process.name);
                process = null;
            }
        }
        System.out.println(currentTime);
        drawGraph(timeline , colors); // Draw the graphical representation
    }

    public static List<Process> GetProcesses(Queue<Process> processes, int currentTime) {
        List<Process> arrivedProcesses = new ArrayList<>();
        Iterator<Process> iterator = processes.iterator();

        // Iterate through the processes to find those that have arrived
        while (iterator.hasNext()) {
            Process process = iterator.next();
            if (process.arrivalTime <= currentTime) {
                arrivedProcesses.add(process);
                iterator.remove(); // Remove the process from the original list to prevent duplication
            }
        }
        return arrivedProcesses;
    }

    // Method to update process parameters
    public void updateProcess(Process process, boolean preempted, int currentTime, Queue<Process> processes) {

        if (!preempted) {
            process.quantum += 2; // Increase quantum time if not preempted
        } else {
            process.quantum += (process.quantum - currentTime);
        }

        System.out.println("Quantum = " + process.quantum);
        process.calculateFCAIFactor(V1, V2); // Update FCAI factor
        System.out.println("FCAI = " + process.fcaiFactor);
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get the number of processes
        System.out.print("Enter the number of processes: ");
        int numProcesses = Integer.parseInt(scanner.nextLine().trim()); // Read full line and parse
        // Get the Context Switching Time
        System.out.print("Enter the Context Switching Time: ");
        int contextSwitchingTime = Integer.parseInt(scanner.nextLine().trim());

        // Create a list of processes
        Queue<Process> processes = new PriorityQueue<Process>(Comparator.comparingInt(c -> c.arrivalTime));

        for (int i = 0; i < numProcesses; i++) {
            System.out.println("\nEnter details for Process " + (i + 1) + ":");

            System.out.print("Process Name: ");
            String name = scanner.nextLine().trim();

            // Get the Round Robin Time Quantum
            System.out.print("Enter the Round Robin Time Quantum: ");
            int roundRobinQuantum = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Process Color (Graphical Representation): ");
            String color = scanner.nextLine().trim();

            System.out.print("Arrival Time: ");
            int arrivalTime = Integer.parseInt(scanner.nextLine().trim()); // Clear buffer after this line

            System.out.print("Burst Time: ");
            int burstTime = Integer.parseInt(scanner.nextLine().trim()); // Clear buffer after this line

            System.out.print("Priority (1-10, lower number = higher priority): ");
            int priority = Integer.parseInt(scanner.nextLine().trim()); // Clear buffer after this line

            processes.add(new Process(name, 0  , arrivalTime, burstTime, priority , roundRobinQuantum , color));
        }

        // Execute FCAI Scheduling
        System.out.println("\nExecuting FCAI Scheduling...");
        SchedulerSimulation inst =new SchedulerSimulation(processes);
        inst.run();

        scanner.close();
    }

    public void drawGraph(List<String> timeline, List<String> colors) {
        JFrame frame = new JFrame("FCAI Scheduling Timeline");
        frame.setSize(1200, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int x = 50; // Starting position
                int y = 50;
                int width = 30; // Width of each block
                int height = 50;

                for (int i = 0; i < timeline.size(); i++) {
                    String processName = timeline.get(i);
                    String processColor = colors.get(i);

                    if (processName.equals("IDLE")) {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(x, y, width, height);
                        g.setColor(Color.BLACK);
                        g.drawRect(x, y, width, height);
                        g.drawString("IDLE", x + 5, y + 30);
                    } else {
                        Color color = getColorFromString(processColor); // Get the color from string input
                        g.setColor(color);
                        g.fillRect(x, y, width, height);
                        g.setColor(Color.BLACK);
                        g.drawRect(x, y, width, height);
                        g.drawString(processName, x + 5, y + 30);

                    }
                    x += width; // Move to the next block
                }
            }
        };
        frame.add(panel);
        frame.setVisible(true);
    }

    // Method to handle named colors and hex codes
    private Color getColorFromString(String colorString) {
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
P1
4
red
0
17
4
P2
3
blue
3
6
9
P3
5
green
4
10
3
P4
2
yellow
29
4
10


 */



