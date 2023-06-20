package br.cefetmg.lsi.bimasco.persistence.dao;

import br.cefetmg.lsi.bimasco.persistence.AgentState;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;

@Dao
public interface AgentStateDAO {

    @Insert
    void save(AgentState state);

    @Select(allowFiltering = true, customWhereClause = "persistent_id = :agentId")
    PagingIterable<AgentState> findByPersistentId(String agentId);

    @Select(allowFiltering = true, customWhereClause = "problem_name = :problemName and persistent_id = :agentId")
    PagingIterable<AgentState> findByProblemIdAndPersistentId(String problemName, String persistentId);

    @Select
    PagingIterable<AgentState> findAll();

    @Select(allowFiltering = true, customWhereClause = "problem_name = :problemName")
    PagingIterable<AgentState> findByProblem(String problemName);

    @Delete
    void delete(AgentState agentState);

    default void clearTable() {
        PagingIterable<AgentState> states = findAll();
        states.all()
                .forEach(this::delete);
    }
}
