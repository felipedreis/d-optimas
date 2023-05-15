package br.cefetmg.lsi.bimasco.core.agents;

import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import org.apache.commons.math3.analysis.function.Sigmoid;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.join;

public class QLearningMemory implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(QLearningMemory.class);

    private Solution bestFoundSolution;
    private SolutionAnalyser analyser;
    private Map<Integer, Double> qTable;

    private double alpha = 0.5;
    private double gamma = 0.8;

    private int maxRegions = 10;

    UniformRealDistribution uniformRealDistribution;
    RandomDataGenerator rnd;

    private static final Sigmoid sigmoid = new Sigmoid(-1, 1);

    public QLearningMemory(SolutionAnalyser analyser) {
        this.analyser = analyser;
        qTable = new HashMap<>();
        rnd = new RandomDataGenerator();
        uniformRealDistribution = new UniformRealDistribution();
    }

    public void initialize(List<Integer> regionIds) {
        List<Integer> ids;

        if (regionIds.size() > maxRegions) {
            Object [] sample = rnd.nextSample(regionIds, maxRegions);
            ids = Arrays.stream(sample)
                .map(s -> (Integer)s)
                .collect(Collectors.toList());
        } else {
            ids = regionIds;
        }

        for (int id : ids) {
            qTable.put(id, sigmoid(0));
        }
    }

    public void updateRegionIds(List<Integer> updatedIds) {

        logger.debug(join("Received ids ", updatedIds, " and the current q-table is ", qTable));

        List<Integer> stay, remove, add;
        List<Integer> regionIds = new ArrayList<>(qTable.keySet());

        stay = updatedIds.stream()
                .filter(regionIds::contains)
                .collect(Collectors.toList());

        remove = regionIds.stream()
                .filter(id -> !stay.contains(id))
                .collect(Collectors.toList());

        add = updatedIds.stream()
                .filter(id -> !stay.contains(id))
                .collect(Collectors.toList());

        logger.debug(join("Ids to stay ", stay, " to add ", add, " and to remove ", remove));

        for (int id : remove) {
            qTable.remove(id);
        }

        for (int id : add) {
            qTable.put(id, sigmoid(0));
        }

        qTable = qTable.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Comparator.comparing(Map.Entry::getValue)))
                .limit(maxRegions)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        logger.debug(join("Obtained new qTable ", qTable));

    }

    public double reward(Solution solution) {
        //TODO how to calculate reward?
        double reward;

        if (bestFoundSolution == null) {
            logger.debug("best solution not initialized yet, using max reward");
            reward = 1;
        } else {
            double diff = bestFoundSolution.getFunctionValue().doubleValue() - solution.getFunctionValue().doubleValue();
            logger.debug(join("Calculated difference between ", bestFoundSolution.getFunctionValue(), "and ",
                    solution.getFunctionValue(), " is ", diff));

            reward = sigmoid(diff);
        }
        return reward;
    }

    public void updateBestFoundSolution(Solution solution) {
        if (bestFoundSolution == null || analyser.compare(solution, bestFoundSolution) < 0) {
            logger.debug(join("updating bestSolution ", bestFoundSolution, " to ", solution));
            this.bestFoundSolution = solution;
        }
    }

    public void updateQTable(int regionId, Solution s) {
        double reward = reward(s);
        double maxQ = qTable.values().stream()
                .max(Double::compareTo).orElse(0.0);

        logger.debug(join("Updating Q-table for region ", regionId, " and solution", s));

        if (qTable.containsKey(regionId)) {
            logger.debug(join("Region ", regionId, " is already in memory"));
            qTable.computeIfPresent(regionId, (id, q) -> q + alpha * (reward + gamma * maxQ - q));
        } else {
            logger.debug(join("Adding region ", regionId, " in memory"));
            if (qTable.size() >= maxRegions) {
                logger.debug("Max number of regions is achieved. Removing region with minimal reward");
                Optional<Map.Entry<Integer, Double>> optionalEntry = qTable.entrySet()
                        .stream().min(Map.Entry.comparingByValue());

                optionalEntry.ifPresent(e -> qTable.remove(e.getKey()));
            }
            qTable.put(regionId, reward);
        }

        logger.debug(join("Updated Q-table: ", qTable));
    }

    public Double getMinReward() {
        return qTable.values().stream().reduce(Double::min).get();
    }

    public List<Double> normalizeRewards(Double minReward) {
        return qTable.values().stream()
                .map(q -> q - minReward + 1)
                .collect(Collectors.toList());
    }

    public Optional<Integer> chooseRegion() {
        if (qTable.isEmpty())
            return Optional.empty();

        Double minReward = getMinReward();

        List<Double> normalizedRewards = normalizeRewards(minReward);

        logger.debug(join("Minimal reward is ", minReward, " of ", qTable.values(),
                " producing a normalized list of ", normalizedRewards));

        Optional<Double> totalReward = normalizedRewards.stream().reduce(Double::sum);

        int i;

        if (totalReward.isPresent() && totalReward.get() != 0) {
            double total = totalReward.get();
            Integer [] ids = qTable.keySet().toArray(Integer[]::new);

            List<Double> probabilities = normalizedRewards.stream()
                    .map(q -> q/total)
                    .collect(Collectors.toList());

            logger.debug(join("Normalized rewards to probabilities ", probabilities));

            Double [] accumulatedProbabilities = new Double[probabilities.size()];

            accumulatedProbabilities[0] = probabilities.get(0);
            for (i = 1; i < accumulatedProbabilities.length; ++i)
                accumulatedProbabilities[i] = accumulatedProbabilities[i - 1] + probabilities.get(i);

            logger.debug(join("Accumulated probabilities: ", List.of(accumulatedProbabilities)));

            double dice = uniformRealDistribution.sample();

            logger.debug(join("Dice: ", dice));

            for (i = 0; i < accumulatedProbabilities.length; ++i) {
                if (dice <= accumulatedProbabilities[i])
                    break;
            }

            logger.debug(join("Chosen region: ", ids[i]));

            return Optional.of(ids[i]);
        }

        return Optional.empty();
    }

    public void resetMemory() {
        bestFoundSolution = null;
        qTable.clear();
    }

    public boolean isEmpty() {
        return qTable.isEmpty();
    }

    private double sigmoid(double x) {
        return sigmoid.value(x);
    }

    public Solution getBestFoundSolution() {
        return bestFoundSolution;
    }

    public void setBestFoundSolution(Solution bestFoundSolution) {
        this.bestFoundSolution = bestFoundSolution;
    }

    public SolutionAnalyser getAnalyser() {
        return analyser;
    }

    public void setAnalyser(SolutionAnalyser analyser) {
        this.analyser = analyser;
    }

    public Map<Integer, Double> getQTable() {
        return qTable;
    }

    public void setQTable(Map<Integer, Double> qTable) {
        this.qTable = qTable;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public int getMaxRegions() {
        return maxRegions;
    }

    public void setMaxRegions(int maxRegions) {
        this.maxRegions = maxRegions;
    }
}
