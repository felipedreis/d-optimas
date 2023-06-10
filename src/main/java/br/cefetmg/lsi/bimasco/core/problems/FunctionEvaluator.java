package br.cefetmg.lsi.bimasco.core.problems;

@FunctionalInterface
public interface FunctionEvaluator {
    double [] evaluate(double [] point);
}
