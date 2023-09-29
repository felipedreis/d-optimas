package br.cefetmg.lsi.bimasco.coco;

import coco.CocoJNI;

import java.io.Serializable;

public class Suite implements Serializable {
	
	private long pointer; // Pointer to the coco_suite_t object
	private String name;

	private int numberOfProblems;

	/**
	 * Constructs the suite from the given suiteName, suiteInstance and suiteOptions.
	 * See http://numbbo.github.io/coco-doc/C/#suite-parameters for more information on 
	 * valid suite parameters.
	 * @param suiteName
	 * @param suiteInstance
	 * @param suiteOptions
	 * @throws Exception
	 */
	public Suite(String suiteName, String suiteInstance, String suiteOptions) throws Exception {

		super();
		try {
			pointer = CocoJNI.cocoGetSuite(suiteName, suiteInstance, suiteOptions);
			numberOfProblems = CocoJNI.cocoSuiteGetNumberOfProblems(pointer);
			name = suiteName;
		} catch (Exception e) {
			throw new Exception("Suite constructor failed.\n" + e.toString());
		}
	}

	/**
	 * Finalizes the suite.
	 * @throws Exception 
	 */
	public void finalizeSuite() throws Exception {
		try {
			CocoJNI.cocoFinalizeSuite(this.pointer);
		} catch (Exception e) {
			throw new Exception("Suite finalization failed.\n" + e.toString());
		}
	}

	public long getPointer() {
		return this.pointer;
	}
	
	public String getName() {
		return this.name;
	}

	public int getNumberOfProblems() {
		return numberOfProblems;
	}

	/* toString method */
	@Override
	public String toString() {
		return getName();
	}
}