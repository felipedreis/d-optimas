package br.cefetmg.lsi.bimasco.core.heuristics.localSearch;

import br.cefetmg.lsi.bimasco.core.Context;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution.SolutionModifier;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution.SolutionModifierHelper;
import br.cefetmg.lsi.bimasco.core.utils.DefaultMetaHeuristicParametersKeySupported;

import java.io.Serializable;
import java.util.Map;

//TODO: Maybe save problem reference
public abstract class LocalSearch implements Serializable {

    protected SolutionModifier neighbor;

    protected Problem problem;

    protected Map<String, Object> parameters;

    public LocalSearch(Problem problem, Map<String, Object> parameters) {
        this.problem = problem;
        this.neighbor = SolutionModifierHelper
                .buildModifiesSolution(parameters.get(DefaultMetaHeuristicParametersKeySupported.LOCAL_SEARCH_NEIGHBOR_KEY).toString(), problem, parameters);
        this.parameters = parameters;
    }

    public abstract Solution search(Solution solution, Context context);
}
