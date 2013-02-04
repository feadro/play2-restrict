package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import restrict.RestrictToHostGroup;
import views.html.index;

@RestrictToHostGroup   // Same as @RestrictToHostGroup("default")
public class Application extends Controller {

    public static Result index() {
        return ok(index.render("User."));
    }

    @RestrictToHostGroup("admin")
    public static Result admin() {
        return ok(index.render("Admin."));
    }
}
