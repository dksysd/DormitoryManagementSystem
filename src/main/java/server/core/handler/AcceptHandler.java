package server.core.handler;

import lombok.AllArgsConstructor;
import server.core.persistence.WorkItem;
import server.util.RemoteAddressResolver;
import shared.protocol.persistence.Header;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@AllArgsConstructor
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {
    private final Consumer<AsynchronousSocketChannel> closeClientConsumer;
    private final BlockingQueue<WorkItem> workQueue;

    @Override
    public void completed(AsynchronousSocketChannel client, AsynchronousServerSocketChannel server) {
        server.accept(server, this);

        System.out.println("Server accepted from " + RemoteAddressResolver.getRemoteAddress(client));

        ByteBuffer headerBuffer = ByteBuffer.allocate(Header.BYTES);
        client.read(headerBuffer, 10, TimeUnit.SECONDS, headerBuffer, new InputHeaderHandler(client, closeClientConsumer, workQueue));
    }

    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel server) {
        exc.printStackTrace();
    }
}
