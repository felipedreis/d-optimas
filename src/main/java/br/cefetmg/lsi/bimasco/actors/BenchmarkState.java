package br.cefetmg.lsi.bimasco.actors;

import br.cefetmg.lsi.bimasco.coco.Observer;
import br.cefetmg.lsi.bimasco.coco.Suite;
import br.cefetmg.lsi.bimasco.core.problems.BenchmarkProblem;

public class BenchmarkState {

    private BenchmarkProblem problem;

    private Suite suite;

    private Observer observer;

    private int problemCounter;

    private long evaluations;

    private long evaluationBudget;

    public BenchmarkState(BenchmarkProblem problem, Suite suite, Observer observer, int problemCounter, long evaluations, long evaluationBudget) {
        this.problem = problem;
        this.suite = suite;
        this.observer = observer;
        this.problemCounter = problemCounter;
        this.evaluations = evaluations;
        this.evaluationBudget = evaluationBudget;
    }

    public BenchmarkProblem getProblem() {
        return problem;
    }

    public void setProblem(BenchmarkProblem problem) {
        this.problem = problem;
    }

    public Suite getSuite() {
        return suite;
    }

    public void setSuite(Suite suite) {
        this.suite = suite;
    }

    public Observer getObserver() {
        return observer;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public int getProblemCounter() {
        return problemCounter;
    }

    public void setProblemCounter(int problemCounter) {
        this.problemCounter = problemCounter;
    }

    public long getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(long evaluations) {
        this.evaluations = evaluations;
    }

    public long getEvaluationBudget() {
        return evaluationBudget;
    }

    public void setEvaluationBudget(long evaluationBudget) {
        this.evaluationBudget = evaluationBudget;
    }
}
