package org.tucn.pt.DataModel;

import java.util.ArrayList;
import java.util.List;

public final class ComplexTask extends Task {
    private static final long serialVersionUID = 1L;

    private List<Task> taskList = new ArrayList<>();

    public ComplexTask(int idTask, String statusTask) {
        super(idTask, statusTask);
    }

    public void addTask(Task task) {
        taskList.add(task);
    }

    public void deleteTask(Task task) {
        taskList.remove(task);
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    @Override
    public int estimateDuration() {
        int totalDuration = 0;

        for (Task task : taskList) {
            totalDuration += task.estimateDuration();
        }

        return totalDuration;
    }
}