package br.cefetmg.lsi.bimasco.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BasicFunctions {
// CriaÃ§Ã£o de um vetor de posiÃ§Ãµes igual Ã  zero de tamanho <tamanho>
    public static ArrayList<Object> vetorNuloObjeto(Integer tamanho){
        ArrayList<Object> listaNulo = new ArrayList<Object>();

        for(int i=0; i<tamanho; i++){
            listaNulo.add(0);
        }

        return listaNulo;
    }

    public static ArrayList<Integer> vetorNuloInteiro(Integer tamanho){
        ArrayList<Integer> listaNulo = new ArrayList<Integer>();

        for(int i=0; i<tamanho; i++){
            listaNulo.add(0);
        }

        return listaNulo;
    }

// CriaÃ§Ã£o de um vetor de posiÃ§Ãµes igual Ã  <numero> de tamanho <tamanho>
    public static ArrayList<Object> vetorNumeroObjeto(Integer tamanho, Integer numero){
        ArrayList<Object> listaNulo = new ArrayList<Object>();

        for(int i=0; i<tamanho; i++){
            listaNulo.add(numero);
        }

        return listaNulo;
    }

    public static ArrayList<Integer> vetorNumeroInteiro(Integer tamanho, Integer numero){
        ArrayList<Integer> listaNulo = new ArrayList<Integer>();

        for(int i=0; i<tamanho; i++){
            listaNulo.add(numero);
        }

        return listaNulo;
    }

// CriaÃ§Ã£o de um vetor de posiÃ§Ãµes crescentes de tamanho <tamanho>
    public static ArrayList<ArrayList<Object>> vetorPosicaoOrdenadoObjeto(Integer tamanho){
        ArrayList<Object> listaAux;
        ArrayList<ArrayList<Object>> listaOrdenada = new ArrayList<ArrayList<Object>>();

        for(int i=0; i<tamanho; i++){
            listaAux = new ArrayList<Object>();
            listaAux.add(i);
            listaOrdenada.add(listaAux);
        }

        return listaOrdenada;
    }

    public static ArrayList<Integer> vetorPosicaoOrdenadoInteiro(Integer tamanho){
        ArrayList<Integer> listaOrdenada = new ArrayList<Integer>();

        for(int i=0; i<tamanho; i++){
            listaOrdenada.add(i);
        }

        return listaOrdenada;
    }

// CriaÃ§Ã£o de um vetor de posiÃ§Ãµes de <0> Ã  <tamanho> desordenado
    public static ArrayList<ArrayList<Object>> vetorPosicaoDesordenadoObjeto(Integer tamanho){
        ArrayList<Object> listaAux = new ArrayList<Object>();
        ArrayList<Integer> listaPosicao = new ArrayList<Integer>();
        ArrayList<ArrayList<Object>> listaDesordenada = new ArrayList<ArrayList<Object>>();
        Random rand;
        int index;

        for (int i=0; i<tamanho; i++){
            listaPosicao.add(i);
        }

        for (int i=0; i<tamanho; i++){
            rand = new Random();
            index = rand.nextInt(tamanho-i);

            listaAux = new ArrayList<Object>();
            listaAux.add(listaPosicao.get(index));
            listaPosicao.remove(index);
            listaDesordenada.add(listaAux);
        }

        return listaDesordenada;
    }

    public static ArrayList<Integer> vetorPosicaoDesordenadoInteiro(Integer tamanho){
        ArrayList<Integer> listaPosicao = new ArrayList<Integer>();
        ArrayList<Integer> listaDesordenada = new ArrayList<Integer>();
        Random rand;
        int index;

        for (int i=0; i<tamanho; i++){
            listaPosicao.add(i);
        }

        for (int i=0; i<tamanho; i++){
            rand = new Random();
            index = rand.nextInt(tamanho-i);

            listaDesordenada.add(listaPosicao.get(index));
            listaPosicao.remove(index);
        }

        return listaDesordenada;
    }

// Desordena um vetor do tipo ArrayList<ArrayList<Object>>
   public static ArrayList<ArrayList<Object>> baguncaVetor(ArrayList<ArrayList<Object>> vetor){
        ArrayList<ArrayList<Object>> vetorBaguncado = new ArrayList<ArrayList<Object>>();
        ArrayList<Integer> listaBaguncada = new ArrayList<Integer>();

        listaBaguncada = BasicFunctions.vetorPosicaoDesordenadoInteiro(vetor.size());

        for(int i=0; i<vetor.size(); i++){
            vetorBaguncado.add(vetor.get(listaBaguncada.get(i)));
        }

        return vetorBaguncado;
    }

   public static ArrayList<Object> baguncaElementosVetor(ArrayList<Object> vetor){
        ArrayList<Object> vetorBaguncado = new ArrayList<Object>();
        ArrayList<Integer> listaBaguncada = new ArrayList<Integer>();

        listaBaguncada = BasicFunctions.vetorPosicaoDesordenadoInteiro(vetor.size());

        for(int i=0; i<vetor.size(); i++){
            vetorBaguncado.add(vetor.get(listaBaguncada.get(i)));
        }

        return vetorBaguncado;
    }

   // Desordena um vetor do tipo ArrayList<ArrayList<Integer>>
   public static ArrayList<ArrayList<Object>> baguncaVetorInteiro(ArrayList<ArrayList<Integer>> vetor){
        ArrayList<ArrayList<Object>> vetorBaguncado = new ArrayList<ArrayList<Object>>();
        ArrayList<Integer> listaBaguncada = new ArrayList<Integer>();
        ArrayList<Object> elemento;
        
        listaBaguncada = BasicFunctions.vetorPosicaoDesordenadoInteiro(vetor.size());

        for(int i=0; i<vetor.size(); i++){
            elemento = new ArrayList<Object>();

            for(int j=0; j<vetor.get(listaBaguncada.get(i)).size(); j++){
                elemento.add(vetor.get(listaBaguncada.get(i)).get(j));
            }
            
            vetorBaguncado.add(elemento);
        }

        return vetorBaguncado;
    }

   public static List<Integer> baguncaElementosVetorInteiro(List<Integer> vetor){
        ArrayList<Integer> vetorBaguncado = new ArrayList<Integer>();
        ArrayList<Integer> listaBaguncada = new ArrayList<Integer>();
        
        listaBaguncada = BasicFunctions.vetorPosicaoDesordenadoInteiro(vetor.size());

        for(int i=0; i<vetor.size(); i++){
            vetorBaguncado.add(vetor.get(listaBaguncada.get(i)));
        }

        return vetorBaguncado;
    }

   public static ArrayList<ArrayList<Integer>> intraVetorPosicaoInteiro(ArrayList<Integer> tamanho){
// Vetores em que a ordem nÃ£o tem importÃ¢ncia, portanto um objeto serÃ¡ descrito na forma: <vetor1,posicao1,vetor2>
        ArrayList<Integer> listaAux = new ArrayList<Integer>();
        ArrayList<Integer> listaPosicao = new ArrayList<Integer>();
        ArrayList<Integer> posicao = new ArrayList<Integer>();
        ArrayList<Integer> listaVetor = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> listaPosicoes = new ArrayList<ArrayList<Integer>>();

        posicao.add(0);
        for(int i=0; i<tamanho.size(); i++){
            listaVetor.addAll(BasicFunctions.vetorNumeroInteiro(tamanho.get(i),i));
            listaPosicao.addAll(BasicFunctions.vetorPosicaoOrdenadoInteiro(tamanho.get(i)));
            posicao.add(posicao.get(i)+tamanho.get(i));
        }

        for(int i=0; i<listaAux.size()-1; i++){
            for(int j=posicao.get(i+1); j<listaAux.size()-1; j++){
                listaAux = new ArrayList<Integer>();

                listaAux.add(listaVetor.get(i));
                listaAux.add(listaPosicao.get(i));
                listaAux.add(listaVetor.get(j));

                listaPosicoes.add(listaAux);
            }
        }

        return listaPosicoes;
    }

   public static ArrayList<ArrayList<Integer>> intraVetorPosicaoOrdemInteiro(ArrayList<Integer> tamanho){
// Vetores em que a ordem tem importÃ¢ncia, portanto um objeto serÃ¡ descrito na forma: <vetor1,posicao1,vetor2,posicao2>
        ArrayList<Integer> listaAux = new ArrayList<Integer>();
        ArrayList<Integer> listaPosicao = new ArrayList<Integer>();
        ArrayList<Integer> listaVetor = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> listaPosicoes = new ArrayList<ArrayList<Integer>>();

        for(int i=0; i<tamanho.size(); i++){
            listaVetor.addAll(BasicFunctions.vetorNumeroInteiro(tamanho.get(i),i));
            listaPosicao.addAll(BasicFunctions.vetorPosicaoOrdenadoInteiro(tamanho.get(i)));
        }

        for(int i=0; i<listaAux.size()-1; i++){
            for(int j=i+1; j<listaAux.size(); j++){
                listaAux = new ArrayList<Integer>();

                listaAux.add(listaVetor.get(i));
                listaAux.add(listaPosicao.get(i));
                listaAux.add(listaVetor.get(j));
                listaAux.add(listaPosicao.get(j));

                listaPosicoes.add(listaAux);
            }
        }

        return listaPosicoes;
    }

    public static ArrayList<ArrayList<Object>> intraVetorPosicao(ArrayList<Integer> tamanho){
// Vetores em que a ordem nÃ£o tem importÃ¢ncia, portanto um objeto serÃ¡ descrito na forma: <vetor1,posicao1,vetor2>
        ArrayList<Object> listaAux = new ArrayList<Object>();
        ArrayList<Integer> listaPosicao = new ArrayList<Integer>();
        ArrayList<Integer> posicao = new ArrayList<Integer>();
        ArrayList<Integer> listaVetor = new ArrayList<Integer>();
        ArrayList<ArrayList<Object>> listaPosicoes = new ArrayList<ArrayList<Object>>();
        int pos = 0;

        posicao.add(0);
        for(int i=0; i<tamanho.size(); i++){
            listaPosicao.addAll(BasicFunctions.vetorPosicaoOrdenadoInteiro(tamanho.get(i)));
            listaVetor.addAll(BasicFunctions.vetorNumeroInteiro(tamanho.get(i),i));
            posicao.add(posicao.get(i)+tamanho.get(i));
        }

        for(int i=0; i<posicao.get(tamanho.size()-1); i++){
            pos = posicao.get(listaVetor.get(i)+1);

            for(int j=pos; j<listaVetor.size(); j++){
                listaAux = new ArrayList<Object>();

                listaAux.add(listaVetor.get(i));
                listaAux.add(listaPosicao.get(i));
                listaAux.add(listaVetor.get(j));

                listaPosicoes.add(listaAux);
            }
        }

        return listaPosicoes;
    }

   public static ArrayList<ArrayList<Object>> intraVetorPosicaoOrdem(ArrayList<Integer> tamanho){
// Vetores em que a ordem tem importÃ¢ncia, portanto um objeto serÃ¡ descrito na forma: <vetor1,posicao1,vetor2,posicao2>
        ArrayList<Object> listaAux = new ArrayList<Object>();
        ArrayList<Integer> listaPosicao = new ArrayList<Integer>();
        ArrayList<Integer> listaVetor = new ArrayList<Integer>();
        ArrayList<ArrayList<Object>> listaPosicoes = new ArrayList<ArrayList<Object>>();

        for(int i=0; i<tamanho.size(); i++){
            listaVetor.addAll(BasicFunctions.vetorNumeroInteiro(tamanho.get(i),i));
            listaPosicao.addAll(BasicFunctions.vetorPosicaoOrdenadoInteiro(tamanho.get(i)));
        }

        for(int i=0; i<listaAux.size()-1; i++){
            for(int j=i+1; j<listaAux.size(); j++){
                listaAux = new ArrayList<Object>();
                
                listaAux.add(listaVetor.get(i));
                listaAux.add(listaPosicao.get(i));
                listaAux.add(listaVetor.get(j));
                listaAux.add(listaPosicao.get(j));

                listaPosicoes.add(listaAux);
            }
        }

        return listaPosicoes;
    }

    public static ArrayList<ArrayList<Object>> escolherSubconjuntoObjeto(Integer tConjunto, Integer tSubconjunto){
        ArrayList<Object> listaAux = new ArrayList<Object>();
        ArrayList<Integer> listaPosicao = new ArrayList<Integer>();
        ArrayList<ArrayList<Object>> listaSubconjunto = new ArrayList<ArrayList<Object>>();
        Random rand;
        int index;

        for (int i=0; i<tConjunto; i++){
            listaPosicao.add(i);
        }

        for (int i=0; i<tSubconjunto; i++){
            rand = new Random();
            index = rand.nextInt(tConjunto-i);

            listaAux = new ArrayList<Object>();
            listaAux.add(listaPosicao.get(index));
            listaPosicao.remove(index);
            listaSubconjunto.add(listaAux);
        }

        return listaSubconjunto;
    }

    public static ArrayList<Integer> escolherSubconjuntoInteiro(Integer tConjunto, Integer tSubconjunto){
        ArrayList<Integer> listaPosicao = new ArrayList<Integer>();
        ArrayList<Integer> listaSubconjunto = new ArrayList<Integer>();
        Random rand;
        int index;

        for (int i=0; i<tConjunto; i++){
            listaPosicao.add(i);
        }

        for (int i=0; i<tSubconjunto; i++){
            rand = new Random();
            index = rand.nextInt(tConjunto-i);

            listaSubconjunto.add(listaPosicao.get(index));
            listaPosicao.remove(index);
        }

        return listaSubconjunto;
    }

    public static ArrayList<ArrayList<Object>> escolherSubconjuntoVetor(ArrayList<ArrayList<Object>> vetor, Integer tamanho){
        ArrayList<ArrayList<Object>> listaSubconjunto = new ArrayList<ArrayList<Object>>();
        ArrayList<ArrayList<Object>> vetorAux = new ArrayList<ArrayList<Object>>();
        Random rand;
        int index;

        vetorAux = vetor;
        for (int i=0; i<tamanho; i++){
            rand = new Random();
            index = rand.nextInt(vetor.size());

            listaSubconjunto.add(vetorAux.get(index));
            vetorAux.remove(index);
        }

        return listaSubconjunto;
    }

    public static ArrayList<Object> escolherElementosSubconjuntoVetor(ArrayList<Object> vetor, Integer tamanho){
        ArrayList<Object> listaSubconjunto = new ArrayList<Object>();
        Random rand;
        int index;
        
        for (int i=0; i<tamanho; i++){
            rand = new Random();
            index = rand.nextInt(vetor.size());

            listaSubconjunto.add(vetor.get(index));
            vetor.remove(index);
        }

        return listaSubconjunto;
    }

// Lista das posiÃ§Ãµes criadas para vetores do tipo BinÃ¡rio, porÃ©m podem ser 
// utilizados por outros tipos de vetores.
    public static ArrayList<ArrayList<Integer>> divideVetorBinario(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<ArrayList<Integer>> vetor = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vet0 = new ArrayList<Integer>();
        ArrayList<Integer> pos0 = new ArrayList<Integer>();
        ArrayList<Integer> vet1 = new ArrayList<Integer>();
        ArrayList<Integer> pos1 = new ArrayList<Integer>();

        for(int i=0; i<vetorSolucao.size(); i++){
            for(int j=0; j<vetorSolucao.get(i).size(); j++){
                if( (Integer)vetorSolucao.get(i).get(j) == 0 ){
                    vet0.add(i);
                    pos0.add(j);
                } else{
                    vet1.add(i);
                    pos1.add(j);
                }
            }
        }

        vetor.add(vet0);
        vetor.add(pos0);
        vetor.add(vet1);
        vetor.add(pos1);

        return vetor;
    }

    public static ArrayList<ArrayList<Integer>> binario1p(ArrayList<ArrayList<Object>> vetor){
        ArrayList<ArrayList<Integer>> listaPosicoes = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vetPosicoes;
        
        for(int i=0; i<vetor.size(); i++){
            for(int j=0; j<vetor.get(i).size(); j++){
                vetPosicoes = new ArrayList<Integer>();
                vetPosicoes.add(i);
                vetPosicoes.add(j);
                
                listaPosicoes.add(vetPosicoes);
            }
        }

        return listaPosicoes;
    }

    public static ArrayList<ArrayList<Integer>> binario2pDv(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<ArrayList<Integer>> vetorAux = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vet0 = new ArrayList<Integer>();
        ArrayList<Integer> pos0 = new ArrayList<Integer>();
        ArrayList<Integer> vet1 = new ArrayList<Integer>();
        ArrayList<Integer> pos1 = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> listaPosicoes = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vetPosicao;

        vetorAux = BasicFunctions.divideVetorBinario(vetorSolucao);
        vet0 = vetorAux.get(0);
        pos0 = vetorAux.get(1);
        vet1 = vetorAux.get(2);
        pos1 = vetorAux.get(3);

        for(int i=0; i<vet0.size(); i++){
            for(int j=0; j<vet1.size(); j++){
                if( vet0.get(i) != vet1.get(j) ){
                    vetPosicao = new ArrayList<Integer>();
                    vetPosicao.add(vet0.get(i));
                    vetPosicao.add(pos0.get(i));
                    vetPosicao.add(vet1.get(j));
                    vetPosicao.add(pos1.get(j));

                    listaPosicoes.add(vetPosicao);
                }
            }
        }

        return listaPosicoes;
    }
    
    public static ArrayList<ArrayList<Integer>> binario2pMt(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<ArrayList<Integer>> vetorAux = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vet0 = new ArrayList<Integer>();
        ArrayList<Integer> pos0 = new ArrayList<Integer>();
        ArrayList<Integer> vet1 = new ArrayList<Integer>();
        ArrayList<Integer> pos1 = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> listaPosicoes = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vetPosicao;

        vetorAux = BasicFunctions.divideVetorBinario(vetorSolucao);
        vet0 = vetorAux.get(0);
        pos0 = vetorAux.get(1);
        vet1 = vetorAux.get(2);
        pos1 = vetorAux.get(3);

        for(int i=0; i<vet0.size(); i++){
            for(int j=0; j<vet1.size(); j++){
                vetPosicao = new ArrayList<Integer>();
                vetPosicao.add(vet0.get(i));
                vetPosicao.add(pos0.get(i));
                vetPosicao.add(vet1.get(j));
                vetPosicao.add(pos1.get(j));

                listaPosicoes.add(vetPosicao);
            }
        }

        return listaPosicoes;
    }

    public static ArrayList<ArrayList<Integer>> binario2pMv(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<ArrayList<Integer>> vetorAux = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vet0 = new ArrayList<Integer>();
        ArrayList<Integer> pos0 = new ArrayList<Integer>();
        ArrayList<Integer> vet1 = new ArrayList<Integer>();
        ArrayList<Integer> pos1 = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> listaPosicoes = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vetPosicao;

        vetorAux = BasicFunctions.divideVetorBinario(vetorSolucao);
        vet0 = vetorAux.get(0);
        pos0 = vetorAux.get(1);
        vet1 = vetorAux.get(2);
        pos1 = vetorAux.get(3);

        for(int i=0; i<vet0.size(); i++){
            for(int j=0; j<vet1.size(); j++){
                if( vet0.get(i) == vet1.get(j) ){
                    vetPosicao = new ArrayList<Integer>();
                    vetPosicao.add(vet0.get(i));
                    vetPosicao.add(pos0.get(i));
                    vetPosicao.add(vet1.get(j));
                    vetPosicao.add(pos1.get(j));

                    listaPosicoes.add(vetPosicao);
                }
            }
        }

        return listaPosicoes;
    }   
    
    public static ArrayList<ArrayList<Integer>> randBinario1p(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<ArrayList<Integer>> listaPosicoes = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vetPosicao = new ArrayList<Integer>();
        Random rand = new Random();

        int vetor = rand.nextInt(vetorSolucao.size());
        int posicao = rand.nextInt(vetorSolucao.size());

        vetPosicao.add(vetor);
        vetPosicao.add(posicao);

        listaPosicoes.add(vetPosicao);

        return listaPosicoes;
    }

    public static ArrayList<ArrayList<Integer>> randBinario2pMt(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<ArrayList<Integer>> listaPosicoes = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vetPosicao;
        Random rand = new Random();

        int vetor1 = rand.nextInt(vetorSolucao.size());
        int posicao1 = rand.nextInt(vetorSolucao.get(vetor1).size());
        int vetor2 = rand.nextInt(vetorSolucao.size());
        int posicao2 = rand.nextInt(vetorSolucao.get(vetor2).size());

        while( vetorSolucao.get(vetor1).get(posicao1) == vetorSolucao.get(vetor2).get(posicao2) ){
            vetor2 = rand.nextInt(vetorSolucao.size());
            posicao2 = rand.nextInt(vetorSolucao.get(vetor2).size());
        }

        vetPosicao = new ArrayList<Integer>();
        vetPosicao.add(vetor1);
        vetPosicao.add(posicao1);
        vetPosicao.add(vetor2);
        vetPosicao.add(posicao2);

        listaPosicoes.add(vetPosicao);

        return listaPosicoes;
    }

    public static ArrayList<ArrayList<Integer>> randBinario2pDv(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<ArrayList<Integer>> listaPosicoes = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vetPosicao;
        Random rand = new Random();

        int vetor1 = rand.nextInt(vetorSolucao.size());
        int posicao1 = rand.nextInt(vetorSolucao.get(vetor1).size());
        int vetor2 = (vetor1+rand.nextInt(vetorSolucao.size()-1)+1)%vetorSolucao.size();
        int posicao2 = rand.nextInt(vetorSolucao.get(vetor2).size());

        while( vetorSolucao.get(vetor1).get(posicao1) == vetorSolucao.get(vetor2).get(posicao2) ){
            posicao2 = rand.nextInt(vetorSolucao.get(vetor2).size());
        }

        vetPosicao = new ArrayList<Integer>();
        vetPosicao.add(vetor1);
        vetPosicao.add(posicao1);
        vetPosicao.add(vetor2);
        vetPosicao.add(posicao2);

        listaPosicoes.add(vetPosicao);

        return listaPosicoes;
    }

    public static ArrayList<ArrayList<Integer>> randBinario2pMv(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<ArrayList<Integer>> listaPosicoes = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vetPosicao;
        Random rand = new Random();
        int vetor1;
        int posicao1;
        int posicao2;

        vetor1 = rand.nextInt(vetorSolucao.size());
        posicao1 = rand.nextInt(vetorSolucao.get(vetor1).size());
        posicao2 = rand.nextInt(vetorSolucao.get(vetor1).size());

        while( vetorSolucao.get(vetor1).get(posicao1) == vetorSolucao.get(vetor1).get(posicao2) ){
            posicao2 = rand.nextInt(vetorSolucao.get(vetor1).size());
        }

        vetPosicao = new ArrayList<Integer>();
        vetPosicao.add(vetor1);
        vetPosicao.add(posicao1);
        vetPosicao.add(vetor1);
        vetPosicao.add(posicao2);

        listaPosicoes.add(vetPosicao);

        return listaPosicoes;
    }    

// Lista das posiÃ§Ãµes criadas para vetores do tipo Inteiro, porÃ©m podem ser 
// utilizados por outros tipos de vetores.    
    public static ArrayList<ArrayList<Integer>> inteiro2pMt(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<ArrayList<Integer>> listaPosicoes = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vetor = new ArrayList<Integer>();
        ArrayList<Integer> posicao = new ArrayList<Integer>();
        ArrayList<Integer> vetPosicao;

        int numElementos = 0;

        for(int i=0; i<vetorSolucao.size(); i++){
            for(int j=0; j<vetorSolucao.get(i).size(); j++){
                vetor.add(i);
                posicao.add(j);
                numElementos++;
            }
        }

        for(int i=0; i<numElementos-1; i++){
            for(int j=i+1; j<numElementos; j++){
                vetPosicao = new ArrayList<Integer>();
                vetPosicao.add(vetor.get(i));
                vetPosicao.add(posicao.get(i));
                vetPosicao.add(vetor.get(j));
                vetPosicao.add(posicao.get(j));

                listaPosicoes.add(vetPosicao);
            }
        }

        return listaPosicoes;
    }

    public static ArrayList<ArrayList<Integer>> inteiro2pDv(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<ArrayList<Integer>> listaPosicoes = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vetor = new ArrayList<Integer>();
        ArrayList<Integer> posicao = new ArrayList<Integer>();
        ArrayList<Integer> vetorAux;
        ArrayList<Integer> posicaoAux;
        ArrayList<Integer> divisao = new ArrayList<Integer>();
        ArrayList<Integer> vetPosicao;

        int numElementos = 0;
        divisao.add(numElementos);

        for(int i=0; i<vetorSolucao.size(); i++){
            for(int j=0; j<vetorSolucao.get(i).size(); j++){
                vetor.add(i);
                posicao.add(j);
                numElementos++;
            }

            divisao.add(numElementos);
        }

        for(int i=0; i<numElementos; i++){
            vetorAux = new ArrayList<Integer>();
            posicaoAux = new ArrayList<Integer>();

            vetorAux.addAll(vetor.subList(0,divisao.get(vetor.get(i))));
            vetorAux.addAll(vetor.subList(divisao.get(vetor.get(i)+1),numElementos));
            posicaoAux.addAll(posicao.subList(0,divisao.get(posicao.get(i))));
            posicaoAux.addAll(posicao.subList(divisao.get(posicao.get(i)+1),numElementos));

            for(int j=0; j<vetorAux.size(); j++){
                vetPosicao = new ArrayList<Integer>();
                vetPosicao.add(vetor.get(i));
                vetPosicao.add(posicao.get(i));
                vetPosicao.add(vetorAux.get(j));
                vetPosicao.add(posicaoAux.get(j));

                listaPosicoes.add(vetPosicao);
            }
        }

        return listaPosicoes;
    }

    public static ArrayList<ArrayList<Integer>> inteiro2pMv(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<ArrayList<Integer>> listaPosicoes = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vetor = new ArrayList<Integer>();
        ArrayList<Integer> posicao = new ArrayList<Integer>();
        ArrayList<Integer> vetorAux;
        ArrayList<Integer> posicaoAux;
        ArrayList<Integer> divisao = new ArrayList<Integer>();
        ArrayList<Integer> vetPosicao;

        int numElementos = 0;
        divisao.add(numElementos);

        for(int i=0; i<vetorSolucao.size(); i++){
            for(int j=0; j<vetorSolucao.get(i).size(); j++){
                vetor.add(i);
                posicao.add(j);
                numElementos++;
            }

            divisao.add(numElementos);
        }

        for(int i=0; i<vetor.size(); i++){
            vetorAux = new ArrayList<Integer>();
            posicaoAux = new ArrayList<Integer>();

            vetorAux.addAll(vetor.subList(divisao.get(vetor.get(i)),divisao.get(vetor.get(i)+1)));
            posicaoAux.addAll(posicao.subList(divisao.get(vetor.get(i)),divisao.get(vetor.get(i)+1)));

            for(int j=posicao.get(i)+1; j<vetorAux.size(); j++){
                vetPosicao = new ArrayList<Integer>();
                vetPosicao.add(vetor.get(i));
                vetPosicao.add(posicao.get(i));
                vetPosicao.add(vetorAux.get(j));
                vetPosicao.add(posicaoAux.get(j));

                listaPosicoes.add(vetPosicao);
            }
        }

        return listaPosicoes;
    }

    public static ArrayList<ArrayList<Integer>> inteiro1pDv(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<ArrayList<Integer>> listaPosicoes = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vetor = new ArrayList<Integer>();
        ArrayList<Integer> posicao = new ArrayList<Integer>();
        ArrayList<Integer> vetPosicao;

        int numElementos = 0;

        for(int i=0; i<vetorSolucao.size(); i++){
            for(int j=0; j<vetorSolucao.get(i).size(); j++){
                vetor.add(i);
                posicao.add(j);
                numElementos++;
            }
        }

        int pos;
        for(int i=0; i<numElementos; i++){
            for(int j=0; j<vetorSolucao.size()-1; j++){
                pos = (vetor.get(i)+j+1)%vetorSolucao.size();
                vetPosicao = new ArrayList<Integer>();
                vetPosicao.add(vetor.get(i));
                vetPosicao.add(posicao.get(i));
                vetPosicao.add(pos);

                listaPosicoes.add(vetPosicao);
            }
        }

        return listaPosicoes;
    }

    public static ArrayList<ArrayList<Integer>> randInteiroInteger2pMt(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<ArrayList<Integer>> listaPosicoes = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vetPosicao;
        Random rand = new Random();

        int vetor1 = rand.nextInt(vetorSolucao.size());
        int posicao1 = rand.nextInt(vetorSolucao.get(vetor1).size());
        int vetor2 = rand.nextInt(vetorSolucao.size());
        int posicao2;

        if( vetor1 == vetor2 ){
            posicao2 = (posicao1+rand.nextInt(vetorSolucao.size()-1)+1)%vetorSolucao.size();
        } else{
            posicao2 = rand.nextInt(vetorSolucao.get(vetor2).size());
        }
        
        vetPosicao = new ArrayList<Integer>();
        vetPosicao.add(vetor1);
        vetPosicao.add(posicao1);
        vetPosicao.add(vetor2);
        vetPosicao.add(posicao2);

        listaPosicoes.add(vetPosicao);

        return listaPosicoes;
    }

    public static ArrayList<Object> randInteiro2pMt(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<Object> vetPosicao;
        Random rand = new Random();

        int vetor1 = rand.nextInt(vetorSolucao.size());
        int posicao1 = rand.nextInt(vetorSolucao.get(vetor1).size());
        int vetor2 = rand.nextInt(vetorSolucao.size());
        int posicao2;

        if( vetor1 == vetor2 ){
            posicao2 = (posicao1+rand.nextInt(vetorSolucao.size()-1)+1)%vetorSolucao.size();
        } else{
            posicao2 = rand.nextInt(vetorSolucao.get(vetor2).size());
        }
        
        vetPosicao = new ArrayList<Object>();
        vetPosicao.add(vetor1);
        vetPosicao.add(posicao1);
        vetPosicao.add(vetor2);
        vetPosicao.add(posicao2);

        return vetPosicao;
    }

    public static ArrayList<ArrayList<Integer>> randInteiroInteger2pDv(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<ArrayList<Integer>> listaPosicoes = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vetPosicao;
        Random rand = new Random();

        int vetor1 = rand.nextInt(vetorSolucao.size());
        int posicao1 = rand.nextInt(vetorSolucao.get(vetor1).size());
        int vetor2 = (vetor1+rand.nextInt(vetorSolucao.size()-1)+1)%vetorSolucao.size();
        int posicao2 = rand.nextInt(vetorSolucao.get(vetor2).size());

        vetPosicao = new ArrayList<Integer>();
        vetPosicao.add(vetor1);
        vetPosicao.add(posicao1);
        vetPosicao.add(vetor2);
        vetPosicao.add(posicao2);

        listaPosicoes.add(vetPosicao);

        return listaPosicoes;
    }

    public static ArrayList<Object> randInteiro2pDv(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<Object> listaPosicoes = new ArrayList<Object>();
        ArrayList<Integer> vetPosicao;
        Random rand = new Random();

        int vetor1 = rand.nextInt(vetorSolucao.size());
        int posicao1 = rand.nextInt(vetorSolucao.get(vetor1).size());
        int vetor2 = (vetor1+rand.nextInt(vetorSolucao.size()-1)+1)%vetorSolucao.size();
        int posicao2 = rand.nextInt(vetorSolucao.get(vetor2).size());

        vetPosicao = new ArrayList<Integer>();
        vetPosicao.add(vetor1);
        vetPosicao.add(posicao1);
        vetPosicao.add(vetor2);
        vetPosicao.add(posicao2);

        listaPosicoes.add(vetPosicao);

        return listaPosicoes;
    }

    public static ArrayList<ArrayList<Integer>> randInteiroInteger2pMv(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<ArrayList<Integer>> listaPosicoes = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vetPosicao;
        Random rand = new Random();

        int vetor1 = rand.nextInt(vetorSolucao.size());
        int posicao1 = rand.nextInt(vetorSolucao.get(vetor1).size());
        int posicao2 = (posicao1+rand.nextInt(vetorSolucao.size()-1)+1)%vetorSolucao.size();

        vetPosicao = new ArrayList<Integer>();
        vetPosicao.add(vetor1);
        vetPosicao.add(posicao1);
        vetPosicao.add(vetor1);
        vetPosicao.add(posicao2);

        listaPosicoes.add(vetPosicao);

        return listaPosicoes;
    }

    public static ArrayList<Object> randInteiro2pMv(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<Object> listaPosicoes = new ArrayList<Object>();
        ArrayList<Integer> vetPosicao;
        Random rand = new Random();

        int vetor1 = rand.nextInt(vetorSolucao.size());
        int posicao1 = rand.nextInt(vetorSolucao.get(vetor1).size());
        int posicao2 = (posicao1+rand.nextInt(vetorSolucao.size()-1)+1)%vetorSolucao.size();

        vetPosicao = new ArrayList<Integer>();
        vetPosicao.add(vetor1);
        vetPosicao.add(posicao1);
        vetPosicao.add(vetor1);
        vetPosicao.add(posicao2);

        listaPosicoes.add(vetPosicao);

        return listaPosicoes;
    }

    public static ArrayList<Integer> randInteiro1pDv(ArrayList<ArrayList<Object>> vetorSolucao){
        ArrayList<Integer> vetPosicao = new ArrayList<Integer>();
        Random rand = new Random();

        int vetor1 = rand.nextInt(vetorSolucao.size());
        int posicao1 = rand.nextInt(vetorSolucao.get(vetor1).size());
        int vetor2 = (vetor1+rand.nextInt(vetorSolucao.size()-1)+1)%vetorSolucao.size();

        vetPosicao = new ArrayList<Integer>();
        vetPosicao.add(vetor1);
        vetPosicao.add(posicao1);
        vetPosicao.add(vetor2);

        return vetPosicao;
    }

    public static ArrayList<ArrayList<String>> dadosEntrada(String nomeProblema){
        ArrayList<String> dados;
        ArrayList<ArrayList<String>> dd = new ArrayList<ArrayList<String>>();

        if( nomeProblema.equals("PartNum") ){
            dados = new ArrayList<String>();
            dados.add("MelhorVizinho"); //BuscaLocal
            dd.add(dados);
            
            dados = new ArrayList<String>();
            dados.add("Inteiro2pDv"); //ListaVizinhos
            dd.add(dados);
            
            dados = new ArrayList<String>();
            dados.add("Inteiro2pDv"); //ModificaSolucao
            dd.add(dados);
            
            dados = new ArrayList<String>();
            dados.add("Posicao"); //Lista Candidatos
            dd.add(dados);

            dados = new ArrayList<String>();
            dados.add("Inteiro2pDv"); //Pertubacao1 - ILS
            dados.add("Inteiro2pDv"); //Pertubacao2 - ILS
            dd.add(dados);
        } else if( nomeProblema.equals("Mochila") ){
            dados = new ArrayList<String>();
            dados.add("MelhorVizinho"); //BuscaLocal
            dd.add(dados);
            
            dados = new ArrayList<String>();
            dados.add("Binario1p"); //ListaVizinhos
            dd.add(dados);
            
            dados = new ArrayList<String>();
            dados.add("Binario1p"); //ModificaSolucao
            dd.add(dados);
            
            dados = new ArrayList<String>();
            dados.add("vetorPosicao"); //Lista Candidatos
            dd.add(dados);

            dados = new ArrayList<String>();
            dados.add("Binario1p"); //Pertubacao1 - ILS
            dados.add("Binario2pMt"); //Pertubacao2 - ILS
            dd.add(dados);
        } else if( nomeProblema.equals("PDM") ){
            dados = new ArrayList<String>();
            dados.add("Randomica"); //BuscaLocal
            dd.add(dados);
            
            dados = new ArrayList<String>();
            dados.add("Inteiro2pDv"); //ListaVizinhos
            dd.add(dados);
            
            dados = new ArrayList<String>();
            dados.add("Inteiro2pDv"); //ModificaSolucao
            dd.add(dados);
            
            dados = new ArrayList<String>();
            dados.add("vetorPosicao"); //Lista Candidatos
            dd.add(dados);

            dados = new ArrayList<String>();
            dados.add("Inteiro2pDv"); //Pertubacao1 - ILS
            dados.add("Inteiro2pDv"); //Pertubacao2 - ILS
            dd.add(dados);
        } else{

        }

        return dd;
    }
}

