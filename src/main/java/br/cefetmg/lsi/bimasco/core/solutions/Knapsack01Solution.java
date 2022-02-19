package br.cefetmg.lsi.bimasco.core.solutions;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.problems.Knapsack01Problem;
import br.cefetmg.lsi.bimasco.core.solutions.element.Knapsack01SolutionElement;

import java.util.List;

public class Knapsack01Solution extends Solution <Knapsack01SolutionElement, Double, Knapsack01Problem> {
    private Double executionTime;

    public Knapsack01Solution(Problem problem){
        super(problem);
    }

    @Override
    public void initialize() {
        this.functionValue = 0.0;

        executionTime = 0.0;
    }

    @Override
    public void generateInitialSolution() {
    }


    public void addElementToSolution(List<Object> element) {
    }

    public void removeElementOfSolution(List<Object> element) {
    }

    @Override
    protected void objectiveFunction(){

        double beneficio = 0;
        Knapsack01Problem problem = getProblem();
        try {
            if (!solutionsVector.get(0).equals(-1)) {
                for (int i = 0; i < problem.getDimension(); i++) {
                    if (solutionsVector.get(i).equals(1)) {
                        beneficio = beneficio + problem.getGain(i);
                    }
                }
            }
        }catch (Exception e){
            throw  e;
        }

        this.functionValue = beneficio;
    }

    public void checkViability(){
        viable = true;
        int contRestr = 0;

        Knapsack01Problem problem = (Knapsack01Problem)getProblem();

        if( !solutionsVector.get(0).equals(-1) ){
            while( contRestr < problem.getRestrictionsCount() ){
                if( !this.calculaViabilidadeRestricao(contRestr) ){
                    viable = false;
                    contRestr = problem.getRestrictionsCount();
                }
            
                contRestr++;
            }
        }
    }
    
    public Boolean calculaViabilidadeRestricao(Integer restricao){
        double peso = 0;

        Knapsack01Problem problem = getProblem();

        try {
            for (int i = 0; i < problem.getDimension(); i++) {
                if (solutionsVector.get(i).equals(1)) {
                    peso = peso + problem.getRestrictionsPeso(restricao, i);
                }
            }
        }catch (Exception e){
            throw  e;
        }

        if( peso > problem.getMaxRestrictionPeso(restricao) ){
            return false;
        } else{
            return true;
        }
    }


    @Override
    public int compareTo(Solution o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

