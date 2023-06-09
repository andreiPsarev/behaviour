package com.fluffy.universe.models;

import io.javalin.core.security.RouteRole;

import java.util.Objects;

public enum Role implements RouteRole {
    GUEST(0, "Guest"),
    USER(1, "User");

    private final Integer id;
    private final String name;

    Role(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Role getRoleById(Integer id) {
        if (id == null) {
            return GUEST;
        }

        for (Role role : Role.values()) {
            if (Objects.equals(id, role.id)) {
                return role;
            }
        }
        throw new RuntimeException("Role for id = " + id + " not found");
    }
}
