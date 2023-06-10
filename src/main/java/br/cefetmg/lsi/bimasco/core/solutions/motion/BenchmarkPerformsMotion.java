package br.cefetmg.lsi.bimasco.core.solutions.motion;

import br.cefetmg.lsi.bimasco.core.solutions.BenchmarkSolution;
import br.cefetmg.lsi.bimasco.core.solutions.FunctionSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;

import java.util.List;

public class BenchmarkPerformsMotion implements PerformsMotion<FunctionSolutionElement, BenchmarkSolution> {


    public BenchmarkSolution movement(BenchmarkSolution solucao, List<Integer> posicao,
                                     List<FunctionSolutionElement> parametro){

        BenchmarkSolution result = (BenchmarkSolution) solucao.clone();

        int position = posicao.get(0);
        FunctionSolutionElement value = parametro.get(0);

        if(result.getSolutionsVector() != null
                && position < result.getSolutionsVector().size()) {
            result.setElement(position, value);
        }

        return result;
    }
}
