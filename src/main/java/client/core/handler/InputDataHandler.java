package client.core.handler;

import shared.protocol.persistence.Header;
import shared.protocol.persistence.Protocol;
import shared.protocol.util.ProtocolParser;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CompletableFuture;

public class InputDataHandler extends shared.protocol.handler.InputDataHandler {
    protected final CompletableFuture<Protocol<?>> resultFuture;

    public InputDataHandler(Header header, ByteBuffer headerBuffer, InputHeaderHandler inputHeaderHandler, CompletableFuture<Protocol<?>> resultFuture) {
        super(header, headerBuffer, inputHeaderHandler);
        this.resultFuture = resultFuture;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        super.completed(result, buffer);

        if (result != -1) {
            buffer.flip();
            Protocol<?> protocol = ProtocolParser.parseProtocol(header, buffer);
//            completeConsumer.accept(protocol);
            resultFuture.complete(protocol);
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        super.failed(exc, buffer);
        resultFuture.completeExceptionally(exc);
    }
}
