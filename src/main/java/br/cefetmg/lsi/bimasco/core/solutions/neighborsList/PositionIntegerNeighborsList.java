package br.cefetmg.lsi.bimasco.core.solutions.neighborsList;

import br.cefetmg.lsi.bimasco.core.Solution;

import java.util.ArrayList;
import java.util.Random;

//TODO rename class name
//TODO rename elements name
//TODO remove all references about instance file (like lerArquivo)
//TODO: analyse type of returns
public class PositionIntegerNeighborsList implements NeighborsList {

    private ArrayList<ArrayList<Object>> neighborsList = new ArrayList<ArrayList<Object>>();

    @Override
    public ArrayList<ArrayList<Object>> getNeighborsList(Solution solution, String neighborhood) {
        /*
        ArrayList<ArrayList<Object>> posicao = new ArrayList<ArrayList<Object>>();
        ArrayList<Object> posicaoAux;
        Random rand = new Random();
        int index = 0;
 
        for(int i=0; i< solution.getSolutionsVector().size(); i++){
            for(int j=0; j< solution.getSolutionsVector().get(i).size(); j++){
                posicaoAux = new ArrayList<Object>();
                posicaoAux.add(i);
                posicaoAux.add(j);

                posicao.add(posicaoAux);
            }
        }          
         
        for(int i=0; i<posicao.size(); i++){
            index = rand.nextInt(posicao.size());

            this.neighborsList.add(posicao.get(index));
            posicao.remove(index);
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