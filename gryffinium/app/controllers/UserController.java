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


    // GET
    public Result login(Http.Request request) {

        return ok(views.html.login.render(formFactory.form(UserController.Login.class), request));
    }

    // POST
    public Result authenticate(Http.Request request) {
        Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest(request);
        if (loginForm.hasErrors()) {
            return badRequest(views.html.login.render(loginForm, request));
        } else {
            User user = userRepository.findByEmail(loginForm.get().email);

            if (user == null || !BCrypt.checkpw(loginForm.get().password, user.getPassword())) {
                loginForm = loginForm.withError("Credentials", "Invalid credentials");
                return notFound(views.html.login.render(loginForm, request));
            }

            return redirect(routes.ApplicationController.index()).addingToSession(request, "user", user.getId().toString());
        }

    }

    public Result logout(Http.Request request) {
        return redirect(routes.ApplicationController.index()).withNewSession();
    }

    // GET
    public Result register(Http.Request request) {
        return ok(views.html.register.render(formFactory.form(UserController.Signup.class), request));
    }

    // POST
    public Result signup(Http.Request request) {
        Form<Signup> signupForm = formFactory.form(Signup.class).bindFromRequest(request);
        if (signupForm.hasErrors()) {
            return badRequest(views.html.register.render(signupForm, request));
        } else if (!signupForm.get().password.equals(signupForm.get().confirmPassword)) {

            return badRequest(views.html.register.render(
                    signupForm.withError("Password", "Password and confirmation password must be the same"),
                    request));
        } else {
            if (userRepository.findByEmail(signupForm.get().email) != null) {
                return badRequest(views.html.register.render(
                        signupForm.withError("Email", "Email already exists"),
                        request));
            }

            User user = new User(
                    signupForm.get().name,
                    signupForm.get().email,
                    BCrypt.hashpw(signupForm.get().password, BCrypt.gensalt())
            );
            userRepository.create(user);

            return redirect(routes.UserController.login());
        }
    }
}
