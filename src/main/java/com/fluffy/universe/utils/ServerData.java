package com.fluffy.universe.utils;

import org.eclipse.jetty.util.ajax.JSON;

import java.util.HashMap;
import java.util.Map;

public class ServerData {
    private final Map<String, Object> storage;

    public ServerData() {
        storage = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public void addErrorBag(ErrorBag errorBag) {
        if (storage.containsKey("errorBags")) {
            Map<String, Map<String, Map<String, String>>> errorBags = (Map<String, Map<String, Map<String, String>>>) storage.get("errorBags");
            errorBags.put(errorBag.getName(), errorBag.getStorage());
        } else {
            Map<String, Map<String, Map<String, String>>> errorBags = new HashMap<>();
            errorBags.put(errorBag.getName(), errorBag.getStorage());
            storage.put("errorBags", errorBags);
        }
    }

    public void setAlertWindow(String title, String description, AlertType type) {
        storage.put("alert", Map.of("title", title, "description", description, "type", type.toString()));
    }

    public void clear() {
        storage.clear();
    }

    @Override
    public String toString() {
        return "<script>const serverData=" + JSON.toString(storage) + ";</script>";
    }
}
