package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import restrict.RestrictToHostGroup;
import views.html.index;

@RestrictToHostGroup("default")
public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    @RestrictToHostGroup("admin")
    public static Result admin() {
        return ok(index.render("Admin."));
    }
}
