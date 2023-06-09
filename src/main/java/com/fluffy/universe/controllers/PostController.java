package com.fluffy.universe.controllers;

import com.fluffy.universe.exceptions.HttpException;
import com.fluffy.universe.models.Post;
import com.fluffy.universe.models.Role;
import com.fluffy.universe.services.CommentService;
import com.fluffy.universe.services.PostService;
import com.fluffy.universe.utils.AlertType;
import com.fluffy.universe.utils.SecurityUtils;
import com.fluffy.universe.utils.ServerData;
import com.fluffy.universe.utils.SessionUtils;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PostController extends Controller {
    public PostController(Javalin application) {
        super("/posts", application);
    }

    public void indexPage(Context context) {
        context.redirect("/");
    }

    public void createPage(Context context) {
        render(context, "/views/pages/models/post/create.vm");
    }

    public void editPage(Context context) {
        render(context, "/views/pages/models/post/edit.vm");
    }

    public void show(Context context) {
        int id = Integer.parseInt(context.pathParam("post"));
        Map<String, Object> userPost = PostService.getUserPost(id);
        if (userPost == null) {
            throw new HttpException(HttpCode.NOT_FOUND, "Post not found");
        }
        List<Map<String, Object>> comments = CommentService.getUserCommentsByPostId((Integer) userPost.get("post.id"));

        Map<String, Object> model = SessionUtils.getCurrentModel(context);
        model.put("post", userPost);
        model.put("comments", comments);

        render(context, "/views/pages/models/post/show.vm");
    }

    public void store(Context context) {
        ServerData serverData = SessionUtils.getCurrentServerData(context);
        String title = context.formParam("title");
        String description = context.formParam("description");

        Post post = new Post();
        post.setUserId(SessionUtils.getCurrentUser(context).getId());
        post.setTitle(SecurityUtils.escape(title));
        post.setDescription(SecurityUtils.escape(description));
        post.setPublicationDateTime(LocalDateTime.now());
        PostService.savePost(post);

        serverData.setAlertWindow("Congratulations!", "Blog post published successfully.", AlertType.SUCCESS);
        context.redirect("/");
    }

    @Override
    public void registerRoutes(Javalin application) {
        application.get(baseUrl, this::indexPage, Role.GUEST);
        application.get(baseUrl + "/create", this::createPage, Role.USER);
        application.get(baseUrl + "/{post}/edit", this::editPage, Role.USER);
        application.get(baseUrl + "/{post}", this::show, Role.GUEST, Role.USER);
        application.post(baseUrl, this::store, Role.USER);
    }
}
