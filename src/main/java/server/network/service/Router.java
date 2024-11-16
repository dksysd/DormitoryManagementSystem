package server.network.service;

import server.network.protocol.Body;
import server.network.protocol.Header;
import server.network.protocol.RequestProtocol;
import java.util.HashMap;
import java.util.Map;

public class Router {


        RoutingService routingService = new RoutingService();

//        public static void main(String[] args) {
//            Router router = new Router();
//            RequestProtocol requestProtocol = new RequestProtocol(RequestProtocol.Method.GET, "/exampleUrl", new Header(), new Body());
//            router.startServer("GET", requestProtocol);
//        }

        public void startServer(String commandRequest, RequestProtocol requestProtocol) {
            try {
                String command = requestProtocol.getMethod().toString();

                if (command.equals(commandRequest)) {
                    String url = requestProtocol.getUrl();
                    // RoutingService를 통해 URL 파싱 및 경로 계산
                    String responseJson = routingService.getRouteFromUrl(url);
                    System.out.println(responseJson);
                } else {
                    // 명령어가 올바르지 않으면 오류 메시지 반환
                    Map<String, String> response = new HashMap<>();
                    response.put("status", "ERROR");
                    response.put("message", "Invalid command");

                    System.out.println(mapToJson(response));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Map을 JSON 형식의 문자열로 변환
        private String mapToJson(Map<String, String> map) {
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{");

            boolean first = true;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (!first) {
                    jsonBuilder.append(", ");
                }
                jsonBuilder.append("\"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\"");
                first = false;
            }

            jsonBuilder.append("}");
            return jsonBuilder.toString();
        }
    }
