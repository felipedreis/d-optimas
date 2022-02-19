package br.cefetmg.lsi.bimasco.core.problems.candidatesList;

import br.cefetmg.lsi.bimasco.core.Problem;

import java.util.ArrayList;
import java.util.List;

public class PositionCandidatesList extends CandidatesList {


    public PositionCandidatesList(Problem problem) {
        super(problem);
    }

    @Override
    public List<Number[]> getCandidates() {

        Number [] positionsList;
        int elementsCount = 0;
        List<Number[]> candidates = new ArrayList<>();

        elementsCount = problem.getDimension();

        for (int i = 0; i < elementsCount; i++) {
            positionsList = new Number[2];
            positionsList[0] = i;
            positionsList[1] = 1;

            candidates.add(positionsList);
        }

        return candidates;
    }
}