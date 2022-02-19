package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolutionCollections;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.utils.BimascoClassPath;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModifiesSolutionCollectionsHelper {

    private static final Logger logger = Logger.getLogger(ModifiesSolutionCollectionsHelper.class.getSimpleName());

    public static ModifiesSolutionCollections buildModifiesSolutionCollection(String modifiesSolutionCollectionsName, Problem problem) {
        Class modifiesSolutionClass = null;
        String classPath = "";
        ModifiesSolutionCollections modifiesSolution = null;

        try {
            classPath = String.format(BimascoClassPath.MODIFIES_SOLUTION_COLLECTIONS,
                    modifiesSolutionCollectionsName);
            modifiesSolutionClass = Class.forName(classPath);

            Constructor constructor = modifiesSolutionClass.getConstructor(Problem.class);
            modifiesSolution = (ModifiesSolutionCollections) constructor.newInstance(problem);

        } catch (Exception ex) {
            logger.log(Level.SEVERE,
                    String.format("Could not instantiate the collection modifier %s to problem %s", classPath,
                            problem.getClass().getSimpleName()), ex);
        }

        return modifiesSolution;
    }
}
