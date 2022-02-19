package br.cefetmg.lsi.bimasco.core.solutions.motion;

import br.cefetmg.lsi.bimasco.core.utils.BimascoClassPath;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PerformsMotionHelper {

    private static final String TAG = PerformsMotion.class.getSimpleName();

    public static PerformsMotion buildPerformsMotion(String performsMotionName) {
        Class performsMotionClass = null;
        PerformsMotion performsMotion = null;

        try {
            performsMotionClass = Class.forName(String.format(BimascoClassPath.MOTIONS, performsMotionName));
            performsMotion = (PerformsMotion) performsMotionClass.newInstance();
        } catch (Exception ex) {
            Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
        }

        return performsMotion;
    }

}
