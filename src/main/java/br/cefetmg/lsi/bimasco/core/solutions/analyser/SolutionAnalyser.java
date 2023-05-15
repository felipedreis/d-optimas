package br.cefetmg.lsi.bimasco.core.solutions.analyser;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.utils.BimascoClassPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

//TODO: Think about change this class to abstract type
public abstract class SolutionAnalyser <P extends Problem, S extends Solution<?, ?, P>> implements Serializable, Comparator<S> {

    private static final Logger logger = LoggerFactory.getLogger(SolutionAnalyser.class);

    private P problem;

    public SolutionAnalyser(P problem) {
        this.problem = problem;
    }

    public static SolutionAnalyser buildSolutionAnalyser(Problem problem) {
        Class solutionAnalyserClass;
        SolutionAnalyser solutionAnalyser = null;
        String solutionAnalyserName = problem.getProblemSettings().getSolutionAnalyserName();

        try {
            solutionAnalyserClass = Class.forName(String.format(BimascoClassPath.SOLUTIONS_ANALYSER, solutionAnalyserName));
            Constructor constructor = solutionAnalyserClass.getConstructor(problem.getClass());
            solutionAnalyser = (SolutionAnalyser) constructor.newInstance(problem);
        } catch (Exception ex) {
            logger.error("Could not create the solution analyser " + solutionAnalyserName, ex);
        }

        return solutionAnalyser;
    }

    public Optional<S> findBestSolution(final Collection<S> solutions) {
        logger.debug("findBestSolution {}", solutions);
        return solutions.stream().reduce(this::getBestSolution);
    }

    public abstract S getBestSolution(S left, S right);


    public abstract int compareFunctionValue(Number left, Number right);

    @Override
    public int compare(S left, S right) {

        if (left.equals(right))
            return 0;

        S best = getBestSolution(left, right);

        return best == left ? -1 : 1;
    }

    public P getProblem() {
        return problem;
    }

    public void setProblem(P problem) {
        this.problem = problem;
    }
}
