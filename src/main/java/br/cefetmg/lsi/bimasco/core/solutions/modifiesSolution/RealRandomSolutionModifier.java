package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution;

import br.cefetmg.lsi.bimasco.core.solutions.FunctionSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;
import br.cefetmg.lsi.bimasco.core.solutions.motion.PerformsMotionHelper;
import br.cefetmg.lsi.bimasco.core.solutions.motion.RealPerformsMotion;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class RealRandomSolutionModifier
        extends SolutionModifier<FunctionSolutionElement, FunctionSolution> {
    private static final Logger logger = LoggerFactory.getLogger(RealRandomSolutionModifier.class);

    private RandomDataGenerator rnd;
    private RealPerformsMotion performsMotion;


    public RealRandomSolutionModifier() {
        rnd = new RandomDataGenerator();
        performsMotion = (RealPerformsMotion) PerformsMotionHelper.buildPerformsMotion("Real");
    }

    public FunctionSolution modify(FunctionSolution solution, List<Integer> position,
                                   List<FunctionSolutionElement> parameter, Integer motionsCount){
        logger.debug(format("Running modification on solution %s", solution));

        double step;
        int numElementos;

        step = solution.getProblem().getStep();
        numElementos = solution.getProblem().getDimension();
        
        logger.debug(format("Performing random %d motions", motionsCount));

        List<Object> randPosicao;
        List<Object> aux = new ArrayList<Object>();
        double value = 0.0;
        int numVariaveis;

        for(int i=0; i<numElementos; i++){
            aux.add(i);
        }

        FunctionSolution result = solution;

        for(int i=0; i< motionsCount; i++){

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