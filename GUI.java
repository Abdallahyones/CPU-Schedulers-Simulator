import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI implements ActionListener {

    JFrame frame;
    JLabel label;
    JComboBox<String> option;
    JButton button;

    public GUI() {
        frame = new JFrame("CPU Scheduler");
        frame.setLayout(null); // Use absolute layout (null layout)

        label = new JLabel("CPU Scheduler");
        option = new JComboBox<>(new String[]{"SJF", "SRTF", "PRIORITY", "FCAI"});
        button = new JButton("Use this Schedule");

        Font font = new Font("Arial", Font.PLAIN, 16);

        // Set font
        label.setFont(font);
        option.setFont(font);
        button.setFont(font);

        // Define margins and component dimensions
        int x = 50; // x-coordinate for all components
        int y = 30; // Initial y-coordinate
        int width = 200; // Component width
        int height = 30; // Component height
        int marginY = 16; // Vertical margin (spacing between components)

        // Set bounds for components
        label.setBounds(x, y, width, height);
        y += height + marginY; // Move y down for the next component

        option.setBounds(x, y, width, height);
        y += height + marginY; // Move y down for the next component

        button.setBounds(x, y, width, height);

        // Add components to frame
        frame.add(label);
        frame.add(option);
        frame.add(button);

        // Frame settings
        frame.setSize(300, 200); // Set frame size
        frame.setLocation(480, 70);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //button click event
        button.addActionListener(this);
    }

    public void ResultUI() {
        JFrame frame = new JFrame("CPU Scheduling Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Top Panel for CPU Scheduling Graph
        JPanel graphPanel = new JPanel();
        graphPanel.setBorder(BorderFactory.createTitledBorder("CPU Scheduling Graph"));
        graphPanel.setPreferredSize(new Dimension(600, 300));
        frame.add(graphPanel, BorderLayout.CENTER);

        // Right Panel for Process Information
        JPanel processPanel = new JPanel(new BorderLayout());
        processPanel.setBorder(BorderFactory.createTitledBorder("Processes Information"));
        String[] columnNames = {"Process", "Color", "Name", "PID", "Priority"};
        Object[][] data = {
                {"0", "Red", "HP1", "3280", "120"},
                {"1", "Purple", "MP1", "3281", "120"},
                // Add rows dynamically
        };
        JTable processTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(processTable);
        processPanel.add(scrollPane, BorderLayout.CENTER);
        frame.add(processPanel, BorderLayout.EAST);

        // Bottom Panel for Statistics
        JPanel statsPanel = new JPanel();
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
        statsPanel.add(new JLabel("Schedule Name: ABC Schedule"));
        statsPanel.add(new JLabel("AWT: 3125"));
        statsPanel.add(new JLabel("ATAT: 12531"));
        frame.add(statsPanel, BorderLayout.SOUTH);

        // Display the frame
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new GUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Retrieve selected option
        String selected = (String) option.getSelectedItem();

        // Prompt user for input
        String input = JOptionPane.showInputDialog(null, "Please enter the number of processors (as an integer):", "Input Required", JOptionPane.QUESTION_MESSAGE);

        // Check if user canceled the dialog or input is empty
        if (input != null && !input.isEmpty()) {
            try {
                // Parse the input to an integer
                int processorsCount = Integer.parseInt(input);
                System.out.println("Selected: " + selected);
                System.out.println("Processors count: " + processorsCount);
                ResultUI();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Input was canceled or left empty.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
