package com.demo.api.portal.core.dto;

import java.util.Set;

public class AuthCheck {
    private String email;
    private Set<String> roles;
    public AuthCheck() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

}
