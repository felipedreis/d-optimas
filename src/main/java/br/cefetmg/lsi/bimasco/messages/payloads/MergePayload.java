package br.cefetmg.lsi.bimasco.messages.payloads;

import br.cefetmg.lsi.bimasco.core.regions.Region;

public class MergePayload extends Payload {

    private Region region;
    private boolean status;

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
