package br.cefetmg.lsi.bimasco.core.agents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class ActionSelection implements Serializable {

    private boolean hasCooperation;

    public ActionSelection(boolean hasCooperation){
        this.hasCooperation = hasCooperation;
    }

    public void selectAgentTargetRequest(Memory memory, String agentName) {

        Double step = 0.0;
        Double nextStep = 0.0;
        Random rand = new Random();
        Double aleatoryDouble = rand.nextDouble();
        for(Probability probability : memory.getProbabilities()){
            nextStep = step + probability.getProbability();
            if(((aleatoryDouble > step) && (aleatoryDouble < nextStep))){
                if(hasCooperation){
                    if(probability.getIdAgent().compareTo(new UUID(0L, 0L)) != 0) {
                        memory.setAgentTargetRequest(probability.getIdAgent());
                    }
                } else {
                    if(probability.getIdAgent().compareTo(new UUID(0L, 0L)) != 0){
                        memory.setAgentTargetRequest(probability.getIdAgent());
                    }
                }
            }
            step = nextStep;
        }
    }

    public Integer howManyTimes(Integer id, ArrayList<Integer> goodTargets){
        Integer result = 0;
        for(Integer target : goodTargets){
            if(target.equals(id))
                result++;
        }
        return result;
    }
}


