package server.network.serialize;

import java.lang.reflect.*;
import java.util.*;

public class JsonDeserializer {
    private static final ThreadLocal<Map<Integer, Object>> objectMap = new ThreadLocal<>();

    public static <T> T deserialize(String json, Class<T> clazz) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        try {
            objectMap.set(new HashMap<>());
            return parseJson(json, clazz);
        } finally {
            objectMap.remove();
        }
    }

    private static <T> T parseJson(String json, Class<T> clazz) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        JsonParser parser = new JsonParser(json.trim());
        Object result = parseValue(parser, clazz);
        return clazz.cast(result);
    }

    private static Object parseValue(JsonParser parser, Type type) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        parser.skipWhitespace();
        char current = parser.peek();

        switch (current) {
            case '{':
                return isMapType(type) ? parseMap(parser, type) : parseObject(parser, type);
            case '[':
                return parseArray(parser, type);
            case 't':
            case 'f':
                return parser.parseBoolean();
            case 'n':
                parser.parseNull();
                return null;
            case '"':
                return parser.parseString();
            default:
                if (current == '-' || Character.isDigit(current)) {
                    return parser.parseNumber();
                }
                throw new IllegalArgumentException("Unrecognized token: " + current);
        }
    }

    private static boolean isMapType(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            Type rawType = parameterizedType.getRawType(); // 원시 타입 추출
            return rawType instanceof Class<?> && Map.class.isAssignableFrom((Class<?>) rawType);
        }
        return type instanceof Class<?> && Map.class.isAssignableFrom((Class<?>) type);
    }

    private static Map<?, ?> parseMap(JsonParser parser, Type type) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        parser.expect('{');

        Map map = null;
        if (type instanceof ParameterizedType parameterizedType) {
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class<?> clazz) {
                if (clazz == Map.class) {
                    map = new HashMap();
                } else {
                    map = (Map) clazz.getDeclaredConstructor().newInstance();
                }
            }
        }

        if (map == null) {
            throw new IllegalArgumentException("Unrecognized type: " + type);
        }

        while (true) {
            if (parser.peek() == '}') {
                parser.next();
                break;
            }

//            String keyString = parser.parseString();
            Object key = parseValue(parser, type);

            parser.expect(':');
//            String valueString = parser.parseString();
            Object value = parseValue(parser, type);

            map.put(key, value);

            parser.skipWhitespace();
            if (parser.peek() == ',') {
                parser.next();
            }
        }

        return map;
    }

    private static Object parseObject(JsonParser parser, Type type) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        parser.expect('{');

        if (!(type instanceof Class<?> clazz)) {
            throw new IllegalArgumentException("Unrecognized object type: " + type);
        }

        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        Object instance = constructor.newInstance();

        parser.skipWhitespace();
        String fieldName = parser.parseString();
        parser.expect(':');

        Integer id;
        if (fieldName.equals("$ref")) {
            Map<Integer, Object> objectHashMap = objectMap.get();
            instance = objectHashMap.get((Integer) parseValue(parser, Integer.TYPE));
            parser.skipWhitespace();
            if (parser.peek() == '}') {
                parser.next();
            }
            return instance;
        } else if (fieldName.equals("$id")) {
            id = (Integer) parseValue(parser, Integer.TYPE);
        } else {
            throw new IllegalArgumentException("Object must has rer or id field");
        }

        parser.skipWhitespace();
        if (parser.peek() == ',') {
            parser.next();
        }

        objectMap.get().put(id, instance);

        Map<String, Field> fields = new HashMap<>();
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            for (Field field : currentClass.getDeclaredFields()) {
                field.setAccessible(true);
                fields.put(field.getName(), field);
            }
            currentClass = currentClass.getSuperclass();
        }

        while (true) {
            fieldName = parser.parseString();
            parser.expect(':');

            Field field = fields.get(fieldName);
            if (field == null) {
                // todo parseValue의 값을 사용하지 않음
                parseValue(parser, Object.class);
            } else {
                Object value = parseValue(parser, field.getGenericType());
                field.set(instance, convertValue(value, field.getType()));
            }

            parser.skipWhitespace();
            if (parser.peek() == ',') {
                parser.next();
            }

            parser.skipWhitespace();
            if (parser.peek() == '}') {
                parser.next();
                break;
            }
        }

        return instance;
    }

    private static Object parseArray(JsonParser parser, Type type) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        parser.expect('[');
        List<Object> list = new ArrayList<>();

        Type componentType = Object.class;
        if (type instanceof ParameterizedType) {
            componentType = ((ParameterizedType) type).getActualTypeArguments()[0];
        } else if (type instanceof Class<?> && ((Class<?>) type).isArray()) {
            componentType = ((Class<?>) type).getComponentType();
        }

        while (true) {
            parser.skipWhitespace();
            if (parser.peek() == ']') {
                parser.next();
                break;
            }

            list.add(parseValue(parser, componentType));

            parser.skipWhitespace();
            if (parser.peek() == ',') {
                parser.next();
            }
        }

        if (type instanceof Class<?> && ((Class<?>) type).isArray()) {
            Object array = Array.newInstance((Class<?>) componentType, list.size());
            for (int i = 0; i < list.size(); i++) {
                Array.set(array, i, convertValue(list.get(i), (Class<?>) componentType));
            }
            return array;
        }
        return list;
    }

    private static Object convertValue(Object value, Class<?> clazz) {
        if (value == null) return null;
        if (clazz.isInstance(value)) return value;

        // 숫자 타입 변환
        if (value instanceof Number num) {
            if (clazz == Integer.class || clazz == int.class) return num.intValue();
            if (clazz == Long.class || clazz == long.class) return num.longValue();
            if (clazz == Double.class || clazz == double.class) return num.doubleValue();
            if (clazz == Float.class || clazz == float.class) return num.floatValue();
            if (clazz == Short.class || clazz == short.class) return num.shortValue();
            if (clazz == Byte.class || clazz == byte.class) return num.byteValue();
        }

        return value;
    }
}
