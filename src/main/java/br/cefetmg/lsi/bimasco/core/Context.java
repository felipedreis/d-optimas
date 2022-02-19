package br.cefetmg.lsi.bimasco.core;

import java.io.Serializable;

public interface Context extends Serializable {
    void accept(Solution<?, ?, ?> solution);

    void resetContext();
}
