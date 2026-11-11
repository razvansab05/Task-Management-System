package org.tucn.pt.DataModel;

import java.io.Serializable;

public sealed abstract class Task implements Serializable permits SimpleTask, ComplexTask {
    private static final long serialVersionUID = 1L;

    private int idTask;
    private String statusTask;

    public Task(int idTask, String statusTask) {
        this.idTask = idTask;
        this.statusTask = statusTask;
    }

    public abstract int estimateDuration();

    public int getId() {
        return idTask;
    }

    public String getStatus() {
        return statusTask;
    }

    public void setStatus(String statusTask) {
        this.statusTask = statusTask;
    }
}