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
public class InputHeaderHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final AsynchronousSocketChannel client;
    private final Consumer<AsynchronousSocketChannel> closeClientConsumer;
    private final BiConsumer<AsynchronousSocketChannel, Protocol<?>> completeConsumer;

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        if (result == -1) {
            closeClientConsumer.accept(client);
            return;
        }

        if (result != Header.BYTES) {
            throw new IllegalBufferSizeException("Invalid header size (expected : " + Header.BYTES + ", value : " + result + ")");
        }

        buffer.flip();
        Header header = ProtocolParser.parseHeader(buffer);
        ByteBuffer dataBuffer = ByteBuffer.allocate(header.getDataLength());
        client.read(dataBuffer, dataBuffer, new InputDataHandler(client, closeClientConsumer, completeConsumer, header));
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        exc.printStackTrace();
    }
}
