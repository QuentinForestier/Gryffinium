package controllers;

import actors.UserActor;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import models.Project;
import models.ProjectUser;
import play.libs.streams.ActorFlow;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class DiagramController extends Controller
{

    private final ActorSystem actorSystem;
    private final Materializer materializer;

    private static Map<String, Project> projectMap = new HashMap();


    @Inject
    public DiagramController(ActorSystem actorSystem, Materializer materializer)
    {
        this.actorSystem = actorSystem;
        this.materializer = materializer;

        projectMap.put("1", new Project());
        projectMap.put("2", new Project());
    }

    public Result project(String uuid)
    {
        return ok(views.html.diagram.render(uuid));
    }

    public WebSocket socket(String uuid)
    {
        return WebSocket.Json.accept(request ->
                ActorFlow.actorRef(out -> UserActor.props(out, new ProjectUser(findProject(uuid))), actorSystem,
                        materializer));
    }

    public Project findProject(String uuid){
        return projectMap.get(uuid);
    }
}
