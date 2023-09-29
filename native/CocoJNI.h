/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class coco_CocoJNI */

#ifndef _Included_coco_CocoJNI
#define _Included_coco_CocoJNI
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     coco_CocoJNI
 * Method:    cocoSetLogLevel
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_coco_CocoJNI_cocoSetLogLevel
  (JNIEnv *, jclass, jstring);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoGetObserver
 * Signature: (Ljava/lang/String;Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_coco_CocoJNI_cocoGetObserver
  (JNIEnv *, jclass, jstring, jstring);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoFinalizeObserver
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_coco_CocoJNI_cocoFinalizeObserver
  (JNIEnv *, jclass, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoProblemAddObserver
 * Signature: (JJ)J
 */
JNIEXPORT jlong JNICALL Java_coco_CocoJNI_cocoProblemAddObserver
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoProblemRemoveObserver
 * Signature: (JJ)J
 */
JNIEXPORT jlong JNICALL Java_coco_CocoJNI_cocoProblemRemoveObserver
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoGetSuite
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_coco_CocoJNI_cocoGetSuite
  (JNIEnv *, jclass, jstring, jstring, jstring);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoSuiteGetNumberOfProblems
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_coco_CocoJNI_cocoSuiteGetNumberOfProblems
  (JNIEnv *, jclass, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoFinalizeSuite
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_coco_CocoJNI_cocoFinalizeSuite
  (JNIEnv *, jclass, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoSuiteGetNextProblem
 * Signature: (JJ)J
 */
JNIEXPORT jlong JNICALL Java_coco_CocoJNI_cocoSuiteGetNextProblem
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoSuiteGetProblem
 * Signature: (JJ)J
 */
JNIEXPORT jlong JNICALL Java_coco_CocoJNI_cocoSuiteGetProblem
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoEvaluateFunction
 * Signature: (J[D)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_coco_CocoJNI_cocoEvaluateFunction
  (JNIEnv *, jclass, jlong, jdoubleArray);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoEvaluateConstraint
 * Signature: (J[D)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_coco_CocoJNI_cocoEvaluateConstraint
  (JNIEnv *, jclass, jlong, jdoubleArray);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoProblemGetDimension
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_coco_CocoJNI_cocoProblemGetDimension
  (JNIEnv *, jclass, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoProblemGetNumberOfObjectives
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_coco_CocoJNI_cocoProblemGetNumberOfObjectives
  (JNIEnv *, jclass, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoProblemGetNumberOfConstraints
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_coco_CocoJNI_cocoProblemGetNumberOfConstraints
  (JNIEnv *, jclass, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoProblemGetSmallestValuesOfInterest
 * Signature: (J)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_coco_CocoJNI_cocoProblemGetSmallestValuesOfInterest
  (JNIEnv *, jclass, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoProblemGetLargestValuesOfInterest
 * Signature: (J)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_coco_CocoJNI_cocoProblemGetLargestValuesOfInterest
  (JNIEnv *, jclass, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoProblemGetNumberOfIntegerVariables
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_coco_CocoJNI_cocoProblemGetNumberOfIntegerVariables
  (JNIEnv *, jclass, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoProblemGetLargestFValuesOfInterest
 * Signature: (J)[D
 */
JNIEXPORT jdoubleArray JNICALL Java_coco_CocoJNI_cocoProblemGetLargestFValuesOfInterest
  (JNIEnv *, jclass, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoProblemGetId
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_coco_CocoJNI_cocoProblemGetId
  (JNIEnv *, jclass, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoProblemGetName
 * Signature: (J)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_coco_CocoJNI_cocoProblemGetName
  (JNIEnv *, jclass, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoProblemGetEvaluations
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_coco_CocoJNI_cocoProblemGetEvaluations
  (JNIEnv *, jclass, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoProblemGetEvaluationsConstraints
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_coco_CocoJNI_cocoProblemGetEvaluationsConstraints
  (JNIEnv *, jclass, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoProblemGetIndex
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_coco_CocoJNI_cocoProblemGetIndex
  (JNIEnv *, jclass, jlong);

/*
 * Class:     coco_CocoJNI
 * Method:    cocoProblemIsFinalTargetHit
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_coco_CocoJNI_cocoProblemIsFinalTargetHit
  (JNIEnv *, jclass, jlong);

#ifdef __cplusplus
}
#endif
#endif
