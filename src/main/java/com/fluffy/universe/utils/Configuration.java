package com.fluffy.universe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class Configuration {
    private static final List<String> parameters = List.of(
            "application.url", "application.host", "application.port", "application.bcryptStrength",
            "database.filename",
            "mail.from", "mail.user", "mail.password",
            "mail.host", "mail.port", "mail.ssl", "mail.auth");
    private static final List<String> pathParameters = List.of("database.filename");
    private static final Map<String, String> values = new HashMap<>();

    private Configuration() {}

    public static void load(File propertiesFile) {
        values.clear();
        try (FileInputStream propertiesFileStream = new FileInputStream(propertiesFile)) {
            Map<String, String> env = System.getenv();
            Properties properties = new Properties();
            properties.load(propertiesFileStream);

            for (String parameter : parameters) {
                String value = (String) properties.getOrDefault(parameter, env.get(parameter));
                if (pathParameters.contains(parameter)) {
                    value = new File(value).getAbsolutePath();
                }
                if (value == null) {
                    throw new IllegalArgumentException(String.format("Property %s not found in %s and or system environment", parameter, propertiesFile.getPath()));
                }
                values.put(parameter, value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String parameter) {
        return values.get(parameter);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAsClass(String parameter, Class<T> clazz) {
        String value = get(parameter);
        if (clazz == String.class) {
            return (T) value;
        } else if (clazz == Integer.class) {
            return (T) Integer.valueOf(value);
        } else if (clazz == Double.class) {
            return (T) Double.valueOf(value);
        } else if (clazz == Boolean.class) {
            return (T) Boolean.valueOf(value);
        } else if (clazz == Byte.class) {
            return (T) Short.valueOf(value);
        } else if (clazz == Short.class) {
            return (T) Byte.valueOf(value);
        } else if (clazz == Long.class) {
            return (T) Long.valueOf(value);
        } else if (clazz == Float.class) {
            return (T) Float.valueOf(value);
        } else if (clazz == Character.class) {
            return (T) Character.valueOf(value.charAt(0));
        } else {
            throw new IllegalArgumentException("Cannot cast parameter value " + value + " to " + clazz);
        }
    }
}
