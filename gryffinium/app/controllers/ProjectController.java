package controllers;

import actors.UserActor;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import models.Project;
import models.ProjectUser;
import models.User;
import play.libs.F;
import play.libs.Json;
import play.mvc.*;
import repository.ProjectRepository;
import repository.ProjectUserRepository;
import repository.UserRepository;
import securities.Secured;
import play.libs.streams.ActorFlow;
import uml.ClassDiagram;
import utils.Utils;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ProjectController extends Controller
{

    private final ActorSystem actorSystem;
    private final Materializer materializer;

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectUserRepository projectUserRepository;


    @Inject
    public ProjectController(ActorSystem actorSystem,
                             Materializer materializer,
                             ProjectRepository projectRepository,
                             UserRepository userRepository,
                             ProjectUserRepository projectUserRepository)
    {
        this.actorSystem = actorSystem;
        this.materializer = materializer;
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


        User user =
                userRepository.findById(UUID.fromString(request.session().get("userId").get()));

        Project project = new Project(json.get("name").asText(), user);

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

        Project project = findProject(UUID.fromString(id), false);

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

        Project project = findProject(UUID.fromString(id), false);

        if (project == null || !project.getOwner().getUser().getId().equals(
                UUID.fromString(request.session().get("userId").get())))
        {
            return forbidden(Utils.createResponse("You are not allowed to " +
                    "delete this project", false));
        }
        projectRepository.delete(project);

        project.close();

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
            // TODO redirect to index avec flash error
            return forbidden(Utils.createResponse("You are not a member of " +
                    "this project", false));
        }

        Project project = findProject(UUID.fromString(uuid), true);

        if (project == null)
        {
            return notFound("Project not found");
        }
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(ClassDiagram.class);

            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StringWriter sw = new StringWriter();

            marshaller.marshal(project.getDiagram(), sw);

            System.out.println(sw.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return ok(views.html.project.render(project.getId().toString(),
                project.getName(), request));
    }

    // WS
    @Security.Authenticated(Secured.class)
    public WebSocket socket(String uuid)
    {
        return WebSocket.Json.acceptOrResult(request -> createActorFlow(request, uuid));
    }

    private CompletionStage<F.Either<Result, Flow<JsonNode, JsonNode, ?>>> createActorFlow(Http.RequestHeader request, String uuid)
    {
        Project p = Project.openProjects.get(UUID.fromString(uuid));
        ProjectUser user =
                p.findProjectUser(UUID.fromString(request.session().get(
                        "userId").get()));

        if (user == null)
        {
            return CompletableFuture.completedFuture(F.Either.Left(forbidden(Utils.createResponse("You are not " +
                    "a member of the project", false))));
        }
        return CompletableFuture.completedFuture(F.Either.Right(ActorFlow.actorRef(out -> UserActor.props(out,
                        user),
                actorSystem,
                materializer)));
    }

    // POST
    @Security.Authenticated(Secured.class)
    public Result addCollaborator(Http.Request request, String uuid)
    {
        JsonNode json = request.body().asJson();
        String email = json.get("email").asText();
        Project project = findProject(UUID.fromString(uuid), false);

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
    // TODO grants in body
    @Security.Authenticated(Secured.class)
    public Result updateCollaborator(Http.Request request, String uuid)
    {
        JsonNode json = request.body().asJson();
        String id = json.get("id").asText();
        Project project = findProject(UUID.fromString(uuid), false);

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

        ProjectUser pu = project.findProjectUser(UUID.fromString(id));
        if (pu == null)
        {
            return badRequest(Utils.createResponse("User not found", false));
        }


        pu.setCanWrite(!pu.getCanWrite());

        projectUserRepository.save(pu);

        return ok(Utils.createResponse("User rights updated", true));
    }

    // DELETE
    @Security.Authenticated(Secured.class)
    public Result removeCollaborator(Http.Request request, String uuid)
    {
        JsonNode json = request.body().asJson();
        String id = json.get("id").asText();
        Project project = findProject(UUID.fromString(uuid), false);

        if (project == null || !project.getOwner().getUser().getId().toString()
                .equals(request.session().get("userId").get()))
        {
            return forbidden(Utils.createResponse(
                    "You are not allowed to remove collaborators from this " +
                            "project", false));
        }

        ProjectUser pu = project.findProjectUser(UUID.fromString(id));

        if (pu == null)
        {
            return badRequest(Utils.createResponse("User is not a member " +
                    "of the project", false));
        }

        // TODO cant remove owner
        if (id.equals(request.session().get("userId").get()))
        {
            return badRequest(Utils.createResponse("You can't remove yourself" +
                    " from a project", false));
        }

        projectUserRepository.delete(pu);

        project.removeUser(pu);

        return ok(Utils.createResponse("User removed from project", true));

    }

    private Project findProject(UUID uuid, boolean findAndOpen)
    {
        Project p = Project.openProjects.get(uuid);
        if (p == null)
        {
            p = projectRepository.findById(uuid);

            if (findAndOpen && p != null)
            {
                Project.openProjects.put(p.getId(), p);
            }
        }

        return p;
    }

}
