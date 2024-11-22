package server.persistence.dto;

import server.network.serialize.JsonSerializable;
import server.persistence.model.Model;

public interface DTO extends JsonSerializable {
    Model toModel();
}
