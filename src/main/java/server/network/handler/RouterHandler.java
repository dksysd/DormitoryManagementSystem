package server.network.handler;

import server.network.protocol.RequestProtocol;
import server.network.protocol.ResponseProtocol;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class RouterHandler implements Handler {
    private static final Map<RouterKey, BiConsumer<RequestProtocol, ResponseProtocol>> routers = new HashMap<>();

    private final RequestProtocol requestProtocol;
    private final ResponseProtocol responseProtocol;

    public RouterHandler(RequestProtocol requestProtocol, ResponseProtocol responseProtocol) {
        this.requestProtocol = requestProtocol;
        this.responseProtocol = responseProtocol;
    }

    @Override
    public void handle() {
        RouterKey key = new RouterKey(requestProtocol.getMethod(), requestProtocol.getUrl());
        BiConsumer<RequestProtocol, ResponseProtocol> consumer = routers.get(key);
        if (consumer == null) {
            throw new IllegalArgumentException("Router " + key + " not found");
        }
        consumer.accept(requestProtocol, responseProtocol);
    }

    public static void addRouter(RequestProtocol.Method method, String url, BiConsumer<RequestProtocol, ResponseProtocol> controller) {
        RouterKey key = new RouterKey(method, url);
        if(routers.containsKey(key)) {
            throw new IllegalArgumentException(url + " is already added");
        }
        routers.put(key, controller);
    }

    private record RouterKey(RequestProtocol.Method method, String url) {
        @Override
        public String toString() {
            return "RouterKey{" +
                    "method=" + method +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
