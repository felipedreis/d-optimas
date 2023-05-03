package br.cefetmg.lsi.bimasco.util;

import akka.actor.ActorRef;
import br.cefetmg.lsi.bimasco.actors.StatisticsHelper;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;

public class RegionSelector {
    private static final Logger logger = Logger.getLogger(RegionSelector.class);

    private final List<Integer> regionUsedIds;

    private final ActorRef[] regions;

    private final SummaryStatistics[] regionsSummary;

    private final UniformRealDistribution randDouble;

    public RegionSelector(List<Integer> regionUsedIds, ActorRef[] regions, SummaryStatistics[] regionsSummary) {
        this.regionUsedIds = regionUsedIds;
        this.regions = regions;
        this.regionsSummary = regionsSummary;
        randDouble = new UniformRealDistribution();
    }

    public int selectRegion() {
        int i, regionId;

        double [] probabilities = new double[regionUsedIds.size()];
        int [] ids = new int[regionUsedIds.size()];
        double sum = 0;

        for (i = 0; i < probabilities.length; ++i) {
            regionId = regionUsedIds.get(i);
            ids[i] = regionId;
            SummaryStatistics regionStats = regionsSummary[regionId];
            probabilities[i] =  regionStats != null ? StatisticsHelper.variationRate(regionStats) : 0;
            probabilities[i] = 1/probabilities[i];
            sum += probabilities[i];
        }

        if (ids.length == 1)
            return ids[0];

        probabilities[0] /= sum;
        for (i = 1; i < probabilities.length; ++i) {
            probabilities[i] /= sum;
            probabilities[i] += probabilities[i - 1];
        }

        logger.info("Region ids: " + Arrays.toString(ids));
        logger.info("Accumulated probabilities: " + Arrays.toString(probabilities));

        if (Double.isInfinite(sum) || Double.isNaN(sum)) {
            do {
                i = (int) (randDouble.sample() * regionUsedIds.size());
                regionId = ids[i];
            } while (regions[regionId] == null);

            logger.info("Sum is negative. Random region chosen");
        } else {
            double prob = randDouble.sample();
            logger.info("Sum of probabilities: " + sum);
            logger.info("Sorted probability: " + prob);

            for (i = 0; i < probabilities.length; ++i) {
                if (prob <= probabilities[i])
                    break;
            }
            regionId = ids[i];
        }

        logger.info("Region choose " + regionId);
        return regionId;
    }
}
