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
        ArrayList<ArrayList<Object>> posicao = new ArrayList<ArrayList<Object>>();
        ArrayList<Object> posicaoAux = new ArrayList<Object>();
        ArrayList<Object> vetorAux = new ArrayList<Object>();
        ArrayList<Object> aux = new ArrayList<Object>();
        Random rand = new Random();
        int index = 0;
        /*
        for(int i=0; i< solution.getSolutionsVector().size(); i++){
            for(int j=0; j< solution.getSolutionsVector().get(i).size(); j++){
                vetorAux.add(i);
                posicaoAux.add(j);
                aux.add(0);
            }
            
            vetorAux.add(i);
            posicaoAux.add(solution.getSolutionsVector().get(i).size());
            aux.add(1);
        }          
         
        for(int i=0; i<posicao.size()-1; i++){
            if( aux.get(i).equals(1)){           
                for(int j=i+1; j<posicao.size(); j++){
                    aux = new ArrayList<Object>();
                    aux.add(vetorAux.get(i));
                    aux.add(posicaoAux.get(i));
                    aux.add(vetorAux.get(j));
                    aux.add(posicaoAux.get(j));

                    posicao.add(aux);
                }
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
        Object elemento = this.neighborsList.get((Integer) position);

        this.neighborsList.remove(elemento);
        return this.neighborsList;
    }
}