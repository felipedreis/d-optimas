package br.cefetmg.lsi.bimasco.core.solutions;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.problems.MOProblem;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;
import br.cefetmg.lsi.bimasco.util.EuclideanVector;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.ArrayList;
/*
TODO: Implement a sort of proxy to solutions. Now every time you change the decision variable you have to recalculate
 everything explicitly. The Abstract class should provide you interfaces to interact with solution and manage when
 is needed to recalculate objective function, while the concrete class should just provide the means to calculate things.

 */
public class MOSolution extends Solution<FunctionSolutionElement, EuclideanVector<Double>, MOProblem> {

    public MOSolution(Problem p) {
        super(p);
    }

    @Override
    public void initialize() {
        solutionsVector = new ArrayList<>(dimension);
        generateInitialSolution();
    }

    @Override
    public void generateInitialSolution() {
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
        for (int i = 0; i < getProblem().getDimension(); ++i) {
            double lower = getProblem().getVariablesLowerLimits().get(i),
                    upper = getProblem().getVariablesUpperLimits().get(i);

            //setElement(i, randomDataGenerator.nextUniform(lower, upper));
        }
        objectiveFunction();
    }

    @Override
    protected void objectiveFunction() {
        //functionValue = getProblem().evaluate(solutionsVector);
    }

    @Override
    protected void checkViability() {
        viable = true;
        for (int i = 0; i < getProblem().getObjectives(); ++i) {
            if (functionValue.get(i) < getProblem().getObjectiveLowerLimits().get(i))
                viable = false;
            if (functionValue.get(i) > getProblem().getObjectiveUpperLimits().get(i))
                viable = false;
        }
    }

    /**
     * Returns if this solution dominates parameter o
     * @param o other solution, should be an instance o MOSolution
     * @return -1 if this dominates o, 0 if they are equal and 1 if this doesn't dominate o.
     */
    @Override
    public int compareTo(Solution o) {
        if (equals(o)) {
            return 0;
        } else if (o instanceof MOSolution) {
            MOSolution other = (MOSolution) o;
            boolean dominates = true;

            for (int i = 0; i < getProblem().getObjectives() && dominates; ++i) {
                if (functionValue.get(i) > other.functionValue.get(i))
                    dominates = false;
            }

            return dominates? -1 : 1;
        } else {
            throw new IllegalArgumentException("Solutions must have the concrete same type");
        }
    }
}
