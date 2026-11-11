package org.tucn.pt.GUI;

import org.tucn.pt.DataModel.SimpleTask;
import org.tucn.pt.DataModel.Task;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class View extends JFrame {
    private JPanel contentPane;
    private JTabbedPane tabbedPane;

    // add employee
    private JTextField employeeIdTextField;
    private JTextField employeeNameTextField;
    private JButton addEmployeeBtn;
    private JLabel employeeStatusLabel;

    // add task
    private JTextField taskIdTextField;
    private JTextField startHourTextField;
    private JTextField endHourTextField;
    private JButton addSimpleTaskBtn;
    private JTextField partTaskField;
    private JButton addComplexTaskBtn;
    private JLabel taskStatusLabel;

    // assign & modify
    private JTextField opEmpIdTextField;
    private JTextField opTaskIdTextField;
    private JButton assignTaskBtn;
    private JButton modifyStatusBtn;
    private JButton calcDurationBtn;
    private JLabel opStatusLabel;

    // display
    private JTextArea displayArea;
    private JButton viewAllBtn;
    private JButton viewUnassignedBtn;
    private JButton viewStatsBtn;
    private JButton filterEmployeesBtn;

    private Controller controller;

    public View(String name) {
        super(name);
        this.controller = new Controller(this);
        this.prepareGui();
    }

    public void prepareGui() {
        this.setSize(680, 560);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.contentPane = new JPanel(new BorderLayout());
        this.tabbedPane = new JTabbedPane();

        this.prepareEmployeePanel();
        this.prepareTaskPanel();
        this.prepareOperationsPanel();
        this.prepareDisplayPanel();

        this.contentPane.add(tabbedPane, BorderLayout.CENTER);
        this.setContentPane(this.contentPane);
    }

    private JLabel createStatusLabel() {
        JLabel label = new JLabel(" ");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(label.getFont().deriveFont(Font.ITALIC));
        return label;
    }

    private void prepareEmployeePanel() {
        JPanel employeePanel = new JPanel(new BorderLayout(5, 5));
        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        fieldsPanel.add(new JLabel("Employee Id:", JLabel.CENTER));
        this.employeeIdTextField = new JTextField();
        fieldsPanel.add(this.employeeIdTextField);

        fieldsPanel.add(new JLabel("Employee Name:", JLabel.CENTER));
        this.employeeNameTextField = new JTextField();
        fieldsPanel.add(this.employeeNameTextField);

        fieldsPanel.add(new JLabel(""));
        this.addEmployeeBtn = new JButton("Add Employee");
        this.addEmployeeBtn.setActionCommand("ADD_EMP");
        this.addEmployeeBtn.addActionListener(this.controller);
        fieldsPanel.add(this.addEmployeeBtn);

        this.employeeStatusLabel = createStatusLabel();

        employeePanel.add(fieldsPanel, BorderLayout.CENTER);
        employeePanel.add(this.employeeStatusLabel, BorderLayout.SOUTH);

        this.tabbedPane.addTab("Add Employees", employeePanel);
    }

    private void prepareTaskPanel() {
        JPanel taskPanel = new JPanel(new BorderLayout(5, 5));
        JPanel fieldsPanel = new JPanel(new GridLayout(6, 2, 5, 5));

        fieldsPanel.add(new JLabel("Task Id (Simple & Complex):", JLabel.CENTER));
        this.taskIdTextField = new JTextField();
        fieldsPanel.add(this.taskIdTextField);

        fieldsPanel.add(new JLabel("Start Hour (Simple, 0-23):", JLabel.CENTER));
        this.startHourTextField = new JTextField();
        fieldsPanel.add(this.startHourTextField);

        fieldsPanel.add(new JLabel("End Hour (Simple, 0-23):", JLabel.CENTER));
        this.endHourTextField = new JTextField();
        fieldsPanel.add(this.endHourTextField);

        fieldsPanel.add(new JLabel(""));
        this.addSimpleTaskBtn = new JButton("Add Simple Task");
        this.addSimpleTaskBtn.setActionCommand("ADD_SIMPLE_TASK");
        this.addSimpleTaskBtn.addActionListener(this.controller);
        fieldsPanel.add(this.addSimpleTaskBtn);

        fieldsPanel.add(new JLabel("Complex sub-task [indices] (comma-separated):", JLabel.CENTER));
        this.partTaskField = new JTextField();
        fieldsPanel.add(this.partTaskField);

        fieldsPanel.add(new JLabel(""));
        this.addComplexTaskBtn = new JButton("Add Complex Task");
        this.addComplexTaskBtn.setActionCommand("ADD_COMPLEX_TASK");
        this.addComplexTaskBtn.addActionListener(this.controller);
        fieldsPanel.add(this.addComplexTaskBtn);

        this.taskStatusLabel = createStatusLabel();

        taskPanel.add(fieldsPanel, BorderLayout.CENTER);
        taskPanel.add(this.taskStatusLabel, BorderLayout.SOUTH);

        this.tabbedPane.addTab("Add Tasks", taskPanel);
    }

    private void prepareOperationsPanel() {
        JPanel opPanel = new JPanel(new BorderLayout(5, 5));
        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 5, 5));

        fieldsPanel.add(new JLabel("Employee Id:", JLabel.CENTER));
        this.opEmpIdTextField = new JTextField();
        fieldsPanel.add(this.opEmpIdTextField);

        fieldsPanel.add(new JLabel("Unassigned Task Index / Task ID:", JLabel.CENTER));
        this.opTaskIdTextField = new JTextField();
        fieldsPanel.add(this.opTaskIdTextField);

        this.assignTaskBtn = new JButton("Assign Task");
        this.assignTaskBtn.setActionCommand("ASSIGN_TASK");
        this.assignTaskBtn.addActionListener(this.controller);
        fieldsPanel.add(this.assignTaskBtn);

        this.modifyStatusBtn = new JButton("Change Task Status");
        this.modifyStatusBtn.setActionCommand("MODIFY_STATUS");
        this.modifyStatusBtn.addActionListener(this.controller);
        fieldsPanel.add(this.modifyStatusBtn);

        this.calcDurationBtn = new JButton("Calculate Work Duration");
        this.calcDurationBtn.setActionCommand("CALC_DURATION");
        this.calcDurationBtn.addActionListener(this.controller);
        fieldsPanel.add(this.calcDurationBtn);
        fieldsPanel.add(new JLabel(" <=== uses Employee ID only", JLabel.CENTER));

        this.opStatusLabel = createStatusLabel();

        opPanel.add(fieldsPanel, BorderLayout.CENTER);
        opPanel.add(this.opStatusLabel, BorderLayout.SOUTH);

        this.tabbedPane.addTab("Manage", opPanel);
    }

    private void prepareDisplayPanel() {
        JPanel displayPanel = new JPanel(new BorderLayout(5, 5));

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));

        this.viewAllBtn = new JButton("View All Employees & Tasks");
        this.viewAllBtn.setActionCommand("VIEW_ALL");
        this.viewAllBtn.addActionListener(this.controller);

        this.viewUnassignedBtn = new JButton("View Unassigned Tasks");
        this.viewUnassignedBtn.setActionCommand("VIEW_UNASSIGNED");
        this.viewUnassignedBtn.addActionListener(this.controller);

        this.viewStatsBtn = new JButton("View Statistics");
        this.viewStatsBtn.setActionCommand("VIEW_STATS");
        this.viewStatsBtn.addActionListener(this.controller);

        this.filterEmployeesBtn = new JButton("Filter Employees (>40h)");
        this.filterEmployeesBtn.setActionCommand("FILTER_EMPLOYEES");
        this.filterEmployeesBtn.addActionListener(this.controller);

        buttonPanel.add(this.viewAllBtn);
        buttonPanel.add(this.viewUnassignedBtn);
        buttonPanel.add(this.viewStatsBtn);
        buttonPanel.add(this.filterEmployeesBtn);

        this.displayArea = new JTextArea();
        this.displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(this.displayArea);

        displayPanel.add(buttonPanel, BorderLayout.NORTH);
        displayPanel.add(scrollPane, BorderLayout.CENTER);

        this.tabbedPane.addTab("View & Stats", displayPanel);
    }

    public void refreshUnassignedList(List<Task> unassignedTaskList) {
        StringBuilder sb = new StringBuilder();
        if (unassignedTaskList.isEmpty()) {
            sb.append("No unassigned tasks.");
        } else {
            int index = 0;
            for (Task task : unassignedTaskList) {
                String type = (task instanceof SimpleTask) ? "Simple" : "Complex";
                sb.append("[").append(index).append("] ")
                        .append(type).append(" | Id: ").append(task.getId())
                        .append(" | Duration: ").append(task.estimateDuration()).append("h\n");
                index++;
            }
        }

        taskStatusLabel.setText("Unassigned list has " + unassignedTaskList.size() + " task(s). See View & Stats tab.");
    }

    public void setEmployeeStatus(String message) {
        employeeStatusLabel.setText(message);
    }

    public void setTaskStatus(String message) {
        taskStatusLabel.setText(message);
    }

    public void setOpStatus(String message) {
        opStatusLabel.setText(message);
    }

    // getters for controller
    public JTextField getEmpIdTextField() { return employeeIdTextField; }
    public JTextField getEmpNameTextField() { return employeeNameTextField; }
    public JTextField getTaskIdTextField() { return taskIdTextField; }
    public JTextField getStartHourTextField() { return startHourTextField; }
    public JTextField getEndHourTextField() { return endHourTextField; }
    public JTextField getPartTaskField() { return partTaskField; }
    public JTextField getOpEmpIdTextField() { return opEmpIdTextField; }
    public JTextField getOpTaskIdTextField() { return opTaskIdTextField; }
    public JTextArea getDisplayArea() { return displayArea; }
}