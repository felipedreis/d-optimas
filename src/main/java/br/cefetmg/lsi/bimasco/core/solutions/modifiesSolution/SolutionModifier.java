package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution;

import br.cefetmg.lsi.bimasco.core.Element;
import br.cefetmg.lsi.bimasco.core.Solution;

import java.io.Serializable;
import java.util.List;

public abstract class SolutionModifier  <Domain extends Element, S extends Solution<Domain, ?, ?> >
        implements Serializable {

    public abstract S modify(S solution, List<Integer> position, List<Domain> parameter, Integer motionsCount);
}
