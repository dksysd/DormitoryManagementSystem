package client.core.util;

import client.core.handler.InputHeaderHandler;
import client.core.handler.OutputHandler;
import shared.protocol.persistence.Header;
import shared.protocol.persistence.Protocol;
import shared.protocol.util.ProtocolSerializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AsyncRequest {
    private final AsynchronousSocketChannel client;

    public AsyncRequest(String host, int port) throws ExecutionException, InterruptedException {
        client = AsyncClientConnector.connect(host, port).get();
    }

    private CompletableFuture<AsynchronousSocketChannel> sendRequest(Protocol<?> requestProtocol) {
        ByteBuffer requestBuffer = ProtocolSerializer.serialize(requestProtocol);
        requestBuffer.flip();

        CompletableFuture<AsynchronousSocketChannel> sendFuture = new CompletableFuture<>();
        client.write(requestBuffer,requestBuffer, new OutputHandler(client,this::closeClient, sendFuture));

        return sendFuture;
    }

    private CompletableFuture<Protocol<?>> receiveResponse() {
        CompletableFuture<Protocol<?>> responseFuture = new CompletableFuture<>();
        ByteBuffer headerBuffer = ByteBuffer.allocate(Header.BYTES);
        client.read(headerBuffer, headerBuffer, new InputHeaderHandler(client,this::closeClient, responseFuture));
        return responseFuture;
    }

    public Protocol<?> sendAndReceive(Protocol<?> requestProtocol) throws ExecutionException, InterruptedException {
        return sendRequest(requestProtocol).thenCompose(client -> receiveResponse()).get();
    }

    private void closeClient(AsynchronousSocketChannel client) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
