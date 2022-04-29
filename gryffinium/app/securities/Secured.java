package securities;

import controllers.routes;
import play.*;
import play.api.ApplicationLoader;
import play.mvc.*;
import play.mvc.Http.*;

import models.*;

import javax.naming.Context;
import java.util.Optional;

public class Secured extends Security.Authenticator
{

    @Override
    public Optional<String> getUsername(Request req)
    {
        return req.session().get("userName");
    }

    @Override
    public Result onUnauthorized(Request req)
    {
        return redirect(routes.ApplicationController.index()).flashing("error"
                , "You must be logged in to access this page");
    }
}