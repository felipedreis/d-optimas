package br.cefetmg.lsi.bimasco.core.heuristics.stopCondition;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.utils.BimascoClassPath;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StopConditionHelper {

    private static final String TAG = StopConditionHelper.class.getSimpleName();

    public static StopCondition buildStopCondition(String stopConditionName, Problem problem) {
        Class stopConditionClass = null;
        Constructor constructor = null;
        StopCondition stopCondition = null;

        try {
            stopConditionClass = Class.forName(String.format(BimascoClassPath.STOP_CONDITIONS, stopConditionName));
            constructor = stopConditionClass.getConstructor(Problem.class);
            stopCondition = (StopCondition) constructor.newInstance(problem);
        } catch (Exception ex) {
            Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
        }

        return stopCondition;
    }
}
