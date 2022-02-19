package br.cefetmg.lsi.bimasco.persistence;

import br.cefetmg.lsi.bimasco.persistence.dao.*;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

@Mapper
public interface DOptimasMapper {

    @DaoFactory
    GlobalStateDAO globalStateDAO();

    @DaoFactory
    RegionStateDAO regionStateDAO();

    @DaoFactory
    SolutionStateDAO solutionStateDAO();

    @DaoFactory
    MessageStateDAO messageStateDAO();

    @DaoFactory
    AgentStateDAO agentStateDAO();

    @DaoFactory
    MemoryStateDAO memoryStateDAO();
}
