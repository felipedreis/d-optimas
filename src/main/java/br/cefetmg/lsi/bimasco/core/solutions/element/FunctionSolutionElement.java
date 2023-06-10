package br.cefetmg.lsi.bimasco.core.solutions.element;

import br.cefetmg.lsi.bimasco.core.Element;

public class FunctionSolutionElement implements Element {
    private final double value;

    public FunctionSolutionElement(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public double toDoubleValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%.6f", value);
    }
}
