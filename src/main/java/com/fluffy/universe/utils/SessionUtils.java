package com.fluffy.universe.utils;

import com.fluffy.universe.models.User;
import io.javalin.http.Context;

import java.util.Map;

public final class SessionUtils {
    private SessionUtils() {}

    public static User getCurrentUser(Context context) {
        return context.sessionAttribute(SessionKey.USER);
    }

    public static Map<String, Object> getCurrentModel(Context context) {
        return context.sessionAttribute(SessionKey.MODEL);
    }

    public static ServerData getCurrentServerData(Context context) {
        return context.sessionAttribute(SessionKey.SERVER_DATA);
    }
}
