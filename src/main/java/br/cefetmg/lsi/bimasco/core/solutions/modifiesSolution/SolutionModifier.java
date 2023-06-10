package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution;

import br.cefetmg.lsi.bimasco.core.Element;
import br.cefetmg.lsi.bimasco.core.Solution;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class SolutionModifier  <Domain extends Element, S extends Solution<Domain, ?, ?> >
        implements Serializable {

    protected Map<String, Object> parameters;

    public SolutionModifier(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public abstract S modify(S solution, List<Integer> position, List<Domain> parameter, Integer motionsCount);
}
