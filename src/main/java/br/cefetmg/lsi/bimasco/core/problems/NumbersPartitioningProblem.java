package br.cefetmg.lsi.bimasco.core.problems;

import br.cefetmg.lsi.bimasco.core.Problem;

import java.util.ArrayList;
import java.util.List;

//TODO change problem name
//TODO rename elements name
//TODO remove all references about instance file (like lerArquivo)
//TODO: analyse type of returns
@Deprecated
public class NumbersPartitioningProblem extends Problem {
    
    private Integer numeroParticoes;
    private Integer numeroElementos;
    private ArrayList<Integer> pesoElementos;
    private ArrayList<Object> parametro;
    private Integer limiteSuperior;

    public NumbersPartitioningProblem(){
        super();
    }

    @Override
    public void initialize() {

        List<List> problemData =
                this.getProblemSettings().getProblemData();

        this.numeroElementos = (int) problemData.get(2).get(0);
        this.numeroParticoes = (int) problemData.get(4).get(0);

        this.pesoElementos = new ArrayList<Integer>();
        this.limiteSuperior = 0;

        int somaLimite = 0;

        for(int i=6; i<problemData.size(); i++){
            this.pesoElementos.add((int) problemData.get(i).get(0));
            somaLimite = somaLimite + ( (int) problemData.get(i).get(0));
        }
        this.limiteSuperior = somaLimite;
        this.parametro = new ArrayList<Object>();
        this.parametro.add(numeroParticoes);
    }

    @Override
    public Integer getDimension() {
        return numeroElementos;
    }

    @Override
    public Integer getSolutionElementsCount() {
        return this.numeroParticoes;
    }

    @Override
    public double getStep() {
        return 0;
    }

    @Override
    public Object getFitnessFunction(List<Object> element) {
        int posicao = (Integer) element.get(0);

        return pesoElementos.get(posicao);
    }

    @Override
    public Object getLimit() {
        return limiteSuperior;
    }

    @Override
    public ArrayList<Object> getParameters() {
        return parametro;
    }
    
    public Integer pesoElemento(Integer posicao){
        return pesoElementos.get(posicao);
    }
}
