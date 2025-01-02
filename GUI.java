import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GUI {

    JFrame frame;
    Process[] processes;
    List<Integer> timeline;
    String schedulerType;

    double AvTimeWaiting;
    double AvTnT;

    public GUI(List<Integer> timeline, Process[] processes, String schedulerType, double AvTime, double AbTnt) {
        this.timeline = timeline;
        this.processes = processes;
        this.schedulerType = schedulerType;
        this.AvTimeWaiting = AvTime;
        this.AvTnT = AbTnt;

        // Set up the frame
        frame = new JFrame("CPU Scheduling Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(1000, 600);

        // Add Gantt Chart Panel with scrolling
        JPanel ganttChartPanel = new GanttChartPanel();
        JScrollPane scrollPane = new JScrollPane(
                ganttChartPanel,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scrollPane.setBorder(BorderFactory.createTitledBorder("Gantt Chart"));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add Process Table Panel
        JPanel processInfoPanel = createProcessInfoPanel();
        frame.add(processInfoPanel, BorderLayout.EAST);

        // Add Statistics Panel
        JPanel statsPanel = createStatisticsPanel();
        frame.add(statsPanel, BorderLayout.SOUTH);

        // Display the frame
        frame.setVisible(true);
    }

    private JPanel createProcessInfoPanel() {
        JPanel processInfoPanel = new JPanel(new BorderLayout());
        processInfoPanel.setBorder(BorderFactory.createTitledBorder("Processes Information"));

        String[] columnNames = {"Name", "Color", "Waiting Time", "Turnaround Time"};
        Object[][] processData = new Object[processes.length][4];
        for (int i = 0; i < processes.length; i++) {
            processData[i][0] = processes[i].name;
            processData[i][1] = processes[i].color;
            processData[i][2] = processes[i].waitingTime;
            processData[i][3] = processes[i].TAT;
        }

        JTable processTable = new JTable(processData, columnNames);
        JScrollPane scrollPane = new JScrollPane(processTable);
        processInfoPanel.add(scrollPane, BorderLayout.CENTER);

        return processInfoPanel;
    }

    private JPanel createStatisticsPanel() {
        JPanel statsPanel = new JPanel();
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
        statsPanel.setLayout(new GridLayout(3, 1));

        // Add scheduler type
        statsPanel.add(new JLabel("Scheduler: " + schedulerType));

        // Placeholder for AWT and ATAT (Replace with actual calculations)
        statsPanel.add(new JLabel("Average Waiting Time (AWT): " + AvTimeWaiting));
        statsPanel.add(new JLabel("Average Turnaround Time (ATAT): " + AvTnT));

        return statsPanel;
    }

    class GanttChartPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int x = 50; // Start position
            int y = 50; // Y-coordinate for Gantt bars
            int barHeight = 40; // Height of each bar
            int blockWidth = 40; // Width of each time unit block

            for (int i = 0; i < timeline.size(); i++) {
                int id = timeline.get(i);

                if (id == -1) { // Idle time block
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(x, y, blockWidth, barHeight);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, blockWidth, barHeight);
                    g.drawString("IDLE", x + 5, y + 25);
                } else { // Process block
                    Process process = processes[id - 1];
                    g.setColor(getColorFromString(process.color));
                    g.fillRect(x, y, blockWidth, barHeight);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, blockWidth, barHeight);
                    g.drawString("P" + process.id, x + 5, y + 25);
                }
                x += blockWidth; // Move to the next time unit
            }

            // Add a time axis
            g.setColor(Color.BLACK);
            int timeX = 50;
            for (int i = 0; i <= timeline.size(); i++) {
                g.drawString(Integer.toString(i), timeX, y + barHeight + 20);
                timeX += blockWidth;
            }

            // Dynamically set the preferred size
            int totalWidth = 50 + blockWidth * timeline.size();
            setPreferredSize(new Dimension(totalWidth, y + barHeight + 40));
        }
    }

    private Color getColorFromString(String colorString) {
        try {
            return Color.decode(colorString); // Try decoding as a hex color
        } catch (NumberFormatException e) {
            // Handle common named colors
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
                default:
                    return Color.BLACK; // Default to black if the color is unknown
            }
        }
    }
}
