package server.network.service;

import java.nio.charset.StandardCharsets;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class RoutingService {

    // URL에서 출발지와 목적지를 추출, JSON 형태의 응답을 반환
    public String getRouteFromUrl(String url) {
        StringBuilder responseJson = new StringBuilder();

        try {
            // URL에서 출발지와 목적지를 추출
            String[] params = parseUrl(url);
            if (params != null) {
                String origin = params[0];
                String destination = params[1];
                // 경로 계산 (예제용으로 고정된 메시지 반환)
                String routeData = calculateRoute(origin, destination);
                // JSON 형식으로 응답 생성
                Map<String, String> response = new HashMap<>();
                response.put("status", "OK");
                response.put("route", routeData);
                responseJson.append(mapToJson(response));
            } else {
                // 파싱 실패 시 오류 메시지
                Map<String, String> response = new HashMap<>();
                response.put("status", "ERROR");
                response.put("message", "Invalid URL format");

                responseJson.append(mapToJson(response));
            }
        } catch (Exception e) {
            // 예외 발생 시 오류 메시지
            Map<String, String> response = new HashMap<>();
            response.put("status", "ERROR");
            response.put("message", "An error occurred while processing the URL");

            responseJson.append(mapToJson(response));
            e.printStackTrace();
        }

        return responseJson.toString();
    }

    // URL에서 출발지와 목적지를 추출
    private String[] parseUrl(String url) {
        try {
            String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8);
            String[] parts = decodedUrl.split("\\?");
            if (parts.length > 1) {
                String[] queryParams = parts[1].split("&");
                String origin = null;
                String destination = null;

                for (String param : queryParams) {
                    String[] keyValue = param.split("=");
                    if (keyValue[0].equals("origin")) {
                        origin = keyValue[1];
                    } else if (keyValue[0].equals("destination")) {
                        destination = keyValue[1];
                    }
                }

                if (origin != null && destination != null) {
                    return new String[]{origin, destination};
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;  // 파싱 실패 시 null 반환
    }


    private String calculateRoute(String origin, String destination) {
        return "Route from " + origin + " to " + destination;
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
