package controllers;

import actors.UserActor;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Project;
import models.ProjectUser;
import models.User;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;
import repository.ProjectRepository;
import repository.UserRepository;
import securities.Secured;
import play.libs.streams.ActorFlow;
import utils.Utils;

import javax.inject.Inject;
import java.util.*;

public class ProjectController extends Controller {

    private final ActorSystem actorSystem;
    private final Materializer materializer;

    private ProjectRepository projectRepository;
    private UserRepository userRepository;

    private static final Map<UUID, Project> openProjects = new HashMap<UUID, Project>();

    private FormFactory formFactory;

    @Inject
    public ProjectController(ActorSystem actorSystem,
                             Materializer materializer,
                             FormFactory formFactory,
                             ProjectRepository projectRepository,
                             UserRepository userRepository) {
        this.actorSystem = actorSystem;
        this.materializer = materializer;
        this.formFactory = formFactory;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    // POST
    public Result create(Http.Request request) {

        System.out.println("create project");

        JsonNode json = request.body().asJson();

        if (json == null || json.get("name").size() < 3) {
            badRequest(Utils.createResponse("Project name must be at least 3 characters long", false));
        }

        Project project = new Project(json.get("name").asText());

        User user = userRepository.findById(UUID.fromString(request.session().get("userId").get()));

        ProjectUser projectUser = new ProjectUser(user, project, true, true);
        project.addUser(projectUser);
        projectRepository.save(project);

        ObjectNode result = (ObjectNode) Json.toJson(project);
        result.remove("projectUsers");
        return ok(Utils.createResponse(result, true));
    }

    // GET
    @Security.Authenticated(Secured.class)
    public Result projects(Http.Request request) {
        User user = userRepository.findById(UUID.fromString(request.session().get("userId").get()));
        List<Project> projects = projectRepository.findProjectOfUser(user.getId());
        return ok(Json.toJson(projects));
    }

    // GET
    public Result project(Http.Request request, String uuid) {
        Project project = openProjects.get(UUID.fromString(uuid));
        if (project == null) {
            project = projectRepository.findById(UUID.fromString(uuid));

            if (project == null) {
                return notFound("Project not found");
            }
            openProjects.put(UUID.fromString(uuid), project);
        }
        return ok(views.html.project.render(project.getId().toString(), request));
    }

    // WS
    public WebSocket socket(String uuid) {
        return WebSocket.Json.accept(request -> {

            Project p = findOpenProject(UUID.fromString(uuid));

            ProjectUser user = p.getProjectUsers()
                    .stream()
                    .filter(pu ->
                            Objects.equals(pu.getUser().getId(), UUID.fromString(request.session().get("userId").get()))).findFirst().get();
            return ActorFlow.actorRef(out -> UserActor.props(out, user), actorSystem,
                    materializer);
        });
    }

    // POST
    @Security.Authenticated(Secured.class)
    public Result addCollaborator(Http.Request request, String uuid) {
        JsonNode json = request.body().asJson();
        String email = json.get("email").asText();
        Project project = findOpenProject(UUID.fromString(uuid));

        if (project == null || project.getProjectUsers().stream().anyMatch(pu -> pu.getUser().getId().equals(
                request.session().get("userId").get()) && !pu.getIsOwner())) {
            return forbidden("You are not allowed to add collaborators to this project");
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return badRequest("User not found");
        }
        ProjectUser projectUser = new ProjectUser(user, project, false, false);
        project.addUser(projectUser);
        projectRepository.save(project);
        return ok("User added");
    }

    private Project findOpenProject(UUID uuid) {
        return openProjects.get(uuid);
    }
}
