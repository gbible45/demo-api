package com.demo.api.portal.users.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by user on 2016-05-16.
 */
public enum UserRole {

    /**
     * The UserRole admin type
     */
    ADMIN("ADMIN"),

    /**
     * The UserRole developer type
     */
    DEVELOPER("DEVELOPER"),

    /**
     * The UserRole auditor type
     */
    AUDITOR("AUDITOR"),

    /**
     * The UserRole auditor type
     */
    MONITOR("MONITOR");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    @JsonCreator
    public static UserRole from(String s) {
        switch (s.toUpperCase()) {
            case "ADMIN":
                return ADMIN;
            case "DEVELOPER":
                return DEVELOPER;
            case "AUDITOR":
                return AUDITOR;
            case "MONITOR":
                return MONITOR;
            default:
                throw new IllegalArgumentException(String.format("Unknown UserRole type: %s", s));
        }
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return getValue();
    }

}