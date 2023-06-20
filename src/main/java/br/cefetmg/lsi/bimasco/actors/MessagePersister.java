package br.cefetmg.lsi.bimasco.actors;

import br.cefetmg.lsi.bimasco.core.Problem;
import br.cefetmg.lsi.bimasco.persistence.MessageState;
import br.cefetmg.lsi.bimasco.persistence.dao.MessageStateDAO;

public interface MessagePersister {

    default MessageState received(String problemId, AbstractMessage message, long time, String entityId) {
        return new MessageState(
                message.messageId,
                problemId,
                time,
                System.currentTimeMillis(), entityId,
                message.getClass().getSimpleName(),
                MessageState.RECEIVED
        );
    }

    default MessageState sent(String problemId, AbstractMessage message, long time, String entityId) {
        return new MessageState(
                message.messageId,
                problemId,
                time,
                System.currentTimeMillis(),
                entityId,
                message.getClass().getSimpleName(),
                MessageState.SENT);
    }


    default void persistMessage(MessageState s) { messageStateDao().save(s); }

    MessageStateDAO messageStateDao();
}
