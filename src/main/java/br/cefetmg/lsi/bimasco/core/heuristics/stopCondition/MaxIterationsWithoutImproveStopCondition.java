package br.cefetmg.lsi.bimasco.core.heuristics.stopCondition;


import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Map;

import static java.lang.String.format;

public class MaxIterationsWithoutImproveStopCondition implements StopCondition {
    private static final Logger logger = LoggerFactory.getLogger(MaxIterationsWithoutImproveStopCondition.class);

    private final Problem problem;
    private boolean isSatisfied;
    private Integer maxIterationsWithoutImprove;


    public MaxIterationsWithoutImproveStopCondition(Problem problem) {
        this.problem = problem;
    }

    @Override
    public boolean isSatisfied(Object f0, double stopTime, int maxIterations, int maxIterationsWI, Map<String, Object> metaHeuristicParameters) {

        this.isSatisfied = false;

        logger.debug("Checking max iteration stop condition");
        if (metaHeuristicParameters.containsKey(DefaultMetaHeuristicParametersKeySupported.MAX_ITERATIONS_WITHOUT_IMPROVE_KEY)) {
            this.maxIterationsWithoutImprove = (Integer) metaHeuristicParameters
                    .get(DefaultMetaHeuristicParametersKeySupported.MAX_ITERATIONS_WITHOUT_IMPROVE_KEY);

            logger.debug(format("Config max iterations without improvement: %d", this.maxIterationsWithoutImprove));
            logger.debug(format("Current iteration without improvement: %d", maxIterationsWithoutImprove));

            if (maxIterationsWI > this.maxIterationsWithoutImprove) {
                this.isSatisfied = true;
                logger.debug("Condition satisfied");
            }
        }

        return isSatisfied;
    }
}
