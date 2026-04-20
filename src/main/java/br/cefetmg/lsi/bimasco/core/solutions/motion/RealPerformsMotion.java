package br.cefetmg.lsi.bimasco.core.solutions.motion;

import br.cefetmg.lsi.bimasco.core.solutions.FunctionSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;

import java.util.List;

//TODO: change elements name
public class RealPerformsMotion implements PerformsMotion<FunctionSolutionElement, FunctionSolution> {
    

    public FunctionSolution movement(FunctionSolution solution, List<Integer> position,
                                     List<FunctionSolutionElement> parameters){

        FunctionSolution result = (FunctionSolution) solution.clone();

        int positionIndex = position.get(0);
        FunctionSolutionElement value = parameters.get(0);

        if(result.getSolutionsVector() != null
                && positionIndex < result.getSolutionsVector().size()) {
            result.setElement(positionIndex, value);
        }

        return result;
    }
}
