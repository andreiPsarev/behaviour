package com.fluffy.universe.controllers;

import com.fluffy.universe.utils.SessionUtils;
import io.javalin.Javalin;
import io.javalin.http.Context;

public abstract class Controller {
    protected final String baseUrl;

    protected Controller(String baseUrl, Javalin application) {
        this.baseUrl = baseUrl;
        registerRoutes(application);
    }

    public void render(Context context, String filePath) {
        context.render(filePath, SessionUtils.getCurrentModel(context));
        SessionUtils.getCurrentServerData(context).clear();
    }

    public void disableCaching(Context context) {
        context.res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        context.res.setHeader("Pragma", "no-cache");
        context.res.setHeader("Expires", "0");
    }

    public abstract void registerRoutes(Javalin application);
}
