package br.cefetmg.lsi.bimasco.core.solutions.neighborsList;

import br.cefetmg.lsi.bimasco.core.utils.BimascoClassPath;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NeighborsListHelper {

    private static final String TAG = NeighborsList.class.getSimpleName();

    public static NeighborsList buildNeighborsList(String neighborsListName)  {
        Class neighborsListClass = null;
        NeighborsList neighborsList = null;

        try {
            neighborsListClass = Class.forName(String.format(BimascoClassPath.NEIGHBORS_LIST, neighborsListName));
            neighborsList = (NeighborsList) neighborsListClass.newInstance();
        } catch (Exception ex) {
            Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
        }

        return neighborsList;
    }
}