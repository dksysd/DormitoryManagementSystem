package TestServer;

import server.network.protocol.RequestProtocol;
import server.network.protocol.ResponseProtocol;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class Router {
    private final Map<RouterKey, BiConsumer<RequestProtocol, ResponseProtocol>> routers = new HashMap<>();

    public Router() {
        init();
    }

    abstract void init();

    public void route(RequestProtocol request, ResponseProtocol response) {
        RouterKey routerKey = new RouterKey(request.getMethod(), request.getUrl());
        BiConsumer<RequestProtocol, ResponseProtocol> consumer = routers.get(routerKey);
        if (consumer == null) {
            throw new IllegalArgumentException("Router " + routerKey + " not found");
        }
        consumer.accept(request, response);
    }

    protected void addRouter(RequestProtocol.Method method, String url, BiConsumer<RequestProtocol, ResponseProtocol> consumer) {
        RouterKey key = new RouterKey(method, url);
        if (routers.containsKey(key)) {
            throw new IllegalArgumentException(url + " is already added");
        }
        routers.put(key, consumer);
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
