package TestServer;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;

public class Config {
    private final Dotenv dotenv;

    @Getter
    private static final Config instance = new Config();

    private Config() {
        dotenv = Dotenv.load();
    }

    public String get(String key) {
        return dotenv.get(key);
    }
}
