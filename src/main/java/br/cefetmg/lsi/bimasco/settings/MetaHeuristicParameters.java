package br.cefetmg.lsi.bimasco.settings;

public class MetaHeuristicParameters {

    private String type;
    private String value;

    public MetaHeuristicParameters(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
