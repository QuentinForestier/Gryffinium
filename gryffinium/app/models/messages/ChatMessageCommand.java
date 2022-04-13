package models.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChatMessageCommand implements Command
{
    private String message;
    private String sender = "";



    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getSender()
    {
        return sender;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    @Override
    public JsonNode execute()
    {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("ChatMessageCommand: " + message);

        return mapper.valueToTree(this);
    }
}
