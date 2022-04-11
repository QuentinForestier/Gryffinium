package controllers;

import actors.ChatSessionActor;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.japi.Pair;
import akka.japi.pf.PFBuilder;
import akka.stream.Materializer;
import akka.stream.javadsl.*;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.F;
import play.mvc.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import static akka.pattern.Patterns.ask;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
@Singleton
public class HomeController extends Controller {


    Map<Integer, Flow<String, String, ?>> flowMap = new HashMap();

    Materializer materializer;

    @Inject
    public HomeController(Materializer mat) {
        this.materializer = mat;
    }


    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(views.html.index.render());
    }

    public Result chat(int id){
        return ok(views.html.chat.render(id));
    }


    public WebSocket ws(int id){
        return WebSocket.Text.accept(request ->
                createOfFind(id)/*.map(msg -> {
                    System.out.println(msg);
                    return "server received " + msg;
                })*/);
    }


    public Flow<String, String, ?> createOfFind(int id){
        if(!flowMap.containsKey(id)){

            Source<String, Sink<String, NotUsed>> source = MergeHub.of(String.class)
                    .recoverWithRetries(-1, new PFBuilder().match(Throwable.class, e -> Source.empty()).build());
            Sink<String, Source<String, NotUsed>> sink = BroadcastHub.of(String.class);

            Pair<Sink<String, NotUsed>, Source<String, NotUsed>> sinkSourcePair = source.toMat(sink, Keep.both()).run(materializer);
            Sink<String, NotUsed> chatSink = sinkSourcePair.first();
            Source<String, NotUsed> chatSource = sinkSourcePair.second();
            flowMap.put(id, Flow.fromSinkAndSource(chatSink, chatSource));
        }

        return flowMap.get(id);
    }



}
