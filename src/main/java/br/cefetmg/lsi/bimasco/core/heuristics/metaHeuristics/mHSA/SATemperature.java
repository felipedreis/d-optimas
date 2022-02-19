package br.cefetmg.lsi.bimasco.core.heuristics.metaHeuristics.mHSA;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.Solution;
import br.cefetmg.lsi.bimasco.core.solutions.analyser.SolutionAnalyser;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution.SolutionModifier;
import br.cefetmg.lsi.bimasco.core.solutions.modifiesSolution.SolutionModifierHelper;

import java.util.List;
import java.util.Random;

//TODO: analyse type of returns
public class  SATemperature implements Temperature{

    private final Problem problem;
    private double alpha;

    public SATemperature(Problem problem){
        this.problem = problem;
    }
    @Override
    public Object initialTemperature(List<Object> parameters){
        Solution solutionBase = null;
        Solution auxiliarySolution = null;
        Solution neighborsSolution = null;
        SolutionModifier solutionModifier = null;
        SolutionAnalyser solutionAnalyser = null;

        int iterations = 0;
        
        String neighbor = (String) parameters.get(3);
        this.alpha = (Double) parameters.get(4);

        solutionAnalyser = SolutionAnalyser.buildSolutionAnalyser(problem);

        solutionModifier = SolutionModifierHelper.buildModifiesSolution(problem.getProblemSettings().getType() + neighbor, this.problem);
        solutionBase = Solution.buildSolution(problem);

        solutionBase.generateInitialSolution();
        Object sB = solutionBase.getFunctionValue();

        
        double temp = 1.0;
        double gamma = 0.95;
        double beta = 1.1;
        boolean continua = true;
        int aceitos;
        int contador;
        Object sV = null;
        
        Random rand = new Random();
        double valX;
        double expTemp;
        
        while( continua ){
            aceitos = 0;
            contador = 0;
            
            while( contador < iterations ){
                auxiliarySolution = (Solution) solutionBase.clone();
                neighborsSolution = solutionModifier.modify(auxiliarySolution, null, null, 1);

                if( neighborsSolution.isViable(null) ){
                    sV = neighborsSolution.getFunctionValue();
                    contador++;
                    
                   /* if( sB.equals(solutionAnalyser.getBest(sV, sB)) ){
                        aceitos++;
                    } else{
                        valX = rand.nextDouble();
                        expTemp = this.calculaDelta(sV,sB,temp);

                        if( valX < expTemp ){
                            aceitos++;             
                       }
                    } */
                }
            }
            
            if( aceitos >= gamma*iterations ){
                continua = false;
            } else{
                temp = beta*temp;                               
            } 
        }
//System.out.println("SA - Temperatura: "+temp);
        return temp;
    }
    
    public Object update(Object currentTemperature, Integer iteration){
        double temp = this.alpha*(Double) currentTemperature;
        return temp;
    }
    
    private Double calculaDelta(Object A, Object B, Double T){
        double resultado;
        double valA = Double.parseDouble(A.toString());
        double valB = Double.parseDouble(B.toString());
        
        resultado = Math.exp(-(valB-valA)/T);

        return resultado;
    }
}