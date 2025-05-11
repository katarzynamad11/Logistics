package org.example.cpmethod;
import java.util.List;


public class BackendResponse {
    private String ganttUrl;
    private String diagramUrl;
    private List<ResultRow> results;

    public BackendResponse(String ganttUrl, String diagramUrl, List<ResultRow> results) {
        this.ganttUrl = ganttUrl;
        this.diagramUrl = diagramUrl;
        this.results = results;
    }

    // Gettery
    public String getGanttUrl() { return ganttUrl; }
    public String getDiagramUrl() { return diagramUrl; }
    public List<ResultRow> getResults() { return results; }
}

