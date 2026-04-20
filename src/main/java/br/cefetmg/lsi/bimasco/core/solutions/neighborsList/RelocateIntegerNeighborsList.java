package br.cefetmg.lsi.bimasco.core.solutions.neighborsList;

import br.cefetmg.lsi.bimasco.core.Solution;

import java.util.ArrayList;
import java.util.Random;

//TODO rename class name
//TODO rename elements name
//TODO remove all references about instance file (like lerArquivo)
//TODO: analyse type of returns
public class RelocateIntegerNeighborsList implements NeighborsList {
    private ArrayList<ArrayList<Object>> neighborsList = new ArrayList<ArrayList<Object>>();

    @Override
    public ArrayList<ArrayList<Object>> getNeighborsList(Solution solution, String neighborhood) {
        ArrayList<ArrayList<Object>> position = new ArrayList<ArrayList<Object>>();
        ArrayList<Object> positionAux = new ArrayList<Object>();
        ArrayList<Object> auxiliaryVector = new ArrayList<Object>();
        ArrayList<Object> auxiliary = new ArrayList<Object>();
        Random rand = new Random();
        int index = 0;
        /*
        for(int i=0; i< solution.getSolutionsVector().size(); i++){
            for(int j=0; j< solution.getSolutionsVector().get(i).size(); j++){
                auxiliaryVector.add(i);
                positionAux.add(j);
                auxiliary.add(0);
            }
            
            auxiliaryVector.add(i);
            positionAux.add(solution.getSolutionsVector().get(i).size());
            auxiliary.add(1);
        }          
         
        for(int i=0; i<position.size()-1; i++){
            if( auxiliary.get(i).equals(1)){           
                for(int j=i+1; j<position.size(); j++){
                    auxiliary = new ArrayList<Object>();
                    auxiliary.add(auxiliaryVector.get(i));
                    auxiliary.add(positionAux.get(i));
                    auxiliary.add(auxiliaryVector.get(j));
                    auxiliary.add(positionAux.get(j));

                    position.add(auxiliary);
                }
            }
        }
        
        for(int i=0; i<position.size(); i++){
            index = rand.nextInt(position.size());

            this.neighborsList.add(position.get(index));
            position.remove(index);
        }


         */
        return this.neighborsList;
    }

    @Override
    public ArrayList<ArrayList<Object>> removeNeighbors(Object position){
        Object element = this.neighborsList.get((Integer) position);

        this.neighborsList.remove(element);
        return this.neighborsList;
    }
}