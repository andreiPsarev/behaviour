package com.fluffy.universe.middleware;

import com.fluffy.universe.exceptions.CSRFTokenVerificationException;
import com.fluffy.universe.utils.ModelKey;
import com.fluffy.universe.utils.SessionKey;
import io.javalin.http.Context;
import org.eclipse.jetty.http.HttpMethod;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CSRFFilter {
    private static final Set<String> alteringMethods = Stream.of(
            HttpMethod.POST,
            HttpMethod.PUT,
            HttpMethod.PATCH,
            HttpMethod.DELETE
    ).map(HttpMethod::toString).collect(Collectors.toSet());

    private CSRFFilter() {}

    public static void verifyToken(Context context) {
        if (!alteringMethods.contains(context.req.getMethod())) {
            return;
        }

        String csrfToken = context.formParam("_csrf");
        String sessionCsrfToken = context.sessionAttribute(SessionKey.CSRF);

        if (sessionCsrfToken == null || !sessionCsrfToken.equals(csrfToken)) {
            throw new CSRFTokenVerificationException();
        }
    }

    public static void generateToken(Context context) {
        if (context.sessionAttribute(SessionKey.CSRF) != null) {
            return;
        }

        Map<String, Object> model = context.sessionAttribute(SessionKey.MODEL);
        String csrfToken = UUID.randomUUID().toString();

        context.sessionAttribute(SessionKey.CSRF, csrfToken);
        model.put(ModelKey.CSRF, csrfToken);
    }
}
