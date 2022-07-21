package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import com.fasterxml.jackson.databind.JsonNode;
import models.ProjectUser;

public class UserActor extends AbstractActor
{

    private final ActorRef out;
    private final ProjectUser user;

    public static Props props(ActorRef out, ProjectUser user)
    {
        return Props.create(UserActor.class, out, user);
    }

    public UserActor(ActorRef out, ProjectUser user)
    {
        this.out = out;
        this.user = user;

        user.setActor(this);
    }

    public void send(JsonNode message){
        out.tell(message, self());
    }

    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
                .match(JsonNode.class, user::handleMessage)
                .build();
    }

    public void postStop(){
        user.setActor(null);
    }

    public void disconnect(){
        self().tell(PoisonPill.getInstance(), self());
    }
}
