package br.cefetmg.lsi.bimasco.core.utils;

public class BimascoClassPath {

    //Note: "%1$s" is used to format a correct path.
    //For know more look: http://docs.oracle.com/javase/1.5.0/docs/api/java/util/Formatter.html#syntax

    public static final String SOLUTIONS_ANALYSER = "br.cefetmg.lsi.bimasco.core.solutions.analyser.%1$sSolutionAnalyser";
    public static final String PROBLEMS_FUNCTIONS = "br.cefetmg.lsi.bimasco.core.problems.functions.%1$sFunction";
    public static final String PROBLEMS = "br.cefetmg.lsi.bimasco.core.problems.%1$sProblem";
    public static final String SOLUTIONS = "br.cefetmg.lsi.bimasco.core.solutions.%1$sSolution";
    public static final String LOCAL_SEARCHES = "br.cefetmg.lsi.bimasco.core.heuristics.localSearch.%1$sLocalSearch";
    public static final String STOP_CONDITIONS = "br.cefetmg.lsi.bimasco.core.heuristics.stopCondition.%1$sStopCondition";
    public static final String MODIFIES_SOLUTION_COLLECTIONS = "br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections.%1$sModifiesSolutionCollections";
    public static final String META_HEURISTIC_TEMPERATURE = "br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.mHSA.%1$sTemperature";
    public static final String META_HEURISTICS = "br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.%1$s";
    public static final String CANDIDATES_LIST = "br.cefetmg.lsi.bimasco.core.problems.candidatesList.%1$sCandidatesList";
    public static final String MODIFIES_SOLUTION = "br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution.%1$sSolutionModifier";
    public static final String MOTIONS_COLLECTIONS = "br.cefetmg.lsi.bimasco.core.solutions.motionCollections.%1$sPerformsMotionCollections";
    public static final String MOTIONS = "br.cefetmg.lsi.bimasco.core.solutions.motion.%1$sPerformsMotion";
    public static final String NEIGHBORS_LIST = "br.cefetmg.lsi.bimasco.core.solutions.neighborsList.%1$sNeighborsList";
}
