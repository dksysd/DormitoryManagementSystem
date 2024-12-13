package server.util;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * RemoteAddressResolver 인터페이스는 클라이언트의 원격 주소를 반환하는 유틸리티 메서드를 제공합니다.
 * <p>
 * 이 인터페이스의 메서드는 {@link AsynchronousSocketChannel} 객체를 사용하여 원격 주소를 확인합니다.
 */
public interface RemoteAddressResolver {

    /**
     * 주어진 {@link AsynchronousSocketChannel}로부터 클라이언트의 원격 주소를 반환합니다.
     * <p>
     * 원격 주소를 가져오는 데 실패하거나 예외가 발생하면 "UNKNOWN" 문자열을 반환합니다.
     *
     * @param client 원격 주소를 확인할 대상인 {@link AsynchronousSocketChannel} 객체
     * @return 클라이언트의 원격 주소를 나타내는 문자열, 실패 시 "UNKNOWN" 반환
     */
    static String getRemoteAddress(AsynchronousSocketChannel client) {
        try {
            return client.getRemoteAddress().toString();
        } catch (IOException e) {
            return "UNKNOWN";
        }
    }
}