package server.config;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Config 클래스는 환경 변수 또는 외부 구성 값을 읽어오도록 설계된 유틸리티 클래스입니다.
 * {@link Dotenv} 라이브러리를 사용하여 환경 설정 파일에서 키-값 쌍을 로드합니다.
 * 문자열 및 정수 데이터 형태의 값을 읽는 메서드를 제공합니다.
 */
public class Config {

    /**
     * {@link Dotenv} 인스턴스로, 환경 변수를 로드합니다.
     */
    private static final Dotenv DOTENV = Dotenv.load();

    /**
     * 지정된 키에 해당하는 환경 변수 값을 문자열로 반환합니다.
     *
     * @param key 환경 변수의 키
     * @return 키에 해당하는 환경 변수 값 (문자열)
     * @throws NullPointerException 키가 존재하지 않을 경우 발생
     */
    public static String get(String key) {
        return DOTENV.get(key);
    }

    /**
     * 지정된 키에 해당하는 환경 변수 값을 정수로 변환하여 반환합니다.
     * 값에 포함된 밑줄("_")을 제거한 후 정수로 변환합니다.
     *
     * @param key 환경 변수의 키
     * @return 키에 해당하는 환경 변수 값 (정수)
     * @throws NullPointerException  키가 존재하지 않을 경우 발생
     * @throws NumberFormatException 값이 숫자로 변환될 수 없을 경우 발생
     */
    public static Integer getInt(String key) {
        return Integer.parseInt(get(key).replaceAll("_", ""));
    }
}