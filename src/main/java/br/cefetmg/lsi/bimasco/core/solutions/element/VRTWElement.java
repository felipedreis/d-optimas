package br.cefetmg.lsi.bimasco.core.solutions.element;

import br.cefetmg.lsi.bimasco.core.Element;

import java.util.Objects;

public class VRTWElement implements Element {

    private int city;

    public VRTWElement(int city) {
        this.city = city;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    @Override
    public double toDoubleValue() {
        return city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VRTWElement that = (VRTWElement) o;
        return city == that.city;
    }

    @Override
    public int hashCode() {
        return Objects.hash(city);
    }

    @Override
    public String toString() {
        return "{" +
                "city: " + city +
                '}';
    }
}
