package controllers;

import actors.UserActor;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.Project;
import models.ProjectUser;
import models.User;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;
import repository.ProjectRepository;
import repository.ProjectUserRepository;
import repository.UserRepository;
import securities.Secured;
import play.libs.streams.ActorFlow;
import utils.Utils;

import javax.inject.Inject;
import java.util.*;

public class ProjectController extends Controller
{

    private final ActorSystem actorSystem;
    private final Materializer materializer;

    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private ProjectUserRepository projectUserRepository;

    private static final Map<UUID, Project> openProjects = new HashMap<UUID,
            Project>();

    private FormFactory formFactory;

    @Inject
    public ProjectController(ActorSystem actorSystem,
                             Materializer materializer,
                             FormFactory formFactory,
                             ProjectRepository projectRepository,
                             UserRepository userRepository,
                             ProjectUserRepository projectUserRepository)
    {
        this.actorSystem = actorSystem;
        this.materializer = materializer;
        this.formFactory = formFactory;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectUserRepository = projectUserRepository;
    }

    // POST
    @Security.Authenticated(Secured.class)
    public Result create(Http.Request request)
    {

        JsonNode json = request.body().asJson();

        if (json == null || json.get("name").asText().length() < 3)
        {
            return badRequest(Utils.createResponse("Project name must be at " +
                    "least 3 " +
                    "characters long", false));
        }

        Project project;

        if (json.has("id"))
        {
            project = projectRepository.findById(UUID.fromString(json.get("id"
            ).asText()));
            project.setName(json.get("name").asText());
        }
        else
        {
            project = new Project(json.get("name").asText());
            User user =
                    userRepository.findById(UUID.fromString(request.session().get("userId").get()));

            ProjectUser projectUser = new ProjectUser(user, project, true,
                    true);
            project.addUser(projectUser);
        }
        project = projectRepository.save(project).toCompletableFuture().join();

        return ok(Utils.createResponse(project.toJson(), true));
    }

    // PATCH
    @Security.Authenticated(Secured.class)
    public Result update(Http.Request request, String id)
    {

        JsonNode json = request.body().asJson();

        if (json == null || json.get("name").asText().length() < 3)
        {
            return badRequest(Utils.createResponse("Project name must be at " +
                    "least 3 " +
                    "characters long", false));
        }

        Project project = findProject(UUID.fromString(id));

        if (project == null || !project.getOwner().getUser().getId().equals(
                UUID.fromString(request.session().get("userId").get())))
        {
            return forbidden(Utils.createResponse("You are not allowed to " +
                    "update this project", false));
        }

        project.setName(json.get("name").asText());
        projectRepository.save(project);

        return ok(Utils.createResponse(project.toJson(), true));
    }

    // DELETE
    @Security.Authenticated(Secured.class)
    public Result delete(Http.Request request, String id)
    {

        Project project = findProject(UUID.fromString(id));

        if (project == null || !project.getOwner().getUser().getId().equals(
                UUID.fromString(request.session().get("userId").get())))
        {
            return forbidden(Utils.createResponse("You are not allowed to " +
                    "delete this project", false));
        }
        projectRepository.delete(project);

        return ok(Utils.createResponse("Project deleted successfully", true));
    }

    // GET
    @Security.Authenticated(Secured.class)
    public Result projects(Http.Request request)
    {
        User user =
                userRepository.findById(UUID.fromString(request.session().get("userId").get()));

        ArrayNode arrayNode = Json.newArray();

        for (ProjectUser pu : user.projects)
        {
            arrayNode.add(pu.project.toJson());
        }

        return ok(Utils.createResponse(arrayNode, true));
    }

    // GET
    @Security.Authenticated(Secured.class)
    public Result project(Http.Request request, String uuid)
    {

        User user =
                userRepository.findById(UUID.fromString(request.session().get("userId").get()));

        if (user.projects.stream().noneMatch(pu -> pu.project.getId().toString().equals(uuid)))
        {
            return forbidden(Utils.createResponse("You are not a member of " +
                    "this project", false));
        }

        Project project = openProjects.get(UUID.fromString(uuid));

        if (project == null)
        {
            project = projectRepository.findById(UUID.fromString(uuid));
            if (project == null)
            {
                return notFound("Project not found");
            }
            openProjects.put(project.id, project);
        }


        return ok(views.html.project.render(project.getId().toString(),
                request));
    }

    // WS
    @Security.Authenticated(Secured.class)
    public WebSocket socket(String uuid)
    {
        return WebSocket.Json.accept(request ->
        {
            Project p = openProjects.get(UUID.fromString(uuid));
            ProjectUser user =
                    p.findProjectUser(UUID.fromString(request.session().get(
                            "userId").get()));
            return ActorFlow.actorRef(out -> UserActor.props(out, user),
                    actorSystem,
                    materializer);
        });
    }

    // POST
    @Security.Authenticated(Secured.class)
    public Result addCollaborator(Http.Request request, String uuid)
    {
        JsonNode json = request.body().asJson();
        String email = json.get("email").asText();
        Project project = findProject(UUID.fromString(uuid));

        if (project == null || project.getProjectUsers().stream().anyMatch(pu -> pu.getUser().getId().equals(
                request.session().get("userId").get()) && !pu.getIsOwner()))
        {
            return forbidden(Utils.createResponse("You are not allowed to add" +
                    " collaborators to " +
                    "this project", false));
        }

        if (project.projectUsers.stream().anyMatch(pu -> pu.getUser().getEmail().equals(email)))
        {
            return badRequest(Utils.createResponse("User is already a member " +
                    "of the project", false));
        }
        User user = userRepository.findByEmail(email);
        if (user == null)
        {
            return badRequest(Utils.createResponse("User not found", false));
        }
        ProjectUser projectUser = new ProjectUser(user, project, false,
                false);
        project.addUser(projectUser);

        projectUserRepository.save(projectUser);
        return ok(Utils.createResponse(projectUser.toJsonCollaborator(),
                true));
    }

    // PATCH
    @Security.Authenticated(Secured.class)
    public Result updateCollaborator(Http.Request request, String uuid)
    {
        JsonNode json = request.body().asJson();
        String id = json.get("id").asText();
        Project project = findProject(UUID.fromString(uuid));

        if (project == null || !project.getOwner().getUser().getId().toString()
                .equals(request.session().get("userId").get()))
        {
            return forbidden(Utils.createResponse(
                    "You are not allowed to update collaborators rights from " +
                            "this " +
                            "project", false));
        }

        if (project.findProjectUser(UUID.fromString(id)) == null)
        {
            return badRequest(Utils.createResponse("User is not a member " +
                    "of the project", false));
        }

        if (id.equals(request.session().get("userId").get()))
        {
            return badRequest(Utils.createResponse("You can't change your own" +
                    " rights", false));
        }

        ProjectUser pu = projectUserRepository.getUser(UUID.fromString(id),
                project.getId());
        if (pu == null)
        {
            return badRequest(Utils.createResponse("User not found", false));
        }


        pu.setCanWrite(!pu.getCanWrite());
        project.findProjectUser(pu.getUser().getId()).setCanWrite(pu.getCanWrite());

        projectUserRepository.save(pu);

        return ok(Utils.createResponse("User rights updated", true));
    }

    // DELETE
    @Security.Authenticated(Secured.class)
    public Result removeCollaborator(Http.Request request, String uuid)
    {
        JsonNode json = request.body().asJson();
        String id = json.get("id").asText();
        Project project = findProject(UUID.fromString(uuid));

        if (project == null || !project.getOwner().getUser().getId().toString()
                .equals(request.session().get("userId").get()))
        {
            return forbidden(Utils.createResponse(
                    "You are not allowed to remove collaborators from this " +
                            "project", false));
        }

        if (project.findProjectUser(UUID.fromString(id)) == null)
        {
            return badRequest(Utils.createResponse("User is not a member " +
                    "of the project", false));
        }

        if (id.equals(request.session().get("userId").get()))
        {
            return badRequest(Utils.createResponse("You can't remove yourself" +
                    " from a project", false));
        }

        projectUserRepository.delete(project.findProjectUser(UUID.fromString(id)));

        project.removeUser(project.findProjectUser(UUID.fromString(id)));

        return ok(Utils.createResponse("User removed from project", true));

    }

    private Project findProject(UUID uuid)
    {
        Project p = openProjects.get(uuid);
        if (p == null)
        {
            p = projectRepository.findById(uuid);
        }

        return p;
    }

}
