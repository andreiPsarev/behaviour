package com.fluffy.universe.utils;

import com.fluffy.universe.models.Role;
import com.fluffy.universe.models.User;
import io.javalin.core.security.AccessManager;
import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ApplicationAccessManager implements AccessManager {
    @Override
    public void manage(@NotNull Handler handler, @NotNull Context context, @NotNull Set<RouteRole> routeRoles) throws Exception {
        if (routeRoles.size() == 0) {
            handler.handle(context);
            return;
        }

        Role role = Role.GUEST;
        User user = SessionUtils.getCurrentUser(context);
        if (user != null) {
            role = Role.getRoleById(user.getRoleId());
        }

        if (routeRoles.contains(role)) {
            handler.handle(context);
            return;
        }

        if (role == Role.GUEST) {
            context.redirect("/sign-in");
        } else {
            context.redirect("/");
        }
    }
}
