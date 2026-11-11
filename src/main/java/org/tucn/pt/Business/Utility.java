package org.tucn.pt.Business;

import org.tucn.pt.DataModel.Employee;
import org.tucn.pt.DataModel.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utility {
    private TasksManagement system;
    private List<Employee> employees;

    public Utility(TasksManagement system, List<Employee> employees) {
        this.system = system;
        this.employees = employees;
    }

    public String orderWorkedEmployees() {
        List<Employee> filteredEmployees = new ArrayList<>();

        for (Employee employee : employees) {
            if (system.calculateEmployeeWorkDuration(employee.getId()) > 40) {
                filteredEmployees.add(employee);
            }
        }

        filteredEmployees.sort(Comparator.comparingInt(e -> system.calculateEmployeeWorkDuration(e.getId())));

        StringBuilder sb = new StringBuilder("~~~ Employees with work duration > 40h (ascending) ~~~\n");
        for (Employee employee : filteredEmployees) {
            int duration = system.calculateEmployeeWorkDuration(employee.getId());
            sb.append(employee.getName()).append(" — ").append(duration).append("h\n");
        }

        if (filteredEmployees.isEmpty()) {
            sb.append("No employees with work duration > 40h.\n");
        }

        return sb.toString();
    }

    public Map<String, Map<String, Integer>> computeTaskStatistic() {
        Map<String, Map<String, Integer>> employeeTaskStatistic = new HashMap<>();

        for (Employee employee : employees) {
            Map<String, Integer> taskStatistic = new HashMap<>();
            int completed = 0;
            int uncompleted = 0;

            for (Task task : system.getTasksForEmployee(employee.getId())) {
                if (task.getStatus().equals("Completed")) {
                    completed++;
                } else if (task.getStatus().equals("Uncompleted")) {
                    uncompleted++;
                }
            }

            taskStatistic.put("Completed", completed);
            taskStatistic.put("Uncompleted", uncompleted);
            employeeTaskStatistic.put(employee.getName(), taskStatistic);
        }

        return employeeTaskStatistic;
    }
}