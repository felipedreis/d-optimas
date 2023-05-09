package br.cefetmg.lsi.bimasco.core.problems.functions;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public interface Function extends Serializable {

    Double getFunctionValue(List<Double> points);
}
