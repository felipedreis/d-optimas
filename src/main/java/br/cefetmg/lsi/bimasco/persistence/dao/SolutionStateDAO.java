package br.cefetmg.lsi.bimasco.persistence.dao;

import br.cefetmg.lsi.bimasco.persistence.SolutionState;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;

import java.util.List;
import java.util.UUID;

@Dao
public interface SolutionStateDAO {
    @Insert
    void save(SolutionState solutionState);

    @Select(allowFiltering = true, customWhereClause = "id = :id and region = :region")
    SolutionState findById(UUID id, String region);

    @Select(allowFiltering = true, customWhereClause = "region = :region")
    PagingIterable<SolutionState> findByRegion(String region);

    @Select(customWhereClause = "id in :ids")
    PagingIterable<SolutionState> findByIds(List<UUID> ids);

    @Select
    PagingIterable<SolutionState> findAll();

    @Delete
    void delete(SolutionState state);

    default void clearTable() {
        PagingIterable<SolutionState> solutionStates = findAll();
        solutionStates.all()
                .forEach(this::delete);
    }
}
