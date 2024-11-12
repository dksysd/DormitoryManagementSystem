package server.network;

import java.nio.charset.StandardCharsets;

public interface Serializable {
    static byte[] serialize(Serializable object) throws Exception {
        return JsonSerializer.serialize(object).getBytes(StandardCharsets.UTF_8);
    }

    static <T extends Serializable> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        return JsonDeserializer.deserialize(new String(bytes, StandardCharsets.UTF_8), clazz);
    }
}
