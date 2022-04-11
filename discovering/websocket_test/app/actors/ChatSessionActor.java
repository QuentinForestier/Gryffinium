package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.util.Timeout;
import play.libs.akka.InjectedActorSupport;
import scala.concurrent.Future;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;
import static akka.pattern.Patterns.pipe;


public class ChatSessionActor extends AbstractActor implements InjectedActorSupport
{

    private final Timeout timeout = new Timeout(2, TimeUnit.SECONDS);


    public static class Create
    {
        final String id;

        public Create(String id)
        {
            this.id = id;
        }
    }

    private final UserActor.Factory userActorFactory;

    @Inject
    public ChatSessionActor(UserActor.Factory userActorFactory)
    {
        this.userActorFactory = userActorFactory;
    }

    public Receive createReceive()
    {
        return receiveBuilder()
                .match(ChatSessionActor.Create.class, create ->
                {
                    ActorRef user =
                            injectedChild(() -> userActorFactory.create(create.id), "userActor-" + create.id);
                    Future<Object> future = ask(user, "Creation",
                            timeout);
                    pipe(future, context().dispatcher()).to(sender());
                })
                .build();
    }
}