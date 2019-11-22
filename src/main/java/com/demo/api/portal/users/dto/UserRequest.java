package com.demo.api.portal.users.dto;

import com.demo.api.framework.validation.Password;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest implements Serializable {
    @NotNull
    private String email;

    @NotNull
    private String name;

    private List<UserRole> roles;

    @Password
    private String password;
}
