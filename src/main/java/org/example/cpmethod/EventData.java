package org.example.cpmethod;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EventData {

    private final StringProperty lp;
    private final StringProperty name;
    private final StringProperty duration;
    private final StringProperty successors;

    public EventData(String lp, String name, String duration, String successors) {
        this.lp = new SimpleStringProperty(lp);
        this.name = new SimpleStringProperty(name);
        this.duration = new SimpleStringProperty(duration);
        this.successors = new SimpleStringProperty(successors);
    }

    // Gettery
    public String getLp() {
        return lp.get();
    }

    public String getName() {
        return name.get();
    }

    public String getDuration() {
        return duration.get();
    }

    public String getSuccessors() {
        return successors.get();
    }

    // Settery
    public void setLp(String value) {
        lp.set(value);
    }

    public void setName(String value) {
        name.set(value);
    }

    public void setDuration(String value) {
        duration.set(value);
    }

    public void setSuccessors(String value) {
        successors.set(value);
    }

    // Properties
    public StringProperty lpProperty() {
        return lp;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty durationProperty() {
        return duration;
    }

    public StringProperty successorsProperty() {
        return successors;
    }
}
