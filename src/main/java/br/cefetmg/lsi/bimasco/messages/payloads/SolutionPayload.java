package br.cefetmg.lsi.bimasco.messages.payloads;

import akka.io.Inet;
import br.cefetmg.lsi.bimasco.core.Solution;

import java.util.List;
import java.util.UUID;

public class SolutionPayload extends Payload {

    private List<Solution> solutionList;
    private int solutionsCount;
    private UUID emitterId;

    public SolutionPayload(){

    }

    public SolutionPayload(List<Solution> solutionList, int solutionsCount, UUID emitterId){
        this.solutionList = solutionList;
        this.solutionsCount = solutionsCount;
        this.emitterId = emitterId;
    }

    public List<Solution> getSolutionList() {
        return solutionList;
    }

    public int getSolutionsCount() {
        return solutionsCount;
    }

    public UUID getEmitterId() {
        return emitterId;
    }
}
