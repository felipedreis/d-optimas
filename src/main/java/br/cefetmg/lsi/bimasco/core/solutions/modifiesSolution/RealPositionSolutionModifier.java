package br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.solutions.FunctionSolution;
import br.cefetmg.lsi.bimasco.core.solutions.element.FunctionSolutionElement;
import br.cefetmg.lsi.bimasco.core.solutions.motion.PerformsMotion;
import br.cefetmg.lsi.bimasco.core.solutions.motion.PerformsMotionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RealPositionSolutionModifier
        extends SolutionModifier<FunctionSolutionElement, FunctionSolution> {

    public RealPositionSolutionModifier(Map<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public FunctionSolution modify(FunctionSolution solution, List<Integer> position,
                                   List<FunctionSolutionElement> parameter, Integer motionsCount){

        Solution solucaoAux = null;
        PerformsMotion performsMotion = null;
        Random rand = new Random();
        Double passo = null;
        int numElementos = 0;

        solucaoAux = Solution.buildSolution(solution.getProblem());
        performsMotion = PerformsMotionHelper.buildPerformsMotion("Real");


        passo = solution.getProblem().getStep();
        numElementos = solution.getProblem().getDimension();

        if( position != null ){
            solucaoAux = (Solution) solution.clone();
            solution = (FunctionSolution) performsMotion.movement(solucaoAux, position, null);
        } else{
            List<Object> posicaoAux;
            double novoValor = 0.0;

             for(int j=0; j<numElementos; j++){
                novoValor = solution.getSolutionsVector().get(j).getValue();
                novoValor += Math.pow(-1,rand.nextInt(2))*rand.nextDouble()* motionsCount *passo;

                posicaoAux = new ArrayList<Object>();
                posicaoAux.add(j);
                posicaoAux.add(novoValor);

                solucaoAux = (Solution) solution.clone();
                solution = (FunctionSolution) performsMotion.movement(solucaoAux, posicaoAux, null);
            } 
        }
        
        return solution;
    }
}
