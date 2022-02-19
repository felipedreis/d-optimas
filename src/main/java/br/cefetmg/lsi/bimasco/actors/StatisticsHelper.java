package br.cefetmg.lsi.bimasco.actors;

import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

public class StatisticsHelper {
    public static double variationRate(StatisticalSummary summary) {
        return Math.abs(summary.getStandardDeviation() / summary.getMean());
    }

    public static double variationRate(SummaryStatistics summary) {
        return Math.abs(summary.getStandardDeviation() / summary.getMean());
    }
}
