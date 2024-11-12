package server.persistence.dto;

import server.network.serialize.Serializable;
import server.persistence.model.Model;

public interface DTO extends Serializable {
    Model toModel();
}
