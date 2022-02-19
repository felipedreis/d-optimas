package br.cefetmg.lsi.bimasco.core.problems.candidatesList;

import br.cefetmg.lsi.bimasco.core.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SolutionElementsCountCandidatesList extends CandidatesList {

    public SolutionElementsCountCandidatesList(Problem problem) {
        super(problem);
    }

    @Override
    public List<Number[]> getCandidates() {
        ArrayList<Object> positionsList;
        ArrayList<Integer> auxiliaryList = new ArrayList<>();
        Random rand = new Random();
        List<Number[]> candidates = new ArrayList<>();

        int elementsCount = 0;
        int solutionsElementsCount = 0;

        elementsCount = this.problem.getDimension();
        solutionsElementsCount = this.problem.getSolutionElementsCount();

        for(int i=0; i<elementsCount; i++){
            auxiliaryList.add(i);
        }
        
        for(int i=0; i<solutionsElementsCount; i++){
            int index = rand.nextInt(auxiliaryList.size());
            Integer element = auxiliaryList.get(index);
            auxiliaryList.remove(element);
            
            Number [] candidate = new Number[2];
            candidate[0] = index;
            candidate[1] = element;

            candidates.add(candidate);
        }
        
        return candidates;
    }

}