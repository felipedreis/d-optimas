package br.cefetmg.lsi.bimasco.settings;

import com.typesafe.config.Config;

import java.io.Serializable;
import java.util.List;

public class ProblemSettings implements Serializable {

    private String name;
    private String type;
    private String description;
    private Boolean isMax;
    private String classPath;

    //TODO: Make this problemData as the older Instance file
    //TODO: Think if this way is the best way
    //TODO: We need think really about this, what we can put here ?
    List<List> problemData;

    //TODO: Check if this approach is interesting
    // (Or maybe put a classpath)
    // (Or maybe yet put a convention to take a name when this field is empty
    private String solutionAnalyserName;

    public ProblemSettings() {
    }

    public ProblemSettings(String name, String type, String description, Boolean isMax, String classPath,
                           List<List> problemData, String solutionAnalyserName) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.isMax = isMax;
        this.classPath = classPath;
        this.problemData = problemData;
        this.solutionAnalyserName = solutionAnalyserName;
    }

    public ProblemSettings(Config config){
        name = config.getString("name");
        type = config.getString("type");
        isMax = config.getBoolean("isMax");
        classPath = config.getString("classPath");
        solutionAnalyserName = config.getString("solutionAnalyserName");
        problemData = (List<List>) config.getAnyRefList("problemData");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getMax() {
        return isMax;
    }

    public void setMax(Boolean max) {
        isMax = max;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public List<List> getProblemData() {
        return problemData;
    }

    public void setProblemData(List<List> problemData) {
        this.problemData = problemData;
    }

    public String getSolutionAnalyserName() {
        return solutionAnalyserName;
    }

    public void setSolutionAnalyserName(String solutionAnalyserName) {
        this.solutionAnalyserName = solutionAnalyserName;
    }
}
