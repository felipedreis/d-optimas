package br.cefetmg.lsi.bimasco.core;

import br.cefetmg.lsi.bimasco.core.utils.BimascoClassPath;
import br.cefetmg.lsi.bimasco.settings.ProblemSettings;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//TODO: analyse type of returns
public abstract class Problem implements Serializable  {

    private static final String TAG = Problem.class.getSimpleName();

    private ProblemSettings problemSettings;

    protected Integer dimension;

    private boolean nullInit;

    public Problem(){
        nullInit = false;
    }

    //TODO: Think about change this builder for buildProblem<T> (I think will be better)
    public static Problem buildProblem(ProblemSettings problemSettings) {

        Class problemClass;
        Problem problem = null;

        try {
            problemClass = Class.forName(String.format(BimascoClassPath.PROBLEMS, problemSettings.getName()));
            problem = (Problem)problemClass.newInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex){
            Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
        }

        problem.setProblemSettings(problemSettings);

        problem.initialize();

        return problem;
    }

    public void initialize() {

    }

    public Integer getDimension() {
        return dimension;
    }
    @Deprecated
    public abstract Integer getSolutionElementsCount();

    public Integer getObjectives() {
        return 1;
    };

    //TODO: Think if put this kind of method is a good thing
    public abstract double getStep();

    public abstract Object getFitnessFunction(List<Object> element);

    public abstract Object getLimit();

    public abstract List<Object> getParameters();

    public ProblemSettings getProblemSettings(){return this.problemSettings;}

    public void setProblemSettings(ProblemSettings problemSettings) {
        this.problemSettings = problemSettings;
    }

    public boolean isNullInit() {
        return nullInit;
    }

    public void setNullInit(boolean nullInit) {
        this.nullInit = nullInit;
    }
}