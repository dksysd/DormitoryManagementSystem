package server.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private static final Dotenv DOTENV = Dotenv.load();

    public static String get(String key) {
        return DOTENV.get(key);
    }

    public static Integer getInt(String key) {
        return Integer.parseInt(get(key).replaceAll("_", ""));
    }
}
