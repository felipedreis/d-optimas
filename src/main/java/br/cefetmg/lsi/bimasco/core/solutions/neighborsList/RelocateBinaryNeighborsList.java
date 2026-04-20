package br.cefetmg.lsi.bimasco.core.solutions.neighborsList;

import br.cefetmg.lsi.bimasco.core.Solution;

import java.util.ArrayList;
import java.util.Random;

//TODO rename class name
//TODO rename elements name
//TODO remove all references about instance file (like lerArquivo)
//TODO: analyse type of returns
public class RelocateBinaryNeighborsList implements NeighborsList {

    private ArrayList<ArrayList<Object>> neighborsList = new ArrayList<ArrayList<Object>>();

    @Override
    public ArrayList<ArrayList<Object>> getNeighborsList(Solution solution, String neighborhood){
        ArrayList<ArrayList<Object>> auxiliaryVector = new ArrayList<ArrayList<Object>>();
        ArrayList<Object> position;       
        int counter = 0;
        int index;
        Random rand = new Random();
        /*
        for(int i=0; i< solution.getSolutionsVector().size(); i++){
            for(int j=0; j< solution.getSolutionsVector().get(i).size(); j++){
                position = new ArrayList<Object>();

                position.add(i);
                position.add(j);
                auxiliaryVector.add(position);
                counter++;
            }
        }        
        
        for(int i=0; i<auxiliaryVector.size(); i++){
            index = rand.nextInt(counter-i);
            this.neighborsList.add(auxiliaryVector.get(index));
            
            auxiliaryVector.remove(index);
        }
        */

        return this.neighborsList;
    }

    @Override
    public ArrayList<ArrayList<Object>> removeNeighbors(Object position){
        int index = (Integer) position;

        this.neighborsList.remove(index);
        return this.neighborsList;
    }
}