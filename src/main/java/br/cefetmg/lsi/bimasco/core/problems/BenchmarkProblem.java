package br.cefetmg.lsi.bimasco.core.problems;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.problems.functions.Function;
import br.cefetmg.lsi.bimasco.settings.ProblemSettings;
import coco.CocoJNI;
import org.agrona.collections.ArrayUtil;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static br.cefetmg.lsi.bimasco.actors.Messages.*;
/**
 * The problem contains some basic properties of the coco_problem_t structure that can be accessed
 * through its getter functions.
 */
public class BenchmarkProblem extends Problem {
	private static final Logger logger = LoggerFactory.getLogger(BenchmarkProblem.class);
	private long pointer; // Pointer to the coco_problem_t object

	private int number_of_objectives;
	private int number_of_constraints;
	
	private double[] lower_bounds;
	private double[] upper_bounds;
	private int number_of_integer_variables;
	
	private String id;
	private String name;
	
	private long index;

	private ActorRef benchmarkActor;

	private FunctionEvaluator evaluator;

	public BenchmarkProblem() {}

	/**
	 * Constructs the problem from the pointer.
	 * @param pointer pointer to the coco_problem_t object
	 * @throws Exception
	 */
	public BenchmarkProblem(long pointer) throws Exception {

		super();
		try {		
			this.dimension = CocoJNI.cocoProblemGetDimension(pointer);
			this.number_of_objectives = CocoJNI.cocoProblemGetNumberOfObjectives(pointer);
			this.number_of_constraints = CocoJNI.cocoProblemGetNumberOfConstraints(pointer);
			
			this.lower_bounds = CocoJNI.cocoProblemGetSmallestValuesOfInterest(pointer);
			this.upper_bounds = CocoJNI.cocoProblemGetLargestValuesOfInterest(pointer);
			this.number_of_integer_variables = CocoJNI.cocoProblemGetNumberOfIntegerVariables(pointer);
			
			this.id = CocoJNI.cocoProblemGetId(pointer);
			this.name = CocoJNI.cocoProblemGetName(pointer);

			this.index = CocoJNI.cocoProblemGetIndex(pointer);
			
			this.pointer = pointer;

			evaluator = (double [] values) -> {
				logger.debug("Evaluating {}", values);
				CompletableFuture<Object> evaluateFuture =
						Patterns.ask(benchmarkActor, new Evaluate(values),
								Duration.ofSeconds(1)).toCompletableFuture();
				try {
					EvaluateResult result = (EvaluateResult) evaluateFuture.get();
					return result.y;
				} catch (InterruptedException ex) {
					logger.error("", ex);
				} catch (ExecutionException ex) {
					logger.error("", ex);
				}
				return new double[dimension];
			};

			setProblemSettings(new ProblemSettings("Benchmark", "Real", "", false, getClass().getName(),
					Collections.emptyList(), "Benchmark"));
		} catch (Exception e) {
			throw new Exception("Problem constructor failed.\n", e);
		}
	}

	public FunctionProblem asFunctionProblem() {
		FunctionProblem functionProblem = new FunctionProblem();
		Double[][] domain = new Double[dimension][2];

		for (int i = 0; i < dimension; ++i) {
			domain[i][0] = getSmallestValueOfInterest(i);
			domain[i][1] = getLargestValueOfInterest(i);
		}

		functionProblem.setDomain(domain);
		functionProblem.setDimension(dimension);
		functionProblem.setFunction(points -> {
			double[] arrayPoints = ArrayUtils.toPrimitive(points.toArray(Double[]::new));
			double[] ans = evaluator.evaluate(arrayPoints);
			return ArrayUtils.toObject(ans)[0];
		});

		return functionProblem;
	}

	/**
	 * Evaluates the function in point x and returns the result as an array of doubles. 
	 * @param x
	 * @return the result of the function evaluation in point x
	 */
	public double[] evaluateFunction(double[] x) {
		return evaluator.evaluate(x);
	}

	/**
	 * Evaluates the constraint in point x and returns the result as an array of doubles. 
	 * @param x
	 * @return the result of the constraint evaluation in point x
	 */
	public double[] evaluateConstraint(double[] x) {
		if (x.length == 0)
			throw new IllegalArgumentException("Deu merda");

		return CocoJNI.cocoEvaluateConstraint(this.pointer, x);
	}

	// Getters
	public long getPointer() {
		return this.pointer;
	}
	@Override
	public Integer getSolutionElementsCount() {
		return getNumberOfIntegerVariabls();
	}

	@Override
	public double getStep() {
		return 0;
	}

	@Override
	public Object getFitnessFunction(List<Object> element) {
		return null;
	}

	@Override
	public Object getLimit() {
		return null;
	}

	@Override
	public List<Object> getParameters() {
		return null;
	}

	public int getNumberOfObjectives() {
		return this.number_of_objectives;
	}
	
	public int getNumberOfConstraints() {
		return this.number_of_constraints;
	}
	
	public double[] getSmallestValuesOfInterest() {
		return this.lower_bounds;
	}
	
	public double getSmallestValueOfInterest(int index) {
		return this.lower_bounds[index];
	}

	public double[] getLargestValuesOfInterest() {
		return this.upper_bounds;
	}
	
	public double getLargestValueOfInterest(int index) {
		return this.upper_bounds[index];
	}
	
	public int getNumberOfIntegerVariabls() {
		return this.number_of_integer_variables;
	}

	public double[] getLargestFValuesOfInterest() {
		return CocoJNI.cocoProblemGetLargestFValuesOfInterest(pointer);
	}
	
	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}
	
	public long getEvaluations() {
		return CocoJNI.cocoProblemGetEvaluations(pointer);
	}
	
	public long getEvaluationsConstraints() {
		return CocoJNI.cocoProblemGetEvaluationsConstraints(pointer);
	}
	
	public long getIndex() {
		return this.index;
	}
	
	public boolean isFinalTargetHit() {
		return (CocoJNI.cocoProblemIsFinalTargetHit(pointer) == 1);
	}
	
	/* toString method */
	@Override
	public String toString() {		
		return this.getId();
	}

	public ActorRef getBenchmarkActor() {
		return benchmarkActor;
	}

	public void setBenchmarkActor(ActorRef benchmarkActor) {
		this.benchmarkActor = benchmarkActor;
	}


	public FunctionEvaluator getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(FunctionEvaluator evaluator) {
		this.evaluator = evaluator;
	}
}
