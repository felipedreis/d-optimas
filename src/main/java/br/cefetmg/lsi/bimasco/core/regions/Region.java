package br.cefetmg.lsi.bimasco.core.regions;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import br.cefetmg.lsi.bimasco.settings.ProblemSettings;
import br.cefetmg.lsi.bimasco.settings.RegionSettings;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.MultivariateSummaryStatistics;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//TODO: Create a different Region interfaces to protect others fields
//TODO: Improve performance
public class Region implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(Region.class);

    private final long id;

    private final RegionSettings regionSettings;
    //TODO: Put this on simulation settings later
    private final List<Solution> solutionList;

    private Problem problem;

    private DescriptiveStatistics stats;

    private SummaryStatistics summary;

    private MultivariateSummaryStatistics searchSpaceSummary;

    private SolutionAnalyser analyser;

    private Solution bestSolution;

    public Region(long id, ProblemSettings problemSettings, RegionSettings regionSettings) {
        this.regionSettings = regionSettings;
        solutionList = new ArrayList<>();
        stats = new DescriptiveStatistics();
        summary = new SummaryStatistics();
        this.id = id;

        problem = Problem.buildProblem(problemSettings);
        analyser = SolutionAnalyser.buildSolutionAnalyser(problem);
        searchSpaceSummary = new MultivariateSummaryStatistics(problem.getDimension(), false);
        bestSolution = null;
    }

    public void addSolutionsCollection(List<Solution> solutions) {
        logger.debug("Adding " + solutions.size() + " solutions to region " + id);

        for (Solution solution : solutions) {
            addSolution(solution);
        }
        solutionList.stream()
                .reduce(analyser::getBestSolution)
                .ifPresentOrElse(solution -> {
                    bestSolution = solution;
                    logger.info("Best solution of region " + id + " updated");
        }, () -> logger.info("Could not update best solution on region-" + id));

        List<Double> values = solutionList.stream()
                .map(s -> s.getFunctionValue().doubleValue())
                .collect(Collectors.toList());

        logger.debug("Region " + id + " contains: " + values);
        logger.debug("Mean: " + stats.getMean() + ", Variance: " + stats.getPopulationVariance() + ", N: " + stats.getN());
    }

    public List<Solution> getRandomSolutions(int solutionsCount) {
        RandomDataGenerator gen = new RandomDataGenerator();
        Object [] sample = gen.nextSample(solutionList, solutionsCount);


        return Arrays.stream(sample)
                .map(x -> (Solution) x)
                .collect(Collectors.toList());
    }

    private void addSolution(Solution solution) {
        solutionList.add(solution);
        stats.addValue(((Number) solution.getFunctionValue()).doubleValue());
        summary.addValue(((Number) solution.getFunctionValue()).doubleValue());
        searchSpaceSummary.addValue(solution.toDoubleArray());
        //TODO: Save on DB the new solution and the best current solution on region
        //TODO: Save also a agentId on DB
    }

    public List<List<Solution>> split() {
        List<Solution> lowerSolutions = new ArrayList<Solution>();
        List<Solution> higherSolutions = new ArrayList<Solution>();
        List<Solution> middleSolutions = new ArrayList<Solution>();

        if (solutionList.size() >= regionSettings.getMinSolutionsToSplit()) {

            Double mean = stats.getMean();
            Double std = stats.getStandardDeviation();

            for (Solution solution : this.solutionList) {
                Number functionValue = (Number) solution.getFunctionValue();
                if (functionValue.doubleValue() < mean - std) {
                    lowerSolutions.add(solution);
                } else if (functionValue.doubleValue() > mean + std) {
                    higherSolutions.add(solution);
                } else {
                    middleSolutions.add(solution);
                }
            }
            clear();
            addSolutionsCollection(middleSolutions);
        }
        return List.of(lowerSolutions, higherSolutions);
    }

    public RegionSettings getRegionSettings() {
        return regionSettings;
    }

    public List<Solution> getSolutionList() {
        return solutionList;
    }

    public int getSolutionListCount() {
        return solutionList.size();
    }

    public Solution getBestSolution() {
        return bestSolution;
    }

    public void setBestSolution(Solution bestSolution) {
        this.bestSolution = bestSolution;
    }

    public SummaryStatistics getSummary() {
        return summary;
    }

    public MultivariateSummaryStatistics getSearchSpaceSummary() {
        return searchSpaceSummary;
    }

    public void setSearchSpaceSummary(MultivariateSummaryStatistics searchSpaceSummary) {
        this.searchSpaceSummary = searchSpaceSummary;
    }

    public void clear(){
        solutionList.clear();
        stats.clear();
        summary.clear();
        searchSpaceSummary.clear();
    }
}

