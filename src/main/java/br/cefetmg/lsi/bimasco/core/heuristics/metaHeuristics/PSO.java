package br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics;

import br.cefetmg.lsi.bimasco.core.Context;
import br.cefetmg.lsi.bimasco.core.Element;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.heuristics.stopCondition.StopCondition;
import br.cefetmg.lsi.bimasco.core.heuristics.stopCondition.StopConditionHelper;
import br.cefetmg.lsi.bimasco.core.problems.candidatesList.CandidatesList;
import br.cefetmg.lsi.bimasco.core.problems.candidatesList.CandidatesListHelper;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections.ModifiesSolutionCollections;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections.ModifiesSolutionCollectionsHelper;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections.SolutionsCollectionUtils;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class PSO extends MetaHeuristic {

    private static final Logger logger = Logger.getLogger(PSO.class);

    private CandidatesList velocitiesCandidateList;
    private List<Solution> particles;
    private List<Solution> particleBest;
    private List<Solution> velocity;
    private Solution globalBest;

    private int populationSize;

    private RandomDataGenerator rnd;

    private StopCondition stopCondition;

    private ModifiesSolutionCollections velocityModifiers;
    private ModifiesSolutionCollections positionModifiers;

    Map<String, Object> metaHeuristicParameters;


    public PSO(Problem problem){
        super(problem);
    }

    @Override
    public void configureMetaHeuristic(AgentSettings agentSettings) {
        metaHeuristicParameters = agentSettings.getMetaHeuristicParameters();
        populationSize = (int) metaHeuristicParameters.get(DefaultMetaHeuristicParametersKeySupported.POPULATION_SIZE_KEY);

        String velocityModifiersName, positionModifiersName, velocityInitCandidateListName, stopConditionName;

        velocityModifiersName = metaHeuristicParameters.get(DefaultMetaHeuristicParametersKeySupported.VELOCITY).toString();
        positionModifiersName = metaHeuristicParameters.get(DefaultMetaHeuristicParametersKeySupported.POSITION).toString();
        velocityInitCandidateListName = metaHeuristicParameters.get(DefaultMetaHeuristicParametersKeySupported.INITIAL_VELOCITY).toString();
        stopConditionName = metaHeuristicParameters.get(DefaultMetaHeuristicParametersKeySupported.STOP_CONDITION_NAME_KEY).toString();

        velocityModifiers = ModifiesSolutionCollectionsHelper
                .buildModifiesSolutionCollection(velocityModifiersName, problem);
        positionModifiers = ModifiesSolutionCollectionsHelper
                .buildModifiesSolutionCollection(positionModifiersName, problem);
        velocitiesCandidateList = CandidatesListHelper.buildCandidatesList(velocityInitCandidateListName, problem);
        stopCondition = StopConditionHelper.buildStopCondition(stopConditionName, problem);
        rnd = new RandomDataGenerator();
    }

    @Override
    public List<Solution> runMetaHeuristic(List<Solution> externalSolution, Context context) {

        long stopTime = 0;
        int iteration = 0, iterationsWI = 0;
        Object f0;

        particles = List.copyOf(externalSolution);
        particles.forEach(solution -> solution.evaluate(context));
        particleBest = new ArrayList<>(particles);

        findGlobalBest();
        f0 = globalBest.getFunctionValue();
        velocity = initVelocities();

        while (!stopCondition.isSatisfied(f0, stopTime, iteration, iterationsWI, metaHeuristicParameters)) {
            updateParticleBest();
            f0 = globalBest.getFunctionValue();

            List<Solution> velocityAndParticles = SolutionsCollectionUtils.concat(velocity, particles, particleBest);
            velocityAndParticles.add(globalBest);

            velocity = velocityModifiers.modify(velocityAndParticles, metaHeuristicParameters, populationSize);
            velocityAndParticles = SolutionsCollectionUtils.concat(velocity, particleBest);

            logger.debug(format("iteration: %d, velocities: %s", iteration, velocity));
            List<Solution> offspringParticles = positionModifiers.modify(velocityAndParticles, metaHeuristicParameters, populationSize);
            offspringParticles.forEach(solution -> solution.evaluate(context));

            List<Solution> nextParticles = new ArrayList<>();

            for (int i = 0; i < populationSize; ++i) {
                Solution offspring = offspringParticles.get(i);

                if (offspring.isViable(context))
                    nextParticles.add(offspring);
                else
                    nextParticles.add(particles.get(i));
            }

            particles = nextParticles;

            logger.debug(format("iteration: %d, particles: %s", iteration, particles));

            iteration++;
        }

        findGlobalBest();

        return List.of(globalBest);
    }

    private List<Solution> initVelocities() {
        List<Solution> velocities = new ArrayList<>();
        while (velocities.size() < populationSize) {
            List<Element> candidates = velocitiesCandidateList.getCandidates();
            Solution solution = Solution.buildSolution(problem);
            candidates.forEach(solution::addElement);

            velocities.add(solution);
        }

        return velocities;
    }

    private void updateParticleBest() {
        if (particleBest.isEmpty()) {
            particleBest = List.copyOf(particles);
        } else {
            for (int i = 0; i < populationSize; ++i) {
                Solution best = solutionAnalyser.getBestSolution(particleBest.get(i), particles.get(i));
                particleBest.set(i, best);

                globalBest = solutionAnalyser.getBestSolution(best, globalBest);
            }
        }
        logger.debug("Global best particle " + globalBest.toString());
    }

    private void findGlobalBest() {
        Solution populationBest = particleBest.stream()
                .reduce(solutionAnalyser::getBestSolution)
                .get();

        globalBest = solutionAnalyser.getBestSolution(populationBest, globalBest);
    }

    @Override
    public List<Solution> getPartialSolutions() {
        return null;
    }

    public int getPopulationSize() {
        return populationSize;
    }
}
