package server.core;

import lombok.Getter;
import server.config.Config;
import server.config.RequestHandlerInitializer;
import server.core.handler.AcceptHandler;
import server.core.handler.RequestHandler;
import server.util.RemoteAddressResolver;
import shared.protocol.handler.OutputHandler;
import shared.protocol.persistence.*;
import shared.protocol.util.ProtocolSerializer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.*;

public class Server {
    @Getter
    public static final Server INSTANCE = new Server();

    private final int port;
    private final int workerThreads;
    private final ExecutorService workerExecutor;
    private final BlockingQueue<WorkItem> workQueue;
    private final RequestHandler requestHandler;

    private Server() {
        port = Config.getInt("PORT");
        workerThreads = Config.getInt("WORKER_THREADS");

        this.workerExecutor = Executors.newFixedThreadPool(workerThreads);
        this.workQueue = new LinkedBlockingQueue<>(workerThreads);

        this.requestHandler = RequestHandler.getINSTANCE();
    }

    public void start() {
        startWorker();

        RequestHandlerInitializer.init(this.requestHandler);

        try (AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port))) {
            server.accept(server, new AcceptHandler(this::closeClient, this::addWorkItem));
            Thread.currentThread().join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startWorker() {
        for (int i = 0; i < workerThreads; i++) {
            workerExecutor.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        WorkItem workItem = workQueue.take();
                        work(workItem);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }
    }

    private void work(WorkItem workItem) {
        // todo session 기능 만들기
        System.out.println("Working " + workItem.toString());
        Protocol<?> protocol = requestHandler.request(workItem.getRequestProtocol());
        workItem.setResponseProtocol(protocol);
        ByteBuffer buffer = ProtocolSerializer.serialize(protocol);
        buffer.flip();
        workItem.getClient().write(buffer, buffer, new OutputHandler(workItem, this::closeClient, this::endResponse));
    }

    private void endResponse(WorkItem workItem) {
        System.out.printf("(%s) %s -> %s [%dms]\n", RemoteAddressResolver.getRemoteAddress(workItem.getClient()), workItem.getResponseProtocol().toString(), workItem.getResponseProtocol().toString(), System.currentTimeMillis() - workItem.getTimestamp());
    }

    private void closeClient(AsynchronousSocketChannel client) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addWorkItem(AsynchronousSocketChannel client, Protocol<?> protocol) {
        System.out.println("Data received from " + RemoteAddressResolver.getRemoteAddress(client) + " : " + protocol);
        WorkItem workItem = new WorkItem(client, protocol, System.currentTimeMillis());
        workQueue.add(workItem);
    }

    public static void main(String[] args) {
        Server.getINSTANCE().start();
    }
}
