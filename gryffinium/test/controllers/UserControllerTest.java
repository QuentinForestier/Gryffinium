package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import repository.DatabaseExecutionContext;
import repository.UserRepository;
import tyrex.services.UUID;

import static javax.security.auth.callback.ConfirmationCallback.OK;
import static org.junit.Assert.assertEquals;
import static play.test.Helpers.POST;
import static play.test.Helpers.route;

import static org.mockito.Mockito.*;

public class UserControllerTest extends WithApplication
{

    @Override
    protected Application provideApplication()
    {
        return new GuiceApplicationBuilder().build();
    }

    @Before
    public void setup()
    {

        try
        {
            User user = new User("test", "test@test.ch",
                    BCrypt.hashpw("MySuperSecretPassowrdWith123$", BCrypt.gensalt()));
            user.save();
        }catch(Exception ignored){

        }
    }

    @Test
    public void testRegister_Success()
    {

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.parse("{\"name\":\"test\", " +
                        "\"email\":\"" + UUID.create() + "@test.ch\"," +
                        "\"password\":\"Test123$Secret\", " +
                        "\"confirmPassword\":\"Test123$Secret\"}"))
                .uri("/register");
        Result result = route(app, request);
        assertEquals(303, result.status());
    }

    @Test
    public void testRegister_AlreadyExisting_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.parse("{\"name\":\"test\", " +
                        "\"email\":\"test@test.ch\"," +
                        "\"password\":\"Test123$Secret\", " +
                        "\"confirmPassword\":\"Test123$Secret\"}"))
                .uri("/register");
        Result result = route(app, request);
        assertEquals(400, result.status());
    }

    @Test
    public void testRegister_BadPassword_Fail(){
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.parse("{\"name\":\"test\", " +
                        "\"email\":\"" + UUID.create() + "@test.ch\"," +
                        "\"password\":\"asd\", " +
                        "\"confirmPassword\":\"asd\"}"))
                .uri("/register");
        Result result = route(app, request);
        assertEquals(400, result.status());
    }

    @Test
    public void testRegister_BadConfirmationPassword_Fail(){
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.parse("{\"name\":\"test\", " +
                        "\"email\":\"" + UUID.create() + "@test.ch\"," +
                        "\"password\":\"MySuperPassword123$\", " +
                        "\"confirmPassword\":\"MySuperPassword1\"}"))
                .uri("/register");
        Result result = route(app, request);
        assertEquals(400, result.status());
    }

    @Test
    public void testLogin_Success()
    {
        JsonNode body = Json.newObject()
                .put("email", "test@test.ch")
                .put("password", "MySuperSecretPassowrdWith123$");
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(body)
                .uri("/login");
        Result result = route(app, request);
        Assert.assertEquals(303, result.status());
    }

    @Test
    public void testLogin_BadCredentials_Fail()
    {
        JsonNode body = Json.newObject()
                .put("email", "test@admin.ch")
                .put("password", "BadPassword123$");
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(body)
                .uri("/login");
        Result result = route(app, request);
        Assert.assertEquals(400, result.status());
    }
}
