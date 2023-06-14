package br.cefetmg.lsi.bimasco.core.agents;

import br.cefetmg.lsi.bimasco.core.AgentContext;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.MetaHeuristic;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;
import br.cefetmg.lsi.bimasco.core.utils.ObjectiveFunctionEvaluationException;
import br.cefetmg.lsi.bimasco.persistence.MemoryState;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;
import br.cefetmg.lsi.bimasco.settings.SimulationSettings;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.join;

public class Agent implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Agent.class);

    private AgentSettings agentSettings;
    private MetaHeuristic metaHeuristic;

    private QLearningMemory qLearningMemory;

    private int solutionsCount;

    private double memoryTax;

    private UniformRealDistribution uniformRealDistribution;

    private AgentContext context;

    public Agent(SimulationSettings simulationSettings, AgentSettings agentSettings) {

        //TODO: think about initialize metaHeuristic on constructor

        this.agentSettings = agentSettings;

        solutionsCount = (Integer) this.agentSettings.getMetaHeuristicParameters()
                .getOrDefault(DefaultMetaHeuristicParametersKeySupported.POPULATION_SIZE_KEY, 1);

        memoryTax = this.agentSettings.getMemoryTax();

        Problem problem = Problem.buildProblem(simulationSettings.getProblem());
        metaHeuristic = MetaHeuristic.buildMetaHeuristic(agentSettings, problem);

        SolutionAnalyser analyser = SolutionAnalyser.buildSolutionAnalyser(problem);
        qLearningMemory = new QLearningMemory(analyser);
        uniformRealDistribution = new UniformRealDistribution();

        context = new AgentContext(agentSettings.getName(), metaHeuristic.getSolutionAnalyser());
    }

    public AgentSettings getAgentSettings() {
        return agentSettings;
    }

    public int getSolutionsCount() {
        return solutionsCount;
    }

    public Optional<Integer> chooseRegion() {
        if (uniformRealDistribution.sample() < memoryTax) {
            logger.info("Using memory to chose a region");
            return qLearningMemory.chooseRegion();
        } else {
            logger.info("Discovering new regions");
            return Optional.empty();
        }
    }

    public void updateMemory(List<Integer> ids) {
        if (qLearningMemory.isEmpty())
            qLearningMemory.initialize(ids);
        else
            qLearningMemory.updateRegionIds(ids);

    }

    public List<Solution> processMetaHeuristic(List<Solution> solutionList, int regionId) {

        List<Solution> result;

        context.resetContext();
        result = runAlgorithm(solutionList);

        if (!result.isEmpty()) {
            Solution solution = result.get(0);

            logger.info(String.format("Agent %s[%s] produced solution %s", getAgentSettings().getName(),
                    getAgentSettings().getId() , solution));
            qLearningMemory.updateQTable(regionId, solution);
            qLearningMemory.updateBestFoundSolution(solution);
        }

        return result;
    }

    //TODO: Pass this for metaHeuristic Processor
    public List<Solution> runAlgorithm(List<Solution> solutions) {

        List<Solution> result = Collections.emptyList();
        try {
            logger.debug(join("Running meta-heuristic ", metaHeuristic.getClass().getSimpleName()));
            result = metaHeuristic.runMetaHeuristic(solutions, context);
        } catch (ObjectiveFunctionEvaluationException ex) {
            logger.warn("Can't evaluate the objective function anymore", ex);
        } catch (Exception exception) {
            logger.error("Error running meta-heuristic", exception);
        }

        return result;
    }

    public MemoryState getMemoryState() {
        UUID bestMemorySolution = null;
        List<Integer> regions = new ArrayList<>(qLearningMemory.getQTable().keySet());
        List<Double> probabilities = new ArrayList<>(qLearningMemory.getQTable().values());

        if (qLearningMemory.getBestFoundSolution() != null)
            bestMemorySolution = qLearningMemory.getBestFoundSolution().getId();
        return new MemoryState(agentSettings.getName(), regions, probabilities, bestMemorySolution);
    }

    public void reset(Problem problem) {
        metaHeuristic.setProblem(problem);
        metaHeuristic.configureMetaHeuristic(agentSettings);
        qLearningMemory.resetMemory();
        context.resetContext();
    }

    public AgentContext getContext() {
        return context;
    }

    public void setContext(AgentContext context) {
        this.context = context;
    }

    public MetaHeuristic getMetaHeuristic() {
        return metaHeuristic;
    }

    public void setMetaHeuristic(MetaHeuristic metaHeuristic) {
        this.metaHeuristic = metaHeuristic;
    }
}
