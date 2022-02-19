package br.cefetmg.lsi.bimasco.core.problems;

import br.cefetmg.lsi.bimasco.core.Problem;

import java.util.ArrayList;
import java.util.List;

//TODO rename elements name
//TODO: analyse type of returns
public class Knapsack01Problem extends Problem {
    private int elementsCount;
    private int restrictionsCount;
    private Integer[] maxRestrictionPeso;
    private Double[] gain;
    private Integer[][] restrictionsPeso;
    private Double lowerBorder;
    private Double optimum;

    public Knapsack01Problem() {
        super();
    }

    @Override
    public void initialize() {
        List<List> problemData =  this.getProblemSettings().getProblemData();

        int counter = 0;

        this.elementsCount = (int) problemData.get(0).get(0);
        this.restrictionsCount = (int) problemData.get(0).get(1);
        this.optimum = (double) problemData.get(0).get(2);

        this.gain = new Double[elementsCount];
        this.restrictionsPeso = new Integer[restrictionsCount][elementsCount];
        this.maxRestrictionPeso = new Integer[restrictionsCount];

        for (int i = 0; i < elementsCount; i++) {
            this.gain[i] = (double) problemData.get(1).get(i);
        }

        counter = 2;

        for (int i = 0; i < restrictionsCount; i++) {
            for (int j = 0; j < elementsCount; j++) {
                this.restrictionsPeso[i][j] = (int) problemData.get(counter + i).get(j);
            }
        }

        counter += restrictionsCount;

        for (int i = 0; i < restrictionsCount; i++) {
            this.maxRestrictionPeso[i] = (int) problemData.get(counter).get(i);
        }

        this.lowerBorder = 0.0;
    }

    @Override
    public Integer getDimension() {
        return elementsCount;
    }

    @Override
    public Integer getSolutionElementsCount() {
        return getDimension();
    }

    @Override
    public double getStep() {
        return 0;
    }

    @Override
    public Object getFitnessFunction(List<Object> element) {
        int posicao = (Integer) element.get(0);

        return gain[posicao];
    }

    @Override
    public Object getLimit() {
        return lowerBorder;
    }

    @Override
    public ArrayList<Object> getParameters() {
        return null;
    }

    public Integer getRestrictionsCount() {
        return restrictionsCount;
    }

    public Double getGain(Integer index) {
        return gain[index];
    }

    public Integer getMaxRestrictionPeso(Integer restriction) {
        return maxRestrictionPeso[restriction];
    }

    public Integer getRestrictionsPeso(Integer restriction, Integer element) {
        return restrictionsPeso[restriction][element];
    }
}