package shared.protocol.handler;

import lombok.AllArgsConstructor;
import server.util.RemoteAddressResolver;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.Consumer;

@AllArgsConstructor
public class OutputHandler implements CompletionHandler<Integer, ByteBuffer> {
    protected final AsynchronousSocketChannel client;
    protected final Consumer<AsynchronousSocketChannel> closeClientConsumer;

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        if(result == -1) {
            closeClientConsumer.accept(client);
        }

        if (buffer.hasRemaining()) {
            System.out.println("Send data to " + RemoteAddressResolver.getRemoteAddress(client));
            client.write(buffer, buffer, this);
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        exc.printStackTrace();
    }
}
