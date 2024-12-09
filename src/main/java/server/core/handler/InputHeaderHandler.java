package server.core.handler;

import server.core.persistence.WorkItem;
import shared.protocol.persistence.Header;
import shared.protocol.util.ProtocolParser;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class InputHeaderHandler extends shared.protocol.handler.InputHeaderHandler {
    private final BlockingQueue<WorkItem> workQueue;

    public InputHeaderHandler(AsynchronousSocketChannel client, Consumer<AsynchronousSocketChannel> closeClientConsumer, BlockingQueue<WorkItem> workQueue) {
        super(client, closeClientConsumer);
        this.workQueue = workQueue;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        super.completed(result, buffer);

        if (result != -1) {
            buffer.flip();
            Header header = ProtocolParser.parseHeader(buffer);
            ByteBuffer dataBuffer = ByteBuffer.allocate(header.getDataLength());
            client.read(dataBuffer, dataBuffer, new InputDataHandler(header, buffer, this, workQueue));
        }
    }
}
