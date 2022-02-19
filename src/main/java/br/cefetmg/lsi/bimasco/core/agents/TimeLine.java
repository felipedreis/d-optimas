package br.cefetmg.lsi.bimasco.core.agents;

import java.io.Serializable;
import java.util.ArrayList;

public class TimeLine implements Serializable {

    private ArrayList<String[]> emittedStimuli;
    private ArrayList<String[]> receivedStimuli;
    private ArrayList<String[]> generatedSolutions;

    public TimeLine(){
        emittedStimuli = new ArrayList<String[]>();
        receivedStimuli = new ArrayList<String[]>();
        generatedSolutions = new ArrayList<String[]>();
    }

    public void addEmittedStimulus(String target, String type, String time){
        String[] emittedStimulus = new String[3];
        emittedStimulus[0] = target;
        emittedStimulus[1] = type;
        emittedStimulus[2] = time;
        emittedStimuli.add(emittedStimulus);
    }

    public void addReceivedStimulus(String emitter, String type, String time){
        String[] receivedStimulus = new String[3];
        receivedStimulus[0] = emitter;
        receivedStimulus[1] = type;
        receivedStimulus[2] = time;
        receivedStimuli.add(receivedStimulus);
    }

    public void addGeneratedSolutions(String solutionString, String time){
        String[] generatedSolution = new String[2];
        generatedSolution[0] = solutionString;
        generatedSolution[1] = time;
        generatedSolutions.add(generatedSolution);
    }

    public ArrayList<String[]> getEmittedStimuli() {
        return emittedStimuli;
    }

    public void setEmittedStimuli(ArrayList<String[]> emittedStimuli) {
        this.emittedStimuli = emittedStimuli;
    }

    public ArrayList<String[]> getReceivedStimuli() {
        return receivedStimuli;
    }

    public void setReceivedStimuli(ArrayList<String[]> receivedStimuli) {
        this.receivedStimuli = receivedStimuli;
    }

    public ArrayList<String[]> getGeneratedSolutions() {
        return generatedSolutions;
    }

    public void setGeneratedSolutions(ArrayList<String[]> generatedSolutions) {
        this.generatedSolutions = generatedSolutions;
    }
}
