package org.tucn.pt.DataModel;

public final class SimpleTask extends Task {
    private static final long serialVersionUID = 1L;

    private int startHour;
    private int endHour;

    public SimpleTask(int idTask, String statusTask, int startHour, int endHour) {
        super(idTask, statusTask);
        this.startHour = startHour;
        this.endHour = endHour;
    }

    @Override
    public int estimateDuration() {
        if (endHour >= startHour) {
            return endHour - startHour;
        } else {
            return (24 - startHour) + endHour;
        }
    }
}