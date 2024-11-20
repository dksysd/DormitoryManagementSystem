package server.network.serialize;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JsonSerializer {
    private static final ThreadLocal<Map<Object, Integer>> objectMap = new ThreadLocal<>();
    private static final ThreadLocal<Map<Integer, Object>> reverseMap = new ThreadLocal<>();
    private static final ThreadLocal<Integer> counter = new ThreadLocal<>();

    public static String serialize(Object object) throws Exception {
        try {
            objectMap.set(new HashMap<>());
            counter.set(0);
            return objectToJson(object);
        } finally {
            objectMap.remove();
            counter.remove();
        }
    }

    private static String objectToJson(Object object) throws Exception {
        if (object == null) {
            return "null";
        }

        if (object instanceof String) {
            return "\"" + escapeString((String) object) + "\"";
        } else if (object instanceof Number || object instanceof Boolean) {
            return object.toString();
        } else if (object instanceof Collection) {
            return collectionToJson((Collection<?>) object);
        } else if (object instanceof Map) {
            return mapToJson((Map<?, ?>) object);
        }

        if (objectMap.get().containsKey(object)) {
            return "{\"$ref\":" + objectMap.get().get(object) + "}";
        }

        StringBuilder json = new StringBuilder("{");
        Integer counterValue = counter.get();
        json.append("\"$id\":").append(counterValue);
        objectMap.get().put(object, counterValue++);
        counter.set(counterValue);

        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(object);
            json.append(",");
            json.append("\"").append(field.getName()).append("\":").append(objectToJson(value));
        }

        return json.append("}").toString();
    }

    private static String collectionToJson(Collection<?> collection) throws Exception {
        StringBuilder json = new StringBuilder("[");
        boolean first = true;

        for (Object item : collection) {
            if (!first) {
                json.append(",");
            }
            first = false;
            json.append(objectToJson(item));
        }

        return json.append("]").toString();
    }

    private static String mapToJson(Map<?, ?> map) throws Exception {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) {
                json.append(",");
            }
            first = false;

            json.append(objectToJson(entry.getKey()))
                    .append(":")
                    .append(objectToJson(entry.getValue()));
        }

        return json.append("}").toString();
    }

    private static String escapeString(String str) {
        return str.replace("\"", "\\\"")
                .replace("\\", "\\\\")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
