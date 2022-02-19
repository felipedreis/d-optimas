package br.cefetmg.lsi.bimasco.core.heuristics.localSearch;

import br.cefetmg.lsi.bimasco.core.Context;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;

import java.util.ArrayList;
import java.util.Map;

//TODO: Change elements name
public class FirstImproveLocalSearch extends LocalSearch {

    public FirstImproveLocalSearch(Problem problem, Map<String, Object> parameters) {
        super(problem, parameters);
    }
    @Override
    public Solution search(Solution solution, Context context) {
        Solution currentSolution = null;
        Solution auxiliarySolution = null;
        //String neighborsName = this.getNomeVizinhanca(neighbor);

        Solution bestSolution;
        if( problem.getProblemSettings().getName() != null )
            bestSolution = Solution.buildSolution(problem);


        SolutionAnalyser solutionAnalyser = null;

        solutionAnalyser = SolutionAnalyser.buildSolutionAnalyser(problem);


        //NeighborsList neighborsList = null;
        //neighborsList = NeighborsListHelper.buildNeighborsList(neighborsName);


        // o procedimento começa aqui...
        Boolean otimoLocal = true;
        ArrayList<ArrayList<Object>> vetorVizinhos = new ArrayList<ArrayList<Object>>();
        Object sV = null;
        Object mS = null;
        int contador;

        bestSolution = (Solution) solution.clone();
        mS = bestSolution.getFunctionValue();
        currentSolution = (Solution) solution.clone();

        while( otimoLocal ){
            otimoLocal = false;
            currentSolution = (Solution) bestSolution.clone();
            //vetorVizinhos = neighborsList.getNeighborsList(currentSolution, neighbor);
            contador = 0;

            while( contador < vetorVizinhos.size() ){
                auxiliarySolution = (Solution)  currentSolution.clone();
                Solution neighborsSolution = neighbor.modify(auxiliarySolution, vetorVizinhos.get(contador), null, 0);
                sV = neighborsSolution.evaluate(context);

                if (neighborsSolution.isViable(context)) {
                   /* if( sV.equals(solutionAnalyser.getBest(sV, mS)) ){
                        bestSolution = neighborsSolution;
                        mS = bestSolution.getFunctionValue();

                        otimoLocal = true;
                        contador = vetorVizinhos.size();
                    } */
                }

                contador++;
            }
        }
        return bestSolution;
    }
}
