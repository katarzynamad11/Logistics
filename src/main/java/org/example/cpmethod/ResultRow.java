package org.example.cpmethod;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignoruj pola, które nie istnieją w tej klasie (np. id)
public class ResultRow {

    @JsonProperty("task")
    private String task;

    @JsonProperty("earliestStart")
    private int earliestStart;

    @JsonProperty("earliestFinish")
    private int earliestFinish;

    @JsonProperty("latestStart")
    private int latestStart;

    @JsonProperty("latestFinish")
    private int latestFinish;

    @JsonProperty("slack")
    private int slack;

    @JsonProperty("duration")
    private int duration;

    @JsonProperty("isCritical")
    private boolean isCritical;

    // Pusty konstruktor (wymagany przez Jackson)
    public ResultRow() {
    }

    // Gettery i Settery
    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getEarliestStart() {
        return earliestStart;
    }

    public void setEarliestStart(int earliestStart) {
        this.earliestStart = earliestStart;
    }

    public int getEarliestFinish() {
        return earliestFinish;
    }

    public void setEarliestFinish(int earliestFinish) {
        this.earliestFinish = earliestFinish;
    }

    public int getLatestStart() {
        return latestStart;
    }

    public void setLatestStart(int latestStart) {
        this.latestStart = latestStart;
    }

    public int getLatestFinish() {
        return latestFinish;
    }

    public void setLatestFinish(int latestFinish) {
        this.latestFinish = latestFinish;
    }

    public int getSlack() {
        return slack;
    }

    public void setSlack(int slack) {
        this.slack = slack;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isCritical() {
        return isCritical;
    }

    public void setCritical(boolean critical) {
        isCritical = critical;
    }
}
