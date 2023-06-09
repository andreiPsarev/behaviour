package com.fluffy.universe.controllers;

import com.fluffy.universe.models.Role;
import com.fluffy.universe.services.PostService;
import com.fluffy.universe.utils.SessionUtils;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;

public class HomeController extends Controller {
    private static final List<Integer> allowedPaginationSizeOptions = List.of(4, 8, 12);

    public HomeController(Javalin application) {
        super("", application);
    }

    private static int tryParse(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public void homePage(Context context) {
        int pageNumber = tryParse(context.queryParam("page"), 1);
        int pageSize = tryParse(context.queryParam("size"), 10);
        int postCount = PostService.getPostCount();
        int maxPage = (int) Math.round(Math.ceil(postCount / (double) pageSize));
        if (pageNumber > maxPage) {
            pageNumber = maxPage;
        }
        if (!allowedPaginationSizeOptions.contains(pageSize)) {
            pageSize = allowedPaginationSizeOptions.get(0);
        }

        Map<String, Object> model = SessionUtils.getCurrentModel(context);
        List<Map<String, Object>> posts = PostService.getUserPosts(pageNumber, pageSize);
        model.put("posts", posts);
        model.put("paginationRecordCount", postCount);
        model.put("paginationPageSize", pageSize);
        model.put("paginationCurrentPage", pageNumber);
        model.put("paginationSpread", 2);
        model.put("paginationBaseURL", "/");
        model.put("paginationPageSizeOptions", allowedPaginationSizeOptions);

        render(context, "/views/pages/home.vm");
    }

    @Override
    public void registerRoutes(Javalin application) {
        application.get("/", this::homePage, Role.values());
        application.get("/home", this::homePage, Role.values());
    }
}
