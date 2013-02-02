package restrict;

import play.Logger;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;

import static play.Play.application;

public class RestrictToHostGroupAction extends Action<RestrictToHostGroup> {
    public static final String RESTRICT_ALREADY_SET = "restricttohostgroup-set";
    public static final String DEFAULT = "default";
    public static final String CONFIG_KEY_GROUPS = "restricttohostgroup.groups.";
    public static final String CONFIG_KEY_DENIED_REDIRECT = "restricttohostgroup.redirect";
    public static final String MSG_GROUP_NOT_FOUND = "RestrictToHostGroup - Did not find group '%s'. Falling back to 'default'";
    public static final String MSG_LOG_ACCESS_DENIED = "RestrictToHostGroup - Access denied for %s [%s]";

    @Override
    public Result call(Http.Context ctx) throws Throwable {
        if(!ctx.args.containsKey(RESTRICT_ALREADY_SET)) {
            ctx.args.put(RESTRICT_ALREADY_SET, "");

            String value = configuration.value();
            String group = (value == null || value.isEmpty()) ? DEFAULT : value;
            String remoteAddress = ctx.request().remoteAddress();

            /* Check if our application.conf contains the group requested. Otherwise
             * fall back to default.
             */
            if(!application().configuration().keys().contains(CONFIG_KEY_GROUPS + group)) {
                Logger.warn(String.format(MSG_GROUP_NOT_FOUND, group));
                group = DEFAULT;
            }

            List<String> allowedList = application().configuration().getStringList(CONFIG_KEY_GROUPS + group);

            for(String host : allowedList) {
                if(remoteAddress.startsWith(host)) {
                    return delegate.call(ctx);
                }
            }

            Logger.warn(String.format(MSG_LOG_ACCESS_DENIED, remoteAddress, ctx.request().uri()));

            if(application().configuration().getString(CONFIG_KEY_DENIED_REDIRECT) != null) {
                return redirect(application().configuration().getString(CONFIG_KEY_DENIED_REDIRECT));
            }

            return forbidden();
        }

        return delegate.call(ctx);
    }
}
