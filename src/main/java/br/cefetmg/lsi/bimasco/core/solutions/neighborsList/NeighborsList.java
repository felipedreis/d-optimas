package br.cefetmg.lsi.bimasco.core.solutions.neighborsList;

import br.cefetmg.lsi.bimasco.core.Solution;

import java.util.ArrayList;

//TODO rename elements name
//TODO: analyse type of returns
public interface NeighborsList {

    ArrayList<ArrayList<Object>> getNeighborsList(Solution solution, String neighborhood);

    ArrayList<ArrayList<Object>> removeNeighbors(Object position);
}
