package com.fluffy.universe.controllers;

import com.fluffy.universe.exceptions.HttpException;
import com.fluffy.universe.utils.SessionUtils;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;

import java.util.Map;

public class ExceptionHandlerController extends Controller {
    public ExceptionHandlerController(Javalin application) {
        super("", application);
    }

    public void handleHttpException(HttpException exception, Context context) {
        HttpCode httpCode = exception.getHttpCode();
        Map<String, Object> model = SessionUtils.getCurrentModel(context);
        model.put("errorPageTitle", httpCode.getMessage());
        model.put("errorCode", httpCode.getStatus());
        model.put("errorHeading", exception.getMessage());
        model.put("errorDescription", "We're sorry, there was an error processing your request.");
        render(context, "/views/pages/errors/http.vm");
    }

    public void handlePageNotFoundError(Context context) {
        handleHttpException(new HttpException(HttpCode.NOT_FOUND, "Oops! Page not found"), context);
    }

    public void handleInternalServerError(Context context) {
        handleHttpException(new HttpException(HttpCode.INTERNAL_SERVER_ERROR, "Internal server error"), context);
    }

    @Override
    public void registerRoutes(Javalin application) {}
}
