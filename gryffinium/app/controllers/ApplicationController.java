package controllers;

import models.Project;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;
import repository.ProjectRepository;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class ApplicationController extends Controller {

    private FormFactory formFactory;

    private UserRepository userRepository;
    private ProjectRepository projectRepository;

    @Inject
    public ApplicationController(FormFactory formFactory,
                                 ProjectRepository projectRepository,
                                 UserRepository userRepository) {
        this.formFactory = formFactory;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Result index(Http.Request request) {
        List<Project> projects = new ArrayList<>();
        if (request.session().get("userId").isPresent()) {
            projects = projectRepository.findProjectOfUser(
                    UUID.fromString(request.session().get("userId").get())
            );
        }

        System.out.println(projects);

        return ok(views.html.index.render(
                formFactory.form(ProjectController.ProjectCreation.class),
                formFactory.form(UserController.Login.class),
                formFactory.form(UserController.Signup.class),
                projects,
                request));
    }


}
