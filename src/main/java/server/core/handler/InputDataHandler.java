package server.core.handler;

import server.core.persistence.WorkItem;
import server.util.RemoteAddressResolver;
import shared.protocol.handler.InputHeaderHandler;
import shared.protocol.persistence.Header;
import shared.protocol.persistence.Protocol;
import shared.protocol.util.ProtocolParser;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;

public class InputDataHandler extends shared.protocol.handler.InputDataHandler {
    private final BlockingQueue<WorkItem> workQueue;

    public InputDataHandler(Header header, ByteBuffer headerBuffer, InputHeaderHandler inputHeaderHandler, BlockingQueue<WorkItem> workQueue) {
        super(header, headerBuffer, inputHeaderHandler);
        this.workQueue = workQueue;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        super.completed(result, buffer);

        if (result != -1) {
            buffer.flip();
            Protocol<?> protocol = ProtocolParser.parseProtocol(header, buffer);
            WorkItem workItem = new WorkItem(inputHeaderHandler.getClient(), protocol, System.currentTimeMillis());
            workQueue.add(workItem);
            System.out.println("Data received from " + RemoteAddressResolver.getRemoteAddress(inputHeaderHandler.getClient()) + " : " + protocol);

            headerBuffer.clear();
            inputHeaderHandler.getClient().read(headerBuffer, headerBuffer, inputHeaderHandler);
        }
    }
}
