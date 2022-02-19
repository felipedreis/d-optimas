package br.cefetmg.lsi.bimasco.core.solutions.analyser;

import br.cefetmg.lsi.bimasco.core.problems.BinaryPartitionNumberProblem;
import br.cefetmg.lsi.bimasco.core.solutions.BinaryPartitionNumberSolution;

public class BinaryPartitionNumberSolutionAnalyser
        extends SolutionAnalyser<BinaryPartitionNumberProblem, BinaryPartitionNumberSolution> {

    public BinaryPartitionNumberSolutionAnalyser(BinaryPartitionNumberProblem problem) {
        super(problem);
    }

    public BinaryPartitionNumberSolution getBestSolution(BinaryPartitionNumberSolution A, BinaryPartitionNumberSolution B){
        
        if( (Integer)A.getFunctionValue() < (Integer)B.getFunctionValue() ){
            return A;
        } else{
            return B;
        }
    }

    @Override
    public int compareFunctionValue(Number left, Number right) {
        int leftValue = left.intValue();
        int rightValue = right.intValue();

        if (leftValue == rightValue)
            return 0;

        if (leftValue < rightValue)
            return -1;
        else
            return 1;
    }
}
