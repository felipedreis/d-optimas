/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.lsi.bimasco.core.solutions;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.problems.BinaryPartitionNumberProblem;
import br.cefetmg.lsi.bimasco.core.solutions.element.BinaryPartitionNumberElementSolution;

import java.util.ArrayList;
import java.util.List;

public class BinaryPartitionNumberSolution extends Solution<BinaryPartitionNumberElementSolution, Integer, BinaryPartitionNumberProblem> {

    public BinaryPartitionNumberSolution(Problem problem) {
        super(problem);
    }

    public BinaryPartitionNumberSolution(BinaryPartitionNumberSolution s) {
        super(s);
    }

    @Override
    public void initialize() {
        dimension = getProblem().getDimension();
        functionValue = Integer.MAX_VALUE;
    }

    @Override
    public void generateInitialSolution() {
    }

    @Override
    protected void checkViability() {
        long elementsOnPartition = getSolutionsVector().stream()
                .map(BinaryPartitionNumberElementSolution::isPresent)
                .filter(Boolean::booleanValue)
                .count();
        viable = elementsOnPartition < getSolutionsVector().size();
    }

    @Override
    public Integer adaptiveFunctionValue(BinaryPartitionNumberElementSolution value) {
        if (getSolutionsVector().size() == getProblem().getDimension())
            return null;

        int sum = getWeightSum();
        int partition = 0;
        List<BinaryPartitionNumberElementSolution> values = new ArrayList<>(getSolutionsVector());
        values.add(value);

        for (int i = 0; i < values.size(); ++i) {
            BinaryPartitionNumberElementSolution element = values.get(i);

            if (element.isPresent())
                partition += getProblem().getWeights().get(i);
        }
        return Math.abs(sum - partition);
    }

    public void objectiveFunction(){
        int partition = 0;
        int sum = getWeightSum();
        for (int i = 0; i < solutionsVector.size(); ++i) {
            BinaryPartitionNumberElementSolution element = solutionsVector.get(i);

            if (element.isPresent())
                partition += getProblem().getWeights().get(i);

        }

        functionValue = Math.abs(partition - sum);
    }   

    public int getWeightSum() {
        return  getProblem().getWeights()
                .stream().reduce(Integer::sum).orElse(0);
    }

    @Override
    public int compareTo(Solution o) {
        return 0;
    }

    @Override
    public Object clone() {
        return new BinaryPartitionNumberSolution(this);
    }
}
