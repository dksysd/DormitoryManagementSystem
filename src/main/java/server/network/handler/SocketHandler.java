package server.network.handler;

import server.network.protocol.Header;
import server.network.protocol.ProtocolSerializable;
import server.network.protocol.RequestProtocol;
import server.network.protocol.ResponseProtocol;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketHandler implements Runnable, Handler {
    private final BufferedInputStream in;
    private final BufferedOutputStream out;
    private boolean isRunning = true;
    private final AtomicInteger connections;

    public SocketHandler(Socket socket, AtomicInteger connections) throws IOException {
        in = new BufferedInputStream(socket.getInputStream());
        out = new BufferedOutputStream(socket.getOutputStream());
        this.connections = connections;
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                handle();
            }
        } catch (SocketException e) {
            System.out.println(e.getMessage());
            isRunning = false;
            connections.decrementAndGet();
            System.out.println("Client disconnected. [Connections : " + connections.get() + "]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void handle() throws Exception {
        ResponseProtocol responseProtocol = new ResponseProtocol();

        try {
            byte[] requestData = readData();
            RequestProtocol requestProtocol = ProtocolSerializable.deserialize(requestData, RequestProtocol.class);
            RequestProtocolHandler requestProtocolHandler = new RequestProtocolHandler(requestData, requestProtocol);
            requestProtocolHandler.handle();

            RouterHandler routerHandler = new RouterHandler(requestProtocol, responseProtocol);
            routerHandler.handle();
        } catch (Exception e) {
            responseProtocol = new ResponseProtocol();
            responseProtocol.setStatus(ResponseProtocol.Status.InternalServerError);
            responseProtocol.getHeader().setContentType(Header.ContentType.APPLICATION_JSON);
            responseProtocol.getBody().addData("error", e.getMessage());
        }

        byte[] responseData = ProtocolSerializable.serialize(responseProtocol);
        out.write(responseData);
        out.flush();
    }

    private byte[] readData() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        byte[] dataBuffer = new byte[1024];
        int bytesRead;

        do {
            bytesRead = in.read(dataBuffer);
            buffer.write(dataBuffer, 0, bytesRead);
            buffer.flush();
        } while (bytesRead == 1024);

        return buffer.toByteArray();
    }
}
