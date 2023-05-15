package br.cefetmg.lsi.bimasco.core.solutions.motion;

import br.cefetmg.lsi.bimasco.core.Element;
import br.cefetmg.lsi.bimasco.core.Solution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

public interface PerformsMotion <Domain extends Element, S extends Solution<Domain, ?, ?> > extends Serializable {

    Logger logger = LoggerFactory.getLogger(PerformsMotion.class);

    default S movement(S solution, List<Integer> posicao, List<Domain> parametro) {
        logger.debug(String.format("Calling IntegerRelocateSO Motion on solution %s with positions %s and parameters %s",
                solution, posicao, parametro));
        return solution;
    }
}
