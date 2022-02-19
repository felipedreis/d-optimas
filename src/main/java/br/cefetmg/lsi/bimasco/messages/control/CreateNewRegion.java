package br.cefetmg.lsi.bimasco.messages.control;

import br.cefetmg.lsi.bimasco.core.regions.Region;

import java.io.Serializable;

//TODO: Review this name and folder
public class CreateNewRegion implements Serializable{

    private Region newRegion;

    public CreateNewRegion(Region region){
        this.newRegion = region;
    }

    public Region getNewRegion() {
        return newRegion;
    }
}
