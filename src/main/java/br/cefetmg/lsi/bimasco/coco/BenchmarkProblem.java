package br.cefetmg.lsi.bimasco.coco;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import br.cefetmg.lsi.bimasco.core.Problem;

import java.time.Duration;
import java.util.List;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static br.cefetmg.lsi.bimasco.actors.Messages.*;
/**
 * The problem contains some basic properties of the coco_problem_t structure that can be accessed
 * through its getter functions.
 */
public class BenchmarkProblem extends Problem {
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
		} catch (Exception e) {
			throw new Exception("Problem constructor failed.\n" + e.toString());
		}
	}
	
	/**
	 * Evaluates the function in point x and returns the result as an array of doubles. 
	 * @param x
	 * @return the result of the function evaluation in point x
	 */
	public double[] evaluateFunction(double[] x) {
		CompletableFuture<Object> evaluateFuture =
				Patterns.ask(benchmarkActor, new Evaluate(x),
						Duration.ofSeconds(1)).toCompletableFuture();
		try {
			EvaluateResult result = (EvaluateResult) evaluateFuture.get();
			return result.y;
		} catch (InterruptedException ex) {

		} catch (ExecutionException ex) {

		}

		return new double[x.length];
	}

	/**
	 * Evaluates the constraint in point x and returns the result as an array of doubles. 
	 * @param x
	 * @return the result of the constraint evaluation in point x
	 */
	public double[] evaluateConstraint(double[] x) {
		return CocoJNI.cocoEvaluateConstraint(this.pointer, x);
	}

	// Getters
	public long getPointer() {
		return this.pointer;
	}
	@Override
	public Integer getSolutionElementsCount() {
		return null;
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
}
