package server.core.persistence;

import lombok.Getter;
import lombok.Setter;
import server.util.RemoteAddressResolver;
import shared.protocol.persistence.Protocol;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.Objects;

/**
 * WorkItem 클래스는 클라이언트 요청을 처리하는 작업 단위를 나타냅니다.
 * 클라이언트 객체, 요청 프로토콜, 응답 프로토콜, 그리고 타임스탬프 정보를 포함하며,
 * 요청 및 응답 처리와 관련된 메타데이터를 제공합니다.
 */
@Getter
@Setter
public class WorkItem {

    /**
     * 클라이언트와의 비동기 채널
     */
    private final AsynchronousSocketChannel client;

    /**
     * 클라이언트에서 수신한 요청 프로토콜
     */
    private final Protocol<?> requestProtocol;

    /**
     * 클라이언트에게 반환할 응답 프로토콜
     */
    private Protocol<?> responseProtocol;

    /**
     * 작업이 생성된 시점의 타임스탬프 (밀리초 단위)
     */
    private final long timestamp;

    /**
     * WorkItem 생성자.
     * 클라이언트 채널, 요청 프로토콜, 생성 시점을 초기화합니다.
     *
     * @param client          클라이언트와 연결된 {@link AsynchronousSocketChannel}
     * @param requestProtocol 클라이언트 요청 {@link Protocol}
     * @param timestamp       현재 시각의 타임스탬프 (밀리초 단위)
     */
    public WorkItem(AsynchronousSocketChannel client, Protocol<?> requestProtocol, long timestamp) {
        this.client = client;
        this.requestProtocol = requestProtocol;
        this.timestamp = timestamp;
    }

    /**
     * WorkItem의 문자열 표현을 반환합니다.
     * 클라이언트 주소, 요청/응답 프로토콜, 타임스탬프 정보를 포함합니다.
     *
     * @return WorkItem의 문자열 표현
     */
    @Override
    public String toString() {
        return "WorkItem{" +
                "client=" + RemoteAddressResolver.getRemoteAddress(client) +
                ", requestProtocol=" + requestProtocol +
                ", responseProtocol=" + responseProtocol +
                ", timestamp=" + timestamp +
                '}';
    }

    /**
     * 두 WorkItem 객체를 비교하여 동일 여부를 결정합니다.
     * 클라이언트, 요청 프로토콜, 응답 프로토콜, 타임스탬프가 모두 동일할 경우 true를 반환합니다.
     *
     * @param obj 비교할 객체
     * @return 객체가 동일하면 true, 그렇지 않으면 false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (WorkItem) obj;
        return Objects.equals(this.client, that.client) &&
                Objects.equals(this.requestProtocol, that.requestProtocol) &&
                Objects.equals(this.responseProtocol, that.responseProtocol) &&
                this.timestamp == that.timestamp;
    }

    /**
     * WorkItem의 해시코드를 생성합니다.
     * 클라이언트, 요청/응답 프로토콜, 타임스탬프를 기반으로 해시값을 계산합니다.
     *
     * @return WorkItem 객체의 해시코드
     */
    @Override
    public int hashCode() {
        return Objects.hash(client, requestProtocol, responseProtocol, timestamp);
    }
}