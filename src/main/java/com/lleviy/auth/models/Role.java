package com.lleviy.auth.models;

public enum Role {
    USER, ADMIN;

    public String getAuthority() {
        return name();
    }
}
