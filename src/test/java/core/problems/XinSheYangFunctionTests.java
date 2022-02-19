package core.problems;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.problems.FunctionProblem;
import br.cefetmg.lsi.bimasco.core.problems.functions.XinSheYang03Function;
import br.cefetmg.lsi.bimasco.core.solutions.FunctionSolution;
import br.cefetmg.lsi.bimasco.core.utils.BimascoClassPath;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class XinSheYangFunctionTests {

    private XinSheYang03Function xinSheYang03Function;
    private Double result;

    public void givenSimulationSettings() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        xinSheYang03Function = new XinSheYang03Function();
    }

    public void whenFunctionProblemIsInitializedWithBuilder() throws IllegalAccessException, ClassNotFoundException, InstantiationException {

        ArrayList<Double> xi = new ArrayList<>();
        xi.add(0.0);
        xi.add(0.0);

        result = xinSheYang03Function.getFunctionValue(xi);
    }

    public void then() throws ClassNotFoundException {
    }
}
