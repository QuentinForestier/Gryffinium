package actors;

import akka.Done;
import akka.NotUsed;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.Pair;
import akka.stream.Materializer;
import akka.stream.javadsl.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.assistedinject.Assisted;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.CompletionStage;

public class UserActor extends AbstractActor
{
    private final String id;
    private final ActorRef userRef;
    private final Materializer materializer;

    private final Sink<JsonNode, NotUsed> hubSink;
    private final Flow<JsonNode, JsonNode, NotUsed> websocketFlow;

    @Inject
    public UserActor(@Assisted String id,
                     @Named("userRef") ActorRef userRef,
                     Materializer materializer)
    {
        this.id = id;
        this.userRef = userRef;
        this.materializer = materializer;

        Pair<Sink<JsonNode, NotUsed>, Source<JsonNode, NotUsed>> sinkSourcePair =
                MergeHub.of(JsonNode.class, 16)
                        .toMat(BroadcastHub.of(JsonNode.class, 256),
                                Keep.both())
                        .run(materializer);

        this.hubSink = sinkSourcePair.first();
        Source<JsonNode, NotUsed> hubSource = sinkSourcePair.second();

        Sink<JsonNode, CompletionStage<Done>> jsonSink =
                Sink.foreach((JsonNode json) ->
        {
            System.out.println("UserActor: " + json);
        });

        this.websocketFlow = Flow.fromSinkAndSource(jsonSink, hubSource)
                .watchTermination((n, stage) ->
                {
                    stage.thenAccept(f -> context().stop(self()));
                    return NotUsed.getInstance();
                });
    }

    public Receive createReceive()
    {
        return receiveBuilder()
                .match(String.class, (String message) ->
                {
                    System.out.println("received: " + message);
                    sender().tell(message, self());
                }).build();
    }

    public interface Factory
    {
        UserActor create(String id);
    }
}

