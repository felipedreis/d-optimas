package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections;

import br.cefetmg.lsi.bimasco.core.Element;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.problems.BenchmarkProblem;
import br.cefetmg.lsi.bimasco.core.solutions.BenchmarkSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class DEBenchmarkProblemSumModifiesSolutionCollections extends ModifiesSolutionCollections <BenchmarkProblem, BenchmarkSolution> {

    private static final Logger logger = LoggerFactory.getLogger(DEFunctionProblemSumModifiesSolutionCollections.class);

    private RandomDataGenerator rdg;

    public DEBenchmarkProblemSumModifiesSolutionCollections(Problem problem) {
        super(problem);
        rdg = new RandomDataGenerator();
    }

    @Override
    public List<BenchmarkSolution> modify(List<BenchmarkSolution> solutionsList,
                                         Map<String, Object> metaHeuristicParameters, Integer solutionsCount) {

        int dimension = problem.getDimension();
        BenchmarkSolution s = solutionsList.get(0);
        BenchmarkSolution r1 = solutionsList.get(1);
        BenchmarkSolution r2 = solutionsList.get(2);
        BenchmarkSolution r3 = solutionsList.get(3);
        int delta = rdg.nextInt(0, dimension);

        double probC = (Double) metaHeuristicParameters.get("probC");
        double scaleFactor = (Double) metaHeuristicParameters.get("scaleFactor");
        Solution solution =  Solution.buildSolution(problem);

        for (int j = 0; j < dimension; ++j) {
            Element element;
            if (rdg.nextUniform(0,1) < probC || j == delta) {
                logger.debug(format("Using differential formula to position %d of to calculate next vector", j));
                double e = r1.getElement(j).toDoubleValue() + scaleFactor * (r2.getElement(j).toDoubleValue()
                        - r3.getElement(j).toDoubleValue());
                element = new FunctionSolutionElement(e);
            } else {
                logger.debug(format("Using %d of previous vector", j));
                element = new FunctionSolutionElement(s.getElement(j).toDoubleValue());
            }
            logger.debug(format("X(%d) = %f", j, element.toDoubleValue()));
            solution.addElement(element);
        }

        return List.of((BenchmarkSolution) solution);
    }
}
