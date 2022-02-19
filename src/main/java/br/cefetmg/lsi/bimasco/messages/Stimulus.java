package br.cefetmg.lsi.bimasco.messages;

import br.cefetmg.lsi.bimasco.messages.payloads.MergePayload;
import br.cefetmg.lsi.bimasco.messages.payloads.Payload;

import java.io.Serializable;

//TODO: I think just a payload is sufficient
public class Stimulus implements Serializable {

    private StimulusInformation information;
    private StimulusType type;
    private Payload payload;

    public Stimulus() {}

    public Stimulus(Payload payload) {
        this.payload = payload;
    }

    public StimulusInformation getInformation() {
        return information;
    }

    public void setInformation(StimulusInformation information) {
        this.information = information;
    }

    //TODO: Remove this
    public enum StimulusInformation {
        ACKNOWLEDGE,
        MERGE_REQUEST,
        MERGE_RESPONSE,
        SOLUTION_REQUEST,
        SOLUTION_RESULT,
        REGION_MERGED,
        CHANGE_MY_STATE,
        BOOTSTRAP,
    }

    public enum StimulusType {
        DIRECTLY,
        BROADCAST
    }

    public Payload getPayload() {
        return this.payload;
    }
}
