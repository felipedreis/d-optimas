package br.cefetmg.lsi.bimasco.core.heuristics.localSearch;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.utils.BimascoClassPath;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalSearchHelper {

    private static final Logger logger = Logger.getLogger(LocalSearchHelper.class.getSimpleName());


    public static LocalSearch buildLocalSearch(String localSearchName, Problem problem, Map<String, Object> parameters) {
        Class localSearchClass;
        LocalSearch localSearch = null;
        String localSearchPath = "";
        Constructor constructor;

        try {
            localSearchPath = String.format(BimascoClassPath.LOCAL_SEARCHES, localSearchName);
            localSearchClass = Class.forName(localSearchPath);
            constructor = localSearchClass.getConstructor(Problem.class, Map.class);
            localSearch = (LocalSearch)constructor.newInstance(problem, parameters);
        } catch (Exception ex) {
            logger.log(Level.SEVERE,
                    String.format("Could not instantiate the localsearch %s to problem %s", localSearchPath,
                            problem.getClass().getSimpleName()), ex);
        }

        return localSearch;
    }
}
