package br.cefetmg.lsi.bimasco.core.solutions.motion;

import br.cefetmg.lsi.bimasco.core.solutions.BenchmarkSolution;
import br.cefetmg.lsi.bimasco.core.solutions.FunctionSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;

import java.util.List;

public class BenchmarkPerformsMotion implements PerformsMotion<FunctionSolutionElement, BenchmarkSolution> {


    public BenchmarkSolution movement(BenchmarkSolution solution, List<Integer> position,
                                     List<FunctionSolutionElement> parameters){

        BenchmarkSolution result = (BenchmarkSolution) solution.clone();

        int positionIndex = position.get(0);
        FunctionSolutionElement value = parameters.get(0);

        if(result.getSolutionsVector() != null
                && positionIndex < result.getSolutionsVector().size()) {
            result.setElement(positionIndex, value);
        }

        return result;
    }
}
