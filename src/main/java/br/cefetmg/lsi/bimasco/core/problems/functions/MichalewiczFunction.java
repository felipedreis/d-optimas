package br.cefetmg.lsi.bimasco.core.problems.functions;

import org.apache.commons.math3.util.Decimal64;

import static org.apache.commons.math3.util.FastMath.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This function comes from the Virtual Library of Simulation Experiments [https://www.sfu.ca/~ssurjano/michal.html]
 * According to the documentation page, the Michalewicz function has d! local minima, and it is multimodal.
 * The parameter m defines the steepness of they valleys and ridges; a larger m leads to a more difficult search.
 * The recommended value of m is m = 10.
 */
public class MichalewiczFunction implements Function {

    private static final Decimal64 BIG_DECIMAL_PI = new Decimal64(PI);
    @Override
    public Double getFunctionValue(List<Double> points){
        Decimal64 value = new Decimal64(0);
        int m = 50;

        List<Decimal64> x = points.stream().map(Decimal64::new).collect(Collectors.toList());

        for (int index = 0; index < points.size(); ++index) {
            Decimal64 i = new Decimal64(index + 1);
            Decimal64 xi = x.get(index);

            Decimal64 xSquared = xi.multiply(xi);
            Decimal64 sinOfX = xSquared.multiply(i).divide(BIG_DECIMAL_PI).sin();
            Decimal64 sinSquared = sinOfX.pow(2 * m);
            value = value.add(xi.sin().multiply(sinSquared));
        }

        return -value.doubleValue();
    }
}
