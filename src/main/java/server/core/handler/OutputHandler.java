package server.core.handler;

import server.core.persistence.WorkItem;
import server.util.RemoteAddressResolver;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.function.Consumer;

public class OutputHandler extends shared.protocol.handler.OutputHandler {
    private final WorkItem workItem;

    public OutputHandler(WorkItem workItem, Consumer<AsynchronousSocketChannel> closeClientConsumer) {
        super(workItem.getClient(), closeClientConsumer);
        this.workItem = workItem;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        super.completed(result, buffer);
        endResponse();
    }

    private void endResponse() {
        System.out.printf("Send (%s) %s -> %s [%dms]\n", RemoteAddressResolver.getRemoteAddress(workItem.getClient()), workItem.getResponseProtocol().toString(), workItem.getResponseProtocol().toString(), System.currentTimeMillis() - workItem.getTimestamp());
    }}
