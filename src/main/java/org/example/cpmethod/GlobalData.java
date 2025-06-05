package org.example.cpmethod;

public class GlobalData {
    private static GlobalData instance;
    private String solutionJson;

    private GlobalData() {
    }

    public static GlobalData getInstance() {
        if (instance == null) {
            instance = new GlobalData();
        }
        return instance;
    }

    public String getSolutionJson() {
        return solutionJson;
    }

    public void setSolutionJson(String json) {
        this.solutionJson = json;
    }
}