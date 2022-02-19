package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;

import java.util.ArrayList;
import java.util.List;

public class SolutionsCollectionUtils {

    public static List<Solution> concat(List<Solution> ... solutions) {
        List<Solution> concatenated = new ArrayList<>();
        for (List<Solution> solutionList : solutions) {
            concatenated.addAll(solutionList);
        }

        return concatenated;
    }

    //TODO: What means copy values ?
    public static List<Solution> copyValues(List<Solution> colSolucoes, Problem problem) {
        List<Solution> vetorSolucoes = new ArrayList<>();
        Solution solucaoAux;

        for(int i=0; i<colSolucoes.size(); i++){
            solucaoAux = (Solution) colSolucoes.get(i).clone();
            vetorSolucoes.add(solucaoAux);
        }

        return vetorSolucoes;
    }

}
