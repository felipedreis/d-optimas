package br.cefetmg.lsi.bimasco.core.problems;

import br.cefetmg.lsi.bimasco.core.Problem;

import java.util.ArrayList;
import java.util.List;

public class BinaryPartitionNumberProblem extends Problem {
    
    protected Integer partitions;
    protected List<Integer> weights;
    private List<Object> parametro;
    private Integer heigherLimit;
    
    public BinaryPartitionNumberProblem(){
        super();
    }

    @Override
    public void initialize() {
        List<List> dadosProblema =
                this.getProblemSettings().getProblemData();

        dimension = (int) dadosProblema.get(2).get(0);
        partitions = (int) dadosProblema.get(4).get(0);

        weights = new ArrayList<>();
        heigherLimit = 0;

        int somaLimite = 0;

        for(int i=6; i<dadosProblema.size(); i++){
            weights.add((int) dadosProblema.get(i).get(0));
            somaLimite = somaLimite + ((int)dadosProblema.get(i).get(0));
        }
        heigherLimit = somaLimite;
        parametro = new ArrayList<>();
        parametro.add(partitions);
    }

    public Integer getPartitions() {
        return partitions;
    }

    public List<Integer> getWeights() {
        return weights;
    }

    @Override
    public Integer getSolutionElementsCount() {
        return this.partitions;
    }

    @Override
    public double getStep() {
        return 0;
    }

    @Override
    public Object getFitnessFunction(List<Object> element) {
        int posicao = (Integer) element.get(0);

        return weights.get(posicao);
    }

    @Override
    public Object getLimit() {
        return heigherLimit;
    }

    @Override
    public List<Object> getParameters() {
        return parametro;
    }

    public Integer numeroParticoes(){
        return partitions;
    }
    
    public Integer pesoElemento(Integer posicao){
        return weights.get(posicao);
    }

    public void setPartitions(Integer partitions) {
        this.partitions = partitions;
    }

    public void setWeights(List<Integer> weights) {
        this.weights = weights;
    }

    public List<Object> getParametro() {
        return parametro;
    }

    public void setParametro(ArrayList<Object> parametro) {
        this.parametro = parametro;
    }

    public Integer getHeigherLimit() {
        return heigherLimit;
    }

    public void setHeigherLimit(Integer heigherLimit) {
        this.heigherLimit = heigherLimit;
    }
}
