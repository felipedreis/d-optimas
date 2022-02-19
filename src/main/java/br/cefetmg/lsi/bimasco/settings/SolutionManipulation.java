package br.cefetmg.lsi.bimasco.settings;

import java.io.Serializable;

public class SolutionManipulation implements Serializable {

    private String type;
    private Boolean isActive;

    public SolutionManipulation() {
        this("", true);
    }

    public SolutionManipulation(String type) {
        this(type, true);
    }

    public SolutionManipulation(String type, Boolean isActive) {
        this.type = type;
        this.isActive = isActive;
    }

    public String getType() {
        return type;
    }

    public Boolean isActive() {
        return isActive;
    }
}
