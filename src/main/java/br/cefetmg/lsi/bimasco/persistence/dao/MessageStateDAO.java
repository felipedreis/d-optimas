package br.cefetmg.lsi.bimasco.persistence.dao;

import br.cefetmg.lsi.bimasco.persistence.MessageState;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;

import java.util.UUID;

@Dao
public interface MessageStateDAO {
    @Insert
    void save(MessageState state);

    @Select
    PagingIterable<MessageState> findAll();

    @Select
    PagingIterable<MessageState> findById(UUID id);

    @Select(allowFiltering = true, customWhereClause = "entity_id = :entityId")
    PagingIterable<MessageState> findByEntity(String entityId);

    @Select(allowFiltering = true, customWhereClause = "message_type = :messageType")
    PagingIterable<MessageState> findByType(String messageType);

    @Delete
    void delete(MessageState messageState);

    default void clearTable() {
        PagingIterable<MessageState> states = findAll();
        states.all()
                .forEach(this::delete);
    }
}
