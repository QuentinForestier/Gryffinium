package controllers;

import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;
import repository.UserRepository;

import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class ApplicationController extends Controller {

    private FormFactory formFactory;

    @Inject
    public ApplicationController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result index(Http.Request request) {
        return ok(views.html.index.render(request));
    }


}
