package models.messages;

import com.fasterxml.jackson.databind.JsonNode;

public interface Command
{
    public abstract JsonNode execute();
}
