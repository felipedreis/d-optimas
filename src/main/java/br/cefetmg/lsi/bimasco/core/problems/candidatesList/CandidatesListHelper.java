package br.cefetmg.lsi.bimasco.core.problems.candidatesList;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.core.utils.BimascoClassPath;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

//TODO rename elements name
//TODO: analyse type of returns
public class CandidatesListHelper {

    private static final String TAG = CandidatesList.class.getSimpleName();

    public static CandidatesList buildCandidatesList(String candidatesListName, Problem problem) {
        Class candidatesListClass;
        CandidatesList candidatesList = null;

        try {
            candidatesListClass = Class.forName(String.format(BimascoClassPath.CANDIDATES_LIST, candidatesListName));
            Constructor constructor = candidatesListClass.getConstructor(problem.getClass());

            candidatesList = (CandidatesList) constructor.newInstance(problem);
        } catch (Exception ex) {
            Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
        }
        return candidatesList;
    }
}