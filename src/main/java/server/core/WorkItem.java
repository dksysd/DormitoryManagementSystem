package server.core;

import lombok.Getter;
import lombok.Setter;
import shared.protocol.persistence.Protocol;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Objects;

@Getter
@Setter
public class WorkItem {
    private final AsynchronousSocketChannel client;
    private final Protocol<?> requestProtocol;
    private Protocol<?> responseProtocol;
    private final long timestamp;

    public WorkItem(AsynchronousSocketChannel client, Protocol<?> requestProtocol, long timestamp) {
        this.client = client;
        this.requestProtocol = requestProtocol;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "WorkItem{" +
                "client=" + client +
                ", requestProtocol=" + requestProtocol +
                ", responseProtocol=" + responseProtocol +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (WorkItem) obj;
        return Objects.equals(this.client, that.client) &&
                Objects.equals(this.requestProtocol, that.requestProtocol) &&
                Objects.equals(this.responseProtocol, that.responseProtocol) &&
                this.timestamp == that.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, requestProtocol, responseProtocol, timestamp);
    }
}
