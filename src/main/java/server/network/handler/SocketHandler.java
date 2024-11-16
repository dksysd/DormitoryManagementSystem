package server.network.handler;

import server.network.protocol.ProtocolSerializable;
import server.network.protocol.RequestProtocol;
import server.network.protocol.ResponseProtocol;

import java.io.*;
import java.net.Socket;

public class SocketHandler implements Runnable {
    private final BufferedInputStream in;
    private final BufferedOutputStream out;

    public SocketHandler(Socket socket) throws IOException {
        in = new BufferedInputStream(socket.getInputStream());
        out = new BufferedOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            byte[] requestData = readData();
            RequestProtocol requestProtocol = ProtocolSerializable.deserialize(requestData, RequestProtocol.class);
            RequestProtocolHandler requestProtocolHandler = new RequestProtocolHandler(requestData, requestProtocol);
            requestProtocolHandler.handle();

            ResponseProtocol responseProtocol = new ResponseProtocol();
            RouterHandler routerHandler = new RouterHandler(requestProtocol, responseProtocol);
            routerHandler.handle();

            byte[] responseData = ProtocolSerializable.serialize(responseProtocol);
            out.write(responseData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] readData() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        byte[] dataBuffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = in.read(dataBuffer)) != -1) {
            buffer.write(dataBuffer, 0, bytesRead);
        }

        return buffer.toByteArray();
    }
}
