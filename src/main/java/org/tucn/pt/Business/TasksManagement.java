package org.tucn.pt.Business;

import org.tucn.pt.DataModel.ComplexTask;
import org.tucn.pt.DataModel.Employee;
import org.tucn.pt.DataModel.SimpleTask;
import org.tucn.pt.DataModel.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksManagement implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<Employee, List<Task>> employeeTaskMap = new HashMap<>();
    private List<Employee> employees = new ArrayList<>();

    public List<Employee> getEmployees() {
        return employees;
    }

    public boolean isEmployeeIdTaken(int idEmployee) {
        for (Employee employee : employees) {
            if (employee.getId() == idEmployee) {
                return true;
            }
        }
        return false;
    }

    public boolean isTaskIdTaken(int idTask) {
        for (Employee employee : employeeTaskMap.keySet()) {
            for (Task task : employeeTaskMap.get(employee)) {
                if (isIdInTask(task, idTask)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isIdInTask(Task task, int idTask) {
        if (task.getId() == idTask) {
            return true;
        }
        if (task instanceof ComplexTask) {
            for (Task subTask : ((ComplexTask) task).getTaskList()) {
                if (isIdInTask(subTask, idTask)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        employeeTaskMap.put(employee, new ArrayList<>());
    }

    public boolean assignTaskToEmployee(int idEmployee, Task task) {
        for (Employee employee : employeeTaskMap.keySet()) {
            if (employee.getId() == idEmployee) {
                employeeTaskMap.get(employee).add(task);
                return true;
            }
        }
        return false;
    }

    public int calculateEmployeeWorkDuration(int idEmployee) {
        int totalTime = 0;

        for (Employee employee : employeeTaskMap.keySet()) {
            if (employee.getId() == idEmployee) {
                for (Task task : employeeTaskMap.get(employee)) {
                    if (task.getStatus().equals("Completed")) {
                        totalTime += task.estimateDuration();
                    }
                }
                break;
            }
        }
        return totalTime;
    }

    public boolean modifyTaskStatus(int idEmployee, int idTask) {
        for (Employee employee : employeeTaskMap.keySet()) {
            if (employee.getId() == idEmployee) {
                return changeTaskStatus(employeeTaskMap.get(employee), idTask);
            }
        }
        return false;
    }

    private boolean changeTaskStatus(List<Task> tasks, int idTask) {
        for (Task task : tasks) {
            if(task instanceof ComplexTask){
                for(Task subTask : ((ComplexTask) task).getTaskList()){
                    if(subTask.getId() == idTask){
                        subTask.setStatus(task.getStatus().equals("Completed") ? "Uncompleted" : "Completed");
                        return true;
                    }
                }
            }

            if (task.getId() == idTask) {
                task.setStatus(task.getStatus().equals("Completed") ? "Uncompleted" : "Completed");
                return true;
            }

//            if (task instanceof ComplexTask) {
//                if (changeTaskStatus(((ComplexTask) task).getTaskList(), idTask)) {
//                    return true;
//                }
//            }
        }
        return false;
    }

    public List<Task> getTasksForEmployee(int idEmployee) {
        for (Employee employee : employeeTaskMap.keySet()) {
            if (employee.getId() == idEmployee) {
                return employeeTaskMap.get(employee);
            }
        }
        return new ArrayList<>();
    }
}