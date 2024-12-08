package server.util;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;

public interface RemoteAddressResolver {
    static String getRemoteAddress(AsynchronousSocketChannel client) {
        try {
            return client.getRemoteAddress().toString();
        } catch (IOException e) {
            return "UNKNOWN";
        }
    }
}
