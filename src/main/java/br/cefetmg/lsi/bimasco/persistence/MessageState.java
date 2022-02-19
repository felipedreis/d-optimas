package br.cefetmg.lsi.bimasco.persistence;

import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import java.util.UUID;

@Entity(defaultKeyspace = "d_optimas")
public class MessageState {
    public static final String SENT = "sent";
    public static final String RECEIVED = "received";

    @PartitionKey
    private UUID id;

    private long time;

    private long timestamp;

    private String entityId;

    private String messageType;

    private String status;

    public MessageState(){}

    public MessageState(UUID id, long time, long timestamp, String entityId, String messageType, String status) {
        this.id = id;
        this.time = time;
        this.timestamp = timestamp;
        this.entityId = entityId;
        this.messageType = messageType;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MessageState{" +
                "id=" + id +
                ", time=" + time +
                ", timestamp=" + timestamp +
                ", entityId='" + entityId + '\'' +
                ", messageType='" + messageType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
