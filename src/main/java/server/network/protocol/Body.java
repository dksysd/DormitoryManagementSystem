package server.network.protocol;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import server.network.serialize.Serializable;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class Body implements Serializable {
    private final Map<String, Object> data;

    public Body() {
        data = new HashMap<>();
    }

    public void addData(String key, Object value) {
        data.put(key, value);
    }

    public Object getData(String key) {
        return data.get(key);
    }

    public boolean hasData(String key) {
        return data.containsKey(key);
    }

    public void clearData() {
        data.clear();
    }
}
