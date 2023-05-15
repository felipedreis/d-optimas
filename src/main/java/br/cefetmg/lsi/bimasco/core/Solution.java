package br.cefetmg.lsi.bimasco.core;

import br.cefetmg.lsi.bimasco.core.utils.BimascoClassPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.lang.String.format;

/**
 * A solution carries the set of values used to calculate the objective function, and the objective function value itself.
 * A solution may be viable or not, is generated by an agent and has a unique hash id (it is used to store on
 * database). The set of values is called domain (in the sense of math), and can be a set of integers, double or even
 * double. It has a dimension associated, and it shouldn't change through the execution.
 * @param <Domain>
 * @param <Image>
 */
public abstract class Solution <Domain extends Element, Image extends Number & Comparable, P extends Problem>
        implements Comparable<Solution>, Serializable, Cloneable {

    private static final Logger logger = LoggerFactory.getLogger(Solution.class);
    private UUID id;

    private String agent;

    private long executionTime;
    private Object initialValueOfFunction;
    private UUID initialSolutionCreator;
    private Double deviation;
    private final P problem;

    protected int dimension;
    protected List<Domain> solutionsVector;
    protected Image functionValue;
    protected boolean viable;

    private boolean changed;

    public Solution(Problem problem) {
        this.problem = (P) problem;
        id = UUID.randomUUID();
        dimension = problem.getDimension();
        dimension = getProblem().getDimension();
        solutionsVector = new ArrayList<>(dimension);

        if (problem.isNullInit()) {
            for (int i = 0; i < dimension; ++i)
                solutionsVector.add(null);
        }
    }

    public Solution(Solution<Domain, Image, P> another) {
        id = UUID.randomUUID();
        problem = another.problem;
        dimension = another.dimension;
        solutionsVector = new ArrayList<>(another.solutionsVector);
        changed = another.changed;
        functionValue = another.functionValue;
    }

    public static Solution<Element, ?, Problem> buildSolution(Problem problem) {
        Class<Solution> solutionClass = null;
        Solution solution = null;
        Constructor<Solution> constructor;

        try {
            solutionClass = (Class<Solution>)
                    Class.forName(format(BimascoClassPath.SOLUTIONS, problem.getProblemSettings().getName()));
            constructor = solutionClass.getConstructor(Problem.class);
            solution = constructor.newInstance(problem);
            solution.initialize();
        } catch (Exception ex) {
            logger.error("", ex);
        }


        return solution;
    }

    public abstract void initialize();

    public abstract void generateInitialSolution();

    protected abstract void objectiveFunction();

    protected abstract void checkViability();

    private void calculate(@Nullable Context context) {
        if (solutionsVector != null) {
            objectiveFunction();
            checkViability();
            changed = false;
            if (solutionsVector.stream().noneMatch(Objects::isNull))
                logger.debug(format("Function value calculated for solution %s", getId()));

            if (context != null)
                context.accept(this);
        } else {
            logger.debug("Found null values on solution vector, calculating function value at this point may cause a NullPointerException");
        }
    }

    public void setElement(int index, Domain value) {
        if (solutionsVector == null) {
            logger.error("Solution not initialized yet");
        } else {
            if (index >= dimension) {
                throw new IllegalArgumentException(format("Index %d exceeds vector dimension", index));
            }
            solutionsVector.set(index, value);
            changed = true;
            logger.debug(format("Changed solution position %d to value %s", index, value.toString()));
        }
    }

    public void addElement(int index, Domain value) {
        if (solutionsVector == null) {
            throw new IllegalArgumentException("Solution is not proper initialized");
        } else {
            long emptyPositions = solutionsVector.stream().filter(Objects::isNull).count();

            if (emptyPositions == 0)
                throw new IllegalStateException(format("The vector is full, adding more elements would imply in losing information"));

            solutionsVector.add(index, value);
            solutionsVector.remove(solutionsVector.size() - 1);
            changed = true;
        }
    }

    public void addElement(Domain element) {
        if (solutionsVector == null) {
            throw new IllegalArgumentException("Solution is not proper initialized");
        } else {
            if (solutionsVector.size() >= problem.getDimension())
                throw new IllegalArgumentException("Cannot add element to solution, it would violate its dimensions");

            solutionsVector.add(element);
            changed = true;
        }
    }

    public void removeElement(int index) {
        if (solutionsVector == null) {
            throw new IllegalArgumentException("Solution is not proper initialized");
        } else {
            if (index >= dimension) {
                throw new IllegalArgumentException(format("Index %d exceeds vector dimension", index));
            }
            solutionsVector.set(index, null);
            changed = true;
            logger.debug(format("Removing position %d", index));
        }
    }

    public void removeElement(Domain value) {
        if (solutionsVector == null) {
            throw new IllegalArgumentException("Solution is not proper initialized");
        } else {

            solutionsVector.remove(value);
            changed = true;
            logger.debug(format("Removing position %s", value));
        }
    }

    public void removeElementShifting(int index) {
        if (solutionsVector == null) {
            throw new IllegalArgumentException("Solution is not proper initialized");
        } else {
            if (index >= dimension) {
                throw new IllegalArgumentException(format("Index %d exceeds vector dimension", index));
            }
            solutionsVector.remove(index);
            solutionsVector.add(null);
            changed = true;
            logger.debug(format("Removing position %d", index));
        }
    }

    public boolean isViable(Context context) {
        if (changed)
            calculate(context);
        return viable;
    }

    public Image evaluate(Context context) {
        if (changed)
            calculate(context);
        return functionValue;
    }

    public Domain getElement(int index) {
        if (solutionsVector == null || solutionsVector.isEmpty())
            throw new IllegalStateException();
        else if(index > solutionsVector.size())
            throw new IllegalArgumentException();

        return solutionsVector.get(index);
    }

    /**
     *
     * @return
     */
    public Image getFunctionValue(){
        return functionValue;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public Image adaptiveFunctionValue(Domain value) {
        addElement(value);
        checkViability();
        Image res = getFunctionValue();
        removeElement(value);
        return res;
    }

    public List<Domain> getSolutionsVector() {
        return solutionsVector;
    }

    @Deprecated
    public void setSolutionsVector(List<Domain> solutionsVector){
        if (solutionsVector.size() > dimension)
            this.solutionsVector = solutionsVector.subList(0, dimension - 1);
        else
            this.solutionsVector = solutionsVector;
        changed = true;
    }

    public Double getDeviation(Double valor, Double variation, Integer variable) {
        return deviation;
    }

    public void setDeviation(Double deviation) {
        this.deviation = deviation;
    }

    public long getExecutionTime() {
        return this.executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public Object getInitialValueOfFunction() {
        return this.initialValueOfFunction;
    }

    public void setInitialValueOfFunction(Object initialValueOfFunction) {
        this.initialValueOfFunction = initialValueOfFunction;
    }

    public UUID getInitialSolutionCreator() {
        return initialSolutionCreator;
    }

    public void setInitialSolutionCreator(UUID initialSolutionCreator) {
        this.initialSolutionCreator = initialSolutionCreator;
    }

    public double[] toDoubleArray() {
        return solutionsVector.stream().mapToDouble(Element::toDoubleValue).toArray();
    }

    public P getProblem() {
        return problem;
    }

    public boolean isChanged() {
        return changed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solution<?, ?, ?> solution = (Solution<?, ?, ?>) o;
        return solutionsVector.equals(solution.solutionsVector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(solutionsVector);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "id=" + id +
                ", solutionsVector=" + solutionsVector +
                ", functionValue=" + functionValue +
                '}';
    }

    @Override
    public Object clone()  {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            logger.error("Clone is not supported on this object", ex);
            return null;
        }
    }
}
