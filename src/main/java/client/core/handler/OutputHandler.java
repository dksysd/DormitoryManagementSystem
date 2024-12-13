package client.core.handler;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class OutputHandler extends shared.protocol.handler.OutputHandler {
    private final CompletableFuture<AsynchronousSocketChannel> sendFuture;

    public OutputHandler(AsynchronousSocketChannel client, Consumer<AsynchronousSocketChannel> closeClientConsumer, CompletableFuture<AsynchronousSocketChannel> sendFuture) {
        super(client, closeClientConsumer);
        this.sendFuture = sendFuture;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        super.completed(result, buffer);

        if(result != -1) {
            System.out.println("Send data ");
            sendFuture.complete(client);
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        super.failed(exc, buffer);
        sendFuture.completeExceptionally(exc);
    }
}
