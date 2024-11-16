package server.network.router;

import server.network.handler.RouterHandler;
import server.network.protocol.RequestProtocol;
import server.network.protocol.ResponseProtocol;

import java.util.function.BiConsumer;

public interface Router {
    static void post(String url, BiConsumer<RequestProtocol, ResponseProtocol> controller) {
        RouterHandler.addRouter(RequestProtocol.Method.POST, url, controller);
    }

    static void get(String url, BiConsumer<RequestProtocol, ResponseProtocol> controller) {
        RouterHandler.addRouter(RequestProtocol.Method.GET, url, controller);
    }

    static void update(String url, BiConsumer<RequestProtocol, ResponseProtocol> controller) {
        RouterHandler.addRouter(RequestProtocol.Method.UPDATE, url, controller);
    }

    static void delete(String url, BiConsumer<RequestProtocol, ResponseProtocol> controller) {
        RouterHandler.addRouter(RequestProtocol.Method.DELETE, url, controller);
    }
}
