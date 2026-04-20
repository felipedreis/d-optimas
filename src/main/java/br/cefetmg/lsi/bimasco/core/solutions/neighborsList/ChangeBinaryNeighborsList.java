package br.cefetmg.lsi.bimasco.core.solutions.neighborsList;

import br.cefetmg.lsi.bimasco.core.Solution;

import java.util.ArrayList;
import java.util.Random;

//TODO rename class name
//TODO rename elements name
//TODO remove all references about instance file (like lerArquivo)
//TODO: analyse type of returns
public class ChangeBinaryNeighborsList implements NeighborsList {

    private ArrayList<ArrayList<Object>> neighborsList = new ArrayList<ArrayList<Object>>();

    @Override
    public ArrayList<ArrayList<Object>> getNeighborsList(Solution solution, String neighborhood) {
        ArrayList<ArrayList<Object>> auxiliaryVector = new ArrayList<ArrayList<Object>>();
        ArrayList<Object> vector0 = new ArrayList<Object>();
        ArrayList<Object> vector1 = new ArrayList<Object>();
        ArrayList<Object> position0 = new ArrayList<Object>();
        ArrayList<Object> position1 = new ArrayList<Object>();
        ArrayList<Object> position;

        for (int i = 0; i < solution.getSolutionsVector().size(); i++) {
            for (int j = 0; j < ((ArrayList) solution.getSolutionsVector().get(i)).size(); j++) {
                if ((Integer) ((ArrayList) solution.getSolutionsVector().get(i)).get(j) == 0) {
                    vector0.add(i);
                    position0.add(j);
                } else {
                    vector1.add(i);
                    position1.add(j);
                }

            }
        }

        for (int i = 0; i < vector0.size(); i++) {
            for (int j = 0; j < vector1.size(); j++) {
                position = new ArrayList<Object>();

                position.add(vector0.get(i));
                position.add(position0.get(i));
                position.add(vector1.get(j));
                position.add(position1.get(j));

                auxiliaryVector.add(position);
            }
        }

        Random rand = new Random();
        int index = 0;
        for (int i = 0; i < auxiliaryVector.size(); i++) {
            index = rand.nextInt(auxiliaryVector.size());

            this.neighborsList.add(auxiliaryVector.get(index));

            auxiliaryVector.remove(index);
        }

        return this.neighborsList;
    }

    @Override
    public ArrayList<ArrayList<Object>> removeNeighbors(Object position){
        int index = (Integer) position;

        this.neighborsList.remove(index);
        return this.neighborsList;
    }
}