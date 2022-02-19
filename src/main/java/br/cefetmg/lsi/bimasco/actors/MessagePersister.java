package br.cefetmg.lsi.bimasco.actors;

import br.cefetmg.lsi.bimasco.persistence.MessageState;
import br.cefetmg.lsi.bimasco.persistence.dao.MessageStateDAO;

public interface MessagePersister {



    default MessageState received(AbstractMessage message, long time, String entityId) {
        return new MessageState(message.messageId, time, System.currentTimeMillis(), entityId,
                message.getClass().getSimpleName(), MessageState.RECEIVED);
    }

    default MessageState sent(AbstractMessage message, long time, String entityId) {
        return new MessageState(message.messageId, time, System.currentTimeMillis(), entityId,
                message.getClass().getSimpleName(), MessageState.SENT);
    }


    default void persistMessage(MessageState s) { messageStateDao().save(s); }

    MessageStateDAO messageStateDao();
}
