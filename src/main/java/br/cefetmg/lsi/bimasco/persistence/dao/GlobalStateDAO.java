package br.cefetmg.lsi.bimasco.persistence.dao;

import br.cefetmg.lsi.bimasco.persistence.GlobalState;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;

@Dao
public interface GlobalStateDAO  {
    @Insert
    void save(GlobalState g);

    @Select
    PagingIterable<GlobalState> findAll();

    @Select(allowFiltering = true, customWhereClause = "problem_name = :problemName")
    PagingIterable<GlobalState> findByProblem(String problemName);

    @Delete
    void delete(GlobalState globalState);

    default void clearTable() {
        PagingIterable<GlobalState> states = findAll();
        states.all()
                .forEach(this::delete);
    }
}
