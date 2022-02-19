package br.cefetmg.lsi.bimasco.persistence.dao;

import br.cefetmg.lsi.bimasco.persistence.MemoryState;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;

@Dao
public interface MemoryStateDAO {

    @Insert
    void save(MemoryState memoryState);

    @Select
    PagingIterable<MemoryState> findAll();

    @Select(allowFiltering = true, customWhereClause = "agent = :agent")
    PagingIterable<MemoryState> findByAgent(String agent);

    @Delete
    void delete(MemoryState memoryState);

    default void clearTable() {
        PagingIterable<MemoryState> states = findAll();
        states.all()
                .forEach(this::delete);
    }
}
