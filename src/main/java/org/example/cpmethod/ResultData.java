package org.example.cpmethod;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ResultData {
    private final StringProperty activity;
    private final StringProperty duration;
    private final StringProperty es; // Early Start
    private final StringProperty ef; // Early Finish
    private final StringProperty ls; // Late Start
    private final StringProperty lf; // Late Finish
    private final StringProperty slack; // Rezerwa
    private final StringProperty isCritical; // Czynność krytyczna

    public ResultData(String activity, String duration, String es, String ef, String ls, String lf, String slack, String isCritical) {
        this.activity = new SimpleStringProperty(activity);
        this.duration = new SimpleStringProperty(duration);
        this.es = new SimpleStringProperty(es);
        this.ef = new SimpleStringProperty(ef);
        this.ls = new SimpleStringProperty(ls);
        this.lf = new SimpleStringProperty(lf);
        this.slack = new SimpleStringProperty(slack);
        this.isCritical = new SimpleStringProperty(isCritical);
    }

    // Gettery
    public String getActivity() { return activity.get(); }
    public String getDuration() { return duration.get(); }
    public String getEs() { return es.get(); }
    public String getEf() { return ef.get(); }
    public String getLs() { return ls.get(); }
    public String getLf() { return lf.get(); }
    public String getSlack() { return slack.get(); }
    public String getIsCritical() { return isCritical.get(); }

    // Settery
    public void setActivity(String value) { activity.set(value); }
    public void setDuration(String value) { duration.set(value); }
    public void setEs(String value) { es.set(value); }
    public void setEf(String value) { ef.set(value); }
    public void setLs(String value) { ls.set(value); }
    public void setLf(String value) { lf.set(value); }
    public void setSlack(String value) { slack.set(value); }
    public void setIsCritical(String value) { isCritical.set(value); }

    // Properties do powiązania z TableView
    public StringProperty activityProperty() { return activity; }
    public StringProperty durationProperty() { return duration; }
    public StringProperty esProperty() { return es; }
    public StringProperty efProperty() { return ef; }
    public StringProperty lsProperty() { return ls; }
    public StringProperty lfProperty() { return lf; }
    public StringProperty slackProperty() { return slack; }
    public StringProperty isCriticalProperty() { return isCritical; }
}
