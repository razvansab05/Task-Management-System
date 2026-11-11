package org.tucn.pt.GUI;

import org.tucn.pt.Business.TasksManagement;
import org.tucn.pt.Business.Utility;
import org.tucn.pt.Data.SerializationOperations;
import org.tucn.pt.DataModel.ComplexTask;
import org.tucn.pt.DataModel.Employee;
import org.tucn.pt.DataModel.SimpleTask;
import org.tucn.pt.DataModel.Task;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Controller implements ActionListener {
    private View view;
    private TasksManagement system;
    private Utility utils;
    private List<Task> unassignedTaskList;

    public Controller(View v) {
        this.view = v;
        this.unassignedTaskList = new ArrayList<>();

        this.system = SerializationOperations.loadData();
        if (this.system == null) {
            this.system = new TasksManagement();
        }
        this.utils = new Utility(this.system, this.system.getEmployees());
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        try {
            switch (command) {
                case "ADD_EMP":
                    handleAddEmployee();
                    break;
                case "ADD_SIMPLE_TASK":
                    handleAddSimpleTask();
                    break;
                case "ADD_COMPLEX_TASK":
                    handleAddComplexTask();
                    break;
                case "ASSIGN_TASK":
                    handleAssignTask();
                    break;
                case "MODIFY_STATUS":
                    handleModifyStatus();
                    break;
                case "CALC_DURATION":
                    handleCalcDuration();
                    break;
                case "VIEW_ALL":
                    handleViewAll();
                    break;
                case "VIEW_UNASSIGNED":
                    handleViewUnassigned();
                    break;
                case "VIEW_STATS":
                    handleViewStats();
                    break;
                case "FILTER_EMPLOYEES":
                    handleFilterEmployees();
                    break;
            }

            SerializationOperations.saveData(system);

        } catch (NumberFormatException ex) {
            String errorMsg = "Error: please enter valid numbers in the required fields.";
            switch (command) {
                case "ADD_EMP":
                    view.setEmployeeStatus(errorMsg);
                    break;
                case "ADD_SIMPLE_TASK":
                case "ADD_COMPLEX_TASK":
                    view.setTaskStatus(errorMsg);
                    break;
                default:
                    view.setOpStatus(errorMsg);
                    break;
            }
        } catch (Exception ex) {
            view.setOpStatus("Error: " + ex.getMessage());
        }
    }

    private void handleAddEmployee() {
        int empId = Integer.parseInt(view.getEmpIdTextField().getText().trim());
        String name = view.getEmpNameTextField().getText().trim();

        if (name.isEmpty()) {
            view.setEmployeeStatus("Error: employee name cannot be empty.");
            return;
        }
        if (system.isEmployeeIdTaken(empId)) {
            view.setEmployeeStatus("Error: employee Id " + empId + " is already taken.");
            return;
        }

        system.addEmployee(new Employee(empId, name));
        view.setEmployeeStatus("Employee \"" + name + "\" | Id : " + empId + "  added successfully.");
    }

    private void handleAddSimpleTask() {
        int taskId = Integer.parseInt(view.getTaskIdTextField().getText().trim());
        int startH = Integer.parseInt(view.getStartHourTextField().getText().trim());
        int endH = Integer.parseInt(view.getEndHourTextField().getText().trim());

        if (isTaskIdTakenAnywhere(taskId)) {
            view.setTaskStatus("Error: task Id " + taskId + " is already taken.");
            return;
        }
        if (startH < 0 || startH > 23 || endH < 0 || endH > 23) {
            view.setTaskStatus("Error: hours must be between 0 and 23.");
            return;
        }
        if (startH == endH) {
            view.setTaskStatus("Error: start and end hour cannot be the same.");
            return;
        }

        SimpleTask sTask = new SimpleTask(taskId, "Uncompleted", startH, endH);
        int duration = sTask.estimateDuration();
        unassignedTaskList.add(sTask);

        view.setTaskStatus("Simple Task : Id = " + taskId + ", duration = " + duration + "h" + " added at unassigned index " + (unassignedTaskList.size() - 1) + ".");
        view.refreshUnassignedList(unassignedTaskList);
    }

    private void handleAddComplexTask() {
        int cTaskId = Integer.parseInt(view.getTaskIdTextField().getText().trim());

        if (isTaskIdTakenAnywhere(cTaskId)) {
            view.setTaskStatus("Error: task Id " + cTaskId + " is already taken.");
            return;
        }

        String indicesText = view.getPartTaskField().getText().trim();
        if (indicesText.isEmpty()) {
            view.setTaskStatus("Error: provide at least one sub-task index for a complex task.");
            return;
        }

        ComplexTask cTask = new ComplexTask(cTaskId, "Uncompleted");
        String[] parts = indicesText.split(",");

        List<Task> subTasksToAdd = new ArrayList<>();
        for (String idStr : parts) {
            int index = Integer.parseInt(idStr.trim());
            if (index >= 0 && index < unassignedTaskList.size()) {
                Task taskToAdd = unassignedTaskList.get(index);
                if (!subTasksToAdd.contains(taskToAdd)) {
                    subTasksToAdd.add(taskToAdd);
                }
            }
        }

        if (subTasksToAdd.isEmpty()) {
            view.setTaskStatus("Error: no valid sub-task indices provided.");
            return;
        }

        for (Task subTask : subTasksToAdd) {
            cTask.addTask(subTask);
            unassignedTaskList.remove(subTask);
        }

        unassignedTaskList.add(cTask);
        view.setTaskStatus("Complex Task : Id = " + cTaskId + " | duration = " + cTask.estimateDuration()
                + "h | sub-tasks = " + subTasksToAdd.size() + " added at unassigned index "
                + (unassignedTaskList.size() - 1) + ".");
        view.refreshUnassignedList(unassignedTaskList);
    }

    private void handleAssignTask() {
        int assignEmpId = Integer.parseInt(view.getOpEmpIdTextField().getText().trim());
        int assignTaskIndex = Integer.parseInt(view.getOpTaskIdTextField().getText().trim());

        if (assignTaskIndex < 0 || assignTaskIndex >= unassignedTaskList.size()) {
            view.setOpStatus("Error: unassigned task index " + assignTaskIndex + " does not exist.");
            return;
        }
        if (!system.isEmployeeIdTaken(assignEmpId)) {
            view.setOpStatus("Error: no employee with Id " + assignEmpId + " exists.");
            return;
        }

        Task taskToAssign = unassignedTaskList.get(assignTaskIndex);
        boolean success = system.assignTaskToEmployee(assignEmpId, taskToAssign);
        if (success) {
            unassignedTaskList.remove(assignTaskIndex);
            view.setOpStatus("Task : Id = " + taskToAssign.getId() + " assigned to employee "
                    + assignEmpId + ". Removed from unassigned list.");
            view.refreshUnassignedList(unassignedTaskList);
        } else {
            view.setOpStatus("Error: assignment failed.");
        }
    }

    private void handleModifyStatus() {
        int modEmpId = Integer.parseInt(view.getOpEmpIdTextField().getText().trim());
        int modTaskId = Integer.parseInt(view.getOpTaskIdTextField().getText().trim());

        String wantedStatus = null;

        // adaugata extra sa pot vedea statusu in care schimb
        for(Employee employee : system.getEmployees()){
            if(employee.getId() == modEmpId){
                for(Task task : system.getTasksForEmployee(employee.getId())){
                    if(task.getId() == modTaskId){
                        wantedStatus = task.getStatus();
                    }
                }
            }
        }

        if (!system.isEmployeeIdTaken(modEmpId)) {
            view.setOpStatus("Error: no employee with Id " + modEmpId + " exists.");
            return;
        }

        boolean success = system.modifyTaskStatus(modEmpId, modTaskId);
        if (success) {
            view.setOpStatus("Task " + modTaskId + " status changed for employee " + modEmpId + " ( old:  " + wantedStatus + " )");
        } else {
            view.setOpStatus("Error: task Id " + modTaskId + " not found for employee " + modEmpId + ".");
        }
    }

    private void handleCalcDuration() {
        int calcEmpId = Integer.parseInt(view.getOpEmpIdTextField().getText().trim());

        if (!system.isEmployeeIdTaken(calcEmpId)) {
            view.setOpStatus("Error: no employee with Id " + calcEmpId + " exists.");
            return;
        }

        int duration = system.calculateEmployeeWorkDuration(calcEmpId);
        view.setOpStatus("Employee " + calcEmpId + " completed work duration: " + duration + "h.");
    }

    private void handleViewAll() {
        StringBuilder sb = new StringBuilder("~~~ Employee List ~~~\n");
        for (Employee employee : system.getEmployees()) {
            sb.append("_________________________________________________________________\n")
            .append("Id: ").append(employee.getId()).append(" | Name: ").append(employee.getName()).append("\n");

            List<Task> tasks = system.getTasksForEmployee(employee.getId());
            if (tasks.isEmpty()) {
                sb.append("  - no tasks assigned\n");
            } else {
                for (Task task : tasks) {
                    sb.append("  - Task Id: ").append(task.getId())
                            .append(" | Status: ").append(task.getStatus())
                            .append(" | Estimated duration: ").append(task.estimateDuration()).append("h\n");
                }
            }
            sb.append("\n");
        }
        if (system.getEmployees().isEmpty()) {
            sb.append("No employees added yet.\n");
        }
        view.getDisplayArea().setText(sb.toString());
    }

    private void handleViewUnassigned() {
        StringBuilder sb = new StringBuilder("~~~ Unassigned Tasks ~~~\n");
        if (unassignedTaskList.isEmpty()) {
            sb.append("No available unassigned tasks.\n");
        } else {
            int index = 0;
            for (Task task : unassignedTaskList) {
                String type = (task instanceof SimpleTask) ? "Simple" : "Complex";
                sb.append("[").append(index).append("] ")
                        .append(type).append(" Task | Id: ").append(task.getId())
                        .append(" | Status: ").append(task.getStatus())
                        .append(" | Estimated duration: ").append(task.estimateDuration()).append("h\n");
                index++;
            }
        }
        view.getDisplayArea().setText(sb.toString());
    }

    private void handleViewStats() {
        StringBuilder statsSb = new StringBuilder("~~~ Task Statistics ~~~\n");
        Map<String, Map<String, Integer>> stats = utils.computeTaskStatistic();
        for (String empName : stats.keySet()) {
            statsSb.append(empName).append(": ").append(stats.get(empName)).append("\n");
        }
        if (stats.isEmpty()) {
            statsSb.append("No employees added yet.\n");
        }
        view.getDisplayArea().setText(statsSb.toString());
    }

    private void handleFilterEmployees() {
        view.getDisplayArea().setText(utils.orderWorkedEmployees());
    }

    private boolean isTaskIdTakenAnywhere(int taskId) {
        for (Task task : unassignedTaskList) {
            if (isIdInTask(task, taskId)) {
                return true;
            }
        }
        return system.isTaskIdTaken(taskId);
    }

    private boolean isIdInTask(Task task, int taskId) {
        if (task.getId() == taskId) {
            return true;
        }
        if (task instanceof ComplexTask) {
            for (Task subTask : ((ComplexTask) task).getTaskList()) {
                if (isIdInTask(subTask, taskId)) {
                    return true;
                }
            }
        }
        return false;
    }
}