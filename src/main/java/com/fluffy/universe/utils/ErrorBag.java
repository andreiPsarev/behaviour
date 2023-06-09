package com.fluffy.universe.utils;

import org.eclipse.jetty.util.ajax.JSON;

import java.util.HashMap;
import java.util.Map;

public class ErrorBag {
    private final String name;
    private final Map<String, Map<String, String>> storage;
    private int errorCounter;
    public static final String NO_ERROR_MESSAGE = "";

    public ErrorBag(String name) {
        this.name = name;
        this.storage = new HashMap<>();
    }

    public void add(String fieldName, String oldValue, String errorMessage) {
        storage.put(fieldName, Map.of("oldValue", oldValue, "errorMessage", errorMessage));
        if (!NO_ERROR_MESSAGE.equals(errorMessage)) {
            errorCounter++;
        }
    }

    public boolean hasErrors() {
        return errorCounter > 0;
    }

    public String getName() {
        return name;
    }

    public Map<String, Map<String, String>> getStorage() {
        return storage;
    }

    @Override
    public String toString() {
        return JSON.toString(storage);
    }
}
