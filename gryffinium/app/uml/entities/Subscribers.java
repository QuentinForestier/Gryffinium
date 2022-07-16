package uml.entities;

import com.fasterxml.jackson.databind.JsonNode;
import uml.entities.operations.Operation;
import uml.types.Type;

public interface Subscribers
{

    void setType(Type type);
    JsonNode getUpdateNameCommand();
}
