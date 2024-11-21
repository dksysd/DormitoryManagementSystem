package server.network.serialize;

import java.nio.charset.StandardCharsets;

public interface JsonSerializable {
    static byte[] serialize(JsonSerializable object) throws Exception {
        return JsonSerializer.serialize(object).getBytes(StandardCharsets.UTF_8);
    }

    static <T extends JsonSerializable> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        return deserialize(new String(bytes, StandardCharsets.UTF_8), clazz);
    }

    static <T extends JsonSerializable> T deserialize(String json, Class<T> clazz) throws Exception {
        return JsonDeserializer.deserialize(json, clazz);
    }
}
