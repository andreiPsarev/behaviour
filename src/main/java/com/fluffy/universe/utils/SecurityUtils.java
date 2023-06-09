package com.fluffy.universe.utils;

import com.google.common.html.HtmlEscapers;

public final class SecurityUtils {
    private SecurityUtils() {}

    @SuppressWarnings("UnstableApiUsage")
    public static String escape(String input) {
        if (input == null) {
            return null;
        }
        return HtmlEscapers.htmlEscaper().escape(input);
    }
}
