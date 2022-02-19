package br.cefetmg.lsi.bimasco.persistence.dao;

import br.cefetmg.lsi.bimasco.persistence.RegionState;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;

@Dao
public interface RegionStateDAO {
    @Insert
    void save(RegionState regionState);

    @Select
    PagingIterable<RegionState> findAll();

    @Select(allowFiltering = true, customWhereClause = "name = :name")
    PagingIterable<RegionState> findByName(String name);

    @Delete
    void delete(RegionState regionState);

    default void clearTable() {
        PagingIterable<RegionState> states = findAll();
        states.all()
                .forEach(this::delete);
    }
}
