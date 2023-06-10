package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.utils.BimascoClassPath;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SolutionModifierHelper {

    private static final Logger logger = Logger.getLogger(SolutionModifier.class.getSimpleName());

    public static SolutionModifier buildModifiesSolution(String modifiesSolutionName,
                                                         Problem problem,
                                                         Map<String,Object> parameters) {
        Class modifiesSolutionClass = null;
        SolutionModifier solutionModifier = null;
        String modifiesSolutionClassPath = "";

        try {
            modifiesSolutionClassPath = String.format(BimascoClassPath.MODIFIES_SOLUTION, modifiesSolutionName);
            modifiesSolutionClass = Class.forName(modifiesSolutionClassPath);
            Constructor constructor = modifiesSolutionClass.getConstructor(Map.class);
            solutionModifier = (SolutionModifier)constructor.newInstance(parameters);

        } catch (Exception ex) {
            logger.log(Level.SEVERE, String.format("Could not instantiate the %s for problem %s",
                    modifiesSolutionClassPath, problem.getClass().getSimpleName()), ex);
        }

        return solutionModifier;
    }
}
