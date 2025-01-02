class Process
{
    String name;
    int id ;
    int arrivalTime;
    int LeaveTime;
    int burstTime;
    int priority;
    int burst;
    int waitingTime = 0;
    int remainingTime;
    double fcaiFactor;
    int TAT ;
    int quantum;
    String color ;
    public Process(String name , int id , int burstTime , int arrivalTime , int priority , int quantum , String color) {
        this.name = name;
        this.id = id ;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.burst = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
        this.quantum = quantum;
        this.TAT = 0 ;
        this.waitingTime = 0 ;
        this.color = color;
    }
    void calculateFCAIFactor(double V1, double V2) {
        //this.fcaiFactor = (10 - priority) + (arrivalTime / V1) + (burst / V2);
        this.fcaiFactor= (int)Math.ceil((10 - priority) + (arrivalTime / V1) + (burst / V2));
    }
}