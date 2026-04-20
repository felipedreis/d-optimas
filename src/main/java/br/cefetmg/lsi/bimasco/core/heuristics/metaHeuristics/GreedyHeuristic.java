package br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics;

import br.cefetmg.lsi.bimasco.core.Context;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.problems.candidatesList.CandidatesList;
import br.cefetmg.lsi.bimasco.core.problems.candidatesList.CandidatesListHelper;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TODO: Rename elements
public class GreedyHeuristic extends MetaHeuristic {

    public static int TIME_DIVISOR = 1000;
    // Inclusion: Verify how this can modify the issue of changes to agent parameters
    //private IStopCondition stopCondition;
    private List<Solution> bestSolutions;
    private Solution currentSolution;
    private Integer maxIterations;
    private String candidatesListName;
    private SolutionAnalyser solutionAnalyser;
    private Object targetFitness;

    public GreedyHeuristic(Problem problem) {
        super(problem);
    }

    @Override
    public void configureMetaHeuristic(AgentSettings agentSettings) {
        this.metaHeuristicParameters = agentSettings.getMetaHeuristicParameters();

        this.maxIterations = (Integer) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.MAX_NUMBER_OF_ITERATIONS_KEY, 0);

        this.candidatesListName = (String) metaHeuristicParameters
                .get(DefaultMetaHeuristicParametersKeySupported.CANDIDATES_LIST_NAME_KEY);


        this.targetFitness = this.problem.getLimit();


        this.solutionAnalyser = SolutionAnalyser.buildSolutionAnalyser(problem);
        if (problem.getProblemSettings().getName() != null) {
            this.currentSolution = Solution.buildSolution(problem);
        }

        this.bestSolutions = new ArrayList<>();
        this.bestSolutions.add(0, this.currentSolution);
    }

    @SuppressWarnings("empty-statement")
    @Override
    public List<Solution> runMetaHeuristic(List<Solution> externalSolution, Context context) {
        double initialTime = 0;
        double finalTime = 0;
        double end = 0;

        initialTime = System.currentTimeMillis();

        System.out.println("\nINITIAL SOLUTION OF GreedyHeuristic! ");
        System.out.println("\nGreedyHeuristic METHOD! ");

        greedyMethod(context);

        finalTime = System.currentTimeMillis();
        end = ((finalTime - initialTime) / TIME_DIVISOR);
        System.err.println("\nEnded - GreedyHeuristic = Time spent: " + end + "");

        System.gc();

        return (this.bestSolutions);
    }

    public void greedyMethod(Context context) {
        List<List<Object>> candidatesList = new ArrayList<>();
        CandidatesList candidatesListHelper = null;
        Solution auxiliarySolution = null;
        Solution elementSolution = null;

        Random rand = new Random();
        int divisor = 0;
        int percentage = 0;
        int nElements = 0;
        int bestCandidateIndex;
        Object sA = null;
        Object best = null;
        Object mS = this.currentSolution.getFunctionValue();
        Object sE = null;

        nElements = problem.getDimension();
        percentage = (int) (0.1 * nElements);

        this.targetFitness = this.problem.getLimit();


        if (percentage <= 0) {
            percentage = 1;
        }

        for (int i = 0; i < this.maxIterations; i++) {
            candidatesListHelper = CandidatesListHelper.buildCandidatesList(this.candidatesListName, this.problem);
            //candidatesList = candidatesListHelper.getCandidates();

            auxiliarySolution = Solution.buildSolution(problem);
            elementSolution = Solution.buildSolution(problem);

            for (int j = 0; j < percentage; j++) {
                bestCandidateIndex = rand.nextInt(candidatesList.size());
                //auxiliarySolution.addElement(candidatesList.get(bestCandidateIndex));
                if (!auxiliarySolution.isViable(context)) {
                    List<Object> candidate = candidatesList.get(bestCandidateIndex);

                    auxiliarySolution.removeElement((int) candidate.get(0));
                }

                //candidatesList = candidatesListHelper.removeCandidates(bestCandidateIndex);
            }

            sA = auxiliarySolution.getFunctionValue();

            while (candidatesList.size() != 0) {
                bestCandidateIndex = -1;
                best = this.targetFitness;

                for (int j = 0; j < candidatesList.size(); j++) {
                    //elementSolution.copyValues(auxiliarySolution);
                    //elementSolution.addElementToSolution(candidatesList.get(j));
                    sE = elementSolution.getFunctionValue();

                    //if (sE.equals(this.solutionAnalyser.getBest(best, sE))) {
                    //    if (elementSolution.isViable(context)) {
                    //        best = sE;
                    //        bestCandidateIndex = j;
                    //    }
                    //}
                }

                if (bestCandidateIndex == -1) {
                    candidatesList.clear();
                } else {
                    //auxiliarySolution.addElementToSolution(candidatesList.get(bestCandidateIndex));
                    //candidatesList = candidatesListHelper.removeCandidates(bestCandidateIndex);
                    sA = auxiliarySolution.getFunctionValue();
                }
            }


            //if (sA.equals(this.solutionAnalyser.getBest(sA, mS))) {
            //    currentSolution = auxiliarySolution;
            //    mS = sA;
            //}

            if (divisor == 0)
                divisor = ((int) (this.maxIterations / 10));
            if (i % (divisor < 2 ? 2 : divisor) == 0)
                System.gc();
        }

        bestSolutions.clear();
        bestSolutions.add(currentSolution);

    }

    public Integer bestElement(Solution currentSolution, List<List<Object>> candidatesList) {
        Solution auxiliarySolution = null;

        auxiliarySolution = Solution.buildSolution(problem);

        int bestSolutionIndex = 0;

        auxiliarySolution = (Solution) currentSolution.clone();
        //auxiliarySolution.addElementToSolution(candidatesList.get(bestSolutionIndex));
        Object bestValue = auxiliarySolution.getFunctionValue();
        Object currentIterationValue = null;

        for (int i = 1; i < candidatesList.size(); i++) {
            auxiliarySolution = (Solution) currentSolution.clone();
            //auxiliarySolution.addElementToSolution(candidatesList.get(i));
            currentIterationValue = auxiliarySolution.getFunctionValue();

            //if (currentIterationValue.equals(this.solutionAnalyser.getBest(currentIterationValue, bestValue))) {
            //    bestValue = currentIterationValue;
            //    bestSolutionIndex = i;
            //}
        }

        return bestSolutionIndex;
    }

    @Override
    public List<Solution> getPartialSolutions() {
        return null;
    }

    public Integer getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(Integer maxIterations) {
        this.maxIterations = maxIterations;
    }
}
