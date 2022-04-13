package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import models.ProjectUser;

public class UserActor extends AbstractActor
{

    private ActorRef out;
    private ProjectUser user;

    public static Props props(ActorRef out, ProjectUser user)
    {
        return Props.create(UserActor.class, out, user);
    }

    public UserActor(ActorRef out, ProjectUser user)
    {
        this.out = out;
        this.user = user;

        user.setUser(this);
    }

    public void send(JsonNode message){
        out.tell(message, self());
    }

    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
                .match(JsonNode.class, message ->
                        user.handleMessage(message))
                .build();
    }
}
