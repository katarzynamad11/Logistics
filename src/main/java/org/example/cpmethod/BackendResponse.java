package org.example.cpmethod;

import java.util.List;

public class BackendResponse {
    private String ganttUrl;       // ASAP
    private String ganttALAPUrl;   // ALAP
    private String diagramUrl;
    private List<ResultRow> results;

    public BackendResponse(String ganttUrl, String ganttALAPUrl, String diagramUrl, List<ResultRow> results) {
        this.ganttUrl = ganttUrl;
        this.ganttALAPUrl = ganttALAPUrl;
        this.diagramUrl = diagramUrl;
        this.results = results;
    }

    // Gettery
    public String getGanttUrl() {
        return ganttUrl;
    }

    public String getGanttALAPUrl() {
        return ganttALAPUrl;
    }

    public String getDiagramUrl() {
        return diagramUrl;
    }

    public List<ResultRow> getResults() {
        return results;
    }

    // Settery
    public void setGanttUrl(String ganttUrl) {
        this.ganttUrl = ganttUrl;
    }

    public void setGanttALAPUrl(String ganttALAPUrl) {
        this.ganttALAPUrl = ganttALAPUrl;
    }

    public void setDiagramUrl(String diagramUrl) {
        this.diagramUrl = diagramUrl;
    }

    public void setResults(List<ResultRow> results) {
        this.results = results;
    }
}
