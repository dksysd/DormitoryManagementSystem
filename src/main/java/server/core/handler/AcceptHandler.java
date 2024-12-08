package server.core.handler;

import lombok.AllArgsConstructor;
import server.util.RemoteAddressResolver;
import shared.protocol.handler.InputHeaderHandler;
import shared.protocol.persistence.Header;
import shared.protocol.persistence.Protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@AllArgsConstructor
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {
    private final Consumer<AsynchronousSocketChannel> closeClientConsumer;
    private final BiConsumer<AsynchronousSocketChannel, Protocol<?>> completeConsumer;

    @Override
    public void completed(AsynchronousSocketChannel client, AsynchronousServerSocketChannel server) {
        server.accept(server, this);

        System.out.println("Server accepted from " + RemoteAddressResolver.getRemoteAddress(client));

        ByteBuffer headerBuffer = ByteBuffer.allocate(Header.BYTES);
        client.read(headerBuffer, headerBuffer, new InputHeaderHandler(client, closeClientConsumer, completeConsumer));
    }

    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel server) {
        exc.printStackTrace();
    }
}
