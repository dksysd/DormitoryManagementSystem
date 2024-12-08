package shared.protocol.handler;

import lombok.AllArgsConstructor;
import server.core.WorkItem;
import server.util.RemoteAddressResolver;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.function.Consumer;

@AllArgsConstructor
public class OutputHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final WorkItem workItem;
    private final Consumer<AsynchronousSocketChannel> closeClientConsumer;
    private final Consumer<WorkItem> completeConsumer;

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        if(result == -1) {
            closeClientConsumer.accept(workItem.getClient());
        }

        if (buffer.hasRemaining()) {
            System.out.println("Send data to " + RemoteAddressResolver.getRemoteAddress(workItem.getClient()));
            workItem.getClient().write(buffer, buffer, this);
        } else {
            completeConsumer.accept(workItem);
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        exc.printStackTrace();
    }
}
