package controllers;

import constraints.PasswordConstraint;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.mvc.Controller;
import org.mindrot.jbcrypt.BCrypt;

import play.mvc.Http;
import play.mvc.Result;
import repository.UserRepository;

import javax.inject.Inject;
import java.util.ArrayList;


public class UserController extends Controller {

    public static class Login {
        @Constraints.Required(message = "Email is required")
        public String email;


        @Constraints.Required(message = "Password is required")
        public String password;

    }

    public static class Signup {

        @Constraints.Required(message = "Name is required")
        @Constraints.MinLength(value = 2, message = "Name must be at least 2 characters")
        public String name;

        @Constraints.Required(message = "Email is required")
        @Constraints.Email(message = "Email is not valid")
        public String email;

        @Constraints.Required(message = "Password is required")
        @Constraints.ValidateWith(value = PasswordConstraint.class, message = "Password must be 8 characters, at least one digit, one lowercase, one uppercase, one special character")
        public String password;

        public String confirmPassword;
    }

    private UserRepository userRepository;
    private FormFactory formFactory;

    @Inject
    public UserController(UserRepository userRepository, FormFactory formFactory) {
        this.userRepository = userRepository;
        this.formFactory = formFactory;
    }

    // POST
    public Result login(Http.Request request) {
        Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest(request);
        Form<Signup> signupForm = formFactory.form(Signup.class).bindFromRequest(request).discardingErrors();

        if (loginForm.hasErrors()) {
            return badRequest(views.html.index.render(loginForm, signupForm, request));
        } else {
            User user = userRepository.findByEmail(loginForm.get().email);

            if (user == null || !BCrypt.checkpw(loginForm.get().password, user.getPassword())) {
                loginForm = loginForm.withError("Credentials", "Invalid credentials");
                return notFound(views.html.index.render(loginForm, signupForm, request));
            }

            System.out.println("User: " + user.getName());

            return redirect(routes.ApplicationController.index())
                    .addingToSession(request, "userId", user.getId().toString())
                    .addingToSession(request, "userName", user.getName());
        }

    }

    // GET
    public Result logout(Http.Request request) {
        return redirect(routes.ApplicationController.index()).withNewSession();
    }

    // POST
    public Result register(Http.Request request) {
        Form<Signup> signupForm = formFactory.form(Signup.class).bindFromRequest(request);

        Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest(request).discardingErrors();

        if (signupForm.hasErrors()) {
            return badRequest(views.html.index.render(loginForm, signupForm, request));
        } else if (!signupForm.get().password.equals(signupForm.get().confirmPassword)) {

            return badRequest(views.html.index.render(loginForm,
                    signupForm.withError("Password", "Password and confirmation password must be the same"),
                    request));
        } else {
            if (userRepository.findByEmail(signupForm.get().email) != null) {
                return badRequest(views.html.index.render(loginForm,
                        signupForm.withError("Email", "Email already exists"),
                        request));
            }

            User user = new User(
                    signupForm.get().name,
                    signupForm.get().email,
                    BCrypt.hashpw(signupForm.get().password, BCrypt.gensalt())
            );
            userRepository.create(user);

            return redirect(routes.ApplicationController.index());
        }
    }
}
