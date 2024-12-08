package shared.protocol.handler;

import lombok.AllArgsConstructor;
import server.exception.IllegalBufferSizeException;
import shared.protocol.persistence.*;
import shared.protocol.util.ProtocolParser;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@AllArgsConstructor
public class InputDataHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final AsynchronousSocketChannel client;
    private final Consumer<AsynchronousSocketChannel> closeClientConsumer;
    private final BiConsumer<AsynchronousSocketChannel,Protocol<?>> completeConsumer;
    private final Header header;

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        if (result == -1) {
            closeClientConsumer.accept(client);
            return;
        }

        if (buffer.position() != header.getDataLength()) {
            throw new IllegalBufferSizeException("Invalid data size (expected : " + header.getDataLength() + ", value : " + result + ")");
        }

        buffer.flip();
        Protocol<?> protocol = ProtocolParser.parseProtocol(header, buffer);
        completeConsumer.accept(client, protocol);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        exc.printStackTrace();
    }
}
