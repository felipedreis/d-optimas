package br.cefetmg.lsi.bimasco.core;

import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;

import java.util.concurrent.atomic.AtomicLong;

public class AgentContext implements Context {

    private String agentName;

    private AtomicLong invocations;

    private Solution<?,?,?> bestSolution;

    private SolutionAnalyser analyser;

    public AgentContext(String agentName, SolutionAnalyser analyser) {
        this.agentName = agentName;
        this.analyser = analyser;
        invocations = new AtomicLong(0);
    }

    @Override
    public void accept(final Solution<?, ?, ?> solution) {
        invocations.incrementAndGet();

        if (bestSolution == null)
            bestSolution = (Solution<?, ?, ?>) solution.clone();
        else
            bestSolution = analyser.getBestSolution(bestSolution, solution);
    }

    @Override
    public void resetContext() {
        invocations.set(0);
        bestSolution = null;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public AtomicLong getInvocations() {
        return invocations;
    }

    public void setInvocations(AtomicLong invocations) {
        this.invocations = invocations;
    }

    public Solution<?, ?, ?> getBestSolution() {
        return bestSolution;
    }

    public void setBestSolution(Solution<?, ?, ?> bestSolution) {
        this.bestSolution = bestSolution;
    }

    public SolutionAnalyser getAnalyser() {
        return analyser;
    }

    public void setAnalyser(SolutionAnalyser analyser) {
        this.analyser = analyser;
    }
}
