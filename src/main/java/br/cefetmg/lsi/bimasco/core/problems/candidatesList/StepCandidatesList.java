package br.cefetmg.lsi.bimasco.core.problems.candidatesList;

import br.cefetmg.lsi.bimasco.core.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Alterei a geração de candidatos para int
public class StepCandidatesList extends CandidatesList {

    public StepCandidatesList(Problem problem) {
        super(problem);
    }

    @Override
    public List<Number[]> getCandidates() {
        Number [] positionsList;
        Random random = new Random();
        int elementsCount;
        double step;
        List<Number[]> candidates = new ArrayList<>();

        elementsCount = this.problem.getDimension();
        step = this.problem.getStep();

        double stepValue;

        for (int i = 0; i < elementsCount; i++) {
            stepValue = Math.pow(-1, random.nextInt(2)) * random.nextDouble() * step;

            positionsList = new Number[2];
            positionsList[0] = i;
            positionsList[1] = stepValue;

            candidates.add(positionsList);
        }

        return candidates;
    }
}