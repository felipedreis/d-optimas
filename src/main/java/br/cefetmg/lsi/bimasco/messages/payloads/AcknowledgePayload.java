package br.cefetmg.lsi.bimasco.messages.payloads;

import br.cefetmg.lsi.bimasco.core.agents.Agent;
import br.cefetmg.lsi.bimasco.core.regions.Region;

public class AcknowledgePayload extends Payload {

    //TODO: Think about create a hierarchy with Region and Agent
    private Agent agent;
    private Region region;

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
