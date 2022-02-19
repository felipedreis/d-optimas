package br.cefetmg.lsi.bimasco.core.heuristics.stopCondition;

import br.cefetmg.lsi.bimasco.settings.MetaHeuristicParameters;

import java.io.Serializable;
import java.util.Map;

//TODO: Analyse about pass problem reference
public interface StopCondition extends Serializable {

    public boolean isSatisfied(Object f0, double stopTime, int maxIterations, int maxIterationsWI, Map<String, Object> metaHeuristicParameters);
}
