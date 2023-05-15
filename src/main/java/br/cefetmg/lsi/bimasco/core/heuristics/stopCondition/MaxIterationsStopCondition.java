package br.cefetmg.lsi.bimasco.core.heuristics.stopCondition;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Map;

import static java.lang.String.format;

public class MaxIterationsStopCondition implements StopCondition {

    private final Problem problem;
    private boolean isSatisfied;
    private Integer maxIterations;

    private static Logger logger = LoggerFactory.getLogger(MaxIterationsStopCondition.class);

    public MaxIterationsStopCondition(Problem problem) {
        this.problem = problem;
    }

    @Override
    public boolean isSatisfied(Object f0, double stopTime, int maxIterations, int maxIterationsWI, Map<String, Object> metaHeuristicParameters) {
        this.isSatisfied = false;
        logger.debug("Checking max iteration stop condition");
        if (metaHeuristicParameters.containsKey(DefaultMetaHeuristicParametersKeySupported.MAX_NUMBER_OF_ITERATIONS_KEY)) {
            this.maxIterations = (Integer) metaHeuristicParameters
                    .get(DefaultMetaHeuristicParametersKeySupported.MAX_NUMBER_OF_ITERATIONS_KEY);

            logger.debug(format("Config max iterations: %d", this.maxIterations));
            logger.debug(format("Current iteration: %d", maxIterations));

            if (maxIterations > this.maxIterations) {
                this.isSatisfied = true;
                logger.debug("Condition satisfied");
            }
        } else {
            logger.warn("Didn't find proper configuration");
        }

        return this.isSatisfied;
    }
}
