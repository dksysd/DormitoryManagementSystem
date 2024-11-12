package server.network.protocol;

import lombok.*;
import server.network.serialize.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
public abstract class Protocol implements Serializable {
    private final Header header;
    private final Body body;
}
