package org.example.cpmethod;

public class ResultRow {
    private String task;
    private int duration;
    private int earliestStart;
    private int earliestFinish;
    private int latestStart;
    private int latestFinish;
    private boolean isCritical;

    // Konstruktor
    public ResultRow(String task, int duration, int es, int ef, int ls, int lf, boolean isCritical) {
        this.task = task;
        this.duration = duration;
        this.earliestStart = es;
        this.earliestFinish = ef;
        this.latestStart = ls;
        this.latestFinish = lf;
        this.isCritical = isCritical;
    }

    // Gettery
    public String getTask() { return task; }
    public int getDuration() { return duration; }
    public int getEarliestStart() { return earliestStart; }
    public int getEarliestFinish() { return earliestFinish; }
    public int getLatestStart() { return latestStart; }
    public int getLatestFinish() { return latestFinish; }
    public int getSlack() {
        return (latestStart - earliestStart); // lub latestFinish - earliestFinish
    }
    public boolean isCritical() {
        return getSlack() == 0;
    }
}
