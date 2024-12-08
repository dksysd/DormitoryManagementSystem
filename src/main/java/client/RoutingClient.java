package client;

import java.io.*;
import java.net.Socket;
import org.json.JSONObject;

public class RoutingClient {

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";  // 서버 주소
        int port = 12345;  // 서버 포트 번호

        try (Socket socket = new Socket(serverAddress, port)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 요청 데이터 생성
            JSONObject requestJson = new JSONObject();
            requestJson.put("command", "GET_ROUTE_FROM_URL");
            requestJson.put("url", "https://example.com/route?origin=Seoul&destination=Busan");

            // 서버로 요청 전송
            out.println(requestJson);

            // 서버 응답 수신
            String response = in.readLine();
            JSONObject responseJson = new JSONObject(response);

            if ("OK".equals(responseJson.getString("status"))) {
                System.out.println("Received route: " + responseJson.getString("route"));
            } else {
                System.out.println("Error: " + responseJson.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
