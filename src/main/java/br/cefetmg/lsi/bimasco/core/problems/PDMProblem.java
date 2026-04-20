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
    
    private Integer[][] diversityMatrix;
    private Integer numElements;
    private Integer numSolutionElements;
    private ArrayList<Double> fitness;
    private ArrayList<Object> parameters;
    private Integer lowerLimit;
    
    public PDMProblem(){
        super();
    }

    @Override
    public void initialize() {
        //System.err.println("PDMProblem");
        ArrayList<ArrayList<String>> problemData = new ArrayList<ArrayList<String>>();
        problemData = this.readFile(this.getProblemSettings());

        numSolutionElements = Integer.parseInt(problemData.get(0).get(1));
        numElements = Integer.parseInt(problemData.get(0).get(0));
        diversityMatrix = new Integer[numElements][numElements];

        int diver = 0;
        int counter = 1;
        for(int i=0; i<numElements-1; i++){
            for(int j=i+1; j<numElements; j++){
                diver = Integer.parseInt(problemData.get(counter).get(2));
                counter++;
                diversityMatrix[i][j] = diver;
            }
        }

        fitness = new ArrayList<Double>();
        fitness.add(0.0);

        ArrayList<Double> valueVector = new ArrayList<Double>();
        double value;

        for(int i=0; i<numElements; i++){
            valueVector.add(0.0);

            for(int j=i+1; j<i+numElements-1; j++){
                counter = 0;
                value = diversityIJ(i,j%numElements);
                while( value > valueVector.get(counter) && counter < valueVector.size()-1 ){
                    counter++;
                }

                valueVector.add(counter,value);
            }

            value = 0;
            for(int j=0; j<numSolutionElements; j++){
                value += valueVector.get(j);
            }

            fitness.add(value);
        }

        parameters = new ArrayList<Object>();
        parameters.add(numSolutionElements);

        lowerLimit = 0;
    }

    @Override
    public Integer getDimension() {
        return numElements;
    }

    @Override
    public Integer getSolutionElementsCount() {
        return numSolutionElements;
    }

    @Override
    public double getStep() {
        return 0;
    }

    @Override
    public Object getFitnessFunction(List<Object> element) {
        int position = (Integer) element.get(0);

        return fitness.get(position);
    }

    @Override
    public Object getLimit() {
        return lowerLimit;
    }

    @Override
    public ArrayList<Object> getParameters() {
        return parameters;
    }
        
    public Integer diversityIJ(int elementI, int elementJ){
        int diversityIJ = 0;

        if( elementI < elementJ ){
            diversityIJ = diversityMatrix[elementI][elementJ];
        } else{
            diversityIJ = diversityMatrix[elementJ][elementI];
        }

        return diversityIJ;
    }

    public ArrayList<ArrayList<String>> readFile(ProblemSettings problemSettings){
        ArrayList<ArrayList<String>> problemData = new ArrayList<ArrayList<String>>();
        ArrayList<String> inputData;
        String line = null;
        
        try {
            StringTokenizer st = null;
            while ((line) != null 
            		&& !(line.contains("EOF"))){
                st = new StringTokenizer(line, "\t");
                String data = null;
                inputData = new ArrayList<String>();
                
                while (st.hasMoreTokens()) {
                    data = st.nextToken();
                    inputData.add(data);
                }
                
                problemData.add(inputData);
            }

      } catch (Exception e) {
         e.printStackTrace();
      }
        
      return problemData;
   }

    public void writeFile(String fileName, ArrayList<ArrayList<String>> output){
        BufferedWriter bufferedWriter = null;
        
        try {
            bufferedWriter = new BufferedWriter( new FileWriter( fileName ) );

            for(int i=0; i<output.size(); i++ ){
                for(int j=0; j<output.get(i).size(); j++){
                    bufferedWriter.write( output.get(i).get(j) );
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
