package br.cefetmg.lsi.bimasco.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BasicFunctions {
    // Creates a zero vector of size <size>
    public static ArrayList<Object> nullObjectVector(Integer size){
        ArrayList<Object> nullList = new ArrayList<Object>();

        for(int i=0; i<size; i++){
            nullList.add(0);
        }

        return nullList;
    }

    public static ArrayList<Integer> nullIntegerVector(Integer size){
        ArrayList<Integer> nullList = new ArrayList<Integer>();

        for(int i=0; i<size; i++){
            nullList.add(0);
        }

        return nullList;
    }

    // Creates a vector of size <size> with all elements equal to <number>
    public static ArrayList<Object> objectNumberVector(Integer size, Integer number){
        ArrayList<Object> nullList = new ArrayList<Object>();

        for(int i=0; i<size; i++){
            nullList.add(number);
        }

        return nullList;
    }

    public static ArrayList<Integer> integerNumberVector(Integer size, Integer number){
        ArrayList<Integer> nullList = new ArrayList<Integer>();

        for(int i=0; i<size; i++){
            nullList.add(number);
        }

        return nullList;
    }

    // Creates an ordered vector of size <size>
    public static ArrayList<ArrayList<Object>> orderedObjectPositionVector(Integer size){
        ArrayList<Object> auxiliaryList;
        ArrayList<ArrayList<Object>> orderedList = new ArrayList<ArrayList<Object>>();

        for(int i=0; i<size; i++){
            auxiliaryList = new ArrayList<Object>();
            auxiliaryList.add(i);
            orderedList.add(auxiliaryList);
        }

        return orderedList;
    }

    public static ArrayList<Integer> orderedIntegerPositionVector(Integer size){
        ArrayList<Integer> orderedList = new ArrayList<Integer>();

        for(int i=0; i<size; i++){
            orderedList.add(i);
        }

        return orderedList;
    }

    // Creates an unordered vector of positions from 0 to <size>
    public static ArrayList<ArrayList<Object>> unorderedObjectPositionVector(Integer size){
        ArrayList<Object> auxiliaryList = new ArrayList<Object>();
        ArrayList<Integer> positionList = new ArrayList<Integer>();
        ArrayList<ArrayList<Object>> unorderedList = new ArrayList<ArrayList<Object>>();
        Random rand;
        int index;

        for (int i=0; i<size; i++){
            positionList.add(i);
        }

        for (int i=0; i<size; i++){
            rand = new Random();
            index = rand.nextInt(size-i);

            auxiliaryList = new ArrayList<Object>();
            auxiliaryList.add(positionList.get(index));
            positionList.remove(index);
            unorderedList.add(auxiliaryList);
        }

        return unorderedList;
    }

    public static ArrayList<Integer> unorderedIntegerPositionVector(Integer size){
        ArrayList<Integer> positionList = new ArrayList<Integer>();
        ArrayList<Integer> unorderedList = new ArrayList<Integer>();
        Random rand;
        int index;

        for (int i=0; i<size; i++){
            positionList.add(i);
        }

        for (int i=0; i<size; i++){
            rand = new Random();
            index = rand.nextInt(size-i);

            unorderedList.add(positionList.get(index));
            positionList.remove(index);
        }

        return unorderedList;
    }

    // Shuffles a vector of type ArrayList<ArrayList<Object>>
   public static ArrayList<ArrayList<Object>> shuffleVector(ArrayList<ArrayList<Object>> vector){
        ArrayList<ArrayList<Object>> shuffledVector = new ArrayList<ArrayList<Object>>();
        ArrayList<Integer> shuffledList = new ArrayList<Integer>();

        shuffledList = BasicFunctions.unorderedIntegerPositionVector(vector.size());

        for(int i=0; i<vector.size(); i++){
            shuffledVector.add(vector.get(shuffledList.get(i)));
        }

        return shuffledVector;
    }

   public static ArrayList<Object> shuffleVectorElements(ArrayList<Object> vector){
        ArrayList<Object> shuffledVector = new ArrayList<Object>();
        ArrayList<Integer> shuffledList = new ArrayList<Integer>();

        shuffledList = BasicFunctions.unorderedIntegerPositionVector(vector.size());

        for(int i=0; i<vector.size(); i++){
            shuffledVector.add(vector.get(shuffledList.get(i)));
        }

        return shuffledVector;
    }

   // Shuffles a vector of type ArrayList<ArrayList<Integer>>
   public static ArrayList<ArrayList<Object>> shuffleIntegerVector(ArrayList<ArrayList<Integer>> vector){
        ArrayList<ArrayList<Object>> shuffledVector = new ArrayList<ArrayList<Object>>();
        ArrayList<Integer> shuffledList = new ArrayList<Integer>();
        ArrayList<Object> element;
        
        shuffledList = BasicFunctions.unorderedIntegerPositionVector(vector.size());

        for(int i=0; i<vector.size(); i++){
            element = new ArrayList<Object>();

            for(int j=0; j<vector.get(shuffledList.get(i)).size(); j++){
                element.add(vector.get(shuffledList.get(i)).get(j));
            }
            
            shuffledVector.add(element);
        }

        return shuffledVector;
    }

   public static List<Integer> shuffleIntegerVectorElements(List<Integer> vector){
        ArrayList<Integer> shuffledVector = new ArrayList<Integer>();
        ArrayList<Integer> shuffledList = new ArrayList<Integer>();
        
        shuffledList = BasicFunctions.unorderedIntegerPositionVector(vector.size());

        for(int i=0; i<vector.size(); i++){
            shuffledVector.add(vector.get(shuffledList.get(i)));
        }

        return shuffledVector;
    }

   public static ArrayList<ArrayList<Integer>> intraVectorIntegerPosition(ArrayList<Integer> size){
        // Vectors where the order does not matter, therefore an object will be described as: <vector1, position1, vector2>
        ArrayList<Integer> auxiliaryList = new ArrayList<Integer>();
        ArrayList<Integer> positionList = new ArrayList<Integer>();
        ArrayList<Integer> position = new ArrayList<Integer>();
        ArrayList<Integer> vectorList = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> positionsList = new ArrayList<ArrayList<Integer>>();

        position.add(0);
        for(int i=0; i<size.size(); i++){
            vectorList.addAll(BasicFunctions.integerNumberVector(size.get(i),i));
            positionList.addAll(BasicFunctions.orderedIntegerPositionVector(size.get(i)));
            position.add(position.get(i)+size.get(i));
        }

        for(int i=0; i<auxiliaryList.size()-1; i++){
            for(int j=position.get(i+1); j<auxiliaryList.size()-1; j++){
                auxiliaryList = new ArrayList<Integer>();

                auxiliaryList.add(vectorList.get(i));
                auxiliaryList.add(positionList.get(i));
                auxiliaryList.add(vectorList.get(j));

                positionsList.add(auxiliaryList);
            }
        }

        return positionsList;
    }

   public static ArrayList<ArrayList<Integer>> intraVectorIntegerPositionOrder(ArrayList<Integer> size){
        // Vectors where the order matters, therefore an object will be described as: <vector1, position1, vector2, position2>
        ArrayList<Integer> auxiliaryList = new ArrayList<Integer>();
        ArrayList<Integer> positionList = new ArrayList<Integer>();
        ArrayList<Integer> vectorList = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> positionsList = new ArrayList<ArrayList<Integer>>();

        for(int i=0; i<size.size(); i++){
            vectorList.addAll(BasicFunctions.integerNumberVector(size.get(i),i));
            positionList.addAll(BasicFunctions.orderedIntegerPositionVector(size.get(i)));
        }

        for(int i=0; i<auxiliaryList.size()-1; i++){
            for(int j=i+1; j<auxiliaryList.size(); j++){
                auxiliaryList = new ArrayList<Integer>();

                auxiliaryList.add(vectorList.get(i));
                auxiliaryList.add(positionList.get(i));
                auxiliaryList.add(vectorList.get(j));
                auxiliaryList.add(positionList.get(j));

                positionsList.add(auxiliaryList);
            }
        }

        return positionsList;
    }

    public static ArrayList<ArrayList<Object>> intraVectorPosition(ArrayList<Integer> size){
        // Vectors where the order does not matter, therefore an object will be described as: <vector1, position1, vector2>
        ArrayList<Object> auxiliaryList = new ArrayList<Object>();
        ArrayList<Integer> positionList = new ArrayList<Integer>();
        ArrayList<Integer> position = new ArrayList<Integer>();
        ArrayList<Integer> vectorList = new ArrayList<Integer>();
        ArrayList<ArrayList<Object>> positionsList = new ArrayList<ArrayList<Object>>();
        int pos = 0;

        position.add(0);
        for(int i=0; i<size.size(); i++){
            positionList.addAll(BasicFunctions.orderedIntegerPositionVector(size.get(i)));
            vectorList.addAll(BasicFunctions.integerNumberVector(size.get(i),i));
            position.add(position.get(i)+size.get(i));
        }

        for(int i=0; i<position.get(size.size()-1); i++){
            pos = position.get(vectorList.get(i)+1);

            for(int j=pos; j<vectorList.size(); j++){
                auxiliaryList = new ArrayList<Object>();

                auxiliaryList.add(vectorList.get(i));
                auxiliaryList.add(positionList.get(i));
                auxiliaryList.add(vectorList.get(j));

                positionsList.add(auxiliaryList);
            }
        }

        return positionsList;
    }

   public static ArrayList<ArrayList<Object>> intraVectorPositionOrder(ArrayList<Integer> size){
        // Vectors where the order matters, therefore an object will be described as: <vector1, position1, vector2, position2>
        ArrayList<Object> auxiliaryList = new ArrayList<Object>();
        ArrayList<Integer> positionList = new ArrayList<Integer>();
        ArrayList<Integer> vectorList = new ArrayList<Integer>();
        ArrayList<ArrayList<Object>> positionsList = new ArrayList<ArrayList<Object>>();

        for(int i=0; i<size.size(); i++){
            vectorList.addAll(BasicFunctions.integerNumberVector(size.get(i),i));
            positionList.addAll(BasicFunctions.orderedIntegerPositionVector(size.get(i)));
        }

        for(int i=0; i<auxiliaryList.size()-1; i++){
            for(int j=i+1; j<auxiliaryList.size(); j++){
                auxiliaryList = new ArrayList<Object>();
                
                auxiliaryList.add(vectorList.get(i));
                auxiliaryList.add(positionList.get(i));
                auxiliaryList.add(vectorList.get(j));
                auxiliaryList.add(positionList.get(j));

                positionsList.add(auxiliaryList);
            }
        }

        return positionsList;
    }

    public static ArrayList<ArrayList<Object>> chooseObjectSubset(Integer setSize, Integer subsetSize){
        ArrayList<Object> auxiliaryList = new ArrayList<Object>();
        ArrayList<Integer> positionList = new ArrayList<Integer>();
        ArrayList<ArrayList<Object>> subsetList = new ArrayList<ArrayList<Object>>();
        Random rand;
        int index;

        for (int i=0; i<setSize; i++){
            positionList.add(i);
        }

        for (int i=0; i<subsetSize; i++){
            rand = new Random();
            index = rand.nextInt(setSize-i);

            auxiliaryList = new ArrayList<Object>();
            auxiliaryList.add(positionList.get(index));
            positionList.remove(index);
            subsetList.add(auxiliaryList);
        }

        return subsetList;
    }

    public static ArrayList<Integer> chooseIntegerSubset(Integer setSize, Integer subsetSize){
        ArrayList<Integer> positionList = new ArrayList<Integer>();
        ArrayList<Integer> subsetList = new ArrayList<Integer>();
        Random rand;
        int index;

        for (int i=0; i<setSize; i++){
            positionList.add(i);
        }

        for (int i=0; i<subsetSize; i++){
            rand = new Random();
            index = rand.nextInt(setSize-i);

            subsetList.add(positionList.get(index));
            positionList.remove(index);
        }

        return subsetList;
    }

    public static ArrayList<ArrayList<Object>> chooseVectorSubset(ArrayList<ArrayList<Object>> vector, Integer size){
        ArrayList<ArrayList<Object>> subsetList = new ArrayList<ArrayList<Object>>();
        ArrayList<ArrayList<Object>> auxiliaryVector = new ArrayList<ArrayList<Object>>();
        Random rand;
        int index;

        auxiliaryVector = vector;
        for (int i=0; i<size; i++){
            rand = new Random();
            index = rand.nextInt(vector.size());

            subsetList.add(auxiliaryVector.get(index));
            auxiliaryVector.remove(index);
        }

        return subsetList;
    }

    public static ArrayList<Object> chooseVectorSubsetElements(ArrayList<Object> vector, Integer size){
        ArrayList<Object> subsetList = new ArrayList<Object>();
        Random rand;
        int index;
        
        for (int i=0; i<size; i++){
            rand = new Random();
            index = rand.nextInt(vector.size());

            subsetList.add(vector.get(index));
            vector.remove(index);
        }

        return subsetList;
    }

    // used by other types of vectors.
    public static ArrayList<ArrayList<Integer>> splitBinaryVector(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<ArrayList<Integer>> vector = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vet0 = new ArrayList<Integer>();
        ArrayList<Integer> pos0 = new ArrayList<Integer>();
        ArrayList<Integer> vet1 = new ArrayList<Integer>();
        ArrayList<Integer> pos1 = new ArrayList<Integer>();

        for(int i=0; i<solutionVector.size(); i++){
            for(int j=0; j<solutionVector.get(i).size(); j++){
                if( (Integer)solutionVector.get(i).get(j) == 0 ){
                    vet0.add(i);
                    pos0.add(j);
                } else{
                    vet1.add(i);
                    pos1.add(j);
                }
            }
        }

        vector.add(vet0);
        vector.add(pos0);
        vector.add(vet1);
        vector.add(pos1);

        return vector;
    }

    public static ArrayList<ArrayList<Integer>> binaryOnePosition(ArrayList<ArrayList<Object>> vector){
        ArrayList<ArrayList<Integer>> positionsList = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> positionsVector;
        
        for(int i=0; i<vector.size(); i++){
            for(int j=0; j<vector.get(i).size(); j++){
                positionsVector = new ArrayList<Integer>();
                positionsVector.add(i);
                positionsVector.add(j);
                
                positionsList.add(positionsVector);
            }
        }

        return positionsList;
    }

    public static ArrayList<ArrayList<Integer>> binaryTwoPositionsDiverse(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<ArrayList<Integer>> auxiliaryVector = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vet0 = new ArrayList<Integer>();
        ArrayList<Integer> pos0 = new ArrayList<Integer>();
        ArrayList<Integer> vet1 = new ArrayList<Integer>();
        ArrayList<Integer> pos1 = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> positionsList = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> positionVector;

        auxiliaryVector = BasicFunctions.splitBinaryVector(solutionVector);
        vet0 = auxiliaryVector.get(0);
        pos0 = auxiliaryVector.get(1);
        vet1 = auxiliaryVector.get(2);
        pos1 = auxiliaryVector.get(3);

        for(int i=0; i<vet0.size(); i++){
            for(int j=0; j<vet1.size(); j++){
                if( vet0.get(i) != vet1.get(j) ){
                    positionVector = new ArrayList<Integer>();
                    positionVector.add(vet0.get(i));
                    positionVector.add(pos0.get(i));
                    positionVector.add(vet1.get(j));
                    positionVector.add(pos1.get(j));

                    positionsList.add(positionVector);
                }
            }
        }

        return positionsList;
    }
    
    public static ArrayList<ArrayList<Integer>> binaryTwoPositionsAll(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<ArrayList<Integer>> auxiliaryVector = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vet0 = new ArrayList<Integer>();
        ArrayList<Integer> pos0 = new ArrayList<Integer>();
        ArrayList<Integer> vet1 = new ArrayList<Integer>();
        ArrayList<Integer> pos1 = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> positionsList = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> positionVector;

        auxiliaryVector = BasicFunctions.splitBinaryVector(solutionVector);
        vet0 = auxiliaryVector.get(0);
        pos0 = auxiliaryVector.get(1);
        vet1 = auxiliaryVector.get(2);
        pos1 = auxiliaryVector.get(3);

        for(int i=0; i<vet0.size(); i++){
            for(int j=0; j<vet1.size(); j++){
                positionVector = new ArrayList<Integer>();
                positionVector.add(vet0.get(i));
                positionVector.add(pos0.get(i));
                positionVector.add(vet1.get(j));
                positionVector.add(pos1.get(j));

                positionsList.add(positionVector);
            }
        }

        return positionsList;
    }

    public static ArrayList<ArrayList<Integer>> binaryTwoPositionsSameVector(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<ArrayList<Integer>> auxiliaryVector = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vet0 = new ArrayList<Integer>();
        ArrayList<Integer> pos0 = new ArrayList<Integer>();
        ArrayList<Integer> vet1 = new ArrayList<Integer>();
        ArrayList<Integer> pos1 = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> positionsList = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> positionVector;

        auxiliaryVector = BasicFunctions.splitBinaryVector(solutionVector);
        vet0 = auxiliaryVector.get(0);
        pos0 = auxiliaryVector.get(1);
        vet1 = auxiliaryVector.get(2);
        pos1 = auxiliaryVector.get(3);

        for(int i=0; i<vet0.size(); i++){
            for(int j=0; j<vet1.size(); j++){
                if( vet0.get(i) == vet1.get(j) ){
                    positionVector = new ArrayList<Integer>();
                    positionVector.add(vet0.get(i));
                    positionVector.add(pos0.get(i));
                    positionVector.add(vet1.get(j));
                    positionVector.add(pos1.get(j));

                    positionsList.add(positionVector);
                }
            }
        }

        return positionsList;
    }   
    
    public static ArrayList<ArrayList<Integer>> randomBinaryOnePosition(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<ArrayList<Integer>> positionsList = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> positionVector = new ArrayList<Integer>();
        Random rand = new Random();

        int vector = rand.nextInt(solutionVector.size());
        int position = rand.nextInt(solutionVector.size());

        positionVector.add(vector);
        positionVector.add(position);

        positionsList.add(positionVector);

        return positionsList;
    }

    public static ArrayList<ArrayList<Integer>> randomBinaryTwoPositionsAll(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<ArrayList<Integer>> positionsList = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> positionVector;
        Random rand = new Random();

        int vector1 = rand.nextInt(solutionVector.size());
        int position1 = rand.nextInt(solutionVector.get(vector1).size());
        int vector2 = rand.nextInt(solutionVector.size());
        int position2 = rand.nextInt(solutionVector.get(vector2).size());

        while( solutionVector.get(vector1).get(position1) == solutionVector.get(vector2).get(position2) ){
            vector2 = rand.nextInt(solutionVector.size());
            position2 = rand.nextInt(solutionVector.get(vector2).size());
        }

        positionVector = new ArrayList<Integer>();
        positionVector.add(vector1);
        positionVector.add(position1);
        positionVector.add(vector2);
        positionVector.add(position2);

        positionsList.add(positionVector);

        return positionsList;
    }

    public static ArrayList<ArrayList<Integer>> randomBinaryTwoPositionsDiverse(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<ArrayList<Integer>> positionsList = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> positionVector;
        Random rand = new Random();

        int vector1 = rand.nextInt(solutionVector.size());
        int position1 = rand.nextInt(solutionVector.get(vector1).size());
        int vector2 = (vector1+rand.nextInt(solutionVector.size()-1)+1)%solutionVector.size();
        int position2 = rand.nextInt(solutionVector.get(vector2).size());

        while( solutionVector.get(vector1).get(position1) == solutionVector.get(vector2).get(position2) ){
            position2 = rand.nextInt(solutionVector.get(vector2).size());
        }

        positionVector = new ArrayList<Integer>();
        positionVector.add(vector1);
        positionVector.add(position1);
        positionVector.add(vector2);
        positionVector.add(position2);

        positionsList.add(positionVector);

        return positionsList;
    }

    public static ArrayList<ArrayList<Integer>> randomBinaryTwoPositionsSameVector(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<ArrayList<Integer>> positionsList = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> positionVector;
        Random rand = new Random();
        int vector1;
        int position1;
        int position2;

        vector1 = rand.nextInt(solutionVector.size());
        position1 = rand.nextInt(solutionVector.get(vector1).size());
        position2 = rand.nextInt(solutionVector.get(vector1).size());

        while( solutionVector.get(vector1).get(position1) == solutionVector.get(vector1).get(position2) ){
            position2 = rand.nextInt(solutionVector.get(vector1).size());
        }

        positionVector = new ArrayList<Integer>();
        positionVector.add(vector1);
        positionVector.add(position1);
        positionVector.add(vector1);
        positionVector.add(position2);

        positionsList.add(positionVector);

        return positionsList;
    }    

    // List of positions created for vectors of type Integer, but can be
    // used by other types of vectors.
    public static ArrayList<ArrayList<Integer>> integerTwoPositionsAll(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<ArrayList<Integer>> positionsList = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vector = new ArrayList<Integer>();
        ArrayList<Integer> position = new ArrayList<Integer>();
        ArrayList<Integer> positionVector;

        int numElements = 0;

        for(int i=0; i<solutionVector.size(); i++){
            for(int j=0; j<solutionVector.get(i).size(); j++){
                vector.add(i);
                position.add(j);
                numElements++;
            }
        }

        for(int i=0; i<numElements-1; i++){
            for(int j=i+1; j<numElements; j++){
                positionVector = new ArrayList<Integer>();
                positionVector.add(vector.get(i));
                positionVector.add(position.get(i));
                positionVector.add(vector.get(j));
                positionVector.add(position.get(j));

                positionsList.add(positionVector);
            }
        }

        return positionsList;
    }

    public static ArrayList<ArrayList<Integer>> integerTwoPositionsDiverse(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<ArrayList<Integer>> positionsList = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vector = new ArrayList<Integer>();
        ArrayList<Integer> position = new ArrayList<Integer>();
        ArrayList<Integer> auxiliaryVector;
        ArrayList<Integer> auxiliaryPosition;
        ArrayList<Integer> division = new ArrayList<Integer>();
        ArrayList<Integer> positionVector;

        int numElements = 0;
        division.add(numElements);

        for(int i=0; i<solutionVector.size(); i++){
            for(int j=0; j<solutionVector.get(i).size(); j++){
                vector.add(i);
                position.add(j);
                numElements++;
            }

            division.add(numElements);
        }

        for(int i=0; i<numElements; i++){
            auxiliaryVector = new ArrayList<Integer>();
            auxiliaryPosition = new ArrayList<Integer>();

            auxiliaryVector.addAll(vector.subList(0,division.get(vector.get(i))));
            auxiliaryVector.addAll(vector.subList(division.get(vector.get(i)+1),numElements));
            auxiliaryPosition.addAll(position.subList(0,division.get(position.get(i))));
            auxiliaryPosition.addAll(position.subList(division.get(position.get(i)+1),numElements));

            for(int j=0; j<auxiliaryVector.size(); j++){
                positionVector = new ArrayList<Integer>();
                positionVector.add(vector.get(i));
                positionVector.add(position.get(i));
                positionVector.add(auxiliaryVector.get(j));
                positionVector.add(auxiliaryPosition.get(j));

                positionsList.add(positionVector);
            }
        }

        return positionsList;
    }

    public static ArrayList<ArrayList<Integer>> integerTwoPositionsSameVector(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<ArrayList<Integer>> positionsList = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vector = new ArrayList<Integer>();
        ArrayList<Integer> position = new ArrayList<Integer>();
        ArrayList<Integer> auxiliaryVector;
        ArrayList<Integer> auxiliaryPosition;
        ArrayList<Integer> division = new ArrayList<Integer>();
        ArrayList<Integer> positionVector;

        int numElements = 0;
        division.add(numElements);

        for(int i=0; i<solutionVector.size(); i++){
            for(int j=0; j<solutionVector.get(i).size(); j++){
                vector.add(i);
                position.add(j);
                numElements++;
            }

            division.add(numElements);
        }

        for(int i=0; i<vector.size(); i++){
            auxiliaryVector = new ArrayList<Integer>();
            auxiliaryPosition = new ArrayList<Integer>();

            auxiliaryVector.addAll(vector.subList(division.get(vector.get(i)),division.get(vector.get(i)+1)));
            auxiliaryPosition.addAll(position.subList(division.get(vector.get(i)),division.get(vector.get(i)+1)));

            for(int j=position.get(i)+1; j<auxiliaryVector.size(); j++){
                positionVector = new ArrayList<Integer>();
                positionVector.add(vector.get(i));
                positionVector.add(position.get(i));
                positionVector.add(auxiliaryVector.get(j));
                positionVector.add(auxiliaryPosition.get(j));

                positionsList.add(positionVector);
            }
        }

        return positionsList;
    }

    public static ArrayList<ArrayList<Integer>> integerOnePositionDiverse(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<ArrayList<Integer>> positionsList = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> vector = new ArrayList<Integer>();
        ArrayList<Integer> position = new ArrayList<Integer>();
        ArrayList<Integer> positionVector;

        int numElements = 0;

        for(int i=0; i<solutionVector.size(); i++){
            for(int j=0; j<solutionVector.get(i).size(); j++){
                vector.add(i);
                position.add(j);
                numElements++;
            }
        }

        int pos;
        for(int i=0; i<numElements; i++){
            for(int j=0; j<solutionVector.size()-1; j++){
                pos = (vector.get(i)+j+1)%solutionVector.size();
                positionVector = new ArrayList<Integer>();
                positionVector.add(vector.get(i));
                positionVector.add(position.get(i));
                positionVector.add(pos);

                positionsList.add(positionVector);
            }
        }

        return positionsList;
    }

    public static ArrayList<ArrayList<Integer>> randomIntegerTwoPositionsAll(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<ArrayList<Integer>> positionsList = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> positionVector;
        Random rand = new Random();

        int vector1 = rand.nextInt(solutionVector.size());
        int position1 = rand.nextInt(solutionVector.get(vector1).size());
        int vector2 = rand.nextInt(solutionVector.size());
        int position2;

        if( vector1 == vector2 ){
            position2 = (position1+rand.nextInt(solutionVector.size()-1)+1)%solutionVector.size();
        } else{
            position2 = rand.nextInt(solutionVector.get(vector2).size());
        }
        
        positionVector = new ArrayList<Integer>();
        positionVector.add(vector1);
        positionVector.add(position1);
        positionVector.add(vector2);
        positionVector.add(position2);

        positionsList.add(positionVector);

        return positionsList;
    }

    public static ArrayList<Object> randomIntegerTwoPositionsAllObject(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<Object> positionVector;
        Random rand = new Random();

        int vector1 = rand.nextInt(solutionVector.size());
        int position1 = rand.nextInt(solutionVector.get(vector1).size());
        int vector2 = rand.nextInt(solutionVector.size());
        int position2;

        if( vector1 == vector2 ){
            position2 = (position1+rand.nextInt(solutionVector.size()-1)+1)%solutionVector.size();
        } else{
            position2 = rand.nextInt(solutionVector.get(vector2).size());
        }
        
        positionVector = new ArrayList<Object>();
        positionVector.add(vector1);
        positionVector.add(position1);
        positionVector.add(vector2);
        positionVector.add(position2);

        return positionVector;
    }

    public static ArrayList<ArrayList<Integer>> randomIntegerTwoPositionsDiverse(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<ArrayList<Integer>> positionsList = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> positionVector;
        Random rand = new Random();

        int vector1 = rand.nextInt(solutionVector.size());
        int position1 = rand.nextInt(solutionVector.get(vector1).size());
        int vector2 = (vector1+rand.nextInt(solutionVector.size()-1)+1)%solutionVector.size();
        int position2 = rand.nextInt(solutionVector.get(vector2).size());

        positionVector = new ArrayList<Integer>();
        positionVector.add(vector1);
        positionVector.add(position1);
        positionVector.add(vector2);
        positionVector.add(position2);

        positionsList.add(positionVector);

        return positionsList;
    }

    public static ArrayList<Object> randomIntegerTwoPositionsDiverseObject(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<Object> positionsList = new ArrayList<Object>();
        ArrayList<Integer> positionVector;
        Random rand = new Random();

        int vector1 = rand.nextInt(solutionVector.size());
        int position1 = rand.nextInt(solutionVector.get(vector1).size());
        int vector2 = (vector1+rand.nextInt(solutionVector.size()-1)+1)%solutionVector.size();
        int position2 = rand.nextInt(solutionVector.get(vector2).size());

        positionVector = new ArrayList<Integer>();
        positionVector.add(vector1);
        positionVector.add(position1);
        positionVector.add(vector2);
        positionVector.add(position2);

        positionsList.add(positionVector);

        return positionsList;
    }

    public static ArrayList<ArrayList<Integer>> randomIntegerTwoPositionsSameVector(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<ArrayList<Integer>> positionsList = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> positionVector;
        Random rand = new Random();

        int vector1 = rand.nextInt(solutionVector.size());
        int position1 = rand.nextInt(solutionVector.get(vector1).size());
        int position2 = (position1+rand.nextInt(solutionVector.size()-1)+1)%solutionVector.size();

        positionVector = new ArrayList<Integer>();
        positionVector.add(vector1);
        positionVector.add(position1);
        positionVector.add(vector1);
        positionVector.add(position2);

        positionsList.add(positionVector);

        return positionsList;
    }

    public static ArrayList<Object> randomIntegerTwoPositionsSameVectorObject(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<Object> positionsList = new ArrayList<Object>();
        ArrayList<Integer> positionVector;
        Random rand = new Random();

        int vector1 = rand.nextInt(solutionVector.size());
        int position1 = rand.nextInt(solutionVector.get(vector1).size());
        int position2 = (position1+rand.nextInt(solutionVector.size()-1)+1)%solutionVector.size();

        positionVector = new ArrayList<Integer>();
        positionVector.add(vector1);
        positionVector.add(position1);
        positionVector.add(vector1);
        positionVector.add(position2);

        positionsList.add(positionVector);

        return positionsList;
    }

    public static ArrayList<Integer> randomIntegerOnePositionDiverse(ArrayList<ArrayList<Object>> solutionVector){
        ArrayList<Integer> positionVector = new ArrayList<Integer>();
        Random rand = new Random();

        int vector1 = rand.nextInt(solutionVector.size());
        int position1 = rand.nextInt(solutionVector.get(vector1).size());
        int vector2 = (vector1+rand.nextInt(solutionVector.size()-1)+1)%solutionVector.size();

        positionVector = new ArrayList<Integer>();
        positionVector.add(vector1);
        positionVector.add(position1);
        positionVector.add(vector2);

        return positionVector;
    }

    public static ArrayList<ArrayList<String>> getInputData(String problemName){
        ArrayList<String> data;
        ArrayList<ArrayList<String>> dd = new ArrayList<ArrayList<String>>();

        if( problemName.equals("PartNum") ){
            data = new ArrayList<String>();
            data.add("BestNeighbor"); //LocalSearch
            dd.add(data);
            
            data = new ArrayList<String>();
            data.add("IntegerTwoPositionsDiverse"); //NeighborsList
            dd.add(data);
            
            data = new ArrayList<String>();
            data.add("IntegerTwoPositionsDiverse"); //ModifySolution
            dd.add(data);
            
            data = new ArrayList<String>();
            data.add("Position"); //Candidates List
            dd.add(data);

            data = new ArrayList<String>();
            data.add("IntegerTwoPositionsDiverse"); //Perturbation1 - ILS
            data.add("IntegerTwoPositionsDiverse"); //Perturbation2 - ILS
            dd.add(data);
        } else if( problemName.equals("Knapsack") ){
            data = new ArrayList<String>();
            data.add("BestNeighbor"); //LocalSearch
            dd.add(data);
            
            data = new ArrayList<String>();
            data.add("BinaryOnePosition"); //NeighborsList
            dd.add(data);
            
            data = new ArrayList<String>();
            data.add("BinaryOnePosition"); //ModifySolution
            dd.add(data);
            
            data = new ArrayList<String>();
            data.add("positionVector"); //Candidates List
            dd.add(data);

            data = new ArrayList<String>();
            data.add("BinaryOnePosition"); //Perturbation1 - ILS
            data.add("BinaryTwoPositionsAll"); //Perturbation2 - ILS
            dd.add(data);
        } else if( problemName.equals("PDM") ){
            data = new ArrayList<String>();
            data.add("Random"); //LocalSearch
            dd.add(data);
            
            data = new ArrayList<String>();
            data.add("IntegerTwoPositionsDiverse"); //NeighborsList
            dd.add(data);
            
            data = new ArrayList<String>();
            data.add("IntegerTwoPositionsDiverse"); //ModifySolution
            dd.add(data);
            
            data = new ArrayList<String>();
            data.add("positionVector"); //Candidates List
            dd.add(data);

            data = new ArrayList<String>();
            data.add("IntegerTwoPositionsDiverse"); //Perturbation1 - ILS
            data.add("IntegerTwoPositionsDiverse"); //Perturbation2 - ILS
            dd.add(data);
        } else{

        }

        return dd;
    }
}
