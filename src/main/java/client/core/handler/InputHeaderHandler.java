package client.core.handler;

import lombok.Getter;
import shared.protocol.persistence.Header;
import shared.protocol.persistence.Protocol;
import shared.protocol.util.ProtocolParser;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Getter
public class InputHeaderHandler extends shared.protocol.handler.InputHeaderHandler {
    protected final CompletableFuture<Protocol<?>> resultFuture;

    public InputHeaderHandler(AsynchronousSocketChannel client, Consumer<AsynchronousSocketChannel> closeClientConsumer, CompletableFuture<Protocol<?>> resultFuture) {
        super(client, closeClientConsumer);
        this.resultFuture = resultFuture;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        super.completed(result, buffer);

        if (result != -1) {
            buffer.flip();
            Header header = ProtocolParser.parseHeader(buffer);
            System.out.println(header);
            ByteBuffer dataBuffer = ByteBuffer.allocate(header.getDataLength());
            client.read(dataBuffer, dataBuffer, new InputDataHandler(header, buffer, this, resultFuture));
        }
    }
}
