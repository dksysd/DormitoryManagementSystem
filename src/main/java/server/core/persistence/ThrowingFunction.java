package server.core.persistence;

/**
 * ThrowingFunction 인터페이스는 입력값을 받아 출력값으로 변환하는 함수형 인터페이스입니다.
 * 이 인터페이스는 함수 처리 중 예외를 던질 수 있도록 지원하며,
 * Java 표준 {@link java.util.function.Function}의 확장된 버전입니다.
 *
 * @param <T> 입력 타입
 * @param <R> 출력 타입
 */
@FunctionalInterface
public interface ThrowingFunction<T, R> {

    /**
     * 입력값을 받아 처리하고 결과를 반환합니다.
     * 처리 도중 예외가 발생할 수 있습니다.
     *
     * @param t 입력값
     * @return 처리 결과
     * @throws Exception 처리 중 발생할 수 있는 예외
     */
    R apply(T t) throws Exception;
}