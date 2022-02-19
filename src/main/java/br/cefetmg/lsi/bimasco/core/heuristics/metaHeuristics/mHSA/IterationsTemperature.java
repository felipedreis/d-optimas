package br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.mHSA;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;

import java.util.List;

//TODO: analyse type of returns
public class IterationsTemperature implements Temperature {

    private final Problem problem;
    private Object temperature = null;

    public IterationsTemperature(Problem problem) {
        this.problem = problem;
    }

    @Override
    public Object initialTemperature(List<Object> parameters) {
        Solution currentSolution = null;
        Solution auxiliarySolution = null;
        SolutionAnalyser solutionAnalyser = null;
        Object auxiliaryTemperature = 0.0;
        Object t0 = 1;

        solutionAnalyser = SolutionAnalyser.buildSolutionAnalyser(problem);


        currentSolution = Solution.buildSolution(problem);
        auxiliarySolution = Solution.buildSolution(problem);


        currentSolution.generateInitialSolution();
        t0 = currentSolution.getFunctionValue();

        for (int i = 0; i < 100; i++) {
            currentSolution.generateInitialSolution();
            auxiliaryTemperature = currentSolution.getFunctionValue();

            //if (auxiliaryTemperature.equals(solutionAnalyser.getBest(t0, auxiliaryTemperature))) {
            //    t0 = auxiliaryTemperature;
            //}
        }

        t0 = 1.1 * Double.parseDouble(t0.toString());
        this.temperature = t0;

        return t0;
    }

    @Override
    public Object update(Object currentTemperature, Integer iteration) {
        double temp = (Double) this.temperature / Math.log(1 + iteration);
        return temp;
    }
}