package br.cefetmg.lsi.bimasco.data.impl;

import br.cefetmg.lsi.bimasco.data.Extractor;
import br.cefetmg.lsi.bimasco.persistence.RegionState;
import br.cefetmg.lsi.bimasco.persistence.dao.RegionStateDAO;

import java.util.Comparator;
import java.util.List;

public class RegionStateExtractor implements Extractor<RegionState> {
    private RegionStateDAO regionStateDAO;
    private String regionName;

    public RegionStateExtractor(RegionStateDAO regionStateDAO, String regionName) {
        this.regionStateDAO = regionStateDAO;
        this.regionName = regionName;
    }

    @Override
    public List<RegionState> getData() {
        List<RegionState> states = regionStateDAO.findByName(regionName).all();
        states.sort(Comparator.comparing(RegionState::getTime));
        return states;
    }

    @Override
    public String formatDataToCsv(RegionState regionState) {
        return null;
    }

    @Override
    public String getFileName() {
        return null;
    }
}
