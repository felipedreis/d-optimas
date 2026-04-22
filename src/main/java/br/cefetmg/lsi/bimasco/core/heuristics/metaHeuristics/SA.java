package br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics;

import br.cefetmg.lsi.bimasco.core.Context;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.mHSA.Temperature;
import br.cefetmg.lsi.bimasco.core.heuristics.stopCondition.StopCondition;
import br.cefetmg.lsi.bimasco.core.heuristics.stopCondition.StopConditionHelper;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution.SolutionModifier;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution.SolutionModifierHelper;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;
import br.cefetmg.lsi.bimasco.core.utils.SolutionManipulationKeys;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;
import br.cefetmg.lsi.bimasco.settings.SolutionManipulation;

import java.util.List;
import java.util.Random;

//TODO: Rename elements
//TODO: Create a table with name of all variables used on architecture
public class SA extends MetaHeuristic {

    private Solution bestSolution;
    private Solution currentSolution;
    private Integer maxIterations;
    private Integer temperatureMaxIterations;
    private String stopConditionName;
    private Double temperature;
    private Double alpha;
    private String temperatureName;
    private SolutionAnalyser solutionAnalyser;
    private String modifieSolutionName;
    private StopCondition stopCondition;
    private Temperature temperatureSA;
    private SolutionModifier solutionModifier;

    public SA(Problem problem) {
        super(problem);
    }

    @Override
    public void configureMetaHeuristic(AgentSettings agentSettings) {

        metaHeuristicParameters = agentSettings.getMetaHeuristicParameters();

        Solution auxiliarySolution;

        maxIterations = (Integer) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.MAX_NUMBER_OF_ITERATIONS_KEY, 0);
        temperatureMaxIterations = (Integer) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.MAX_NUMBER_OF_ITERATIONS_TEMPERATURE_KEY, 0);
        alpha = (Double) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.ALPHA_KEY, 0);
        stopConditionName = (String) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.STOP_CONDITION_NAME_KEY, "");
        temperatureName = (String) metaHeuristicParameters
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.TEMPERATURE_NAME_KEY, "");

        modifieSolutionName = "";

        modifieSolutionName = agentSettings.getSolutionManipulation()
                .getOrDefault(SolutionManipulationKeys.NEIGHBOR_KEY, new SolutionManipulation()).getType();

        solutionAnalyser = SolutionAnalyser.buildSolutionAnalyser(problem);
        if (problem.getProblemSettings().getName() != null) {
            currentSolution = Solution.buildSolution(problem);
        }
        auxiliarySolution = currentSolution;
        auxiliarySolution.initialize();

        stopCondition = StopConditionHelper.buildStopCondition(stopConditionName, problem);

        temperatureSA = Temperature.buildTemperature(problem, temperatureName);

        solutionModifier = SolutionModifierHelper
                .buildModifiesSolution(problem.getProblemSettings().getType() + modifieSolutionName, problem, metaHeuristicParameters);
    }

    @SuppressWarnings("empty-statement")
    @Override
    public List<Solution> runMetaHeuristic(List<Solution> externalSolution, Context context) {

        if (externalSolution != null && !externalSolution.isEmpty()) {
            currentSolution = externalSolution.get(0);
            bestSolution = (Solution) currentSolution.clone();
            methodSA(currentSolution, context);
            return List.of(bestSolution);
        } else {
            return null;
        }
    }

    public void methodSA(Solution initialSolution, Context context) {

        int temperatureIt = 0;
        int it = 0;
        int noImproveIt = 0;

        getStopWatch().reset();
        getStopWatch().start();

        temperature = (Double) temperatureSA.initialTemperature(
                List.of(maxIterations, temperatureMaxIterations, alpha, modifieSolutionName, alpha));

        Random rand = new Random();
        double valX;
        double expTemp;

        currentSolution = (Solution) initialSolution.clone();

        while (!stopCondition.isSatisfied(currentSolution.getFunctionValue(), getStopWatch().getTime(), it,
                noImproveIt, metaHeuristicParameters)) {
            while (temperatureIt < temperatureMaxIterations && !stopCondition.isSatisfied(currentSolution.getFunctionValue(), getStopWatch().getTime(), it,
                    noImproveIt, metaHeuristicParameters)) {
                temperatureIt++;
                it++;
                Solution neighborsSolution = solutionModifier.modify(currentSolution, null, null, 1);
                neighborsSolution.evaluate(context);

                if (neighborsSolution.isViable(context)) {

                    if (solutionAnalyser.compare(neighborsSolution, currentSolution) < 0) {
                        currentSolution = neighborsSolution;
                        Solution newBest = solutionAnalyser.getBestSolution(bestSolution, currentSolution);
                        if (!newBest.equals(bestSolution)) {
                            bestSolution = newBest;
                            noImproveIt = 0;
                        } else {
                            noImproveIt++;
                        }
                    } else {
                        valX = rand.nextDouble();
                        expTemp = calculateDeltaValue(neighborsSolution.getFunctionValue(),
                                currentSolution.getFunctionValue(), temperature);

                        if (valX < expTemp) {
                            currentSolution = neighborsSolution;
                        }
                        noImproveIt++;
                    }
                } else {
                    noImproveIt++;
                }
            }

            temperature = (Double) temperatureSA.update(temperature, it);

            temperatureIt = 0;
        }
        getStopWatch().stop();
    }

    public Double calculateDeltaValue(Number neighborsValue, Number currentValue, Double T) {
        double diff = neighborsValue.doubleValue() - currentValue.doubleValue();
        
        // If it's a maximization problem, a better solution has a positive diff.
        // If it's a minimization problem, a better solution has a negative diff.
        // Standard SA (minimization): exp(-delta / T) where delta = neighbor - current > 0.
        // For maximization: delta = current - neighbor > 0.
        
        if (problem.getProblemSettings().getMax()) {
            diff = -diff; // flip for maximization
        }

        return Math.exp(-diff / T);
    }

    private void setAlpha(Double parseDouble) {
        this.alpha = parseDouble;
    }

    public Solution getCurrentSolution() {
        return currentSolution;
    }

    public void setCurrentSolution(Solution currentSolution) {
        this.currentSolution = currentSolution;
    }

    public SolutionAnalyser getSolutionAnalyser() {
        return solutionAnalyser;
    }

    public void setSolutionAnalyser(SolutionAnalyser solutionAnalyser) {
        this.solutionAnalyser = solutionAnalyser;
    }

    public Integer getMaxIterations() {
        return maxIterations;
    }

    private void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    @Override
    public List<Solution> getPartialSolutions() {
        return null;
    }
}
