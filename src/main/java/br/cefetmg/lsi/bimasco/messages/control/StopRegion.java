package br.cefetmg.lsi.bimasco.messages.control;

import java.util.UUID;

public class StopRegion {
    private UUID regionId;

    public StopRegion(UUID regionId){
        this.regionId = regionId;
    }

    public UUID getRegionId() {
        return regionId;
    }
}
