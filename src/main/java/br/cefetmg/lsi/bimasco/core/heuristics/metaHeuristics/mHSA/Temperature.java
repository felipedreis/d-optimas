package br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.mHSA;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.SA;
import br.cefetmg.lsi.bimasco.core.utils.BimascoClassPath;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface Temperature {

    static Temperature buildTemperature(Problem problem, String temperatureName) {
        Temperature temperature = null;
        try {
            Class metaHeuristicTemperatureClass = Class.forName(String.format(BimascoClassPath.META_HEURISTIC_TEMPERATURE, temperatureName));
            Constructor constructor = metaHeuristicTemperatureClass.getConstructor(Problem.class);
            temperature = (Temperature) constructor.newInstance(problem);
        } catch (Exception ex) {
            Logger.getLogger(SA.class.getName()).log(Level.SEVERE, null, ex);
        }

        return temperature;
    }

    Object initialTemperature(List<Object> parameters);
    
    Object update(Object currentTemperature, Integer iteration);
}
