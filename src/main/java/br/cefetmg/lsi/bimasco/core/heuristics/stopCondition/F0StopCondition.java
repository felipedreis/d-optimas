package br.cefetmg.lsi.bimasco.core.heuristics.stopCondition;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;
import org.apache.log4j.Logger;

import java.util.Map;

import static java.lang.String.format;

public class F0StopCondition implements StopCondition {

    private final Problem problem;
    private boolean isSatisfied;
    private Object objectiveFunction;
    private SolutionAnalyser solutionAnalyser;

    private static Logger logger = Logger.getLogger(F0StopCondition.class);

    public F0StopCondition(Problem problem) {
        this.problem = problem;
    }

    @Override
    public boolean isSatisfied(Object f0, double stopTime, int maxIterations, int maxIterationsWI, Map<String, Object> metaHeuristicParameters) {
        int counter = 0;
        this.isSatisfied = false;

        logger.debug("Checking max iteration stop condition");
        this.solutionAnalyser = SolutionAnalyser.buildSolutionAnalyser(problem);

        if (metaHeuristicParameters.containsKey(DefaultMetaHeuristicParametersKeySupported.F0_KEY)) {

            this.objectiveFunction = metaHeuristicParameters.get(DefaultMetaHeuristicParametersKeySupported.F0_KEY);

            logger.debug(format("Config f0: %s", this.objectiveFunction));
            logger.debug(format("Current iteration: %s", f0));

            //if (this.solutionAnalyser.getBest(f0, this.objectiveFunction).equals(f0)) {
            //    this.isSatisfied = true;
            //}
        }

        return this.isSatisfied;
    }
}