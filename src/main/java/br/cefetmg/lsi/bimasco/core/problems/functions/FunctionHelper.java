package br.cefetmg.lsi.bimasco.core.problems.functions;

import br.cefetmg.lsi.bimasco.core.utils.BimascoClassPath;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FunctionHelper {

    private static final String TAG = FunctionHelper.class.getSimpleName();

    public static Function buildFunction(String functionName) {
        Class functionClass = null;
        Function function = null;

        try {
            functionClass = Class.forName(String.format(BimascoClassPath.PROBLEMS_FUNCTIONS, functionName));
            function = (Function) functionClass.newInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
        }

        return function;
    }
}