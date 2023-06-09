package com.fluffy.universe;

import com.fluffy.universe.controllers.*;
import com.fluffy.universe.exceptions.HttpException;
import com.fluffy.universe.middleware.CSRFFilter;
import com.fluffy.universe.middleware.ModelFilter;
import com.fluffy.universe.utils.ApplicationAccessManager;
import com.fluffy.universe.utils.Configuration;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Javalin application = Javalin.create(configuration -> {
            configuration.addStaticFiles("/public", Location.CLASSPATH);
            configuration.accessManager(new ApplicationAccessManager());
            if (args.length > 0) {
                Configuration.load(new File(args[0]));
            }
        });
        ExceptionHandlerController exceptionHandlerController = new ExceptionHandlerController(application);

        application
                .before(ModelFilter::initializeModel)
                .before(CSRFFilter::verifyToken)
                .before(CSRFFilter::generateToken)
                .exception(HttpException.class, exceptionHandlerController::handleHttpException)
                .error(404, exceptionHandlerController::handlePageNotFoundError)
                .error(500, exceptionHandlerController::handleInternalServerError);

        new HomeController(application);
        new UserController(application);
        new PostController(application);
        new CommentController(application);
        application.start(Configuration.get("application.host"), Configuration.getAsClass("application.port", Integer.class));
    }
}
