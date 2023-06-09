package com.fluffy.universe.controllers;

import com.fluffy.universe.models.Comment;
import com.fluffy.universe.models.Role;
import com.fluffy.universe.services.CommentService;
import com.fluffy.universe.utils.SecurityUtils;
import com.fluffy.universe.utils.SessionUtils;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.time.LocalDateTime;

public class CommentController extends Controller {
    public CommentController(Javalin application) {
        super("/comments", application);
    }

    public void store(Context context) {
        Integer parentId = context.formParam("parent-id") == null || context.formParam("parent-id").isEmpty() ? null : Integer.parseInt(context.formParam("parent-id"));
        Integer postId = context.formParamAsClass("post-id", Integer.class).get();
        String description = context.formParam("description");
        Integer userID = SessionUtils.getCurrentUser(context).getId();
        Comment comment = new Comment();
        comment.setParentId(parentId);
        comment.setUserId(userID);
        comment.setPostId(postId);
        comment.setDescription(SecurityUtils.escape(description));
        comment.setPublicationDateTime(LocalDateTime.now());
        CommentService.saveComment(comment);

        context.redirect("/posts/" + postId);
    }

    @Override
    public void registerRoutes(Javalin application) {
        application.post(baseUrl, this::store, Role.USER);
    }
}
