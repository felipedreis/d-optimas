package br.cefetmg.lsi.bimasco.core.solutions.motion;

import br.cefetmg.lsi.bimasco.core.solutions.FunctionSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;

import java.util.List;

//TODO: change elements name
public class RealPerformsMotion implements PerformsMotion<FunctionSolutionElement, FunctionSolution> {
    

    public FunctionSolution movement(FunctionSolution solucao, List<Integer> posicao,
                                     List<FunctionSolutionElement> parametro){

        FunctionSolution result = (FunctionSolution) solucao.clone();

        int position = posicao.get(0);
        FunctionSolutionElement value = parametro.get(0);

        if(result.getSolutionsVector() != null
                && position < result.getSolutionsVector().size()) {
            result.setElement(position, value);
        }

        return result;
    }
}
