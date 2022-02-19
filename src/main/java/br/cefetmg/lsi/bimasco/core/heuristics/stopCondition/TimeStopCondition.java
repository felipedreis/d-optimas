package br.cefetmg.lsi.bimasco.core.heuristics.stopCondition;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;
import br.cefetmg.lsi.bimasco.settings.MetaHeuristicParameters;

import java.util.Map;

public class TimeStopCondition implements StopCondition {

    private final Problem problem;
    private boolean isSatisfied;
    private double time;

    public TimeStopCondition(Problem problem) {
        this.problem = problem;
    }

    @Override
    public boolean isSatisfied(Object f0, double stopTime, int maxIterations, int maxIterationsWI, Map<String, Object> metaHeuristicParameters) {

        this.isSatisfied = false;

        if (metaHeuristicParameters.containsKey(DefaultMetaHeuristicParametersKeySupported.TIME_KEY)) {
            this.time = (Double) metaHeuristicParameters.get(DefaultMetaHeuristicParametersKeySupported.TIME_KEY);

            if (stopTime > this.time) {
                this.isSatisfied = true;
            }
        }
        return this.isSatisfied;
    }
}