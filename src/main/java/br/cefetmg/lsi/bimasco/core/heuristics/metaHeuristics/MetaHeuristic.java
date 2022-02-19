package br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics;

import br.cefetmg.lsi.bimasco.core.Context;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import br.cefetmg.lsi.bimasco.core.utils.BimascoClassPath;
import br.cefetmg.lsi.bimasco.settings.AgentSettings;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

//TODO: Rename elements
//TODO: Maybe put constants more used here
public abstract class MetaHeuristic implements Serializable {

    private static final String TAG = MetaHeuristic.class.getSimpleName();
    protected Problem problem;
    protected AgentSettings agentSettings;
    protected RandomDataGenerator rnd;
    protected SolutionAnalyser solutionAnalyser;


    protected Map<String, Object> metaHeuristicParameters;

    public MetaHeuristic(Problem problem) {
        this.problem = problem;
        rnd = new RandomDataGenerator();
        solutionAnalyser = SolutionAnalyser.buildSolutionAnalyser(problem);
    }

    public static MetaHeuristic buildMetaHeuristic(AgentSettings agentSettings, Problem problem) {

        Class metaHeuristicClass;
        Constructor constructor;
        MetaHeuristic metaHeuristic = null;

        try {
            metaHeuristicClass = Class.forName(String.format(BimascoClassPath.META_HEURISTICS, agentSettings.getMetaHeuristicName()));
            constructor = metaHeuristicClass.getConstructor(Problem.class);
            metaHeuristic = (MetaHeuristic) constructor.newInstance(problem);
        } catch (Exception ex) {
            Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
        }

        metaHeuristic.setAgentSettings(agentSettings);
        metaHeuristic.configureMetaHeuristic(agentSettings);

        return metaHeuristic;
    }

    public abstract void configureMetaHeuristic(AgentSettings agentSettings);

    public abstract List<Solution> runMetaHeuristic(List<Solution> externalSolution, final Context context);

    @Deprecated
    public abstract List<Solution> getPartialSolutions();

    public AgentSettings getAgentSettings() {
        return agentSettings;
    }

    public void setAgentSettings(AgentSettings agentSettings) {
        this.agentSettings = agentSettings;
    }

    public SolutionAnalyser getSolutionAnalyser() {
        return solutionAnalyser;
    }

    public void setSolutionAnalyser(SolutionAnalyser solutionAnalyser) {
        this.solutionAnalyser = solutionAnalyser;
    }
}
