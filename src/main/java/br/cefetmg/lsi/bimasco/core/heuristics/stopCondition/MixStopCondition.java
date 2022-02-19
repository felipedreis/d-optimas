package br.cefetmg.lsi.bimasco.core.heuristics.stopCondition;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;

import java.util.Map;

public class MixStopCondition implements StopCondition {

    private final Problem problem;
    private boolean isSatisfied;
    private Integer maxIterations;
    private Integer maxIterationsWithoutImprove;
    private double time;
    private Object objectiveFunction;
    private SolutionAnalyser solutionAnalyser;

    public MixStopCondition(Problem problem) {
        this.problem = problem;
    }

    @Override
    public boolean isSatisfied(Object f0, double stopTime, int maxIterations, int maxIterationsWI, Map<String, Object> metaHeuristicParameters) {
        isSatisfied = false;

        if (metaHeuristicParameters.containsKey(DefaultMetaHeuristicParametersKeySupported.MAX_NUMBER_OF_ITERATIONS_KEY)) {
            maxIterations = (Integer) metaHeuristicParameters
                    .get(DefaultMetaHeuristicParametersKeySupported.MAX_NUMBER_OF_ITERATIONS_KEY);

            if (maxIterations > this.maxIterations) {
                this.isSatisfied = true;
            }
        } else if (metaHeuristicParameters.containsKey(DefaultMetaHeuristicParametersKeySupported.MAX_ITERATIONS_WITHOUT_IMPROVE_KEY)) {
            this.maxIterationsWithoutImprove = (Integer) metaHeuristicParameters
                    .get(DefaultMetaHeuristicParametersKeySupported.MAX_ITERATIONS_WITHOUT_IMPROVE_KEY);

            if (maxIterationsWI > this.maxIterationsWithoutImprove) {
                this.isSatisfied = true;
            }
        } else if (metaHeuristicParameters.containsKey(DefaultMetaHeuristicParametersKeySupported.TIME_KEY)) {
            this.time = (Double) metaHeuristicParameters.get(DefaultMetaHeuristicParametersKeySupported.TIME_KEY);

            if (stopTime > this.time) {
                this.isSatisfied = true;
            }
        } else if (metaHeuristicParameters.containsKey(DefaultMetaHeuristicParametersKeySupported.F0_KEY)) {

            solutionAnalyser = SolutionAnalyser.buildSolutionAnalyser(problem);

            objectiveFunction = metaHeuristicParameters.get(DefaultMetaHeuristicParametersKeySupported.F0_KEY);

            //if (this.solutionAnalyser.getBest(f0, this.objectiveFunction).equals(f0)) {
            //    this.isSatisfied = true;
            //}
        }

        return this.isSatisfied;
    }
}
