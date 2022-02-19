/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.lsi.bimasco.core.problems;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.settings.ProblemSettings;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

//TODO change problem name
//TODO rename elements name
//TODO remove all references about instance file (like lerArquivo)
//TODO: analyse type of returns
@Deprecated
public class PDMProblem extends Problem {
    
    private Integer[][] matrizDiversidade;
    private Integer numeroElementos;
    private Integer numeroElementosSolucao;
    private ArrayList<Double> aptidao;
    private ArrayList<Object> parametro;
    private Integer limiteInferior;
    
    public PDMProblem(){
        super();
    }

    @Override
    public void initialize() {
        //System.err.println("ProblemaPDM");
        ArrayList<ArrayList<String>> dadosProblema = new ArrayList<ArrayList<String>>();
        dadosProblema = this.lerArquivo(this.getProblemSettings());

        numeroElementosSolucao = Integer.parseInt(dadosProblema.get(0).get(1));
        numeroElementos = Integer.parseInt(dadosProblema.get(0).get(0));
        matrizDiversidade = new Integer[numeroElementos][numeroElementos];

        int diver = 0;
        int contador = 1;
        for(int i=0; i<numeroElementos-1; i++){
            for(int j=i+1; j<numeroElementos; j++){
                diver = Integer.parseInt(dadosProblema.get(contador).get(2));
                contador++;
                matrizDiversidade[i][j] = diver;
            }
        }

        aptidao = new ArrayList<Double>();
        aptidao.add(0.0);

        ArrayList<Double> vetorValor = new ArrayList<Double>();
        double valor;

        for(int i=0; i<numeroElementos; i++){
            vetorValor.add(0.0);

            for(int j=i+1; j<i+numeroElementos-1; j++){
                contador = 0;
                valor = diversidadeIJ(i,j%numeroElementos);
                while( valor > vetorValor.get(contador) && contador < vetorValor.size()-1 ){
                    contador++;
                }

                vetorValor.add(contador,valor);
            }

            valor = 0;
            for(int j=0; j<numeroElementosSolucao; j++){
                valor += vetorValor.get(j);
            }

            aptidao.add(valor);
        }

        parametro = new ArrayList<Object>();
        parametro.add(numeroElementosSolucao);

        limiteInferior = 0;
    }

    @Override
    public Integer getDimension() {
        return numeroElementos;
    }

    @Override
    public Integer getSolutionElementsCount() {
        return numeroElementosSolucao;
    }

    @Override
    public double getStep() {
        return 0;
    }

    @Override
    public Object getFitnessFunction(List<Object> element) {
        int posicao = (Integer) element.get(0);

        return aptidao.get(posicao);
    }

    @Override
    public Object getLimit() {
        return limiteInferior;
    }

    @Override
    public ArrayList<Object> getParameters() {
        return parametro;
    }
        
    public Integer diversidadeIJ(int elementoI, int elementoJ){
        int diversidadeIJ = 0;

        if( elementoI < elementoJ ){
            diversidadeIJ = matrizDiversidade[elementoI][elementoJ];
        } else{
            diversidadeIJ = matrizDiversidade[elementoJ][elementoI];
        }

        return diversidadeIJ;
    }

    public ArrayList<ArrayList<String>> lerArquivo(ProblemSettings problemSettings){
        ArrayList<ArrayList<String>> dadosProblema = new ArrayList<ArrayList<String>>();
        ArrayList<String> dadosEntrada;
        String linha = null;
        //String caminhoInstancia = "Instancias/" + Problema.getNome() + "/" + nomeInstancia;
        
        try {
            StringTokenizer st = null;
            //linha = leitor.readLine();
            while ((linha) != null 
            		&& !(linha.contains("EOF"))){
                st = new StringTokenizer(linha, "\t");
                String dados = null;
                dadosEntrada = new ArrayList<String>();
                
                while (st.hasMoreTokens()) {
                    dados = st.nextToken();
                    dadosEntrada.add(dados);
                }
                
                dadosProblema.add(dadosEntrada);
                //linha = leitor.readLine();
            }
            
            //leitor.close();

      } catch (Exception e) {
         e.printStackTrace();
      }
        
      return dadosProblema;
   }

    public void gravarArquivo(String arquivo, ArrayList<ArrayList<String>> saida){
        BufferedWriter bufferedWriter = null;
        
        try {
            bufferedWriter = new BufferedWriter( new FileWriter( arquivo ) );

            for(int i=0; i<saida.size(); i++ ){
                for(int j=0; j<saida.get(i).size(); j++){
                    bufferedWriter.write( saida.get(i).get(j) );
                    bufferedWriter.newLine();
                }
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the BufferedWriter
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
