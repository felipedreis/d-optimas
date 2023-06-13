package br.cefetmg.lsi.bimasco.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class EuclideanVector<T extends Number> extends Number implements Comparable<EuclideanVector<T>> {

    private List<T> values;

    public EuclideanVector(T ... values) {
        this.values = new ArrayList<>();
        for (T value : values)
            this.values.add(value);
    }

    public EuclideanVector(int size, T defaultValue) {
        this.values = new ArrayList<>();
        IntStream.range(0, size)
                .mapToObj(x -> defaultValue)
                .forEach(values::add);

    }

    public T get(int i) {
        if (i >= values.size())
            throw new IllegalArgumentException();

        return values.get(i);
    }

    @Override
    public int intValue() {
        return 0;
    }

    @Override
    public long longValue() {
        return 0;
    }

    @Override
    public float floatValue() {
        return 0;
    }

    @Override
    public double doubleValue() {
        return values.get(0).doubleValue();
    }

    @Override
    public int compareTo(EuclideanVector<T> o) {
        return 0;
    }

    @Override
    public String toString() {
        return "EuclideanVector{" +
                "values=" + values +
                '}';
    }
}
