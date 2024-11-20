package server;

import server.config.ConfigLoader;
import server.network.handler.SocketHandler;
import server.network.router.Router;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadServer {
    private final ServerSocket serverSocket;
    private final ExecutorService threadPool;
    private boolean isRunning;
    private final AtomicInteger connections;
    private final int maxConnections;

    public ThreadServer() throws IOException {
        System.out.println("Initializing thread server...");

        System.out.println("Creating ServerSocket...");
        serverSocket = new ServerSocket(ConfigLoader.getInt("PORT"));
        System.out.println("ServerSocket created");

        System.out.println("Initializing thread pool...");
        int corePoolSize = ConfigLoader.getInt("CORE_POOL_SIZE");
        int maxPoolSize = ConfigLoader.getInt("MAX_POOL_SIZE");
        int keepAliveTime = ConfigLoader.getInt("KEEP_ALIVE_TIME");
        int queueCapacity = ConfigLoader.getInt("QUEUE_CAPACITY");
        threadPool = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueCapacity),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        System.out.println("Initializing thread pool done");

        System.out.println("Initializing parameters....");
        connections = new AtomicInteger(0);
        maxConnections = ConfigLoader.getInt("MAX_POOL_SIZE");
        isRunning = true;
        System.out.println("Initializing parameters done");

        System.out.println("Initializing routers....");
        Router.init();
        System.out.println("Initializing routers done");

        System.out.println("Initializing thread server done");
    }

    public void run() {
        try {
            while (isRunning) {
                Socket socket = serverSocket.accept();

                if (connections.get() >= maxConnections) {
                    try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                        out.println("Server reaches max connections.");
                        socket.close();
                        continue;
                    }
                }

                connections.incrementAndGet();
                System.out.println("New client connected. [Connections : " + connections.get() + "]");

                threadPool.execute(new SocketHandler(socket, connections));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

    public void shutdown() {
        isRunning = false;
        threadPool.shutdown();

        if (!serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            ThreadServer threadServer = new ThreadServer();
            threadServer.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
