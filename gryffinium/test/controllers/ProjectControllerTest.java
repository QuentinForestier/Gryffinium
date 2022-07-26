package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import tyrex.services.UUID;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.HttpVerbs.PATCH;
import static play.test.Helpers.*;

public class ProjectControllerTest extends WithApplication
{

    @Override
    protected Application provideApplication()
    {
        return new GuiceApplicationBuilder().build();
    }

    Http.Session sessionUserTest;
    Http.Session sessionUserTest2;
    Http.Session sessionAdminTest;
    ArrayList<String> projectIds = new ArrayList<>();

    @Before
    public void setup()
    {
        try
        {
            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method(POST)
                    .bodyJson(Json.parse("{\"name\":\"test\", " +
                            "\"email\":\"" + "admin@test.ch\"," +
                            "\"password\":\"Test123$Secret\", " +
                            "\"confirmPassword\":\"Test123$Secret\"}"))
                    .uri("/register");
            Result result = route(app, request);
        }
        catch (Exception ignored)
        {

        }

        try
        {
            Http.RequestBuilder request = new Http.RequestBuilder()
                    .method(POST)
                    .bodyJson(Json.parse("{\"name\":\"test\", " +
                            "\"email\":\"" + "test2@test.ch\"," +
                            "\"password\":\"Test123$Secret\", " +
                            "\"confirmPassword\":\"Test123$Secret\"}"))
                    .uri("/register");
            Result result = route(app, request);
        }
        catch (Exception ignored)
        {

        }

        JsonNode body = Json.newObject()
                .put("email", "admin@test.ch")
                .put("password", "Test123$Secret");
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(body)
                .uri("/login");
        Result result = route(app, request);
        sessionAdminTest = result.session();

        body = Json.newObject()
                .put("email", "test@test.ch")
                .put("password", "MySuperSecretPassowrdWith123$");
        request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(body)
                .uri("/login");
        result = route(app, request);
        sessionUserTest = result.session();

        body = Json.newObject()
                .put("email", "test2@test.ch")
                .put("password", "Test123$Secret");
        request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(body)
                .uri("/login");
        result = route(app, request);
        sessionUserTest2 = result.session();

        request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(POST)
                .bodyJson(Json.parse("{\"name\":\"" + UUID.create() + "\"}"))
                .uri("/projects");
        result = route(app, request);
        projectIds.add(Json.parse(Helpers.contentAsString(result)).get("body").get("id").asText());

        request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(POST)
                .bodyJson(Json.parse("{\"name\":\"" + UUID.create() + "\"}"))
                .uri("/projects");
        result = route(app, request);
        projectIds.add(Json.parse(Helpers.contentAsString(result)).get("body").get("id").asText());

        request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(POST)
                .bodyJson(Json.parse("{\"name\":\"" + UUID.create() + "\"}"))
                .uri("/projects");
        result = route(app, request);
        projectIds.add(Json.parse(Helpers.contentAsString(result)).get("body").get("id").asText());

        request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(POST)
                .bodyJson(Json.parse("{\"email\":\"test2@test.ch\"}"))
                .uri("/projects/" + projectIds.get(2) + "/addCollaborator");
        result = route(app, request);

        request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(POST)
                .bodyJson(Json.parse("{\"email\":\"test2@test.ch\"}"))
                .uri("/projects/" + projectIds.get(0) + "/addCollaborator");
        result = route(app, request);
    }

    @Test
    public void testCreate_Success()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(POST)
                .bodyJson(Json.parse("{\"name\":\"" + UUID.create() + "\"}"))
                .uri("/projects");
        Result result = route(app, request);
        assertEquals(200, result.status());
    }

    @Test
    public void testCreate_InvalidName_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(POST)
                .bodyJson(Json.parse("{\"name\":\"" + UUID.create().substring(0, 2) + "\"}"))
                .uri("/projects");
        Result result = route(app, request);
        assertEquals(400, result.status());
    }

    @Test
    public void testCreate_NotLogged_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.parse("{\"name\":\"" + UUID.create() + "\"}"))
                .uri("/projects");
        Result result = route(app, request);
        assertEquals(303, result.status());
    }

    @Test
    public void testUpdate_Success()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(POST)
                .bodyJson(Json.parse("{\"name\":\"" + UUID.create() + "\"}"))
                .uri("/projects/" + projectIds.get(0));
        Result result = route(app, request);
        assertEquals(200, result.status());
    }

    @Test
    public void testUpdate_InvalidName_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(POST)
                .bodyJson(Json.parse("{\"name\":\"" + UUID.create().substring(0, 2) + "\"}"))
                .uri("/projects/" + projectIds.get(0));
        Result result = route(app, request);
        assertEquals(400, result.status());
    }

    @Test
    public void testUpdate_NotOwner_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionUserTest.data())
                .method(POST)
                .bodyJson(Json.parse("{\"name\":\"" + UUID.create() + "\"}"))
                .uri("/projects/" + projectIds.get(0));
        Result result = route(app, request);
        assertEquals(403, result.status());
    }

    @Test
    public void testUpdate_NotLogged_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.parse("{\"name\":\"" + UUID.create() + "\"}"))
                .uri("/projects?id=" + projectIds.get(0));
        Result result = route(app, request);
        assertEquals(303, result.status());
    }

    @Test
    public void testDelete_Success()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(DELETE)
                .uri("/projects/" + projectIds.get(1));
        Result result = route(app, request);
        assertEquals(200, result.status());
    }

    @Test
    public void testDelete_NotOwner_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionUserTest.data())
                .method(DELETE)
                .uri("/projects/" + projectIds.get(1));
        Result result = route(app, request);
        assertEquals(403, result.status());
    }

    @Test
    public void testDelete_NotLogged_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(DELETE)
                .uri("/projects/" + projectIds.get(1));
        Result result = route(app, request);
        assertEquals(303, result.status());
    }

    @Test
    public void testProjects_Success()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(GET)
                .uri("/projects");
        Result result = route(app, request);
        ArrayNode body =
                (ArrayNode) Json.parse(Helpers.contentAsString(result)).get(
                        "body");

        assertTrue(body.size() > 0);
        assertEquals(200, result.status());
    }

    @Test
    public void testProjects_NotLogged_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/projects");
        Result result = route(app, request);
        assertEquals(303, result.status());
    }

    @Test
    public void testProjects_WithoutProject_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionUserTest.data())
                .method(GET)
                .uri("/projects");
        Result result = route(app, request);
        ArrayNode body =
                (ArrayNode) Json.parse(Helpers.contentAsString(result)).get(
                        "body");

        assertEquals(0, body.size());
        assertEquals(200, result.status());
    }


    @Test
    public void testProject_Success()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(GET)
                .uri("/projects/" + projectIds.get(0));
        Result result = route(app, request);
        assertEquals(200, result.status());
    }

    @Test
    public void testProject_NotMember_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionUserTest.data())
                .method(GET)
                .uri("/projects/" + projectIds.get(0));
        Result result = route(app, request);
        assertEquals(403, result.status());
    }

    @Test
    public void testProject_NotLogged_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri("/projects/" + projectIds.get(0));
        Result result = route(app, request);
        assertEquals(303, result.status());
    }

    @Test
    public void testAddCollaborator_Success()
    {

        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(POST)
                .bodyJson(Json.parse("{\"email\":\"test2@test.ch\"}"))
                .uri("/projects/" + projectIds.get(1) + "/addCollaborator");
        Result result = route(app, request);
        assertEquals(200, result.status());
    }

    @Test
    public void testAddCollaborator_NotOwner_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionUserTest.data())
                .method(POST)
                .bodyJson(Json.parse("{\"email\":\"admin@test.ch\"}"))
                .uri("/projects/" + projectIds.get(1) + "/addCollaborator");
        Result result = route(app, request);
        assertEquals(400, result.status());
    }

    @Test
    public void testAddCollaborator_AlreadyMember_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(POST)
                .bodyJson(Json.parse("{\"email\":\"test2@test.ch\"}"))
                .uri("/projects/" + projectIds.get(1) + "/addCollaborator");
        Result result = route(app, request);

        request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(POST)
                .bodyJson(Json.parse("{\"email\":\"test2@test.ch\"}"))
                .uri("/projects/" + projectIds.get(1) + "/addCollaborator");
        result = route(app, request);
        assertEquals(400, result.status());
    }

    @Test
    public void testAddCollaborator_NotLogged_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .bodyJson(Json.parse("{\"email\":\"admin@test.ch\"}"))
                .uri("/projects/" + projectIds.get(1) + "/addCollaborator");
        Result result = route(app, request);
        assertEquals(303, result.status());
    }

    @Test
    public void testAddCollaborator_UserNotFound_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(POST)
                .bodyJson(Json.parse("{\"email\":\"notfound@test.ch\"}"))
                .uri("/projects/" + projectIds.get(1) + "/addCollaborator");
        Result result = route(app, request);
        assertEquals(400, result.status());
    }

    @Test
    public void testUpdateCollaborator_Success()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(PATCH)
                .bodyJson(Json.parse("{\"id\":\""+ sessionUserTest2.data().get("userId") +"\"}"))
                .uri("/projects/" + projectIds.get(2) + "/updateCollaborator");
        Result result = route(app, request);
        assertEquals(200, result.status());
    }

    @Test
    public void testUpdateCollaborator_NotOwner_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionUserTest.data())
                .method(PATCH)
                .bodyJson(Json.parse("{\"id\":\""+ sessionUserTest2.data().get("userId") +"\"}"))
                .uri("/projects/" + projectIds.get(2) + "/updateCollaborator");
        Result result = route(app, request);
        assertEquals(403, result.status());
    }

    @Test
    public void testUpdateCollaborator_NotLogged_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PATCH)
                .bodyJson(Json.parse("{\"id\":\""+ sessionUserTest2.data().get("userId") +"\"}"))
                .uri("/projects/" + projectIds.get(2) + "/updateCollaborator");
        Result result = route(app, request);
        assertEquals(303, result.status());
    }

    @Test
    public void testUpdateCollaborator_UserNotFound_Fail()
    {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(PATCH)
                .bodyJson(Json.parse("{\"id\":\""+ UUID.create() +"\"}"))
                .uri("/projects/" + projectIds.get(2) + "/updateCollaborator");
        Result result = route(app, request);
        assertEquals(400, result.status());
    }

    @Test
    public void testUpdateCollaborator_Owner_Fail(){
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(PATCH)
                .bodyJson(Json.parse("{\"id\":\""+ sessionAdminTest.data().get("userId") +"\"}"))
                .uri("/projects/" + projectIds.get(2) + "/updateCollaborator");
        Result result = route(app, request);
        assertEquals(400, result.status());
    }

    @Test
    public void testRemoveCollaborator_Success(){
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(DELETE)
                .bodyJson(Json.parse("{\"id\":\""+ sessionUserTest2.data().get("userId") +"\"}"))
                .uri("/projects/" + projectIds.get(0) + "/removeCollaborator");
        Result result = route(app, request);
        assertEquals(200, result.status());
    }

    @Test
    public void testRemoveCollaborator_NotOwner_Fail(){
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionUserTest.data())
                .method(DELETE)
                .bodyJson(Json.parse("{\"id\":\""+ sessionUserTest2.data().get("userId") +"\"}"))
                .uri("/projects/" + projectIds.get(0) + "/removeCollaborator");
        Result result = route(app, request);
        assertEquals(403, result.status());
    }

    @Test
    public void testRemoveCollaborator_NotLogged_Fail(){
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(DELETE)
                .bodyJson(Json.parse("{\"id\":\""+ sessionUserTest2.data().get("userId") +"\"}"))
                .uri("/projects/" + projectIds.get(0) + "/removeCollaborator");
        Result result = route(app, request);
        assertEquals(303, result.status());
    }

    @Test
    public void testRemoveCollaborator_UserNotFound_Fail(){
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(DELETE)
                .bodyJson(Json.parse("{\"id\":\""+ UUID.create() +"\"}"))
                .uri("/projects/" + projectIds.get(0) + "/removeCollaborator");
        Result result = route(app, request);
        assertEquals(400, result.status());
    }

    @Test
    public void testRemoveCollaborator_Owner_Fail(){
        Http.RequestBuilder request = new Http.RequestBuilder()
                .session(sessionAdminTest.data())
                .method(DELETE)
                .bodyJson(Json.parse("{\"id\":\""+ sessionAdminTest.data().get("userId") +"\"}"))
                .uri("/projects/" + projectIds.get(0) + "/removeCollaborator");
        Result result = route(app, request);
        assertEquals(400, result.status());
    }
}
