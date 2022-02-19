package br.cefetmg.lsi.bimasco.core.solutions.element;

import br.cefetmg.lsi.bimasco.core.Element;

public class BinaryPartitionNumberElementSolution implements Element {
    private boolean present;

    public BinaryPartitionNumberElementSolution(boolean present) {
        this.present = present;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    @Override
    public double toDoubleValue() {
        return present ? 1 : 0;
    }

    @Override
    public String toString() {
        return "{" +
                "present=" + (present ? 1 : 0) +
                '}';
    }
}
