package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.ProjectUser;
import models.messages.ChatMessageCommand;
import models.messages.Command;

public class UserActor extends AbstractActor
{

    private ActorRef out;
    private ProjectUser user;

    public static Props props(ActorRef out, ProjectUser user)
    {
        System.out.println("Creating UserActor");
        return Props.create(UserActor.class, out, user);
    }

    public UserActor(ActorRef out, ProjectUser user)
    {
        this.out = out;
        this.user = user;

        user.setUser(this);
    }

    public void send(JsonNode message){
        System.out.println("Sending message to user : " + message);
        out.tell(message, self());
    }

    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
                .match(JsonNode.class, command ->
                        user.handleMessage(new ObjectMapper().treeToValue(command, ChatMessageCommand.class)))
                .build();
    }
}
