package br.cefetmg.lsi.bimasco.core.agents;

import java.util.UUID;

public class Probability {

    private UUID idAgent;
    private Double probability;

    public Probability(UUID id, Double prob){
        idAgent = id;
        probability = prob;
    }

    public UUID getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(UUID idAgent) {
        this.idAgent = idAgent;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }
}

