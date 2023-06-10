package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution;

import br.cefetmg.lsi.bimasco.core.solutions.BenchmarkSolution;
import br.cefetmg.lsi.bimasco.core.solutions.FunctionSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;
import br.cefetmg.lsi.bimasco.core.solutions.motion.BenchmarkPerformsMotion;
import br.cefetmg.lsi.bimasco.core.solutions.motion.PerformsMotionHelper;
import br.cefetmg.lsi.bimasco.core.solutions.motion.RealPerformsMotion;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class BenchmarkRandomSolutionModifier
        extends SolutionModifier<FunctionSolutionElement, BenchmarkSolution> {
    private static final Logger logger = LoggerFactory.getLogger(RealRandomSolutionModifier.class);

    private RandomDataGenerator rnd;
    private BenchmarkPerformsMotion performsMotion;


    public BenchmarkRandomSolutionModifier(Map<String, Object> parameters) {
        super(parameters);
        rnd = new RandomDataGenerator();
        performsMotion = (BenchmarkPerformsMotion) PerformsMotionHelper
                .buildPerformsMotion("Benchmark");
    }

    public BenchmarkSolution modify(BenchmarkSolution solution, List<Integer> position,
                                   List<FunctionSolutionElement> parameter, Integer motionsCount){
        logger.debug(format("Running modification on solution %s", solution));

        double step;

        // TODO Benchmark Problems don't have step property
        step = (Double) parameters.get("step");

        logger.debug(format("Performing random %d motions", motionsCount));

        double value = 0.0;

        BenchmarkSolution result = solution;

        for(int i = 0; i < motionsCount; i++){

            int index = rnd.nextInt(0, solution.getProblem().getDimension() - 1);

            //Rever este método
            double variacao = Math.pow(-1, rnd.nextInt(1,2))
                    * rnd.nextUniform(0, 1) * step;

            value += solution.getDeviation(value, variacao, index);

            logger.debug(format("Variation: %f", variacao));
            logger.debug(format("Moving position %d to value %f", index, value));

            result = performsMotion.movement(result, List.of(index),
                    List.of(new FunctionSolutionElement(value)));

            logger.debug(format("Solution after motion %s", result));
        }

        return result;
    }
}
