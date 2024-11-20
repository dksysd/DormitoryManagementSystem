package server.network.router;

import server.controller.AuthController;
import server.network.handler.RouterHandler;
import server.network.protocol.RequestProtocol;
import server.network.protocol.ResponseProtocol;

import java.util.function.BiConsumer;

public interface Router {
    static void init() {
        post("/login", AuthController::login);
    }

    static void get(String url, BiConsumer<RequestProtocol, ResponseProtocol> controller) {
        RouterHandler.addRouter(RequestProtocol.Method.GET, url, controller);
    }

    static void post(String url, BiConsumer<RequestProtocol, ResponseProtocol> controller) {
        RouterHandler.addRouter(RequestProtocol.Method.POST, url, controller);
    }

    static void put(String url, BiConsumer<RequestProtocol, ResponseProtocol> controller) {
        RouterHandler.addRouter(RequestProtocol.Method.PUT, url, controller);
    }

    static void delete(String url, BiConsumer<RequestProtocol, ResponseProtocol> controller) {
        RouterHandler.addRouter(RequestProtocol.Method.DELETE, url, controller);
    }

    static void patch(String url, BiConsumer<RequestProtocol, ResponseProtocol> controller) {
        RouterHandler.addRouter(RequestProtocol.Method.PATCH, url, controller);
    }
}
